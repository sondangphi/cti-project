package org.meetmejava;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.logging.Logger;

import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.MeetMeMuteAction;
import org.asteriskjava.manager.action.MeetMeUnmuteAction;
import org.meetmejava.event.Event;
import org.meetmejava.event.EventType;

public class Conference extends Observable {

        private final MeetMeRoom meetMeRoom;
        private final Map<String, User> conferenceUserMap; /*
                                                                                                                 * A map of conference
                                                                                                                 * users to their
                                                                                                                 * respective meetme
                                                                                                                 * userIDs
                                                                                                                 */
        private final String recordingNumber;
        private String recordingName;

        private final State state;
        private final String conferenceId;
        private boolean recording;

        private final static Logger logger = Logger.getLogger(Conference.class
                        .getName());

        // private final String conferenceId;

        public Conference(String conferenceId, String recordingNumber, State state) {

                meetMeRoom = state.getAsteriskServer().getMeetMeRoom(conferenceId);
                this.state = state;
                this.conferenceUserMap = meetMeUsersToConferenceUserMap(meetMeRoom
                                .getUsers());
                this.recordingNumber = recordingNumber;
                this.conferenceId = conferenceId;
        }

        private static Map<String, User> meetMeUsersToConferenceUserMap(
                        Collection<MeetMeUser> meetMeUsers) {
                Map<String, User> conferenceUserMap = new HashMap<String, User>();
                for (MeetMeUser meetMeUser : meetMeUsers) {
                        User conferenceUser = meetMeUserToConferenceUser(meetMeUser);
                        conferenceUserMap.put(conferenceUser.getUserId(), conferenceUser);
                }
                return conferenceUserMap;
        }

        private static User meetMeUserToConferenceUser(MeetMeUser meetMeUser) {
                User conferenceUser = new User(meetMeUser);
                return conferenceUser;
        }

        private MeetMeUser getMeetMeUser(String userId) {
                for (MeetMeUser meetMeUser : meetMeRoom.getUsers()) {
                        if (meetMeUser.getChannel().getId().equals(userId))
                                return meetMeUser;
                }
                return null;
        }

        private User getConferenceUser(String phoneNumber) {
                for (User conferenceUser : this.conferenceUserMap.values()) {
                        if (phoneNumber.equals(conferenceUser.getPhoneNumber()))
                                return conferenceUser;
                }
                return null;
        }

        public void setConferenceUserMuteState(String userId, boolean mute)
                        throws Exception {
                if (mute) {
                        MeetMeMuteAction muteAction = new MeetMeMuteAction(conferenceId,
                                        conferenceUserMap.get(userId).getUserNumber());

                        state.getConnection().sendAction(muteAction);
                } else {
                        MeetMeUnmuteAction unmuteAction = new MeetMeUnmuteAction(
                                        conferenceId, conferenceUserMap.get(userId).getUserNumber());

                        state.getConnection().sendAction(unmuteAction);
                }

        }

        public void requestHangup(String userId) throws Exception {
                HangupAction hangupAction = new HangupAction(getMeetMeUser(userId)
                                .getChannel().getName());
                state.getConnection().sendAction(hangupAction);
        }

        public void requestEndConference() throws Exception {
                for (String userId : conferenceUserMap.keySet()) {
                        requestHangup(userId);
                }
        }

        public String requestDialOut(String phoneNumber) throws Exception {
                Map<String, String> dialOutLock = new HashMap<String, String>();
                synchronized (dialOutLock) {
                        state.getDialOutLocks().put(phoneNumber, dialOutLock);
                        URL url = new URL(state.getAsteriskExtURL()
                                        + "?context=call&action=meetme-dialout&channel="
                                        // TODO Channel hardcoded to SIP for now
                                        + URLEncoder.encode("SIP", "UTF-8") + "&number="
                                        + URLEncoder.encode(phoneNumber, "UTF-8") + "&room="
                                        + URLEncoder.encode(conferenceId, "UTF-8"));
                        URLConnection httpConn = url.openConnection();
                        httpConn.connect();
                        httpConn.getInputStream();
                        // TODO This should be a timed wait. On timeout, throw dialout
                        // failure
                        while (!dialOutLock.containsKey("user-id"))
                                dialOutLock.wait();
                }
                String userId = dialOutLock.get("user-id");
                state.getDialOutLocks().remove(phoneNumber);
                return userId;
        }

        public void requestStartRecording(String recordingName) throws Exception {
                this.recordingName = recordingName;
                requestDialOut(recordingNumber);
        }

        public void requestStopRecording() throws Exception {
                String recUserId = getConferenceUser(recordingNumber).getUserId();
                getMeetMeUser(recUserId).getChannel().stopMonitoring();
                requestHangup(recUserId);
        }

        public void handleAddConferenceUser(MeetMeUser user, String phoneNumber)
                        throws Exception {
                User conferenceUser = new User(user);
                conferenceUserMap.put(conferenceUser.getUserId(), conferenceUser);

                setChanged();
                if (phoneNumber.equals(recordingNumber)) {
                        user.getChannel().startMonitoring(
                                        state.getTempRecDir() + File.separator + recordingName);
                        recording = true;
                        notifyObservers(new Event(EventType.RECORDING));
                } else {
                        logger.fine("Dispatching user-joined");
                        notifyObservers(new Event(EventType.USER_JOINED, conferenceUser));
                }
        }

        public void handleRemoveConferenceUser(String userId) {
                User conferenceUser = conferenceUserMap.get(userId);
                setChanged();
                if (conferenceUser.getPhoneNumber().equals(recordingNumber)) {
                        recording = false;
                        notifyObservers(new Event(EventType.RECORDING));
                } else
                        notifyObservers(new Event(EventType.USER_LEFT, conferenceUser));

                conferenceUserMap.remove(userId);
        }

        public void handleEndConference() {
                conferenceUserMap.clear();
                setChanged();
                notifyObservers(new Event(EventType.CONFERENCE_ENDED));
        }

        public void destroy() {
                for (User user : conferenceUserMap.values())
                        user.destroy();
                state.getStartedConferences().remove(conferenceId);             
        }

        public Map<String, User> getUsers() {
                return conferenceUserMap;
        }

        public State getState() {
                return state;
        }

        public boolean isRecording() {
                return recording;
        }

        public String getConferenceId() {
                return conferenceId;
        }

        public String getRecordingPath() {
                return state.getTempRecDir() + File.separator + recordingName;
        }

        // public static void main(String[] args) throws Exception{
        // State state = new State();
        // Conference conference = new Conference("6300",state);
        // conference.getUsersFromServer();
        // }
}

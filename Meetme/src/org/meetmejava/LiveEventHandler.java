package org.meetmejava;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MeetMeEndEvent;
import org.asteriskjava.manager.event.NewStateEvent;

//this class listen the events send from Asterisk server and handle them.
//the event include AsteriskServerListener,ManagerEventListener,
public class LiveEventHandler implements AsteriskServerListener,PropertyChangeListener,ManagerEventListener {

        private final static Logger logger = Logger.getLogger(LiveEventHandler.class.getName());

        private final State state;

        public LiveEventHandler(State state) {
                this.state = state;
        }

        public void init() {
                AsteriskServer asteriskServer = state.getAsteriskServer();
                asteriskServer.addAsteriskServerListener(this);
                state.getConnection().addManagerEventListener(this);

                for (AsteriskChannel asteriskChannel : asteriskServer.getChannels()) {
                        logger.fine(asteriskChannel.toString());
                        asteriskChannel.addPropertyChangeListener(this);
                }

                for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()) {
                        logger.fine(asteriskQueue.toString());
                        for (AsteriskQueueEntry asteriskQueueEntry : asteriskQueue
                                        .getEntries()) {
                                asteriskQueueEntry.getChannel().addPropertyChangeListener(this);
                        }
                }

                for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms()) {
                        logger.fine(meetMeRoom.toString());
                        for (MeetMeUser user : meetMeRoom.getUsers()) {
                                user.addPropertyChangeListener(this);
                        }
                }
        }

        public void destroy() {
                AsteriskServer asteriskServer = state.getAsteriskServer();
                asteriskServer.removeAsteriskServerListener(this);
                state.getConnection().removeManagerEventListener(this);

                for (AsteriskChannel asteriskChannel : asteriskServer.getChannels()) {
                        logger.fine(asteriskChannel.toString());
                        asteriskChannel.removePropertyChangeListener(this);
                }

                for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()) {
                        logger.fine(asteriskQueue.toString());
                        for (AsteriskQueueEntry asteriskQueueEntry : asteriskQueue
                                        .getEntries()) {
                                asteriskQueueEntry.getChannel().removePropertyChangeListener(
                                                this);
                        }
                }

                for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms()) {
                        logger.fine(meetMeRoom.toString());
                        for (MeetMeUser user : meetMeRoom.getUsers()) {
                                user.removePropertyChangeListener(this);
                        }
                }

        }

        @Override
        public void onNewAgent(AsteriskAgentImpl agent) {
                logger.fine(agent.toString());
                agent.addPropertyChangeListener(this);
        }

        @Override
        public void onNewAsteriskChannel(AsteriskChannel channel) {
                logger.fine(channel.toString());
                String id = channel.getId();
                String phoneNumber = AsteriskUtils.getPhoneNumberFromChannel(channel
                                .getName());
                Map<String, String> dialOutLock = state.getDialOutLocks().get(
                                phoneNumber);
                if (dialOutLock != null) {
                        synchronized (dialOutLock) {
                                dialOutLock.put("user-id", id);
                                dialOutLock.notifyAll();
                        }
                }
                channel.addPropertyChangeListener(this);
        }

        @Override
        public void onNewMeetMeUser(MeetMeUser user) {
                logger.fine(user.toString());
                try {
                        Conference conference = state.getStartedConferences().get(
                                        user.getRoom().getRoomNumber());
                        conference.handleAddConferenceUser(user, AsteriskUtils
                                        .getUserPhoneNumber(user));
                } catch (Exception ex) {
                        logger.log(Level.WARNING, ex.getMessage(), ex);
                }
        }

        @Override
        public void onNewQueueEntry(AsteriskQueueEntry queueEntry) {
                logger.fine(queueEntry.toString());
                queueEntry.addPropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                logger.fine(propertyChangeEvent.toString());
        }

        @Override
        public void onManagerEvent(ManagerEvent managerEvent) {
                logger.fine("Received manager event: " + managerEvent);
                if(managerEvent instanceof NewStateEvent){
                	NewStateEvent n  = (NewStateEvent) managerEvent;
                	n.getCallerIdNum();
                }
                	
                
                if (managerEvent instanceof MeetMeEndEvent) {
                        MeetMeEndEvent endEvent = (MeetMeEndEvent) managerEvent;
                        Conference conference = state.getStartedConferences().get(
                                        endEvent.getMeetMe());
                        if (conference != null)
                                conference.handleEndConference();
                }
                
                if(managerEvent instanceof HangupEvent){
                	
                
	                HangupEvent hang = (HangupEvent)managerEvent;
	                hang.getCallerIdName();
                }
        }

}


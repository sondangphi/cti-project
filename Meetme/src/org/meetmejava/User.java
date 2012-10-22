package org.meetmejava;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.logging.Logger;

import org.asteriskjava.live.MeetMeUser;
import org.meetmejava.event.Event;
import org.meetmejava.event.EventType;

public class User extends Observable implements PropertyChangeListener {

        private MeetMeUser meetMeUser;
        private boolean talking = false;

        private final static Logger logger = Logger.getLogger(User.class.getName());

        // public User(AsteriskChannel channel, String userId, String phoneNumber,
        // boolean muted, boolean talking) {
        // this.channel = channel;
        // this.userId = userId;
        // this.phoneNumber = phoneNumber;
        // this.muted = muted;
        // this.talking = talking;
        // }

        public User(MeetMeUser meetMeUser) {
                this.meetMeUser = meetMeUser;
                meetMeUser.addPropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
                logger.fine("Received event:" + evt);
                String propertyName = evt.getPropertyName();
                String propertyValue = evt.getNewValue().toString();
                if ("muted".equals(propertyName)) {
                        boolean newMuteState = Boolean.parseBoolean(propertyValue);
                        if (newMuteState != meetMeUser.isMuted()) {
                                if (newMuteState)
                                        meetMeUser.mute();
                                else
                                        meetMeUser.unmute();
                                setChanged();
                                notifyObservers(new Event(EventType.MUTE));
                        }
                }
                if ("talking".equals(propertyName)) {
                        boolean newTalkState = Boolean.parseBoolean(propertyValue);
                        if (newTalkState != talking) {
                                talking = newTalkState;
                                setChanged();
                                notifyObservers(new Event(EventType.TALKER));
                        }
                } else if ("state".equals(propertyName) && "LEFT".equals(propertyValue)) {
                        destroy();
                }
        }

        public void destroy() {
                meetMeUser.removePropertyChangeListener(this);
                setChanged();
                notifyObservers(new Event(EventType.USER_LEFT));

        }

        public String getUserId() {
                return meetMeUser.getChannel().getId();
        }

        public boolean isMuted() {
                return meetMeUser.isMuted();
        }

        public boolean isTalking() {
                return meetMeUser.isTalking();
        }

        public String getPhoneNumber() {
                return AsteriskUtils.getPhoneNumberFromChannel(meetMeUser.getChannel()
                                .getName());
        }

        public Integer getUserNumber() {
                return meetMeUser.getUserNumber();
        }

}
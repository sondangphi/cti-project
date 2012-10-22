package org.meetmejava;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class AsteriskUtils {

		//register a conference room for user. return true if success.
        public static boolean verifyMeetMeRoom(String roomNumber, String witness, ManagerConnection managerConnection) throws Exception {
                boolean validRoom = false;
                OriginateAction originateAction;
                ManagerResponse originateResponse;

                //le thanh hai
                System.out.println("");
                
                
                originateAction = new OriginateAction();
                originateAction.setChannel("SIP/" + witness);
                originateAction.setContext("conferences");
                originateAction.setExten(roomNumber);
                originateAction.setPriority(new Integer(1));
                originateAction.setTimeout(new Long(30000));

                // //send the originate action and wait for a maximum of 30 seconds for
                // Asterisk
                // to send a reply
                originateResponse = managerConnection.sendAction(originateAction, 30000);

                // print out whether the originate succeeded or not
                System.out.println(originateResponse.getResponse());
				if(originateResponse.getResponse().equals("Success"))
					validRoom = true;

                return validRoom;
        }

        public static final String getPhoneNumberFromChannel(String channel) {
                Pattern channelNamePattern = Pattern.compile("SIP/[0-9]+");
                Matcher matcher = channelNamePattern.matcher(channel);
                String phoneNumber = null;
                if (matcher.find()) {
                        phoneNumber = matcher.group().substring(4);
                }
                return phoneNumber;
        }

        public static final String getUserPhoneNumber(MeetMeUser user) {
                String channel = user.getChannel().getName();
                Pattern channelNamePattern = Pattern.compile("SIP/[0-9]+");
                Matcher matcher = channelNamePattern.matcher(channel);
                String phoneNumber = null;
                if (matcher.find()) {
                        phoneNumber = matcher.group().substring(4);
                }
                return phoneNumber;
        }

        public static void main(String[] args) throws Exception {
                ManagerConnectionFactory factory = new ManagerConnectionFactory(
                                "172.168.10.205", "manager", "P@$$w0rd");

                ManagerConnection managerConnection = factory.createManagerConnection();
                managerConnection.login();
                verifyMeetMeRoom("6300", "6000", managerConnection);
                managerConnection.logoff();
        }
}
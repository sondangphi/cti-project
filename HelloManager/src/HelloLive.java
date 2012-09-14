import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.MeetMeRoom;
import org.asteriskjava.live.DefaultAsteriskServer;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.action.LoginAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.event.AgentLoginEvent;

public class HelloLive
{
    private AsteriskServer asteriskServer;
    private Object o;

    public HelloLive()
    {
        asteriskServer = new DefaultAsteriskServer("172.168.10.15", "manager", "pa55w0rd");
    }

    public void run() throws ManagerCommunicationException
    {
        for (AsteriskChannel asteriskChannel : asteriskServer.getChannels())
        {
            System.out.println("asterisk channel: "+asteriskChannel);
        }

        for (AsteriskQueue asteriskQueue : asteriskServer.getQueues())
        {
            System.out.println("asteriskQueue: "+asteriskQueue.getMembers());  
            o = asteriskQueue;
        }
        
        

        for (MeetMeRoom meetMeRoom : asteriskServer.getMeetMeRooms())
        {
            System.out.println("meetMeRoom: "+meetMeRoom);
        }
    }

    public static void main(String[] args) throws Exception
    {
        HelloLive helloLive = new HelloLive();
        helloLive.run();
    }
}
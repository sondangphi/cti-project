import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.EventsAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.SipShowPeerAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.AgentCallbackLoginEvent;
import org.asteriskjava.manager.event.AgentCallbackLogoffEvent;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.AgentCompleteEvent;
import org.asteriskjava.manager.event.AgentLoginEvent;
import org.asteriskjava.manager.event.AgentLogoffEvent;
import org.asteriskjava.manager.event.AgentRingNoAnswerEvent;
import org.asteriskjava.manager.event.AgentsEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.HoldedCallEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LinkEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.TransferEvent;
import org.asteriskjava.manager.event.UnlinkEvent;
import org.asteriskjava.manager.response.ManagerResponse;

/* *
 * Ket noi voi Asterisk Server su dung API
 * Lang nghe cac su kien tu Asterisk Server gui ve, co the loc cac thong tin gui ve bang cach
 * su dung ham EventsAction("su kien can bat, phan biet cac su kien bang dau ','")
 * 
 * */
public class HelloEvents2 implements ManagerEventListener
{
    private ManagerConnection managerConnection;
    Writer output = null;
    String text = " \t";
    File file = new File("write.txt");       
    

    public HelloEvents2() throws IOException
    {
//        ManagerConnectionFactory factory = new ManagerConnectionFactory(
//                "172.168.10.100", "manager", "pa55w0rd");
        ManagerConnectionFactory factory = new ManagerConnectionFactory("172.168.10.208", "manager", "123456");        
        this.managerConnection = factory.createManagerConnection();
    }

    public void run() throws IOException, AuthenticationFailedException,
            TimeoutException, InterruptedException
    {
        // register for events
        managerConnection.addEventListener(this);
        
        // connect to Asterisk and log in
        managerConnection.login();
        
        if(managerConnection != null)
        	System.out.println("server is still running...");
        
        EventsAction ec = new EventsAction("call");
        // request channel state
        managerConnection.sendAction(new StatusAction());
        
        output = new BufferedWriter(new FileWriter(file));
        
        // wait 10 seconds for events to come in
        Thread.sleep(1000*60*5);
        
        output.close();

        // and finally log off and disconnect
        managerConnection.logoff();
        System.out.println("server is stop!");
    }

    public void onManagerEvent(ManagerEvent event)
    {
        try{
        	text = event.toString()+"\r\n";
            output.write(text);
        	
        }catch(Exception ex){
        	
        }
    }

    public static void main(String[] args) throws Exception
    {
        HelloEvents2 helloEvents;
        helloEvents = new HelloEvents2();
        helloEvents.run();
    }
}
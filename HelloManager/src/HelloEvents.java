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
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.SipShowPeerAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;

/*
 * Ket noi voi Asterisk Server su dung API
 * Lang nghe cac su kien tu Asterisk Server gui ve, co the loc cac thong tin gui ve bang cach
 * su dung ham EventsAction("su kien can bat, phan biet cac su kien bang dau ','")
 * 
 * */
public class HelloEvents implements ManagerEventListener
{
    private ManagerConnection managerConnection;
    Writer output = null;
    String text = "";
    File file = new File("write.txt");       
    

    public HelloEvents() throws IOException
    {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
                "172.168.10.19", "manager", "pa55w0rd");

        this.managerConnection = factory.createManagerConnection();
    }

    public void run() throws IOException, AuthenticationFailedException,
            TimeoutException, InterruptedException
    {
        // register for events
        managerConnection.addEventListener(this);
        
        // connect to Asterisk and log in
        managerConnection.login();

        
        EventsAction ec = new EventsAction("call,agent");
        // request channel state
        managerConnection.sendAction(new StatusAction());
        managerConnection.sendAction(ec);
//        managerConnection.get
        

        SipShowPeerAction sip;
        sip = new  SipShowPeerAction();
        sip.setPeer("8000");
        
        output = new BufferedWriter(new FileWriter(file));
        
        // wait 10 seconds for events to come in
        Thread.sleep(120000);
        
        output.close();

        // and finally log off and disconnect
        managerConnection.logoff();
    }

    public void onManagerEvent(ManagerEvent event)
    {
        // just print received events
        System.out.println("thong tin: "+event);

        try{
        	text = event.toString()+"\r\n";
            output.write(text);
        	
        }catch(Exception ex){
        	
        }
        

        
//        System.out.println("getFile: "+event.getFile());
//        System.out.println("getFunc: "+event.getFunc());
//        System.out.println("getPrivilege: "+event.getPrivilege());
//        System.out.println("getServer: "+event.getServer());
//        System.out.println("getLine: "+event.getLine());        
//        System.out.println("getSequenceNumber: "+event.getSequenceNumber());
//        System.out.println("getDateReceived: "+event.getDateReceived().toString());
//        System.out.println("thong tin: "+event.);        
    }

    public static void main(String[] args) throws Exception
    {
        HelloEvents helloEvents;

        helloEvents = new HelloEvents();
        helloEvents.run();
    }
}
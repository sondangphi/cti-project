import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.AgentLogoffAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.action.QueueRemoveAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.action.SipPeersAction;
import org.asteriskjava.manager.action.SipShowPeerAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;


/* *
 * Thiet lap mot ket noi den Asterisk Server, su dung dia chi IP, USENAME-PASSWORD
 * Tao mot Event, cung cap cac thong so can thiet cho Event --> gui Event den Asterisk Server de thuc hien
 * va nhan ket qua tra ve la Success hoac Error
 * 
 * */
public class HelloManager
{
    private ManagerConnection managerConnection;

    public HelloManager() throws IOException
    {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
                "172.168.10.19", "manager", "pa55w0rd");

        this.managerConnection = factory.createManagerConnection();
    }

    public void run() throws IOException, AuthenticationFailedException, TimeoutException{
    	
        OriginateAction originateAction;
        ManagerResponse originateResponse;

        originateAction = new OriginateAction();
        originateAction.setChannel("SIP/8002");//gan sip acc
        originateAction.setContext("default");//gan context
        originateAction.setExten("3000");//chua bik co the la queue
        originateAction.setPriority(new Integer(1));
        originateAction.setTimeout(new Integer(30000));        
        originateAction.setCallerId("le thanh hai");
        
        //add Extension vao queue
        QueueAddAction qa = new QueueAddAction();
        qa.setQueue("900");
        qa.setInterface("SIP/8000");
        qa.setPenalty(99);
        qa.setMemberName("Agent/9000");       
        
        //Pause Ext khoi queue
        QueuePauseAction qp = new QueuePauseAction();
        qp.setQueue("900");
        qp.setInterface("SIP/8000");
        qp.setPaused(false);
        QueueStatusAction qs = new QueueStatusAction();
        
        
        //remove Ext khoi queue
        QueueRemoveAction qr = new QueueRemoveAction();
        qr.setQueue("900");
        qr.setInterface("SIP/8000"); 
        
        SipShowPeerAction sip = new  SipShowPeerAction();
        sip.setPeer("SIP/8000");
        
        SipPeersAction sippeer = new SipPeersAction();
        sippeer.setActionId("SIP/8000");
        
        AgentLogoffAction agentOff = new AgentLogoffAction();
        agentOff.setAgent("SIP/8000");
        agentOff.setSoft(true);
        
        
        
        

        // connect to Asterisk and log in
        managerConnection.login();
        System.out.println("hostname: "+managerConnection.getHostname().toString());//lay dia chi ip server asterisk
        System.out.println("getLocalAddress: "+managerConnection.getLocalAddress().toString());//lay dia chi ip cua CTIServer
        System.out.println("port: "+managerConnection.getLocalPort());// port tren CTIServer

        // send the originate action and wait for a maximum of 30 seconds for Asterisk
        // to send a reply
//        originateResponse = managerConnection.sendAction(originateAction, 50000);
        originateResponse = managerConnection.sendAction(agentOff);

        // print out whether the originate Success or Error 
        System.out.println(originateResponse.getResponse());
        
        

        // and finally log off and disconnect
        managerConnection.logoff();
    }

    public static void main(String[] args) throws Exception{
        HelloManager helloManager;

        helloManager = new HelloManager();
        helloManager.run();
    }
}
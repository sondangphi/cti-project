import java.io.IOException;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.command.ChannelStatusCommand;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.AgentLogoffAction;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.action.QueueRemoveAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.action.RedirectAction;
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
                "172.168.10.205", "manager", "pa55w0rd");

        this.managerConnection = factory.createManagerConnection();
    }

    public void run() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException, AgiException{
    	
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
        qa.setQueue("8882");
        qa.setInterface("SIP/8015");
        qa.setPenalty(99);
        qa.setMemberName("Agent/8015");       
        
        //Pause Ext khoi queue
        QueuePauseAction qp = new QueuePauseAction();
        qp.setQueue("8882");
        qp.setInterface("SIP/8015");
//        qp.setPaused(true);
        qp.setPaused(false);
        
        QueueStatusAction qs = new QueueStatusAction();
        
        
        //remove Ext khoi queue
        QueueRemoveAction qr = new QueueRemoveAction();
        qr.setQueue("8882");
        qr.setInterface("SIP/8015"); 
        
        SipShowPeerAction sip = new  SipShowPeerAction();
        sip.setPeer("SIP/8000");
        
        SipPeersAction sippeer = new SipPeersAction();
        sippeer.setActionId("SIP/8000");
        
        AgentLogoffAction agentOff = new AgentLogoffAction();
        agentOff.setAgent("SIP/8000");
        agentOff.setSoft(true);
        
        RedirectAction ra = new RedirectAction("Local/8009@from-internal", "from-internal", "8000", 1);
        
        HangupAction ha = new HangupAction("Agent/9000", 1);
//        AgiChannel ac = null;
//        ac.getVariable("SIP/8000");
 
        // connect to Asterisk and log in
        managerConnection.login();
        System.out.println("hostname: "+managerConnection.getHostname().toString());//lay dia chi ip server asterisk
        System.out.println("getLocalAddress: "+managerConnection.getLocalAddress().toString());//lay dia chi ip cua CTIServer
        System.out.println("port: "+managerConnection.getLocalPort());// port tren CTIServer

        //wait a moment time
//        Thread.sleep(20000);
        
        
        // send the originate action and wait for a maximum of 30 seconds for Asterisk
        // to send a reply
//        originateResponse = managerConnection.sendAction(originateAction, 50000);
        originateResponse = managerConnection.sendAction(qa,10000);
//        System.out.println("getchannel: "+originateAction.getChannel().toString());

//        Thread.sleep(20000);
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
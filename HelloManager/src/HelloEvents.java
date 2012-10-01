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
import org.asteriskjava.manager.event.AgentCompleteEvent;
import org.asteriskjava.manager.event.AgentLoginEvent;
import org.asteriskjava.manager.event.AgentLogoffEvent;
import org.asteriskjava.manager.event.AgentRingNoAnswerEvent;
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
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.TransferEvent;
import org.asteriskjava.manager.event.UnlinkEvent;

/* *
 * Ket noi voi Asterisk Server su dung API
 * Lang nghe cac su kien tu Asterisk Server gui ve, co the loc cac thong tin gui ve bang cach
 * su dung ham EventsAction("su kien can bat, phan biet cac su kien bang dau ','")
 * 
 * */
public class HelloEvents implements ManagerEventListener
{
    private ManagerConnection managerConnection;
    Writer output = null;
    String text = " \t";
    File file = new File("write.txt");       
    

    public HelloEvents() throws IOException
    {
//        ManagerConnectionFactory factory = new ManagerConnectionFactory(
//                "172.168.10.100", "manager", "pa55w0rd");
        ManagerConnectionFactory factory = new ManagerConnectionFactory("172.168.10.205", "manager", "pa55w0rd");        
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
        Thread.sleep(360000);
        
        output.close();

        // and finally log off and disconnect
        managerConnection.logoff();
    }

    public void onManagerEvent(ManagerEvent event)
    {
        // just print received events        

    	/*A NewChannelEvent is triggered when a new channel is created.*/
        if(event instanceof NewChannelEvent){        	
        	NewChannelEvent channelEvent = (NewChannelEvent)event;
        	System.out.println("***********************\t NewChannelEvent\t ***********************");
        	System.out.println("getAccountCode \t"+channelEvent.getAccountCode());
        	System.out.println("getCallerId \t"+channelEvent.getCallerId());
        	System.out.println("getCallerIdName \t"+channelEvent.getCallerIdName());
        	System.out.println("getCallerIdNum \t"+channelEvent.getCallerIdNum());
        	System.out.println("getChannel \t"+channelEvent.getChannel());
        	System.out.println("getChannelStateDesc \t"+channelEvent.getChannelStateDesc());
        	System.out.println("getContext \t"+channelEvent.getContext());
        	System.out.println("getExten \t"+channelEvent.getExten());
        	System.out.println("getFile \t"+channelEvent.getFile());
        	System.out.println("getFunc \t"+channelEvent.getFunc());
        	System.out.println("getPrivilege \t"+channelEvent.getPrivilege());
        	System.out.println("getServer \t"+channelEvent.getServer());
        	System.out.println("getState \t"+channelEvent.getState());
        	System.out.println("getUniqueId \t"+channelEvent.getUniqueId());
        	System.out.println("getChannelState \t"+channelEvent.getChannelState());
        	System.out.println("getSequenceNumber \t"+channelEvent.getSequenceNumber());
        	System.out.println("getDateReceived \t"+channelEvent.getDateReceived().toString());
        	String channel = channelEvent.getChannel();
        	System.out.println("Channel is:\t"+channel.substring(0, channel.indexOf("-")));
        }
        
        /*A dial event is triggered whenever a phone attempts to dial someone.*/
        if(event instanceof DialEvent){
        	DialEvent dial = (DialEvent)event;        	
        	System.out.println("***********************\t DialEvent\t ***********************");
        	System.out.println("getCallerId \t"+dial.getCallerId());
        	System.out.println("getCallerIdName \t"+dial.getCallerIdName());
        	System.out.println("getCallerIdNum \t"+dial.getCallerIdNum());
        	System.out.println("getChannel \t"+dial.getChannel());
        	System.out.println("getDestination \t"+dial.getDestination());
        	System.out.println("getDestUniqueId \t"+dial.getDestUniqueId());
        	System.out.println("getDialStatus \t"+dial.getDialStatus());
        	System.out.println("getDialString \t"+dial.getDialString());
        	System.out.println("getFile \t"+dial.getFile());
        	System.out.println("getFunc \t"+dial.getFunc());
        	System.out.println("getPrivilege \t"+dial.getPrivilege());
        	System.out.println("getServer \t"+dial.getServer());
        	System.out.println("getSrc \t"+dial.getSrc());
        	System.out.println("getSrcUniqueId \t"+dial.getSrcUniqueId());
        	System.out.println("getSubEvent \t"+dial.getSubEvent());
        	System.out.println("getUniqueId \t"+dial.getUniqueId());
        }
        
        /*A NewStateEvent is triggered when the state of a channel has changed.*/
        if(event instanceof NewStateEvent){
        	NewStateEvent stateEvent = (NewStateEvent)event;
        	System.out.println("***********************\t NewStateEvent\t ***********************");
        	System.out.println("getCallerId \t"+stateEvent.getCallerId());
        	System.out.println("getCallerIdName \t"+stateEvent.getCallerIdName());
        	System.out.println("getCallerIdNum \t"+stateEvent.getCallerIdNum());
        	System.out.println("getChannel \t"+stateEvent.getChannel());
        	System.out.println("getChannelStateDesc \t"+stateEvent.getChannelStateDesc());
        	System.out.println("getUniqueId \t"+stateEvent.getUniqueId());
        }
        
        /*Response to an OriginateAction.*/
        if(event instanceof OriginateResponseEvent){
        	OriginateResponseEvent oriEvent = (OriginateResponseEvent)event;
        	System.out.println("***********************\t OriginateResponseEvent\t ***********************");
        	System.out.println("getActionId \t"+oriEvent.getActionId());
        	System.out.println("getCallerIdName \t"+oriEvent.getCallerIdName());
        	System.out.println("getCallerIdNum \t"+oriEvent.getCallerIdNum());
        	System.out.println(" \t"+oriEvent.getChannel());
        	System.out.println(" \t"+oriEvent.getContext());
        	System.out.println(" \t"+oriEvent.getExten());
        	System.out.println(" \t"+oriEvent.getInternalActionId());
        	System.out.println(" \t"+oriEvent.getReason());
        }
        
        /*A NewExtenEvent is triggered when a channel is connected to a new extension.*/
//        if(event instanceof NewExtenEvent){
//        	NewExtenEvent extenEvent = (NewExtenEvent)event;
//        	System.out.println("***********************\t NewExtenEvent\t ***********************");
//        	System.out.println("getAppData \t"+extenEvent.getAppData());
//        	System.out.println("getApplication \t"+extenEvent.getApplication());
//        	System.out.println("getChannel \t"+extenEvent.getChannel());
//        	System.out.println("getContext \t"+extenEvent.getContext());
//        	System.out.println("getUniqueId \t"+extenEvent.getUniqueId());
//        	System.out.println("getSource \t"+extenEvent.getSource().toString());        	
//        }
        
        /*A LinkEvent is triggered when two voice channels are linked together and voice data exchange commences.
         * Several Link events may be seen for a single call. 
         * This can occur when Asterisk fails to setup a native bridge for the call.
         * This is when Asterisk must sit between two telephones and perform CODEC conversion on their behalf.*/
//        if(event instanceof LinkEvent){
//        	LinkEvent linkEvent = (LinkEvent)event;
//        	System.out.println("***********************\t LinkEvent\t ***********************");
//        	System.out.println("getBridgeState \t"+linkEvent.getBridgeState());
//        	System.out.println("getBridgeType \t"+linkEvent.getBridgeType());
//        	System.out.println("getCallerId1 \t"+linkEvent.getCallerId1());
//        	System.out.println("getCallerId2 \t"+linkEvent.getCallerId2());
//        	System.out.println("getChannel1 \t"+linkEvent.getChannel1());
//        	System.out.println("getChannel2 \t"+linkEvent.getChannel2());
//        	System.out.println("getUniqueId1 \t"+linkEvent.getUniqueId1());
//        	System.out.println("getUniqueId2 \t"+linkEvent.getUniqueId2());
//        	System.out.println("getDateReceived \t"+linkEvent.getDateReceived().toString());
//        }        
                
        /* An UnlinkEvent is triggered when a link between two voice channels is discontinued, 
         * for example, just before call completion.*/
//        if(event instanceof UnlinkEvent){
//        	UnlinkEvent unlinkEvent = (UnlinkEvent)event;
//        	System.out.println("***********************\t UnlinkEvent\t ***********************");
//        	System.out.println("getBridgeState \t"+unlinkEvent.getBridgeState());
//        	System.out.println("getBridgeType \t"+unlinkEvent.getBridgeType());
//        	System.out.println("getCallerId1 \t"+unlinkEvent.getCallerId1());
//        	System.out.println("getCallerId2 \t"+unlinkEvent.getCallerId2());
//        	System.out.println("getChannel1 \t"+unlinkEvent.getChannel1());
//        	System.out.println("getChannel2 \t"+unlinkEvent.getChannel2());
//        	System.out.println("getUniqueId1 \t"+unlinkEvent.getUniqueId1());
//        	System.out.println("getUniqueId2 \t"+unlinkEvent.getUniqueId2());
//        	System.out.println("getDateReceived \t"+unlinkEvent.getDateReceived().toString());
//        }        
        
        /* A BridgeEvent is triggered when a link between two voice channels is established ("Link") or discontinued ("Unlink").
         * As of Asterisk 1.6 the Bridge event is reported directly by Asterisk. 
         * Asterisk versions up to 1.4 report individual events: LinkEvent and UnlinkEvent.
         * For maximum compatibily do not use the Link and Unlink events in your code. 
         * Just use the Bridge event and check for isLink() and isUnlink().*/
        if(event instanceof BridgeEvent){
        	BridgeEvent briEvent = (BridgeEvent)event;
        	String state = null;
        	if(briEvent.isLink()){        		
            	System.out.println("***********************\t BridgeEvent Link\t ***********************");
            	state = briEvent.BRIDGE_STATE_LINK;
            	System.out.println("state\t"+state);
            	System.out.println("getBridgeState \t"+briEvent.getBridgeState());
            	System.out.println("getBridgeType \t"+briEvent.getBridgeType());
            	System.out.println("getCallerId1 \t"+briEvent.getCallerId1());
            	System.out.println("getCallerId2 \t"+briEvent.getCallerId2());
            	System.out.println("getChannel1 \t"+briEvent.getChannel1());
            	System.out.println("getChannel2 \t"+briEvent.getChannel2());
            	System.out.println("getUniqueId1 \t"+briEvent.getUniqueId1());
            	System.out.println("getUniqueId2 \t"+briEvent.getUniqueId2());
            	System.out.println("getDateReceived \t"+briEvent.getDateReceived().toString());
        	}else{
            	System.out.println("***********************\t BridgeEvent UnLink\t ***********************");
            	state = briEvent.BRIDGE_STATE_UNLINK;
            	System.out.println("state\t"+state);
            	System.out.println("getBridgeState \t"+briEvent.getBridgeState());
            	System.out.println("getBridgeType \t"+briEvent.getBridgeType());
            	System.out.println("getCallerId1 \t"+briEvent.getCallerId1());
            	System.out.println("getCallerId2 \t"+briEvent.getCallerId2());
            	System.out.println("getChannel1 \t"+briEvent.getChannel1());
            	System.out.println("getChannel2 \t"+briEvent.getChannel2());
            	System.out.println("getUniqueId1 \t"+briEvent.getUniqueId1());
            	System.out.println("getUniqueId2 \t"+briEvent.getUniqueId2());
            	System.out.println("getDateReceived \t"+briEvent.getDateReceived().toString());
        	}

        }
        
        
        /*A HoldEvent is triggered when a channel is put on hold (or no longer on hold).*/
        if(event instanceof HoldEvent){
        	HoldEvent holdEvent = (HoldEvent)event;
        	if(holdEvent.isHold()){
            	System.out.println("***********************\t HoldEvent\t ***********************");
            	System.out.println("getChannel \t"+holdEvent.getChannel());
            	System.out.println("getUniqueId \t"+holdEvent.getUniqueId());
            	System.out.println("getLine \t"+holdEvent.getLine());
            	System.out.println("getStatus \t"+holdEvent.getStatus().toString());
        	}else{
            	System.out.println("***********************\t UNHoldEvent\t ***********************");
            	System.out.println("getChannel \t"+holdEvent.getChannel());
            	System.out.println("getUniqueId \t"+holdEvent.getUniqueId());
            	System.out.println("getLine \t"+holdEvent.getLine());
            	System.out.println("getStatus \t"+holdEvent.getStatus().toString());
        	}
        }
        
        
        /*A HoldEvent is triggered when a channel is put on hold (or no longer on hold).*/
        if(event instanceof MusicOnHoldEvent){
        	MusicOnHoldEvent musicEvent = (MusicOnHoldEvent)event;
        	if(musicEvent.isStart()){
            	System.out.println("***********************\t HoldEvent\t ***********************");
            	System.out.println("getChannel \t"+musicEvent.getChannel());
            	System.out.println("getUniqueId \t"+musicEvent.getUniqueId());
            	System.out.println("getLine \t"+musicEvent.getLine());
            	System.out.println("getStatus \t"+musicEvent.getDateReceived());
        	}else if(musicEvent.isStop()){
            	System.out.println("***********************\t UNHoldEvent\t ***********************");
            	System.out.println("getChannel \t"+musicEvent.getChannel());
            	System.out.println("getUniqueId \t"+musicEvent.getUniqueId());
            	System.out.println("getLine \t"+musicEvent.getLine());
            	System.out.println("getStatus \t"+musicEvent.getDateReceived());
        	}
        }
        
        
        /*A HoldedCallEvent is triggered when a channel is put on hold.*/
        if(event instanceof HoldedCallEvent){
        	HoldedCallEvent holdcallEvent = (HoldedCallEvent)event;
        	System.out.println("***********************\t HoldedCallEvent\t ***********************");
        	System.out.println("getChannel1 \t"+holdcallEvent.getChannel1());
        	System.out.println("getChannel2 \t"+holdcallEvent.getChannel2());
        	System.out.println("getUniqueId1 \t"+holdcallEvent.getUniqueId1());
        	System.out.println("getUniqueId2 \t"+holdcallEvent.getUniqueId2());
        	System.out.println("getSequenceNumber \t"+holdcallEvent.getSequenceNumber());
        }
        
        /* A TransferEvent is triggered when a SIP channel is transfered.*/
        if(event instanceof TransferEvent){
        	TransferEvent transEvent = (TransferEvent)event;
        	System.out.println("***********************\t TransferEvent\t ***********************");
        	System.out.println(" \t"+transEvent.getChannel());
        	System.out.println(" \t"+transEvent.getSipCallId());
        	System.out.println(" \t"+transEvent.getTargetChannel());
        	System.out.println(" \t"+transEvent.getTargetUniqueId());
        	System.out.println(" \t"+transEvent.getTransferContext());
        	System.out.println(" \t"+transEvent.getTransferExten());
        	System.out.println(" \t"+transEvent.getTransferMethod());
        	System.out.println(" \t"+transEvent.getTransferType());
        	System.out.println(" \t"+transEvent.getUniqueId());
        	System.out.println(" \t"+transEvent.getTransfer2Parking().toString());
        }
        
        /* A HangupEvent is triggered when a channel is hung up.*/
        if(event instanceof HangupEvent){
        	HangupEvent hangEvent = (HangupEvent)event;
        	System.out.println("***********************\t HangupEvent\t ***********************");
        	System.out.println("getCallerId \t"+hangEvent.getCallerId());
        	System.out.println("getCallerIdName \t"+hangEvent.getCallerIdName());
        	System.out.println("getCallerIdNum \t"+hangEvent.getCallerIdNum());
        	System.out.println("getCauseTxt \t"+hangEvent.getCauseTxt());
        	System.out.println("getChannel \t"+hangEvent.getChannel());
        	System.out.println("getUniqueId \t"+hangEvent.getUniqueId());
        	System.out.println("getCause \t"+hangEvent.getCause());        	
        }
        
        /* An AgentsCompleteEvent is triggered after the state of all agents has been 
         * reported in response to an AgentsAction.*/
        if(event instanceof AgentCompleteEvent){
        	AgentCompleteEvent comEvent= (AgentCompleteEvent)event;
        	System.out.println("***********************\t AgentCompleteEvent\t ***********************");
        	System.out.println("getChannel \t"+comEvent.getChannel());
        	System.out.println("getMember \t"+comEvent.getMember());
        	System.out.println("getMemberName \t"+comEvent.getMemberName());
        	System.out.println("getQueue \t"+comEvent.getQueue());
        	System.out.println("getReason \t"+comEvent.getReason());
        	System.out.println("getUniqueId \t"+comEvent.getUniqueId());
        	System.out.println("getTalkTime \t"+comEvent.getTalkTime());         	
        }
        
        /* An AgentRingNoAnswerEvent is triggered when a call is routed to 
         * an agent but the agent does not answer the call.*/
        if(event instanceof AgentRingNoAnswerEvent){
        	AgentRingNoAnswerEvent noAnsEvent= (AgentRingNoAnswerEvent)event;
        	System.out.println("***********************\t AgentRingNoAnswerEvent\t ***********************");
        	System.out.println(" \t"+noAnsEvent.getChannel());
        	System.out.println(" \t"+noAnsEvent.getMember());
        	System.out.println(" \t"+noAnsEvent.getMemberName());
        	System.out.println(" \t"+noAnsEvent.getQueue());
        	System.out.println(" \t"+noAnsEvent.getRingtime());
        	System.out.println(" \t"+noAnsEvent.getUniqueId());
        	System.out.println(" \t"+noAnsEvent.getSource());         	
        }
        
        /* An AgentLoginEvent is triggered when an agent is successfully logged in using AgentLogin.*/
        if(event instanceof AgentLoginEvent){
        	AgentLoginEvent loginEvent= (AgentLoginEvent)event;
        	System.out.println("***********************\t AgentLoginEvent\t ***********************");
        	System.out.println("getAgent \t"+loginEvent.getAgent());
        	System.out.println("getChannel \t"+loginEvent.getChannel());
        	System.out.println("getLoginChan \t"+loginEvent.getLoginChan());
        	System.out.println("getSource \t"+loginEvent.getSource());
        	System.out.println("getUniqueId \t"+loginEvent.getUniqueId());
        	System.out.println("getSource \t"+loginEvent.getSource());         	
        }
        
        /* An AgentCallbackLogoffEvent is triggered when an agent that previously 
         * logged in using AgentLogin is logged of.*/
        if(event instanceof AgentLogoffEvent){
        	AgentLogoffEvent logoffEvent= (AgentLogoffEvent)event;
        	System.out.println("***********************\t AgentCompleteEvent\t ***********************");
        	System.out.println("getAgent \t"+logoffEvent.getAgent());
        	System.out.println("getLoginTime \t"+logoffEvent.getLoginTime());
        	System.out.println("getPrivilege \t"+logoffEvent.getPrivilege());
        	System.out.println("getSequenceNumber \t"+logoffEvent.getSequenceNumber());
        	System.out.println("getDateReceived \t"+logoffEvent.getDateReceived());
        	System.out.println("getUniqueId \t"+logoffEvent.getUniqueId());
        	System.out.println("getSource \t"+logoffEvent.getSource());         	
        }
        
        /* A ShutdownEvent is triggered when the asterisk server is shut down or restarted.*/
	    if(event instanceof ShutdownEvent){
	    	ShutdownEvent shutEvent= (ShutdownEvent)event;
	    	System.out.println("***********************\t ShutdownEvent\t ***********************");
	    	System.out.println(" \t"+shutEvent.getShutdown());
	    	System.out.println(" \t"+shutEvent.getTimestamp());
	    	System.out.println(" \t"+shutEvent.getDateReceived());
	    	System.out.println(" \t"+shutEvent.getRestart().toString());    	
	    	System.out.println(" \t"+shutEvent.getSource()); 
	    }
        
	    /* A ReloadEvent is triggerd when the reload console command is executed or the Asterisk server is started.  */
	    if(event instanceof ReloadEvent){
	    	ReloadEvent reloadEvent= (ReloadEvent)event;
	    	System.out.println("***********************\t ReloadEvent\t ***********************");
	    	System.out.println(" \t"+reloadEvent.getMessage());
	    	System.out.println(" \t"+reloadEvent.getTimestamp());
	    	System.out.println(" \t"+reloadEvent.getDateReceived());
	    	System.out.println(" \t"+reloadEvent.getModule());    	
	    	System.out.println(" \t"+reloadEvent.getSource());
	    	System.out.println(" \t"+reloadEvent.getStatus());
	    }
                       
	    /* A JoinEvent is triggered when a channel joines a queue.*/
        if(event instanceof JoinEvent){
        	JoinEvent joinEvent= (JoinEvent)event;
        	System.out.println("***********************\t JoinEvent\t ***********************");
        	System.out.println(" \t"+joinEvent.getChannel());
        	System.out.println(" \t"+joinEvent.getCallerId());
        	System.out.println(" \t"+joinEvent.getCallerIdName());
        	System.out.println(" \t"+joinEvent.getQueue());
        	System.out.println(" \t"+joinEvent.getCallerIdNum());
        	System.out.println(" \t"+joinEvent.getUniqueId());
        	System.out.println(" \t"+joinEvent.getSource());         	
        	System.out.println(" \t"+joinEvent.getCount());
        	System.out.println(" \t"+joinEvent.getDateReceived());
        }
        
        /* A ConnectEvent is triggered after successful login to the Asterisk server.*/
	    if(event instanceof ConnectEvent){
	    	ConnectEvent conEvent= (ConnectEvent)event;
	    	System.out.println("***********************\t ConnectEvent\t ***********************");
	    	System.out.println(" \t"+conEvent.getTimestamp());
	    	System.out.println(" \t"+conEvent.getDateReceived());
	    	System.out.println(" \t"+conEvent.getServer());    	
	    	System.out.println(" \t"+conEvent.getSource());
	    }
	    
	    /*A DisconnectEvent is triggered when the connection to the asterisk server is lost.*/
	    if(event instanceof DisconnectEvent){
	    	DisconnectEvent disconEvent= (DisconnectEvent)event;
	    	System.out.println("***********************\t disConnectEvent\t ***********************");
	    	System.out.println(" \t"+disconEvent.getTimestamp());
	    	System.out.println(" \t"+disconEvent.getDateReceived());
	    	System.out.println(" \t"+disconEvent.getServer());    	
	    	System.out.println(" \t"+disconEvent.getSource());
	    }
	    
        try{
//        	System.out.println("thong tin: "+event);
        	text = event.toString()+"\r\n";
            output.write(text);
        	
        }catch(Exception ex){
        	
        }
               
    }

    public static void main(String[] args) throws Exception
    {
        HelloEvents helloEvents;
        helloEvents = new HelloEvents();
        helloEvents.run();
    }
}
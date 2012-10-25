package org.asterisk.utility;

//import java.io.BufferedInputStream;
import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.asterisk.main.ctiServer;
import org.asterisk.model.AgentObject;
import org.asterisk.model.CallObject;
import org.asterisk.utility.*;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.action.QueueRemoveAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.AgentCalledEvent;
import org.asteriskjava.manager.event.AgentCompleteEvent;
import org.asteriskjava.manager.event.AgentConnectEvent;
import org.asteriskjava.manager.event.AgentLoginEvent;
import org.asteriskjava.manager.event.AgentLogoffEvent;
import org.asteriskjava.manager.event.AgentRingNoAnswerEvent;
import org.asteriskjava.manager.event.BridgeEvent;
import org.asteriskjava.manager.event.DialEvent;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.HoldEvent;
import org.asteriskjava.manager.event.HoldedCallEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.MusicOnHoldEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.NewStateEvent;
import org.asteriskjava.manager.event.OriginateResponseEvent;
import org.asteriskjava.manager.event.QueueCallerAbandonEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.TransferEvent;
import org.asteriskjava.manager.response.ManagerResponse;

public class ManagerAgent implements Runnable,ManagerEventListener {

	
	private ctiServer ctiS;
        private AgentListen alisten;
	private Socket clientSocket;
	private String fromAgent;
	private Thread thread;
	private BufferedReader   inputStream;
	private int flag;
	private Utility uti;
	private ManagerConnection manager;
        private QueueRemoveAction removeQueue;
	private String result;
	private AgentObject agent;
	private String addressAgent;
	private Managerdb mdb_agent;
	private int penalty = 99;
	private String loginAct = "";
	private String logoutAct = "";
	private String pauseAct = "";
	private String unpauseAct = "";
        private String pauseSession = "";
        private CallObject callObject;
	public ManagerAgent(){

	}
	
	public ManagerAgent(AgentListen alis,Socket client, Managerdb db) throws IOException{
            try{
                alisten = alis;
                clientSocket = client;
                mdb_agent = db;
                inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                thread = new Thread(this);
                thread.start();
            }catch(Exception se){
                uti.writeAgentLog("Cannot connect to agent");
                closeConnect();
            }
	}
	public ManagerAgent(ctiServer ctiserver,Socket client) throws IOException{
            try{
                ctiS = ctiserver;
                clientSocket = client;                
                inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                thread = new Thread(this);
                thread.start();
            }catch(Exception se){
                uti.writeAgentLog("Cannot connect to agent");
                closeConnect();
            }
	}        
	
	@Override
	public void run() {
		// TODO Auto-generated method stub		
		try {
                    uti = new Utility();
                    manager = alisten.manager;
                    manager.addEventListener(this);
                    manager.sendAction(new StatusAction());
                    //connect to datase cti_database to get data
//                    mdb_agent = new Managerdb("cti_database");
//                    mdb_agent.connect();
                    addressAgent = clientSocket.getInetAddress().toString();
                    while(thread != null && clientSocket.isConnected()){
                        fromAgent = inputStream.readLine();
                        uti.writeAgentLog("request from agent \t"+addressAgent+"\t"+fromAgent);
                        ArrayList<String> cmdList = uti.getList(fromAgent);
                        flag = Integer.parseInt(cmdList.get(0));				
                        switch(flag){
                            case 100:  
                            uti.writeAgentLog("agent login \t"+addressAgent+"\t"+fromAgent);
                            getAgent(cmdList);	
                            mdb_agent.database = "cti_database";
                            if(mdb_agent.checkLogin(agent.getAgent(), agent.getPass(), agent.getRole())){
                                if(mdb_agent.checkStatus(agent.getInterface())){
                                QueueAddAction qAdd = addQueue(agent.getAgent(), agent.getInterface(), agent.getQueue(),agent.getPenalty());
                                result = manager.sendAction(qAdd, 2000).getResponse().toString();
                                    if(result.equalsIgnoreCase("success")){
                                        mdb_agent.updateStatus(agent.getAgent(), agent.getInterface(), agent.getQueue());
                                        mdb_agent.loginAction(agent.getSesion(),agent.getAgent(), agent.getInterface(), agent.getQueue());
                                        System.out.println("LOGIN success");
                                        sendToAgent("LOGINSUCC");
                                    }else{
                                        sendToAgent("LOGINFAIL");
                                        System.out.println("LOGIN fail");
                                        closeConnect();
                                    }
                                }else{
                                    System.out.println("Interface Already login");
                                    closeConnect();
                                }
                            }else {
                                System.out.println("wrong user OR pass");
                                sendToAgent("LOGINFAIL");
                                closeConnect();
                            }
                            break;
		            case 102:  
		            	if(agent != null){
                                    removeQueue = removeQueue(agent.getInterface(), agent.getQueue());
                                    result = manager.sendAction(removeQueue).getResponse().toString();
                                    if(result.equalsIgnoreCase("success")){
                                        System.out.println("remove queue success");
                                        mdb_agent.updateStatus(agent.getAgent(), "NULL", "NULL");		
		            		mdb_agent.logoutAction(agent.getSesion(), agent.getAgent());
                                        sendToAgent("LOGOUTSUCC");
                                        System.out.println("LOGOUTSUCC");
                                        Thread.sleep(2000);
                                        agent = null;
                                        closeConnect();                                        
                                    }else{		            			
                                        sendToAgent("LOGOUTFAIL");
                                    }		            		
		            	}
                                System.out.println("logout result: "+result);			            	
                            break;
		            case 104:
		            	QueuePauseAction pauseAction = null;
		            	if(cmdList.get(1).equalsIgnoreCase("off")){
                                    pauseAction = queuePause(agent.getInterface(), agent.getQueue(), true);		
                                    result = manager.sendAction(pauseAction).getResponse().toString();
                                    if(result.equalsIgnoreCase("success")){
                                        System.out.println("Pause queue success");
                                        pauseSession = uti.getSession();
                                        mdb_agent.pauseAction(pauseSession, agent.getAgent());
                                        sendToAgent("PAUSESUCC");
                                    }else{
                                        System.out.println("Pause queue fail");                                        
                                        sendToAgent("PAUSEFAIL");
                                    }		            			
		            	}else if(cmdList.get(1).equalsIgnoreCase("on")){
                                    pauseAction = queuePause(agent.getInterface(), agent.getQueue(), false);
                                    result = manager.sendAction(pauseAction).getResponse().toString();
                                    if(result.equalsIgnoreCase("success")){
                                        System.out.println("unPause queue success");	
                                        mdb_agent.unpauseAction(pauseSession, agent.getAgent());
                                        sendToAgent("UNPAUSESUCC");
                                    }else{
                                        System.out.println("unPause queue fail");
                                        sendToAgent("UNPAUSEFAIL");
                                    }		            			
		            	}
                            break;
		            case 106:  		
                                //hangup the call form agent request
                                ManagerResponse managerRes;
                                HangupAction hangAct = new HangupAction();
                                hangAct.setChannel(agent.getInterface());
                                managerRes =  manager.sendAction(hangAct, 1000);
                                System.out.println("managerRes\t"+managerRes.getResponse());
                                
                            break;
		            case 108:  
                            break;
		            case 110:  
                            break;
		            case 112:  
                            break;	       		 		
		            default: 
		            	System.out.println("(invalid) " +fromAgent);
                            break;					
                        }											
                    }
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try {                            
                            removeQueue = removeQueue(agent.getInterface(), agent.getQueue());
                            result = manager.sendAction(removeQueue).getResponse().toString();
                            mdb_agent.updateStatus(agent.getAgent(), "", "");		
                            mdb_agent.logoutAction(agent.getSesion(), agent.getAgent());   
                            agent = null;
                            closeConnect();
                        } catch (Exception e1) {}
                        //logout for agent exit                                               
			System.out.println("Close connect from client\t"+e);
		} 
                catch (TimeoutException e) {
			e.printStackTrace();
		} 
                catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
                catch (SQLException e) {
			e.printStackTrace();
		} 
                catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getString(String cmd){
            ArrayList<String> list =  new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens()){
                list.add(st.nextToken());
            }
            return list;
        }
	
	public OriginateAction getAction(String channel,String queue){
		OriginateAction originate = new OriginateAction();
		
		return originate;
	}
	
	public QueueAddAction addQueue(String agent, String iface, String queue, int penalty){
            QueueAddAction queueAdd = new QueueAddAction();
            queueAdd.setMemberName(agent);		
            queueAdd.setInterface(iface);
            queueAdd.setQueue(queue);
            queueAdd.setPenalty(penalty);		
            return queueAdd;
	}
	
	public QueueRemoveAction removeQueue(String iface, String queue){
            QueueRemoveAction queueRemove = new QueueRemoveAction();
            queueRemove.setInterface(iface);
            queueRemove.setQueue(queue);		
            return queueRemove;
	}
	
	public QueuePauseAction queuePause(String iface, String queue, boolean pause){
            QueuePauseAction queuePause = new QueuePauseAction();
            queuePause.setQueue(queue);
            queuePause.setInterface(iface);
            queuePause.setPaused(pause);
            return queuePause;
	}
	
	public void getAgent(ArrayList<String> list){
            ArrayList<String> cmdList = list;
            agent = new AgentObject();
            agent.setSocket(clientSocket);
            agent.setAgent(cmdList.get(1));
            agent.setPass(cmdList.get(2));
            agent.setInterface(cmdList.get(3));
            agent.setQueue(cmdList.get(4));	
            agent.setPenalty(penalty);
            agent.setRole(cmdList.get(5));
            agent.setSession(uti.getSession());
	}
	
	public void closeConnect() throws IOException{
            System.out.println("start close session");
            if(thread!=null){
                thread.interrupt();
                thread = null;
                System.out.println("close thread");
            }
            if(!clientSocket.isClosed()){
                clientSocket.close();
                inputStream.close();
                System.out.println("close socket");
            }
            System.out.println("finish close session");
	}
	
	public void sendToAgent(String data) throws IOException{
            PrintWriter outToAgent = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outToAgent.println(data);
            outToAgent.flush();
	}

	@Override
	public void onManagerEvent(ManagerEvent event) {
		// TODO Auto-generated method stub
		try {
		        
	        /*A NewStateEvent is triggered when the state of a channel has changed.*/
//	        if(event instanceof NewStateEvent){
//                    NewStateEvent stateEvent = (NewStateEvent)event;
//                    System.out.println("***********************\t NewStateEvent\t ***********************");                    	                     
//                    String channel = stateEvent.getChannel();
//                    channel = channel.substring(0, channel.indexOf("-"));                    
//                    String iface = agent.getInterface();                                      
//                    if(iface.equalsIgnoreCase(channel)){     
//                        String state = stateEvent.getChannelStateDesc();
//                        String callerName = stateEvent.getCallerIdName();
//                        String callerNum = stateEvent.getCallerIdNum();
//                        String srcNum = channel.substring(channel.indexOf("/")+1, channel.length()); 
//                        if(state.equalsIgnoreCase("RINGING")){
//                            callObject = new CallObject();                        
//                            callObject.setsession(uti.getSession());   
//                            callObject.setcallerName(callerName);
//                            callObject.setcallerNumber(callerNum);
//                            callObject.setdesNum(srcNum);                            
////                            mdb_agent.writeDialLog(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "3");
//                            mdb_agent.enterQueue(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "3",callerNum);
//                            sendToAgent(state);
//                            System.out.println("state\t "+state); 
//                        }
//                        if(state.equalsIgnoreCase("UP") && callObject.getcallerNumber()!= null){
//                            mdb_agent.writeDialLog(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "4");
//                            sendToAgent(state);
//                            System.out.println("state\t "+state); 
//                        }    
//                        if(state.equalsIgnoreCase("RING")){
////                            sendToAgent(state);
//                            System.out.println("state\t "+state); 
//                        }       
//                        System.out.println("getsession\t "+callObject.getsession()); 
//                        System.out.println("getCallerIdName: "+stateEvent.getCallerIdName());
//                        System.out.println("getCallerIdNum: "+stateEvent.getCallerIdNum());
//                        System.out.println("chann: "+channel);
//                        System.out.println("exten: "+agent.getInterface());      
//                        System.out.println("getChannelStateDesc: "+stateEvent.getChannelStateDesc());
//                    }	        		
//	        }
            //enter queue
            if(event instanceof AgentCalledEvent){
                AgentCalledEvent callEvent = (AgentCalledEvent)event;
                System.out.println("***********************\t AgentCalledEvent\t ***********************");
                String iface = agent.getInterface();
                String agentCaller = callEvent.getAgentCalled();
                if(iface.equalsIgnoreCase(agentCaller)){
                    String callerName = callEvent.getCallerIdName();
                    String callerNum = callEvent.getCallerIdNum();
                    callObject = new CallObject();                        
                    callObject.setsession(uti.getSession());   
                    callObject.setcallerName(callerName);
                    callObject.setcallerNumber(callerNum);        
                    callObject.setdesNum("");//?????
                    mdb_agent.enterQueue(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "3",callerNum);
                    sendToAgent("RINGING");                                                         
                }              
            } 	        
            //connect queue
            if(event instanceof AgentConnectEvent){
                AgentConnectEvent connect= (AgentConnectEvent)event;
                System.out.println("***********************\t AgentConnectEvent\t ***********************");
                String iface = agent.getInterface();
                String agentCaller = connect.getMember();
                if(iface.equalsIgnoreCase(agentCaller)){
                    String ringtime = connect.getRingtime().toString();
//                    mdb_agent.writeDialLog(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "4");
                    mdb_agent.connectQueue(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "4", ringtime);
                    sendToAgent("UP"); 
                }                               	
            }      
	        /* An AgentsCompleteEvent is triggered after the state of all agents has been 
	         * reported in response to an AgentsAction.*/
	        if(event instanceof AgentCompleteEvent){
                    AgentCompleteEvent comEvent= (AgentCompleteEvent)event;
                    System.out.println("***********************\t AgentCompleteEvent\t ***********************");  
                    String iface = agent.getInterface();
                    String channel = comEvent.getMember();                                                        
                    if(iface.equalsIgnoreCase(channel)){
                        String reason = comEvent.getReason();
                        String talkTime = comEvent.getTalkTime().toString();
                        if(reason.equalsIgnoreCase("agent")){
                            mdb_agent.completeCall(callObject.getsession(), agent.getAgent(), agent.getInterface(), comEvent.getQueue(), "5",talkTime);
                            System.out.println("reason \t"+reason);
                        }else if(reason.equalsIgnoreCase("caller")){
                            mdb_agent.completeCall(callObject.getsession(), agent.getAgent(), agent.getInterface(), comEvent.getQueue(), "6",talkTime);
                            System.out.println("reason \t"+reason);
                        }
                        callObject = null;                      
                    }                    
	        }
	        
	        /* An AgentRingNoAnswerEvent is triggered when a call is routed to 
	         * an agent but the agent does not answer the call.*/
	        if(event instanceof AgentRingNoAnswerEvent){
                    AgentRingNoAnswerEvent noAnsEvent= (AgentRingNoAnswerEvent)event;
                    System.out.println("***********************\t AgentRingNoAnswerEvent\t ***********************");    
                    String iface = agent.getInterface();
                    String channel = noAnsEvent.getMember();                     
                    if(iface.equalsIgnoreCase(channel)){
                        String ringTime = String.valueOf(noAnsEvent.getRingtime()/1000);
                        System.out.println("ringTime\t"+ringTime);
                        mdb_agent.ringNoans(callObject.getsession(), agent.getAgent(), agent.getInterface(), agent.getQueue(), "21", ringTime);
                        callObject = null;                                               
                    }                                                                                    
	        }       
                //abandon queue
                if(event instanceof QueueCallerAbandonEvent){
                    QueueCallerAbandonEvent abandon= (QueueCallerAbandonEvent)event;
                    System.out.println("***********************\t QueueCallerAbandonEvent\t ***********************"); 
                    System.out.println("getHoldTime: "+abandon.getHoldTime().toString());            
                    System.out.println("getPosition: "+abandon.getPosition().toString());            
                    System.out.println("getChannel: "+abandon.getChannel());            
                    System.out.println("getQueue: "+abandon.getQueue());            
                    System.out.println("getServer: "+abandon.getServer());            
                    System.out.println("getOriginalPosition: "+abandon.getOriginalPosition().toString());            
                }                 
	        /* A HangupEvent is triggered when a channel is hung up.*/
	        if(event instanceof HangupEvent){
                    HangupEvent hangEvent = (HangupEvent)event;
                    System.out.println("***********************\t HangupEvent\t ***********************");
                    String channel = hangEvent.getChannel();                    
                    channel = channel.substring(0, channel.indexOf("-"));
                    String iface = agent.getInterface();
                    if(iface.equalsIgnoreCase(channel)){  
                        sendToAgent("HANGUP");
                        System.out.println("chann: "+channel);
                        System.out.println("exten: "+agent.getInterface());
                        if(callObject != null){
                            System.out.println("write event hangup: "+channel);
                            mdb_agent.abandon(callObject.getsession(), agent.getAgent(), iface, agent.getQueue(), "7","");
                            callObject = null;                             
                        }else
                            System.out.println("callObject == null "+channel);
                    }                              	
	        }                
	        
	        /* A BridgeEvent is triggered when a link between two voice channels is established ("Link") or discontinued ("Unlink").
	         * As of Asterisk 1.6 the Bridge event is reported directly by Asterisk. 
	         * Asterisk versions up to 1.4 report individual events: LinkEvent and UnlinkEvent.
	         * For maximum compatibily do not use the Link and Unlink events in your code. 
	         * Just use the Bridge event and check for isLink() and isUnlink().*/
//	        if(event instanceof BridgeEvent){
//                    BridgeEvent briEvent = (BridgeEvent)event;
//                    String state = null;
//                    String channel = briEvent.getChannel2();
//                    channel = channel.substring(0, channel.indexOf("-"));     
//                    if(agent.getInterface().equals(channel)){
//                        System.out.println("map\t"+channel);
//                    }
//                    if(briEvent.isLink()){        		
//                        System.out.println("***********************\t BridgeEvent Link\t ***********************");
//                        state = briEvent.BRIDGE_STATE_LINK;
//                        System.out.println("state\t"+state);
//                        System.out.println("getBridgeState \t"+briEvent.getBridgeState());
//                        System.out.println("getBridgeType \t"+briEvent.getBridgeType());
//                        System.out.println("getCallerId1 \t"+briEvent.getCallerId1());
//                        System.out.println("getCallerId2 \t"+briEvent.getCallerId2());
//                        System.out.println("getChannel1 \t"+briEvent.getChannel1());
//                        System.out.println("getChannel2 \t"+briEvent.getChannel2());
//                        System.out.println("getUniqueId1 \t"+briEvent.getUniqueId1());
//                        System.out.println("getUniqueId2 \t"+briEvent.getUniqueId2());
//                        System.out.println("getDateReceived \t"+briEvent.getDateReceived().toString());
                        //begin answer, connect event
//                        callObject.setdesChannel(agent.getInterface());
//                        mdb_agent.writeDialLog(callObject.getsession(), agent.getAgent(), agent.getInterface(), agent.getQueue(), "4");                                                   
//                    }else{
//                        System.out.println("***********************\t BridgeEvent UnLink\t ***********************");
//                        state = briEvent.BRIDGE_STATE_UNLINK;
//                        System.out.println("state\t"+state);
//                        System.out.println("getBridgeState \t"+briEvent.getBridgeState());
//                        System.out.println("getBridgeType \t"+briEvent.getBridgeType());
//                        System.out.println("getCallerId1 \t"+briEvent.getCallerId1());
//                        System.out.println("getCallerId2 \t"+briEvent.getCallerId2());
//                        System.out.println("getChannel1 \t"+briEvent.getChannel1());
//                        System.out.println("getChannel2 \t"+briEvent.getChannel2());
//                        System.out.println("getUniqueId1 \t"+briEvent.getUniqueId1());
//                        System.out.println("getUniqueId2 \t"+briEvent.getUniqueId2());
//                        System.out.println("getDateReceived \t"+briEvent.getDateReceived().toString());                        
                        //hangup event
//                        mdb_agent.writeDialLog(callObject.getsession(), agent.getAgent(), agent.getInterface(), agent.getQueue(), "4");                                                   
//                        callObject = null;
//                    }

//	        }
	        
	        
	        /*A HoldEvent is triggered when a channel is put on hold (or no longer on hold).*/
//	        if(event instanceof HoldEvent){
//	        	HoldEvent holdEvent = (HoldEvent)event;
//	        	if(holdEvent.isHold()){
//	            	System.out.println("***********************\t HoldEvent\t ***********************");
//	            	System.out.println("getChannel \t"+holdEvent.getChannel());
//	            	System.out.println("getUniqueId \t"+holdEvent.getUniqueId());
//	            	System.out.println("getLine \t"+holdEvent.getLine());
//	            	System.out.println("getStatus \t"+holdEvent.getStatus().toString());
//	        	}else{
//	            	System.out.println("***********************\t UNHoldEvent\t ***********************");
//	            	System.out.println("getChannel \t"+holdEvent.getChannel());
//	            	System.out.println("getUniqueId \t"+holdEvent.getUniqueId());
//	            	System.out.println("getLine \t"+holdEvent.getLine());
//	            	System.out.println("getStatus \t"+holdEvent.getStatus().toString());
//	        	}
//	        }
	        
	        
	        /*A HoldEvent is triggered when a channel is put on hold (or no longer on hold).*/
//	        if(event instanceof MusicOnHoldEvent){
//	        	MusicOnHoldEvent musicEvent = (MusicOnHoldEvent)event;
//	        	if(musicEvent.isStart()){
//	            	System.out.println("***********************\t HoldEvent\t ***********************");
//	            	System.out.println("getChannel \t"+musicEvent.getChannel());
//	            	System.out.println("getUniqueId \t"+musicEvent.getUniqueId());
//	            	System.out.println("getLine \t"+musicEvent.getLine());
//	            	System.out.println("getStatus \t"+musicEvent.getDateReceived());
//	        	}else if(musicEvent.isStop()){
//	            	System.out.println("***********************\t UNHoldEvent\t ***********************");
//	            	System.out.println("getChannel \t"+musicEvent.getChannel());
//	            	System.out.println("getUniqueId \t"+musicEvent.getUniqueId());
//	            	System.out.println("getLine \t"+musicEvent.getLine());
//	            	System.out.println("getStatus \t"+musicEvent.getDateReceived());
//	        	}
//	        }
	        
	        /* A TransferEvent is triggered when a SIP channel is transfered.*/
//	        if(event instanceof TransferEvent){
//	        	TransferEvent transEvent = (TransferEvent)event;
//	        	System.out.println("***********************\t TransferEvent\t ***********************");
//	        	System.out.println(" \t"+transEvent.getChannel());
//	        	System.out.println(" \t"+transEvent.getSipCallId());
//	        	System.out.println(" \t"+transEvent.getTargetChannel());
//	        	System.out.println(" \t"+transEvent.getTargetUniqueId());
//	        	System.out.println(" \t"+transEvent.getTransferContext());
//	        	System.out.println(" \t"+transEvent.getTransferExten());
//	        	System.out.println(" \t"+transEvent.getTransferMethod());
//	        	System.out.println(" \t"+transEvent.getTransferType());
//	        	System.out.println(" \t"+transEvent.getUniqueId());
//	        	System.out.println(" \t"+transEvent.getTransfer2Parking().toString());
//	        }	        	        
	        
//	        if(event instanceof QueueCallerAbandonEvent){
//                    QueueCallerAbandonEvent abandon= (QueueCallerAbandonEvent)event;
//                    System.out.println("***********************\t AgentRingNoAnswerEvent\t ***********************");                                                                                           
//	        }                
                
	        /* An AgentLoginEvent is triggered when an agent is successfully logged in using AgentLogin.*/
//	        if(event instanceof AgentLoginEvent){
//	        	AgentLoginEvent loginEvent= (AgentLoginEvent)event;
//	        	System.out.println("***********************\t AgentLoginEvent\t ***********************");
//	        	System.out.println("getAgent \t"+loginEvent.getAgent());
//	        	System.out.println("getChannel \t"+loginEvent.getChannel());
//	        	System.out.println("getLoginChan \t"+loginEvent.getLoginChan());
//	        	System.out.println("getSource \t"+loginEvent.getSource());
//	        	System.out.println("getUniqueId \t"+loginEvent.getUniqueId());
//	        	System.out.println("getSource \t"+loginEvent.getSource());         	
//	        }
	        
	        /* An AgentCallbackLogoffEvent is triggered when an agent that previously 
	         * logged in using AgentLogin is logged of.*/
//	        if(event instanceof AgentLogoffEvent){
//	        	AgentLogoffEvent logoffEvent= (AgentLogoffEvent)event;
//	        	System.out.println("***********************\t AgentCompleteEvent\t ***********************");
//	        	System.out.println("getAgent \t"+logoffEvent.getAgent());
//	        	System.out.println("getLoginTime \t"+logoffEvent.getLoginTime());
//	        	System.out.println("getPrivilege \t"+logoffEvent.getPrivilege());
//	        	System.out.println("getSequenceNumber \t"+logoffEvent.getSequenceNumber());
//	        	System.out.println("getDateReceived \t"+logoffEvent.getDateReceived());
//	        	System.out.println("getUniqueId \t"+logoffEvent.getUniqueId());
//	        	System.out.println("getSource \t"+logoffEvent.getSource());         	
//	        }	        	       
	                       
		    /* A JoinEvent is triggered when a channel joines a queue.*/
//	        if(event instanceof JoinEvent){
//                    JoinEvent joinEvent= (JoinEvent)event;
//                    System.out.println("***********************\t JoinEvent\t ***********************");
//                    System.out.println("getChannel \t"+joinEvent.getChannel());
//                    System.out.println("getCallerIdName \t"+joinEvent.getCallerIdName());
//                    System.out.println("getQueue \t"+joinEvent.getQueue());
//                    System.out.println("getCallerIdNum \t"+joinEvent.getCallerIdNum());
//                    System.out.println(" \t"+joinEvent.getUniqueId());
//                    System.out.println("getSource \t"+joinEvent.getSource());         	
//                    System.out.println("getCount \t"+joinEvent.getCount());
//                    System.out.println(" \t"+joinEvent.getDateReceived());
 
//                    mdb_agent.writeDialLog(callObject.getsession(), agent.getAgent(), agent.getInterface(), agent.getQueue(), "3");                                                
//	        }
	        
//	        if(event instanceof QueueEntryEvent){
//                    QueueEntryEvent qentryEvent= (QueueEntryEvent)event;
//                    System.out.println("***********************\t JoinEvent\t ***********************");
//                    System.out.println(" \t"+qentryEvent.getChannel());
//                    System.out.println(" \t"+qentryEvent.getCallerIdName());
//                    System.out.println(" \t"+qentryEvent.getQueue());
//                    System.out.println(" \t"+qentryEvent.getUniqueId());
//                    System.out.println(" \t"+qentryEvent.getSource());         	
//                    System.out.println(" \t"+qentryEvent.getDateReceived());
//	        }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	
	
}

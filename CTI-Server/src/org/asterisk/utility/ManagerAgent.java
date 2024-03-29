package org.asterisk.utility;

//import java.io.BufferedInputStream;
import java.io.BufferedReader;
//import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.asterisk.main.Server;
import org.asterisk.model.AgentObject;
import org.asterisk.model.CallObject;
import org.asterisk.utility.*;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.*;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.response.ManagerResponse;

public class ManagerAgent implements Runnable,ManagerEventListener {

	
	private Server ctiS;
        private AgentListen alisten;
	private Socket clientSocket;
	private String fromAgent;
	private Thread thread;
	private int flag;
	private Utility uti;
	private ManagerConnection manager;
        private QueueRemoveAction removeQueue;
	private String result;
	private AgentObject agent = new AgentObject();
	private String addressAgent;
	private Managerdb mdb_agent;
	private int penalty = 10;
	private String loginAct = "";
	private String logoutAct = "";
	private String pauseAct = "";
	private String unpauseAct = "";
        private String pauseSession = "";
        private CallObject callObject;
        private static BufferedReader   inputStream = null ;
        private String numberOut = "";
	public ManagerAgent(){

	}
	
	public ManagerAgent(AgentListen alis, Socket client, Managerdb db) throws IOException{
            try{
                alisten = alis;
                clientSocket = client;
                mdb_agent = db;
                thread = new Thread(this);
                thread.start();
            }catch(Exception se){
                uti.writeAgentLog("Cannot connect to agent");
                closeConnect();
            }
	}
	public ManagerAgent(Server ctiserver,Socket client) throws IOException{
            try{
                ctiS = ctiserver;
                clientSocket = client;                
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
                addressAgent = clientSocket.getInetAddress().toString();
                addressAgent = addressAgent.substring(1);
                while(thread != null && clientSocket.isConnected()){
                    inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    fromAgent = inputStream.readLine();
                    ArrayList<String> cmdList = uti.getList(fromAgent);
                    flag = Integer.parseInt(cmdList.get(0));				
                    switch(flag){
                        case 100:                              
                        getAgent(cmdList);	
                        if(mdb_agent.checkLogin(agent.getAgentId(), agent.getPass(), agent.getRole())){
                            if(mdb_agent.checkStatus(agent.getInterface())){
                            QueueAddAction qAdd = addQueue(agent.getAgentId(), agent.getInterface(), agent.getQueueId(),agent.getPenalty());
                            result = manager.sendAction(qAdd, 60000).getResponse().toString();
                                if(result.equalsIgnoreCase("success")){
                                    mdb_agent.updateStatus(agent.getAgentId(), agent.getInterface(), agent.getQueueId());
                                    mdb_agent.loginAction(agent.getSesion(),agent.getAgentId(), agent.getInterface(), agent.getQueueId());
                                    System.out.println("LOGIN success");
                                    String agentName = mdb_agent.getAgentName(agent.getAgentId());
                                    sendToAgent("LOGINSUCC@"+agentName+"@"+agent.getSesion());
                                    uti.writeAgentLog("- AGENT - Agent login successful \t"+addressAgent+"\t"+agent.getAgentId());
                                }else{
                                    sendToAgent("LOGINFAIL");
                                    System.out.println("LOGIN fail");                                       
                                    closeConnect();
                                    uti.writeAgentLog("- AGENT - Agent login fail\t"+addressAgent+"\t"+agent.getAgentId());
                                }
                            }else{
                                System.out.println("Interface Already login");
                                uti.writeAgentLog("- AGENT - Agent login fail (already login)\t"+addressAgent+"\t"+agent.getAgentId());
                                closeConnect();
                            }
                        }else {
                            System.out.println("wrong user OR pass");
                            sendToAgent("LOGINFAIL");
                            closeConnect();
                            uti.writeAgentLog("- AGENT - Agent login fail(wrong username-password)\t"+addressAgent+"\t"+agent.getAgentId());
                        }
                        break;
                        case 102:  
                            if(agent != null){
                                removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                                result = manager.sendAction(removeQueue).getResponse().toString();
                                if(result.equalsIgnoreCase("success")){
                                    System.out.println("remove queue success");
                                    mdb_agent.updateStatus(agent.getAgentId(), "NULL", "NULL");		
                                    mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());
                                    sendToAgent("LOGOUTSUCC");
                                    System.out.println("LOGOUTSUCC");            
                                    uti.writeAgentLog("- AGENT - Agent logout\t"+addressAgent+"\t"+agent.getAgentId());
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
                                pauseAction = queuePause(agent.getInterface(), agent.getQueueId(), true);		
                                result = manager.sendAction(pauseAction).getResponse().toString();
                                if(result.equalsIgnoreCase("success")){
                                    System.out.println("Pause queue success");
                                    pauseSession = uti.getSession();
                                    mdb_agent.pauseAction(pauseSession, agent.getAgentId());
                                    sendToAgent("PAUSESUCC");
                                    System.out.println("Pause queue suc"); 
                                    uti.writeAgentLog("- AGENT - Agent pause\t"+addressAgent+"\t"+agent.getAgentId());
                                }else{
                                    System.out.println("Pause queue fail");                                        
                                    sendToAgent("PAUSEFAIL");
                                    uti.writeAgentLog("- AGENT - Agent pause fail\t"+addressAgent+"\t"+agent.getAgentId());
                                }		            			
                            }else if(cmdList.get(1).equalsIgnoreCase("on")){
                                pauseAction = queuePause(agent.getInterface(), agent.getQueueId(), false);
                                result = manager.sendAction(pauseAction).getResponse().toString();
                                if(result.equalsIgnoreCase("success")){
                                    System.out.println("unPause queue success");	
                                    mdb_agent.unpauseAction(pauseSession, agent.getAgentId());
                                    sendToAgent("UNPAUSESUCC");
                                    System.out.println("unPause queue suc"); 
                                    uti.writeAgentLog("- AGENT - Agent unpause\t"+addressAgent+"\t"+agent.getAgentId());
                                }else{
                                    System.out.println("unPause queue fail");
                                    sendToAgent("UNPAUSEFAIL");
                                    uti.writeAgentLog("- AGENT - Agent unpause fail\t"+addressAgent+"\t"+agent.getAgentId());
                                }		            			
                            }
                        break;
                        case 106:  		
                            //hangup the call from agent request
                            ManagerResponse managerRes;
                            HangupAction hangAct = new HangupAction();
                            hangAct.setChannel(agent.getInterface());
                            managerRes =  manager.sendAction(hangAct, 1000);
                            System.out.println("managerRes\t"+managerRes.getResponse());

                        break;
                        case 108: //dial out
                            numberOut = cmdList.get(1).toString();
                            String iface = agent.getInterface();
                            OriginateAction originateAction = null;
                            originateAction = dialOut(iface, numberOut);                                                      
                            System.out.println("number: "+iface);                            
                            System.out.println("numberOut: "+numberOut);                            
                            result = manager.sendAction(originateAction, 30000).getResponse().toString();  
                            if(result.equalsIgnoreCase("success")){
                                System.out.println("Dial out success");	
                                uti.writeAgentLog("- AGENT - Agent DialOut Success\t"+agent.getAgentId()+"\t"+numberOut);
                                sendToAgent("DIALOUT");
                                System.out.println("Dial time: "+uti.getDatetime());
                                //wrire database
                            }else{
                                System.out.println("Dial out fail");	                          
                                uti.writeAgentLog("- AGENT - Agent DialOut Fail\t"+agent.getAgentId()+"\t"+numberOut);
                                //wrire database
                            }                                                        
                        break;
                        case 110:  
                        break;
                        case 112:  
                            if(agent != null){
                                removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                                result = manager.sendAction(removeQueue).getResponse().toString();
                                if(result.equalsIgnoreCase("success")){
                                    System.out.println("remove queue success");
                                    mdb_agent.updateStatus(agent.getAgentId(), "NULL", "NULL");		
                                    mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());
                                    System.out.println("LOGOUTSUCC(exit system)");            
                                    uti.writeAgentLog("- AGENT - Agent logout successful - exit system\t"+addressAgent+"\t"+agent.getAgentId());
                                    agent = null;
                                    closeConnect();                                                  
                                }	            		
                            }                              
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
            } catch (SocketException e) {
                    try {                            
                        removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                        result = manager.sendAction(removeQueue).getResponse().toString();
                        mdb_agent.updateStatus(agent.getAgentId(), "NULL", "NULL");		
                        mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());   
                        uti.writeAgentLog("- AGENT - Interrup connection by client\t"+addressAgent+"\t"+agent.getAgentId());
                        agent = null;
                        closeConnect();                            
                    } catch (Exception e1) {}
                    //logout for agent exit                                               
                    System.out.println("Interrup connection by client\t"+e);
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
            catch(IOException e){}
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
        
        public OriginateAction dialOut(String iface, String phoneNumber){
            OriginateAction originateAction = new OriginateAction();
            originateAction.setChannel(iface);// so extension can su dung de goi di "SIP/number"
            originateAction.setContext("from-internal");
            originateAction.setAsync(true);
            originateAction.setExten(phoneNumber);// so dien thoai can goi "number"        
            originateAction.setPriority(new Integer(1));
            originateAction.setTimeout(new Long(30000));
            originateAction.setCallerId(agent.getAgentName());            
            return originateAction;
        }
	
	public void getAgent(ArrayList<String> list){
            ArrayList<String> cmdList = list;
            agent = new AgentObject();
            agent.setSocket(clientSocket);
            agent.setAgentId(cmdList.get(1));
            agent.setPass(cmdList.get(2));
            agent.setInterface(cmdList.get(3));
            agent.setQueueId(cmdList.get(4));	
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
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                clientSocket.close();                
                System.out.println("close socket");
            }
            inputStream = null;
            System.out.println("finish close session");
	}
	
	public void sendToAgent(String data) throws IOException{
            PrintWriter outToAgent = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outToAgent.println(data);
            outToAgent.flush();
	}
        
        void printinfor(){
            System.out.println("getAgentId "+agent.getAgentId());
            System.out.println("getAgentName "+agent.getAgentName());
            System.out.println("getInterface "+agent.getInterface());
            System.out.println("getPass "+agent.getPass());
            System.out.println("getQueueId "+agent.getQueueId());
            System.out.println("getQueueName "+agent.getQueueName());
            System.out.println("getRole "+agent.getRole());
            System.out.println("getSesion "+agent.getSesion());
            System.out.println("getPenalty "+agent.getPenalty());
            
        }

	@Override
	public void onManagerEvent(ManagerEvent event) {
            // TODO Auto-generated method stub
            try {                  
            //enter queue
            if(event instanceof AgentCalledEvent){
                AgentCalledEvent callEvent = (AgentCalledEvent)event;
                System.out.println("***********************\t AgentCalledEvent\t ***********************");
                //hay bi loi null pointer exception
                String iface = agent.getInterface();
//                System.out.println("iface(AgentCalledEvent):"+iface);
                String agentCaller = callEvent.getAgentCalled();
                if(iface.equalsIgnoreCase(agentCaller)){
                    System.out.println("agent: "+agent.getAgentId());
                    System.out.println("iface(AgentCalledEvent):"+iface);
                    String callerName = callEvent.getCallerIdName();
                    String callerNum = callEvent.getCallerIdNum();
                    callObject = new CallObject();                        
                    callObject.setsession(uti.getSession());   
                    callObject.setcallerName(callerName);
                    callObject.setcallerNumber(callerNum);        
                    callObject.setdesNum("");//?????
                    mdb_agent.enterQueue(callObject.getsession(), agent.getAgentId(), iface, agent.getQueueId(), "3",callerNum);
                    uti.writeAgentLog("- AGENT - Enter Queue\t" + agent.getAgentId() + "\t" + iface + "\t" + callerNum);
                    sendToAgent("RINGING@"+callerNum);                                                         
                }              
            } 	        
            //connect queue
            if(event instanceof AgentConnectEvent){
                AgentConnectEvent connect= (AgentConnectEvent)event;
                System.out.println("***********************\t AgentConnectEvent\t ***********************");
                String iface = agent.getInterface();
                printinfor();
//                System.out.println("iface(AgentConnectEvent):"+iface);
                String agentCaller = connect.getMember();
                if(iface.equalsIgnoreCase(agentCaller)){
                    System.out.println("agent: "+agent.getAgentId());
                    System.out.println("iface(AgentConnectEvent):"+iface);
                    String ringtime = connect.getRingtime().toString();
                    mdb_agent.connectQueue(callObject.getsession(), agent.getAgentId(), iface, agent.getQueueId(), "4", ringtime);
                    uti.writeAgentLog("- AGENT - Connect Queue\t" + agent.getAgentId() + "\t" + iface + "\t" + callObject.getcallerNumber());
                    sendToAgent("UP"); 
                }                               	
            }      
            //complete call
            /* An AgentsCompleteEvent is triggered after the state of all agents has been 
             * reported in response to an AgentsAction.*/
            if(event instanceof AgentCompleteEvent){
                AgentCompleteEvent comEvent= (AgentCompleteEvent)event;
                System.out.println("***********************\t AgentCompleteEvent\t ***********************");  
                printinfor();
                String iface = agent.getInterface();
//                System.out.println("iface(AgentCompleteEvent):"+iface);
                String channel = comEvent.getMember();                                                        
                if(iface.equalsIgnoreCase(channel)){
                    System.out.println("agent: "+agent.getAgentId());
                    System.out.println("iface(AgentCompleteEvent):"+iface);
                    String reason = comEvent.getReason();
                    String talkTime = comEvent.getTalkTime().toString();
                    if(reason.equalsIgnoreCase("agent")){
                        mdb_agent.completeCall(callObject.getsession(), agent.getAgentId(), agent.getInterface(), comEvent.getQueue(), "5",talkTime);
                        uti.writeAgentLog("- AGENT - Agent complete\t" + agent.getAgentId() + "\t" + iface + "\t" + callObject.getcallerNumber());
                        System.out.println("reason \t"+reason);
                    }else if(reason.equalsIgnoreCase("caller")){
                        mdb_agent.completeCall(callObject.getsession(), agent.getAgentId(), agent.getInterface(), comEvent.getQueue(), "6",talkTime);
                        uti.writeAgentLog("- AGENT - Caller complete\t" + agent.getAgentId() + "\t" + iface + "\t" + callObject.getcallerNumber());
                        System.out.println("reason \t"+reason);
                    }
                    callObject = null;                      
                }                    
            }
	        
            //ringing but no answer
            /* An AgentRingNoAnswerEvent is triggered when a call is routed to 
             * an agent but the agent does not answer the call.*/
            if(event instanceof AgentRingNoAnswerEvent){
                AgentRingNoAnswerEvent noAnsEvent= (AgentRingNoAnswerEvent)event;
                System.out.println("***********************\t AgentRingNoAnswerEvent\t ***********************");  
                //hay bi loi nullpointer exception
                String iface = agent.getInterface();
//                System.out.println("iface(AgentRingNoAnswerEvent):"+iface);
                String channel = noAnsEvent.getMember();                     
                if(iface.equalsIgnoreCase(channel)){
                    System.out.println("agent: "+agent.getAgentId());
                    System.out.println("iface(AgentRingNoAnswerEvent):"+iface);
                    String ringTime = String.valueOf(noAnsEvent.getRingtime()/1000);
                    System.out.println("ringTime\t"+ringTime);
                    //hay bi loi nullpointer exception
                    mdb_agent.ringNoans(callObject.getsession(), agent.getAgentId(), agent.getInterface(), agent.getQueueId(), "21", ringTime);
                    uti.writeAgentLog("- AGENT - Ring noAns\t" + agent.getAgentId() + "\t" + iface + "\t" + callObject.getcallerNumber());
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
                //agent dial out
                if(numberOut.equalsIgnoreCase(hangEvent.getCallerIdNum())){   
                    System.out.println("agent: "+agent.getAgentId());
                    System.out.println("getCallerIdNum: "+hangEvent.getCallerIdNum());
                    System.out.println("numberOut: "+numberOut);
                    numberOut = "";
                    System.out.println("hangup dial out: "+uti.getDatetime());
                }
                //imcoming call
                if(iface.equalsIgnoreCase(channel)){  
                    System.out.println("agent: "+agent.getAgentId());
                    System.out.println("iface(hangup):"+iface);
                    sendToAgent("HANGUP");
                    System.out.println("chann: "+channel);
                    System.out.println("exten: "+agent.getInterface());
                    if(callObject != null){
                        System.out.println("write event hangup: "+channel);
                        mdb_agent.abandon(callObject.getsession(), agent.getAgentId(), iface, agent.getQueueId(), "7","");
                        uti.writeAgentLog("- AGENT - Abandon Call\t" + agent.getAgentId() + "\t" + iface + "\t" + callObject.getcallerNumber());
                        callObject = null;                             
                    }else
                        System.out.println("callObject == null "+channel);
                }                              	
            }             
            
//            if(event instanceof BridgeEvent){
//                BridgeEvent briEvent = (BridgeEvent)event;
//                String state = null;
//                if(briEvent.isLink()){        		
//                    System.out.println("***********************\t BridgeEvent Link\t ***********************");
//                    state = briEvent.BRIDGE_STATE_LINK;
//                    if(numberOut.equalsIgnoreCase(briEvent.getCallerId2())){                            
//                        System.out.println("numberOut \t"+briEvent.getCallerId2());  
//                        //connect dial out succesfull and write database
//                    }
//                }else{
//                    System.out.println("***********************\t BridgeEvent UnLink\t ***********************");
//                    state = briEvent.BRIDGE_STATE_UNLINK;
//                    if(numberOut.equalsIgnoreCase(briEvent.getCallerId2())){
//                        System.out.println("numberOut \t"+briEvent.getCallerId2());
//                        //finish dial out and write database
//                    }
//                }
//            }            
	        
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
            catch (NullPointerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
	}		
}

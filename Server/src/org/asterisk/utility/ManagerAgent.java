package org.asterisk.utility;

//import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
//import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.asterisk.main.Server;
import org.asterisk.model.AgentObject;
import org.asterisk.model.IncomingCallObject;
import org.asterisk.utility.*;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.*;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.response.ManagerResponse;

public class ManagerAgent implements Runnable,ManagerEventListener {

	
//	private Server ctiS;
        private boolean connected = true;
        private AgentListen alisten;
	private Socket clientSocket;
//	private String fromAgent;
	private Thread thread;
	private int flag;
	private Utility uti;
	private ManagerConnection manager;
        private QueueRemoveAction removeQueue;
	private String result;
	private  AgentObject agent ;
	private String addClient;
	private Managerdb mdb_agent;
	private int penalty = 10;
        private String pauseSession = "";
        private IncomingCallObject incomingCall;
        private String numberOut = "";
        private String iface = "";
        private String incomeTemp;
        
        private  BufferedReader inPutStream;
        private  PrintWriter outPutStream;
        
//        private static DataOutputStream outstream;
//        private static DataInputStream instream;
        private boolean close = true;
	public ManagerAgent(){

	}
	
//	public ManagerAgent(AgentListen alis, Socket client, Managerdb db) throws IOException{
//            try{
//                agent = new AgentObject();
//                alisten = alis;
//                clientSocket = client;
//                mdb_agent = db;
//                thread = new Thread(this);
//                thread.start();
//            }catch(Exception se){
//                uti.writeAgentLog("Cannot connect to agent");
//                closeConnect();
//            }
//	}
        //start thread
	public ManagerAgent(ManagerConnection m, Socket client, Managerdb db) throws IOException{
            try{                
                manager = m;
                clientSocket = client;
                mdb_agent = db;
//                agent = new AgentObject();
                thread = new Thread(this);
                thread.start();
            }catch(Exception se){
                uti.writeAgentLog("Cannot connect to agent");
                closeConnect();
            }
	}               
        //start thread
	public ManagerAgent(AgentListen al, ManagerConnection m, Socket client, Managerdb db) throws IOException{
            try{                
                alisten = al;
                manager = m;
                clientSocket = client;
                mdb_agent = db;
//                agent = new AgentObject();
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
                String fromClient = "";                
                uti = new Utility();
//                manager = alisten.manager;
                manager.addEventListener(this);
                manager.sendAction(new StatusAction());
                addClient = clientSocket.getInetAddress().toString();
                addClient = addClient.substring(1);
                
                outPutStream = new PrintWriter(clientSocket.getOutputStream());
                inPutStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                outstream = new DataOutputStream(clientSocket.getOutputStream());
//                out = new PrintStream(clientSocket.getOutputStream());
                
                //open inputstream & outputstream for each client
//                instream =  new DataInputStream (clientSocket.getInputStream());
//                outstream = new DataOutputStream(clientSocket.getOutputStream());   
                System.out.println("***open inputstream & outputstream***");
                while(connected){                    
//                    instream =  new DataInputStream (clientSocket.getInputStream());
//                    inPutStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    fromClient = inPutStream.readLine();
                    System.out.println("***listen from client***");
//                    fromClient = instream.readUTF();
                    System.out.println("***Receive from client: "+fromClient);
                    ArrayList<String> cmdList = uti.getList(fromClient);
                    flag = Integer.parseInt(cmdList.get(0));				
                    switch(flag){
                        case 100:                              
                            getAgent(cmdList);	
                            if(mdb_agent.checkLogin(agent.getAgentId(), agent.getPass(), agent.getRole())){
                                if(mdb_agent.checkStatus(agent.getInterface())){
                                QueueAddAction qAdd = addQueue(agent.getAgentId(), agent.getInterface(), agent.getQueueId(),agent.getPenalty());
                                result = manager.sendAction(qAdd, 60000).getResponse().toString();
                                    if("success".equalsIgnoreCase(result)){
                                        mdb_agent.updateStatus(agent.getAgentId(), agent.getInterface(), agent.getQueueId());
                                        mdb_agent.loginAction(agent.getSesion(),agent.getAgentId(), agent.getInterface(), agent.getQueueId());
                                        System.out.println("LOGIN success");
                                        String agentName = mdb_agent.getAgentName(agent.getAgentId());
                                        sendToAgent("LOGINSUCC@"+agentName+"@"+agent.getSesion());
//                                        out.println("LOGINSUCC@"+agentName+"@"+agent.getSesion());
//                                        sendToClient("LOGINSUCC@"+agentName+"@"+agent.getSesion());
                                        uti.writeAgentLog("- AGENT - Agent login successful \t"+addClient+"\t"+agent.getAgentId());
                                        
                                    }else{
                                        sendToAgent("LOGINFAIL@Check Information again");
//                                        out.println("LOGINFAIL");
//                                        sendToClient("LOGINFAIL");
                                        System.out.println("LOGIN fail");  
                                        uti.writeAgentLog("- AGENT - Agent login fail\t"+addClient+"\t"+agent.getAgentId());
                                        closeConnect();                                        
                                    }
                                }else{
                                    sendToAgent("LOGINFAIL@Interface Already Login");
//                                    sendToClient("LOGINFAIL");
                                    System.out.println("Interface Already login");
                                    uti.writeAgentLog("- AGENT - Agent login fail (already login)\t"+addClient+"\t"+agent.getAgentId());
                                    closeConnect();
                                }
                            }else {
                                System.out.println("wrong user OR pass");                                
                                sendToAgent("LOGINFAIL@Wrong Username or Password");
//                                out.println("LOGINFAIL");
//                                sendToClient("LOGINFAIL");
                                uti.writeAgentLog("- AGENT - Agent login fail(wrong username-password)\t"+addClient+"\t"+agent.getAgentId());
                                closeConnect();                                
                            }
                        break;
                        case 102:  
                            if(agent != null){
                                removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                                result = manager.sendAction(removeQueue).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    close = false;
                                    System.out.println("remove queue success");
                                    mdb_agent.updateStatus(agent.getAgentId(), "NULL", "NULL");		
                                    mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());
                                    sendToAgent("LOGOUTSUCC");
//                                    out.println("LOGOUTSUCC");
//                                    System.out.println("LOGOUTSUCC");            
                                    uti.writeAgentLog("- AGENT - Agent logout\t"+addClient+"\t"+agent.getAgentId());
//                                    removesocket();
                                    closeConnect();          
                                }else{		          
                                    System.out.println("logout result: "+result);                                    
                                    sendToAgent("LOGOUTFAIL@Server error");
//                                    out.println("LOGOUTFAIL");
                                }		            		
                            }                            			            	
                        break;
                        case 104:
                            QueuePauseAction pauseAction = null;
                            if(cmdList.get(1).equalsIgnoreCase("off")){
                                pauseAction = queuePause(agent.getInterface(), agent.getQueueId(), true);		
                                result = manager.sendAction(pauseAction).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    System.out.println("Pause queue success");
                                    pauseSession = uti.getSession();
                                    mdb_agent.pauseAction(pauseSession, agent.getAgentId());
                                    sendToAgent("PAUSESUCC");
//                                    out.println("PAUSESUCC"); 
                                    uti.writeAgentLog("- AGENT - Agent pause\t"+addClient+"\t"+agent.getAgentId());
                                }else{
                                    System.out.println("Pause queue fail");                                        
                                    sendToAgent("PAUSEFAIL");
//                                    out.println("PAUSEFAIL");
                                    uti.writeAgentLog("- AGENT - Agent pause fail\t"+addClient+"\t"+agent.getAgentId());
                                }		            			
                            }else if(cmdList.get(1).equalsIgnoreCase("on")){
                                pauseAction = queuePause(agent.getInterface(), agent.getQueueId(), false);
                                result = manager.sendAction(pauseAction).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    System.out.println("unPause queue success");	
                                    mdb_agent.unpauseAction(pauseSession, agent.getAgentId());
                                    sendToAgent("UNPAUSESUCC");
//                                    out.println("UNPAUSESUCC");
                                    uti.writeAgentLog("- AGENT - Agent unpause\t"+addClient+"\t"+agent.getAgentId());
                                }else{
                                    System.out.println("unPause queue fail");
                                    sendToAgent("UNPAUSEFAIL");
//                                    out.println("UNPAUSEFAIL");
                                    uti.writeAgentLog("- AGENT - Agent unpause fail\t"+addClient+"\t"+agent.getAgentId());
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
                            String inface = agent.getInterface();
                            OriginateAction originateAction = null;
                            originateAction = dialOut(inface, numberOut);                                                      
                            System.out.println("iface: "+inface);                            
                            System.out.println("numberOut: "+numberOut);                            
                            result = manager.sendAction(originateAction, 30000).getResponse().toString();  
                            if("success".equalsIgnoreCase(result)){
                                System.out.println("Dial out success");	
                                uti.writeAgentLog("- AGENT - Agent DialOut \t"+agent.getAgentId()+"\t"+numberOut);
                                sendToAgent("DIALOUT");
//                                out.println("DIALOUT");
                                System.out.println("Dial time: "+uti.getDatetime());
                                //wrire database
                            }else{
                                sendToAgent("DIALOUTFAIL");
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
                                if("success".equalsIgnoreCase(result)){
                                    System.out.println("remove queue success");
                                    mdb_agent.updateStatus(agent.getAgentId(), "NULL", "NULL");		
                                    mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());
                                    System.out.println("LOGOUTSUCC(exit system)");            
                                    uti.writeAgentLog("- AGENT - Agent logout - exit system\t"+addClient+"\t"+agent.getAgentId());
//                                    removesocket();
                                    closeConnect();                                                  
                                }	            		
                            }                              
                        break;	       		 		
                        default: 
                            System.out.println("(invalid) " +fromClient);
                        break;					
                    }											
                }
            } catch (IllegalArgumentException e) {
                    e.printStackTrace();
            } catch (IllegalStateException e) {
                    e.printStackTrace();
            } catch (SocketException e) {
                try {                            
                    if(close){
                        removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                        result = manager.sendAction(removeQueue).getResponse().toString();
                        mdb_agent.updateStatus(agent.getAgentId(), "NULL", "NULL");		
                        mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());   
                        uti.writeAgentLog("- AGENT - Interrup connection by client\t"+addClient+"\t"+agent.getAgentId());
//                        removesocket();
                        closeConnect(); 
                        System.out.println("Interrup connection by client\t"+e);
                    }                           
                } catch (Exception e1) {e1.printStackTrace();}                                                         
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
	
//        public void removesocket(){
//            for(Socket s: alisten.lists){
//                if(s.getPort()==clientSocket.getPort()){
//                    alisten.lists.remove(s);
//                }
//            }
//        }
        
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
            agent = new AgentObject();
            agent.setSocket(clientSocket);
            agent.setAgentId(list.get(1));
            agent.setPass(list.get(2));
            agent.setInterface(list.get(3));
            agent.setQueueId(list.get(4));	
            agent.setPenalty(penalty);
            agent.setRole(list.get(5));
            agent.setSession(uti.getSession());
	}
	
	public void closeConnect(){
            try{
                System.out.println("start close session");
                if(clientSocket != null){
//                    instream.close();
//                    outstream.close();
                    inPutStream.close();
                    outPutStream.close();
                    clientSocket.close();      
                    System.out.println("close socket");
//                    if(thread != null){
//                        thread.interrupt();
//                        System.out.println("close thread");
//                    }
                    connected = false;
                }
                System.out.println("finish close session");                
            }catch(IOException e){
                System.out.println("closeConnect(IOException): "+e);
            }
	}
	
	public void sendToAgent(String data) throws IOException{
            try{
//                outstream = new DataOutputStream(clientSocket.getOutputStream());
//                outPutStream = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                if(clientSocket != null && outPutStream != null){                  
                    System.out.println("senttoagent:\t"+data+"\t"+agent.getAgentId()+"\t"+clientSocket.getPort());
                    outPutStream.println(data);
                    outPutStream.flush();
//                    outstream.writeUTF(data);
//                    outstream.flush();                
                }else
                    System.out.println("senttoagent: socket close: "+data);
            }catch(Exception e){
                System.out.println("sendToClient(IOException): "+e);
            }             
            
	}
        
//	public void sendToClient(String data){
//            try{
//                if(clientSocket.isConnected()){
//                    System.out.println("sendToClient: "+data);
//                    outstream.writeUTF(data);
//                    outstream.flush();                
//                }else
//                    System.out.println("sendToClient: socket close: "+data);
//            }catch(IOException e){
//                System.out.println("sendToClient(IOException): "+e);
//            }            
//	}        
        
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
//                if(agent != null){
//                    String iface = "";
                    //enter queue                
                    if(event instanceof AgentCalledEvent){
                        AgentCalledEvent callEvent = (AgentCalledEvent)event;
                        System.out.println("***********************\t AgentCalledEvent\t ***********************");  
                        iface = "";
                        iface = agent.getInterface().toString();//hay bi loi null pointer exception
                        String agentCaller = callEvent.getAgentCalled();
                        System.out.println("iface(AgentCalledEvent):"+iface);                        
                        System.out.println("agentCaller(AgentCalledEvent):"+agentCaller);
                        if(iface.equalsIgnoreCase(agentCaller)){
                            System.out.println("iface(AgentCalledEvent):"+iface);
                            String callerName = callEvent.getCallerIdName();
                            String callerNum = callEvent.getCallerIdNum();
                            incomingCall = new IncomingCallObject();                        
                            incomingCall.setsession(uti.getSession());   
                            incomingCall.setcallerName(callerName);
                            incomingCall.setcallerNumber(callerNum);   
                            incomeTemp = incomingCall.getsession();
//                            incomingCall.setdesNum("");//?????
                            mdb_agent.enterQueue(incomingCall.getsession(), agent.getAgentId(), iface, agent.getQueueId(), "3",callerNum);
                            uti.writeAgentLog("- AGENT - Enter Queue\t" + agent.getAgentId() + "\t" + iface + "\t" + callerNum);
                            sendToAgent("RINGING@"+callerNum);       
//                            out.println("RINGING@"+callerNum);
                        }              
                    }                                               
                    //connect queue
                    if(event instanceof AgentConnectEvent){
                        AgentConnectEvent connect= (AgentConnectEvent)event;
                        System.out.println("***********************\t AgentConnectEvent\t ***********************");
//                        iface = agent.getInterface().toString();
//                        printinfor();
                        System.out.println("iface(AgentConnectEvent):"+iface);
                        String agentCaller = connect.getMember();
                        System.out.println("agentCaller(AgentConnectEvent):"+agentCaller);
                        if(iface.equalsIgnoreCase(agentCaller)){
                            System.out.println("iface(AgentConnectEvent):"+iface);
                            String ringtime = connect.getRingtime().toString();
                            mdb_agent.connectQueue(incomingCall.getsession(), agent.getAgentId(), iface, agent.getQueueId(), "4", ringtime);
                            uti.writeAgentLog("- AGENT - Connect Queue\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());
                            sendToAgent("UP"); 
//                            out.println("UP");
                        }                               	
                    }      
                    //complete call
                    /* An AgentsCompleteEvent is triggered after the state of all agents has been 
                     * reported in response to an AgentsAction.*/
                    if(event instanceof AgentCompleteEvent){
                        AgentCompleteEvent comEvent= (AgentCompleteEvent)event;
                        System.out.println("***********************\t AgentCompleteEvent\t ***********************");  
//                        printinfor();
//                        iface = agent.getInterface().toString();
                        System.out.println("iface(AgentCompleteEvent):"+iface);
                        String channel = comEvent.getMember();                                                        
                        if(iface.equalsIgnoreCase(channel)){
                            System.out.println("iface(AgentCompleteEvent):"+iface);
                            String reason = comEvent.getReason().toString();
                            String talkTime = comEvent.getTalkTime().toString();
                            if("agent".equalsIgnoreCase(reason)){
                                mdb_agent.completeCall(incomingCall.getsession(), agent.getAgentId(), agent.getInterface(), comEvent.getQueue(), "5",talkTime);
                                uti.writeAgentLog("- AGENT - Agent complete\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());
                                System.out.println("reason \t"+reason);
                            }else if("caller".equalsIgnoreCase(reason)){
                                mdb_agent.completeCall(incomingCall.getsession(), agent.getAgentId(), agent.getInterface(), comEvent.getQueue(), "6",talkTime);
                                uti.writeAgentLog("- AGENT - Caller complete\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());
                                System.out.println("reason \t"+reason);
                            }
//                            incomingCall = null;                      
                        }                    
                    }          
                    //ringing but no answer
                    /* An AgentRingNoAnswerEvent is triggered when a call is routed to 
                     * an agent but the agent does not answer the call.*/
                    if(event instanceof AgentRingNoAnswerEvent){
                        AgentRingNoAnswerEvent noAnsEvent= (AgentRingNoAnswerEvent)event;
                        System.out.println("***********************\t AgentRingNoAnswerEvent\t ***********************");  
                        //hay bi loi nullpointer exception
//                        iface = agent.getInterface().toString();
        //                System.out.println("iface(AgentRingNoAnswerEvent):"+iface);
//                        printinfor();
                        String channel = noAnsEvent.getMember();                     
                        if(iface.equalsIgnoreCase(channel)){                            
                            System.out.println("iface(AgentRingNoAnswerEvent):"+iface);
                            String ringTime = String.valueOf(noAnsEvent.getRingtime()/1000);
                            System.out.println("ringTime\t"+ringTime);
                            //hay bi loi nullpointer exception
                            printinfor();
//                            System.out.println("incomingCall.getsession()\t"+incomingCall.getsession());
                            mdb_agent.ringNoans(incomeTemp, agent.getAgentId(), agent.getInterface(), agent.getQueueId(), "21", ringTime);
                            uti.writeAgentLog("- AGENT - Ring noAns\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());
//                            callObject = null;                                               
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
//                        iface = agent.getInterface().toString();           
                        //outgoing call
                        if(numberOut.equalsIgnoreCase(hangEvent.getCallerIdNum())){   
                            System.out.println("agent: "+agent.getAgentId());
                            System.out.println("getCallerIdNum: "+hangEvent.getCallerIdNum());
                            System.out.println("numberOut: "+numberOut);
                            numberOut = "";
                            System.out.println("hangup dial out: "+uti.getDatetime());
                            System.out.println("hangup outgoing call: "+channel);
                            sendToAgent("HANGUP");
                        }else if(iface.equalsIgnoreCase(channel)){  //imcoming call
                            System.out.println("iface(hangup):"+iface);
                            System.out.println("chann: "+channel);
                            sendToAgent("HANGUP");                           
                            if(incomingCall != null){
                                System.out.println("hangup incoming call: "+channel);
                                mdb_agent.abandon(incomingCall.getsession(), agent.getAgentId(), iface, agent.getQueueId(), "7","");
                                uti.writeAgentLog("- AGENT - Abandon Call\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());
//                                incomingCall = null;                             
                            }else
                                System.out.println("incomingCall == null "+channel);
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
//                }else {
//                    System.out.println("*************agent null value******************");
//                    printinfor();
//                }	                                	                                                                    	       
            } 
            catch (IOException e) {
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

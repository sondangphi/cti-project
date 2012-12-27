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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        private boolean connected = true;
        private AgentListen alisten;
	private Socket clientSocket;
	private Thread thread;
	private int flag;
	private Utility uti = new Utility();
	private ManagerConnection manager;
        private QueueRemoveAction removeQueue;
	private String result;
	private  AgentObject agent ;
	private String addClient;
	private Managerdb mdb_agent;
	private int penalty = 10;
        private String pauseSession = "";
        private IncomingCallObject incomingCall;
        private String dialoutNumber = "";
        private String dialoutSession;
        private String dialoutStatus = "";
        private String dialoutTalktime = "0";
        private boolean beginDialout = true;
        private boolean connectDialout;
        private boolean completeDialout;
//        private boolean dialin = false;
        private boolean ringing = true;
        private boolean connect = false;
        private boolean complete = false;
        private boolean ringno = false;
        private boolean abandonCall = true;
        private String iface = "";
        private String incomeTemp;        
        private  BufferedReader inPutStream;
        private  PrintWriter outPutStream;
//        DataInputStream in;
//        DataOutputStream out;
        
        private boolean close = true;
        private TimerClock clock;
        private String uniqueidDialout = "";
        
        private static String ENTERQUEUE = "3";
        private static String CONNECT = "4";
        private static String COMPLETEAGENT = "5";        
        private static String COMPLETECALLER = "6";
        private static String ABANDON = "7";
        private static String RINGNOANSWER = "21";
        
	public ManagerAgent(){

	}

        //start thread
	public ManagerAgent(ManagerConnection m, Socket client, Managerdb db) throws IOException, Throwable{
            try{                
                manager = m;
                clientSocket = client;
                mdb_agent = db;
                thread = new Thread(this);
                thread.start();
            }catch(Exception se){
                uti.writeAgentLog("Cannot connect to agent");
                closeConnect();
            }
	}               
        //start thread
	public ManagerAgent(AgentListen al, ManagerConnection m, Socket client, Managerdb db) throws IOException, Throwable{
            try{                
                alisten = al;
                manager = m;
                clientSocket = client;
                mdb_agent = db;
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
                manager.addEventListener(this);
                manager.sendAction(new StatusAction());
                addClient = clientSocket.getInetAddress().toString();
                addClient = addClient.substring(1);
                outPutStream = new PrintWriter(clientSocket.getOutputStream());
                inPutStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  
//                in = new DataInputStream(clientSocket.getInputStream());
//                out = new DataOutputStream(clientSocket.getOutputStream());
                System.out.println("***open inputstream & outputstream***");
                while(connected){                    
                    fromClient = inPutStream.readLine();
                    System.out.println("***listen from client***");
                    System.out.println("***Receive from client: "+fromClient);
                    ArrayList<String> cmdList = uti.getList(fromClient);
                    flag = Integer.parseInt(cmdList.get(0));				
                    switch(flag){
                        case 100:   //login  
                            try{
                                getAgent(cmdList);	
                                if(mdb_agent.checkLogin(agent.getAgentId(), agent.getPass(), agent.getRole())){
                                    if(mdb_agent.checkStatus(agent.getAgentId(), agent.getInterface(), agent.getQueueId())){
                                    QueueAddAction qAdd = addQueue(agent.getAgentId(), agent.getInterface(), agent.getQueueId(),agent.getPenalty());
                                    result = manager.sendAction(qAdd, 60000).getResponse().toString();
                                        if("success".equalsIgnoreCase(result)){
                                            mdb_agent.updateStatus(agent.getAgentId(), agent.getInterface(), agent.getQueueId());
                                            mdb_agent.loginAction(agent.getSesion(),agent.getAgentId(), agent.getInterface(), agent.getQueueId());
                                            System.out.println("LOGIN success");
                                            String agentName = mdb_agent.getAgentName(agent.getAgentId());
                                            sendToAgent("LOGINSUCC@"+agentName+"@"+agent.getSesion());
                                            uti.writeAgentLog("- AGENT - Agent login successful \t"+addClient+"\t"+agent.getAgentId());

                                        }else{
                                            sendToAgent("LOGINFAIL@Check Information again");
                                            System.out.println("LOGIN fail");  
                                            uti.writeAgentLog("- AGENT - Agent login fail\t"+addClient+"\t"+agent.getAgentId());
                                            closeConnect();                                        
                                        }
                                    }else{
                                        sendToAgent("LOGINFAIL@Already Login");
                                        System.out.println("Interface Already login");
                                        uti.writeAgentLog("- AGENT - Agent login fail (already login)\t"+addClient+"\t"+agent.getAgentId());
                                        closeConnect();
                                    }
                                }else {
                                    System.out.println("wrong user OR pass");                                
                                    sendToAgent("LOGINFAIL@Wrong Username or Password");
                                    uti.writeAgentLog("- AGENT - Agent login fail(wrong username-password)\t"+addClient+"\t"+agent.getAgentId());
                                    closeConnect();                                
                                }                                
                            }catch(Throwable t){
                            }
                        break;
                        case 102://logout
                            if(agent != null){
                                removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                                result = manager.sendAction(removeQueue).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    close = false;
                                    System.out.println("remove queue success");
                                    mdb_agent.updateStatus(agent.getAgentId(), "0", "0");		
                                    mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());
                                    sendToAgent("LOGOUTSUCC");           
                                    uti.writeAgentLog("- AGENT - Agent logout\t"+addClient+"\t"+agent.getAgentId());
                                    try {          
                                        closeConnect();
                                    } catch (Throwable ex) {
                                        Logger.getLogger(ManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }else{		          
                                    System.out.println("logout result: "+result);                                    
                                    sendToAgent("LOGOUTFAIL@Server error");
                                }		            		
                            }                            			            	
                        break;
                        case 104: // pause & unpause
                            QueuePauseAction pauseAction = null;
                            if(cmdList.get(1).equalsIgnoreCase("off")){
                                pauseAction = queuePause(agent.getInterface(), agent.getQueueId(), true);		
                                result = manager.sendAction(pauseAction).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    System.out.println("Pause queue success");
                                    pauseSession = uti.getSession();
//                                    mdb_agent.pauseAction(pauseSession, agent.getAgentId());
                                    mdb_agent.pauseAction(pauseSession, agent.getAgentId(),agent.getInterface(),agent.getQueueId(),agent.getSesion());
                                    sendToAgent("PAUSESUCC");
                                    uti.writeAgentLog("- AGENT - Agent pause\t"+addClient+"\t"+agent.getAgentId());
                                }else{
                                    System.out.println("Pause queue fail");                                        
                                    sendToAgent("PAUSEFAIL");
                                    uti.writeAgentLog("- AGENT - Agent pause fail\t"+addClient+"\t"+agent.getAgentId());
                                }		            			
                            }else if(cmdList.get(1).equalsIgnoreCase("on")){
                                pauseAction = queuePause(agent.getInterface(), agent.getQueueId(), false);
                                result = manager.sendAction(pauseAction).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    System.out.println("unPause queue success");	
                                    mdb_agent.unpauseAction(pauseSession, agent.getAgentId());
                                    sendToAgent("UNPAUSESUCC");
                                    uti.writeAgentLog("- AGENT - Agent unpause\t"+addClient+"\t"+agent.getAgentId());
                                }else{
                                    System.out.println("unPause queue fail");
                                    sendToAgent("UNPAUSEFAIL");
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
                            try{
                                dialoutNumber = cmdList.get(1).toString();
                                result = outGoingCall(dialoutNumber);                                                                                                            
                                if("success".equalsIgnoreCase(result)){
                                    beginDialout = true;
                                    System.out.println("Dial out success: "+dialoutNumber);	
                                    uti.writeAgentLog("- AGENT - Agent Dialout \t"+agent.getAgentId()+"\t"+dialoutNumber);
//                                    sendToAgent("DIALOUT");                                      
                                }else{
                                    sendToAgent("DIALOUTFAIL");
                                    System.out.println("Dial out fail");	                          
                                    uti.writeAgentLog("- AGENT - Agent DialOut Fail\t"+agent.getAgentId()+"\t"+dialoutNumber);
                                }                                
                            }catch(Exception e){
                                sendToAgent("DIALOUTFAIL");
                            }                                                        
                        break;
                        case 110://change password
                            String newpass = cmdList.get(1);
                            if(mdb_agent.changePwd(newpass, agent.getAgentId())){
                                agent.setPass(newpass);
                                sendToAgent("CHANGEPWD@"+newpass);
                            }else{
                                sendToAgent("CHANGEPWDFAIL");
                            }
                        break;
                        case 112:  //exit & logout                            
                            if(agent != null){
                                removeQueue = removeQueue(agent.getInterface(), agent.getQueueId());
                                result = manager.sendAction(removeQueue).getResponse().toString();
                                if("success".equalsIgnoreCase(result)){
                                    System.out.println("remove queue success");
                                    mdb_agent.updateStatus(agent.getAgentId(), "0", "0");		
                                    mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());
                                    System.out.println("LOGOUTSUCC(exit system)");            
                                    uti.writeAgentLog("- AGENT - Agent logout - exit system\t"+addClient+"\t"+agent.getAgentId());
                                    try {                                                  
                                        closeConnect();
                                    } catch (Throwable ex) {
                                        Logger.getLogger(ManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
                                    }
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
                        mdb_agent.updateStatus(agent.getAgentId(), "0", "0");		
                        mdb_agent.logoutAction(agent.getSesion(), agent.getAgentId());   
                        uti.writeAgentLog("- AGENT - Interrup connection by client\t"+addClient+"\t"+agent.getAgentId());
                        try {
                            closeConnect();
                        } catch (Throwable ex) {
                            Logger.getLogger(ManagerAgent.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
        
        public String outGoingCall(String phoneNumber)throws Exception{
            OriginateAction originateAction = new OriginateAction();
            ManagerResponse originateResponse;
            originateAction.setChannel(agent.getInterface());// so extension can su dung de goi di "SIP/number"
            originateAction.setContext("from-internal");
            originateAction.setAsync(true);
            originateAction.setExten(phoneNumber);// so dien thoai can goi "number"        
            originateAction.setPriority(new Integer(1));
            originateAction.setTimeout(new Long(30000));//thoi gian time out
            String agentid = agent.getAgentId();
            originateAction.setCallerId(agentid);          
            originateResponse = manager.sendAction(originateAction, 30000);
            return originateResponse.getResponse().toString();
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
	
	public void closeConnect() throws Throwable{
            try{
                System.out.println("start close session");
                if(clientSocket != null){
                    inPutStream.close();
                    outPutStream.close();
                    clientSocket.close();      
                    System.out.println("close socket");
                    connected = false;
                }
                if(thread !=  null){
                    thread.interrupt();
                    System.out.println("finish interrupt thread");                
                }
                System.out.println("finish close session");                           
            }catch(IOException e){
                System.out.println("closeConnect(IOException): "+e);
            }

//            try{
//                System.out.println("start close session");
//                if(clientSocket != null){
//                    in.close();
//                    out.close();
//                    clientSocket.close();      
//                    System.out.println("close socket");
//                    connected = false;
//                }
//                if(thread !=  null){
//                    thread.interrupt();
//                    System.out.println("finish interrupt thread");                
//                }
//                System.out.println("finish close session");                           
//            }catch(IOException e){
//                System.out.println("closeConnect(IOException): "+e);
//            }            
	}
	
	public void sendToAgent(String data) throws IOException{
            try{
                if(clientSocket != null && outPutStream != null){                  
                    System.out.println("senttoagent:\t"+data+"\t"+agent.getAgentId());
                    outPutStream.println(data);
                    outPutStream.flush();
                }else
                    System.out.println("senttoagent: socket close: "+data);
            }catch(Exception e){
                System.out.println("sendToClient(IOException): "+e);
            }             

//            try{
//                if(clientSocket != null && out != null){                  
//                    System.out.println("senttoagent:\t"+data+"\t"+agent.getAgentId());
//                    out.writeUTF(data);
//                    out.flush();
//                }else
//                    System.out.println("senttoagent: socket close: "+data);
//            }catch(Exception e){
//                System.out.println("sendToClient(IOException): "+e);
//            }            
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
                    iface = agent.getInterface().toString();
                    String agentCaller = callEvent.getAgentCalled();
                    if(iface.equalsIgnoreCase(agentCaller) && ringing == true){                        
//                        dialin = true;
                        ringing = false;
                        connect = true;
                        ringno = true;
                        abandonCall = true;
                        System.out.println("iface(AgentCalledEvent):"+iface);
                        String callerName = callEvent.getCallerIdName();
                        String callerNum = callEvent.getCallerIdNum();
                        incomingCall = new IncomingCallObject();                        
                        incomingCall.setsession(uti.getSession());   
                        incomingCall.setcallerName(callerName);
                        incomingCall.setcallerNumber(callerNum);   
                        incomeTemp = incomingCall.getsession();
//                        mdb_agent.enterQueue(incomingCall.getsession(), agent.getAgentId(), iface, agent.getQueueId(), ENTERQUEUE,callerNum);
                        mdb_agent.inboudCallLog(incomingCall.getsession(), agent.getSesion(),agent.getAgentId(), iface, agent.getQueueId(), ENTERQUEUE,callerNum);
                        uti.writeAgentLog("- AGENT - Enter Queue\t" + agent.getAgentId() + "\t" + iface + "\t" + callerNum);
                        sendToAgent("RINGING@"+callerNum);       
                    }              
                }                                               
                //connect queue
                if(event instanceof AgentConnectEvent){
                    AgentConnectEvent connectEvent= (AgentConnectEvent)event;
                    System.out.println("***********************\t AgentConnectEvent\t ***********************");
                    String agentCaller = connectEvent.getMember();
                    iface = agent.getInterface().toString();
                    if(iface.equalsIgnoreCase(agentCaller) && connect == true){
                        connect = false;
                        abandonCall = false;
                        ringno = false;
                        complete = true;
                        System.out.println("iface(AgentConnectEvent):"+iface);
                        String ringtime = connectEvent.getRingtime().toString();
//                        mdb_agent.connectQueue(incomingCall.getsession(), agent.getAgentId(), iface, agent.getQueueId(), CONNECT, ringtime);
                        mdb_agent.inboudCallLog(incomingCall.getsession(), agent.getSesion(),agent.getAgentId(), iface, agent.getQueueId(), CONNECT,ringtime);
                        uti.writeAgentLog("- AGENT - Connect Queue\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());                             
                        sendToAgent("CONNECTED");
                    }
                }      
                //complete call
                /* An AgentsCompleteEvent is triggered after the state of all agents has been 
                 * reported in response to an AgentsAction.*/
                if(event instanceof AgentCompleteEvent){
                    AgentCompleteEvent comEvent= (AgentCompleteEvent)event;
                    System.out.println("***********************\t AgentCompleteEvent\t ***********************");  
                    String channel = comEvent.getMember();  
                    iface = agent.getInterface().toString();
                    if(iface.equalsIgnoreCase(channel) && complete == true){
                        complete = false;
                        ringing = true;
                        System.out.println("iface(AgentCompleteEvent):"+iface);
                        String reason = comEvent.getReason().toString();
                        String talkTime = comEvent.getTalkTime().toString();
                        if("agent".equalsIgnoreCase(reason)){
//                            mdb_agent.completeCall(incomingCall.getsession(), agent.getAgentId(), agent.getInterface(), comEvent.getQueue(), COMPLETEAGENT,talkTime);
                            mdb_agent.inboudCallLog(incomingCall.getsession(), agent.getSesion(),agent.getAgentId(), iface, agent.getQueueId(), COMPLETEAGENT,talkTime);
                            uti.writeAgentLog("- AGENT - Agent complete\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());
                        }else if("caller".equalsIgnoreCase(reason)){                            
//                            mdb_agent.completeCall(incomingCall.getsession(), agent.getAgentId(), agent.getInterface(), comEvent.getQueue(), COMPLETECALLER,talkTime);
                            mdb_agent.inboudCallLog(incomingCall.getsession(), agent.getSesion(),agent.getAgentId(), iface, agent.getQueueId(), COMPLETECALLER,talkTime);
                            uti.writeAgentLog("- AGENT - Caller complete\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());                                
                        }          
                        System.out.println("reason \t"+reason);
                        sendToAgent("COMPLETED");
                    }                    
                }          
                //ringing but no answer
                /* An AgentRingNoAnswerEvent is triggered when a call is routed to 
                 * an agent but the agent does not answer the call.*/
                if(event instanceof AgentRingNoAnswerEvent){
                    AgentRingNoAnswerEvent noAnsEvent= (AgentRingNoAnswerEvent)event;
                    System.out.println("***********************\t AgentRingNoAnswerEvent\t ***********************");  
                    String channel = noAnsEvent.getMember(); 
                    iface = agent.getInterface().toString();
                    if(iface.equalsIgnoreCase(channel) && ringno == true){                            
                        abandonCall = false;
                        ringing = true;
                        ringno = false;
                        System.out.println("iface(AgentRingNoAnswerEvent):"+iface);
                        String ringTime = String.valueOf(noAnsEvent.getRingtime()/1000);
                        System.out.println("ringTime\t"+ringTime);
//                        mdb_agent.ringNoans(incomeTemp, agent.getAgentId(), agent.getInterface(), agent.getQueueId(), RINGNOANSWER, ringTime);
                        mdb_agent.inboudCallLog(incomingCall.getsession(), agent.getSesion(),agent.getAgentId(), iface, agent.getQueueId(), RINGNOANSWER,ringTime);
                        uti.writeAgentLog("- AGENT - Ring noAns\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());                         
                        sendToAgent("RINGNOANWSER");
                    }                                                                                    
                }                                                
                if(event instanceof DialEvent){
                    System.out.println("***********************\t DialEvent\t ***********************");
                    DialEvent dialevent = (DialEvent)event;
                    if(uti.checkChannel(dialevent.getChannel())){                                                        
                        String channel = dialevent.getChannel();                    
                        channel = channel.substring(0, channel.indexOf("-"));
                        iface = agent.getInterface().toString();
                        if(channel.equalsIgnoreCase(iface)){
                            String subevent = dialevent.getSubEvent().toString();
                            System.out.println("dialoutNumber: "+dialoutNumber);
                            //begin dial out(Ringing)
                            if("begin".equalsIgnoreCase(subevent) && beginDialout){
                                System.out.println("subevent: "+subevent);
                                if(!dialoutNumber.equals("")){
                                    System.out.println("begin dial out(channel): "+channel);
                                    beginDialout = false;
                                    connectDialout = true;
                                    completeDialout = true;
                                    abandonCall = false;                    
                                    sendToAgent("DIALOUT");
                                    uniqueidDialout = dialevent.getDestUniqueId();
                                    dialoutSession = uti.getSession();
                                    mdb_agent.startDialout(agent.getAgentId(), agent.getInterface(), agent.getQueueId(), dialoutNumber, agent.getSesion(), dialoutSession);                                                                    
                                }
                            }else if("end".equalsIgnoreCase(subevent) && completeDialout && !"".equalsIgnoreCase(dialoutSession)){ 
                            //finish dial out
                                beginDialout = true;
                                completeDialout = false;
                                connectDialout = false;
                                String status = dialevent.getDialStatus();
                                System.out.println("status\t"+status);
                                if("ANSWER".equalsIgnoreCase(status)){
                                    System.out.println("finish dial out(channel): "+channel);                                        
                                    if(clock != null){
                                        dialoutTalktime = String.valueOf(clock.secs);
                                        clock.stop(); 
                                    }                                                                                                                                                                                                          
                                }                                 
                                mdb_agent.finishDialout(dialoutSession, dialoutTalktime,status);
                                dialoutSession = "";    
                                dialoutTalktime = "0";
                                dialoutNumber = "";
                                sendToAgent("HANGUPDIALOUT");
                            }
                        }                             
                    }else{
                        System.out.println("dialout fail\t "+dialevent.getChannel());
                    }  
                }                
                if(event instanceof NewStateEvent){                    
                    System.out.println("***********************\t NewStateEvent\t ***********************");
                    NewStateEvent stateEvent = (NewStateEvent)event;
                    String callerIdNum = stateEvent.getCallerIdNum();
                    String channelState = stateEvent.getChannelStateDesc();                           
                    //begin talking dial out
                    if(callerIdNum != null && connectDialout == true && !"".equalsIgnoreCase(dialoutNumber)){
                        if(callerIdNum.equalsIgnoreCase(dialoutNumber) && channelState.equalsIgnoreCase("UP") && uniqueidDialout.equalsIgnoreCase(stateEvent.getUniqueId())){
                            System.out.println("callerIdNum: "+callerIdNum+"\tchannelState: "+channelState+"\tdialoutNumber: "+dialoutNumber);       
                            connectDialout = false;      
                            abandonCall = false;
                            clock = new TimerClock();
                            clock.start();
                            sendToAgent("CONNECTEDDIALOUT");//CONNECTEDDIALOUT
                        }
                    }
                }                
                /* A HangupEvent is triggered when a channel is hung up.*/
                if(event instanceof HangupEvent){
                    HangupEvent hangEvent = (HangupEvent)event;
                    System.out.println("***********************\t HangupEvent\t ***********************");                        
                    if(abandonCall == true){
                        if(uti.checkChannel(hangEvent.getChannel())){
                            String channel = hangEvent.getChannel();                    
                            channel = channel.substring(0, channel.indexOf("-"));
                            iface = agent.getInterface().toString();
                            if(iface.equalsIgnoreCase(channel)){
                                abandonCall = false; 
                                ringing = true;
                                sendToAgent("HANGUPABANDON");
                                System.out.println("hangup abadon call: "+channel);
//                                mdb_agent.abandon(incomingCall.getsession(), agent.getAgentId(), iface, agent.getQueueId(), ABANDON,"");
                                mdb_agent.inboudCallLog(incomingCall.getsession(), agent.getSesion(),agent.getAgentId(), iface, agent.getQueueId(), ABANDON,"");
                                uti.writeAgentLog("- AGENT - Abandon Call\t" + agent.getAgentId() + "\t" + iface + "\t" + incomingCall.getcallerNumber());     
                            }
                        }
                    }                             	
                }                                                                                       
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
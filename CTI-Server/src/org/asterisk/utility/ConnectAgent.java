package org.asterisk.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.asterisk.main.ctiServer;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.*;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.QueuePauseAction;
import org.asteriskjava.manager.action.QueueRemoveAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class ConnectAgent implements Runnable{

	
	private ctiServer server;
	private Socket clientSocket;
	private String cmd;
	private Thread thread;
	private BufferedReader   inputStream;
	private int data;
	private Utility uti;
	private ManagerConnection manager;
	private String result;
	private AgentObject agent;
	private String addressAgent;
	public ConnectAgent(){

	}
	
	public ConnectAgent(ctiServer ctiserver,Socket client) throws IOException{
		try{
			server = ctiserver;
			clientSocket = client;
			inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}catch(Exception se){
			uti.writeAgentLog("Cannot connect to agent");
		}
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		uti = new Utility();
		manager = server.manager;
		addressAgent = clientSocket.getInetAddress().toString();
		while(thread != null){
			try{
				cmd = inputStream.readLine();
				uti.writeAgentLog("request from agent \t"+addressAgent+"\t"+cmd);
				ArrayList<String> cmdList = uti.getList(cmd);
				data = Integer.parseInt(cmdList.get(0));				
				switch(data){
	            	case 100:  
	            		try{
	            			System.out.println("(Login) " +cmd);
	            			uti.writeAgentLog("agent login \t"+addressAgent+"\t"+cmd);
	            			getAgent(cmdList);		            		
		            		//access DB to check login 
		            		//check status of agent & exten & queue
		            		QueueAddAction qAdd = addQueue(agent.getAgent(), agent.getExtension(), agent.getQueue(),agent.getPenalty());
		            		result = manager.sendAction(qAdd, 2000).getResponse().toString();
		            		if(result.equalsIgnoreCase("success")){
		            			System.out.println("<100OK>add queue success");//if success send 100@OK to client
		            			sendToAgent("100");
		            		}else{
		            			sendToAgent("101");
		            			System.out.println("<101FAIL>add queue fail");//if fail send 101@<reason fail> to client	
		            		}
		            				            		
	            		}catch(Exception ex){
	            			sendToAgent("101");
	            		}
	            		break;
		            case 102:  
		            	try{
			            	System.out.println("(Logout) "+cmd);
			            	//check status of agent 
//			            	if(agent!=null)			            		
			            	//remove agent out of queue
			            	QueueRemoveAction removeQueue = removeQueue(agent.getExtension(), agent.getQueue());
			            	result = manager.sendAction(removeQueue).getResponse().toString();
			            	System.out.println("logout result: "+result);
			            	//return result for agent
		            		if(result.equalsIgnoreCase("success")){
		            			System.out.println("<102OK>remove queue success");//if success send 100@OK to client
		            			closeConnect();
		            			//write logfile
		            		}else
		            			System.out.println("<103FAIL>remove queue fail");//if fail send 101@<reason fail> to client
		            	}catch(Exception ex){
		            		
		            	}
	                	break;
		            case 104:  
		            	try{
			            	System.out.println("(Pause/unPause) " +cmd);
			            	QueuePauseAction pauseAction = null;
			            	if(cmdList.get(3).equalsIgnoreCase("off")){
			            		System.out.println("(Pause)");
			            		pauseAction = queuePause(agent.getExtension(), agent.getQueue(), true);
			            	}else if(cmdList.get(3).equalsIgnoreCase("on")){
			            		System.out.println("(unPause)");
			            		pauseAction = queuePause(agent.getExtension(), agent.getQueue(), false);
			            	}
			            	result = manager.sendAction(pauseAction).getResponse().toString();
			            	System.out.println("pause result: "+result);
			            	//return result for agent
		            		if(result.equalsIgnoreCase("success")){
		            			System.out.println("<104@pause>Pause queue success");//if success send 100@OK to client
		            			System.out.println("<104@unpause>unPause queue success");//if success send 100@OK to client
		            		}else
		            			System.out.println("<105@FAIL>Pause/unPause queue fail");//if fail send 101@<reason fail> to client
		            	}catch(Exception ex){
		            		
		            	}
	                	break;
		            case 106:  		            	
		            	try{
		            		System.out.println("(Transfer) " +cmd);
		            		
		            	}catch(Exception ex){
		            		
		            	}
	                	break;
		            case 108:  
		            	try{
			            	System.out.println("(Hold) " +cmd);    			            	
		            	}catch(Exception ex){
		            		
		            	}
	       		 		break;
		            case 110:  
		            	try{
		            		System.out.println("(...110...) "+cmd);
		            	}catch(Exception ex){
		            		
		            	}
	       		 		break;
		            case 112:  
		            	try{
		            		System.out.println("(...112...) "+cmd);
		            	}catch(Exception ex){
		            		
		            	}
	       		 		break;	       		 		
		            default: 
		            	System.out.println("(invalid) " +cmd);
		            	try{
		            		
		            	}catch(Exception ex){
		            		
		            	}
	                	break;					
				}
				
				
			}catch(IOException ex){
				
			}
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
		agent.setExtension(cmdList.get(3));
		agent.setQueue(cmdList.get(4));	
		agent.setPenalty(99);
	}
	
	public void closeConnect() throws IOException{
		if(thread!=null){
			thread.interrupt();
			thread = null;
		}
		if(!clientSocket.isClosed()){
			clientSocket.close();
			inputStream.close();
		}
	}
	public void sendToAgent(String data) throws IOException{
		PrintWriter outToAgent = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		outToAgent.println(data);
		outToAgent.flush();
//		outToAgent.close();
	}
}

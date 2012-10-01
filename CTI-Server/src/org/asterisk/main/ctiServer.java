package org.asterisk.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.asterisk.utility.ConnectAgent;
import org.asterisk.utility.ConnectAsterisk;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;
import org.asteriskjava.fastagi.command.DatabaseDelCommand;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.ShutdownEvent;
import org.asteriskjava.manager.event.StatusEvent;

public class ctiServer implements ManagerEventListener{

	static ServerSocket server;
	static boolean running = true;
	static ConnectAgent agent;
	static Utility uti;
	static Thread thread;
	public static ManagerConnection manager;
	static ConnectAsterisk connectAsterisk;
	private static String host = "172.168.10.205";
	private static String username = "manager";
	private static String password = "pa55w0rd";
	static ConnectDatabase database;
		
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		uti = new Utility();		
		ctiServer s = new ctiServer();
		
		/*connect to asterisk server*/
		uti.writeAsteriskLog("Start connect to Asterisk server");
		getManager(host, username, password);
		manager.login();
		System.out.println("connect to asterisk server successfull");
		uti.writeAsteriskLog("Connect to Asterisk server successfull");
		manager.addEventListener(s);		
		manager.sendAction(new StatusAction());		
		System.out.println("listen event from asterisk");
		uti.writeAsteriskLog("listen event/send action from/to Asterisk");
		
		/*check connect to Database*/	
		uti.writeAsteriskLog("connect to database");
		database = new ConnectDatabase();
		if(database.checkDatabase())
			uti.writeAsteriskLog("connect to database successfull");
		else
			uti.writeAsteriskLog("connect to database fail");
		
		/*open socket and waiting for client connect*/
		server = new ServerSocket(22222);
		System.out.println("Waiting for agent connect");		
		uti.writeAgentLog("Start CTIServer");
		uti.writeAgentLog("Waiting for agent connect...");
		uti.writeAgentLog("");
		while(running){			
			Socket clientsocket = server.accept();
			agent = new ConnectAgent(s, clientsocket);
			uti.writeAgentLog("Acept connect from client"+"\t"+clientsocket.getInetAddress().getHostAddress());							
			System.out.println("acept connect from agent ");
		}
	}
	
	@Override
	public void onManagerEvent(ManagerEvent event){
		// TODO Auto-generated method stub		
		try {
	        /* A ConnectEvent is triggered after successful login to the Asterisk server.*/
		    if(event instanceof ConnectEvent){
		    	ConnectEvent conEvent= (ConnectEvent)event;
		    	System.out.println("***********************\t ConnectEvent\t ***********************");
		    	System.out.println("getDateReceived \t"+conEvent.getDateReceived());  	
		    	System.out.println("getSource \t"+conEvent.getSource());
		    }
		    
		    /*A DisconnectEvent is triggered when the connection to the asterisk server is lost.*/
		    if(event instanceof DisconnectEvent){
		    	DisconnectEvent disconEvent= (DisconnectEvent)event;
		    	System.out.println("***********************\t disConnectEvent\t ***********************");
		    	System.out.println("getDateReceived \t"+disconEvent.getDateReceived());
		    	System.out.println("getSource \t"+disconEvent.getSource());
		    }
		    /* A ReloadEvent is triggerd when the reload console command is executed or the Asterisk server is started.  */
		    if(event instanceof ReloadEvent){
		    	ReloadEvent reloadEvent= (ReloadEvent)event;
		    	System.out.println("***********************\t ReloadEvent\t ***********************");
		    	System.out.println("getMessage \t"+reloadEvent.getMessage());
		    	System.out.println("getDateReceived \t"+reloadEvent.getDateReceived());    	
		    	System.out.println("getSource \t"+reloadEvent.getSource());
		    }
		    
	        /* A ShutdownEvent is triggered when the asterisk server is shut down or restarted.*/
		    if(event instanceof ShutdownEvent){
		    	ShutdownEvent shutEvent= (ShutdownEvent)event;
		    	System.out.println("***********************\t ShutdownEvent\t ***********************");
		    	System.out.println("getShutdown \t"+shutEvent.getShutdown());		    	
		    	System.out.println("getDateReceived \t"+shutEvent.getDateReceived());
		    	System.out.println("getRestart \t"+shutEvent.getRestart().toString());    	
		    	System.out.println("getSource \t"+shutEvent.getSource()); 
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getManager(String host, String username, String password){
		ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
		manager = factory.createManagerConnection();
	}
		

}

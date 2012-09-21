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
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewExtenEvent;
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
		database = new ConnectDatabase();
		database.checkDatabase();		
		
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
	
//	public String CheckLogin(ArrayList list){
//		String result = "";
//		ArrayList listCMD = list;
//		return result;
//	}

	@Override
	public void onManagerEvent(ManagerEvent event){
		// TODO Auto-generated method stub		
		try {
			System.out.println("event:  "+event);
			uti.writeAsteriskLog(event.toString());
			NewExtenEvent n = (NewExtenEvent)event;
//			event.get
//			n.get
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getManager(String host, String username, String password){
		ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
		manager = factory.createManagerConnection();
	}
		

}

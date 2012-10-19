package org.asterisk.main;

import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLServerSocket;
import org.asterisk.utility.AgentListen;

import org.asterisk.utility.ManagerAgent;
import org.asterisk.utility.Managerdb;
import org.asterisk.utility.QueueInfo;
import org.asterisk.utility.QueueListen;
import org.asterisk.utility.Utility;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.ShutdownEvent;

public class ctiServer implements ManagerEventListener{

//	private static ServerSocket aserver;
//	private static ManagerAgent agent = null;
	private static Utility uti;
//	public static ManagerConnection manager;	
	private static int aport = 22222;
	private static int qport = 33333;
//	private static String userAsterisk = "manager";
//	private static String pwdAsterisk = "123456";
        private static String host = "172.168.10.208";
//        private static String host = "localhost";
        private static String dbname = "cti_database";
	private static String userSql = "cti";
	private static String pwdSql  = "123456";
	private static Managerdb mdb_agent;
        private static QueueListen qlisten;  
        private static AgentListen alisten;  
        
        
        
		
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
            // TODO Auto-generated method stub
            uti = new Utility();		
//            ctiServer cti = new ctiServer();
            /*connect to asterisk server*/
//            uti.writeAsteriskLog("Start Connect to Asterisk Server");
//            connectAsterisk(host, userAsterisk, pwdAsterisk);
//            manager.login();
//            System.out.println("connect to asterisk server successfull");
//            uti.writeAsteriskLog("Connect to Asterisk Server Successfull");
//            manager.addEventListener(cti);		
//            manager.sendAction(new StatusAction());		
//            System.out.println("listen event from asterisk");
//            uti.writeAsteriskLog("listen event/send action from/to Asterisk");
            /*Connect to Database*/	
//            uti.writeAsteriskLog("Connecting to Database...");
            mdb_agent = new Managerdb(dbname,userSql,pwdSql,host);
            if(mdb_agent.isConnect()){
                uti.writeAsteriskLog("Connect to Database Successfull");
                System.out.println("Connect to Database Successfull");
                alisten = new AgentListen( aport, mdb_agent );
                qlisten = new QueueListen( qport, mdb_agent );                
            } else {
                uti.writeAsteriskLog("Connect to Database Fail");
                System.out.println("Connect to Database Fail");
                System.out.println("Interrup Connection.");                
            }
                
//            System.out.println("Connected to Database");
            /*open socket and waiting for client connect*/
//            aserver = new ServerSocket(aport);
//            System.out.println("Waiting for agent connect");		
//            uti.writeAgentLog("Start CTIServer");
//            uti.writeAgentLog("Waiting for agent connect...");
            //send list queue for client
            
//            new QueueListen(qport);
            
//            new AgentListen(aport);
//            while(true){			
//                Socket clientsocket = aserver.accept();
////                agent = new ManagerAgent(cti, clientsocket);
//                agent = new ManagerAgent(cti, clientsocket,mdb_agent);
//                uti.writeAgentLog("Connect To Agent From Address"+"\t"+clientsocket.getInetAddress().getHostAddress());							
//                System.out.println("acept connect from agent ");
//            }
	}
        
//        private static void queue_listen()throws Exception{
//            QueueInfo qInfor = null;
//            while(true){
//                System.out.println("start queue_listen");
//                Socket client = server2.accept();                
//                qInfor = new QueueInfo(client);
//                System.out.println("send list queue for client");
//            }
//        }
//        private static void agent_listen()throws Exception{
//            ManagerAgent agent = null;
//            while(true){
//                Socket client = server1.accept();
//                agent = new ManagerAgent(this, client);
//                agent = new ManagerAgent();                
//            }
//        }
	
	@Override
	public void onManagerEvent(ManagerEvent event){
		// TODO Auto-generated method stub		
//		try {
//	        /* A ConnectEvent is triggered after successful login to the Asterisk server.*/
//		    if(event instanceof ConnectEvent){
//		    	ConnectEvent conEvent= (ConnectEvent)event;
//		    	System.out.println("***********************\t ConnectEvent\t ***********************");
//		    	System.out.println("getDateReceived \t"+conEvent.getDateReceived());  	
//		    	System.out.println("getSource \t"+conEvent.getSource());
//		    }
//		    
//		    /*A DisconnectEvent is triggered when the connection to the asterisk server is lost.*/
//		    if(event instanceof DisconnectEvent){
//		    	DisconnectEvent disconEvent= (DisconnectEvent)event;
//		    	System.out.println("***********************\t disConnectEvent\t ***********************");
//		    	System.out.println("getDateReceived \t"+disconEvent.getDateReceived());
//		    	System.out.println("getSource \t"+disconEvent.getSource());
//		    }
//		    /* A ReloadEvent is triggerd when the reload console command is executed or the Asterisk server is started.  */
//		    if(event instanceof ReloadEvent){
//		    	ReloadEvent reloadEvent= (ReloadEvent)event;
//		    	System.out.println("***********************\t ReloadEvent\t ***********************");
//		    	System.out.println("getMessage \t"+reloadEvent.getMessage());
//		    	System.out.println("getDateReceived \t"+reloadEvent.getDateReceived());    	
//		    	System.out.println("getSource \t"+reloadEvent.getSource());
//		    }
//		    
//	        /* A ShutdownEvent is triggered when the asterisk server is shut down or restarted.*/
//		    if(event instanceof ShutdownEvent){
//		    	ShutdownEvent shutEvent= (ShutdownEvent)event;
//		    	System.out.println("***********************\t ShutdownEvent\t ***********************");
//		    	System.out.println("getShutdown \t"+shutEvent.getShutdown());		    	
//		    	System.out.println("getDateReceived \t"+shutEvent.getDateReceived());
//		    	System.out.println("getRestart \t"+shutEvent.getRestart().toString());    	
//		    	System.out.println("getSource \t"+shutEvent.getSource()); 
//		    }
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
//	public static void connectAsterisk(String host, String username, String password){
//            ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
//            manager = factory.createManagerConnection();
//	}
		

}

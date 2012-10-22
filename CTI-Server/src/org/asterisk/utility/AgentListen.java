/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import org.asterisk.main.ctiServer;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ConnectEvent;
import org.asteriskjava.manager.event.DisconnectEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.ReloadEvent;
import org.asteriskjava.manager.event.ShutdownEvent;

/**
 *
 * @author leehoa
 */
public class AgentListen implements Runnable, ManagerEventListener{
    
    public static ManagerConnection manager;
    private static String hostAsterisk = "172.168.10.208";
//    private static String host = "127.0.0.1";
    private static String userAsterisk = "manager";
    private static String pwdAsterisk = "123456";
    private static int port;
    private static ServerSocket aserver;
    private static Utility uti;
    public  static ctiServer cti = null;
    private static ManagerAgent agent = null;
    static String filename = "infor.properties";    
    private static Thread thread;
    private static Managerdb mdb_agent;
    
    public AgentListen(){
    }
    public AgentListen(int p) throws IOException{
        port = p;        
        thread = new Thread(this);
        thread.start();
    }        
    public AgentListen(int p, Managerdb mdb) throws IOException{
        mdb_agent = mdb;
        port = p;        
        thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        try{
            uti = new Utility();
            if(uti.readInfor(filename, pwdAsterisk)==null){
                System.out.println("write infor asterisk");
                uti.writeInfor(filename, "pwdAsterisk", pwdAsterisk);
                uti.writeInfor(filename, "userAsterisk", userAsterisk);
                uti.writeInfor(filename, "hostAsterisk", hostAsterisk);
            }
            pwdAsterisk = uti.readInfor(filename, "pwdAsterisk");
            userAsterisk = uti.readInfor(filename, "userAsterisk");
            hostAsterisk = uti.readInfor(filename, "hostAsterisk");
            
                
            aserver = new ServerSocket(port);
            /*connect to asterisk server*/
            uti.writeAsteriskLog("Start Connect to Asterisk Server");
            connectAsterisk(hostAsterisk, userAsterisk, pwdAsterisk);
            manager.login();
            uti.writeAsteriskLog("Connect to Asterisk Server Successfull");
            manager.addEventListener(this);		
            manager.sendAction(new StatusAction());		
            System.out.println("listen event from asterisk");
            uti.writeAsteriskLog("listen event/send action from/to Asterisk");                        
            if( manager != null ){
                uti.writeAsteriskLog("Connect to Asterisk Server Successfull");
                System.out.println("Connect to Asterisk Server Successfull");
                while(true){			
                    System.out.println("start agent_listen");
                    Socket clientsocket = aserver.accept();
                    agent = new ManagerAgent(this, clientsocket, mdb_agent);
                    uti.writeAgentLog("Connect To Agent From Address"+"\t"+clientsocket.getInetAddress().getHostAddress());							
                    System.out.println("acept connect from agent ");
                }                
            }else{
                uti.writeAsteriskLog("Connect to Asterisk Server Fail");
                System.out.println("Connect to Asterisk Server Fail");
                System.out.println("Interrup Connection.");     
                aserver = null;
                manager.logoff();
            }                                    
        }catch(Exception e){            
            System.out.println("AgentListen thread exception\r\n"+e);
        }                
    }
    
    public static void connectAsterisk(String host, String username, String password){
        ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
        manager = factory.createManagerConnection();
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
}

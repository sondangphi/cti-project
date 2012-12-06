/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import org.asterisk.main.Server;
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
    private static String hostAsterisk = "10.0.8.149";
    private static String userAsterisk = "manager";
    private static String pwdAsterisk = "123456";
    private static int port;
    private static ServerSocket aserver;
    private static Utility uti;
    public  static Server cti = null;
    private static ManagerAgent agent = null;
    private static String filename = "infor.properties";    
    private static Thread thread;
    private static Managerdb mdb_agent;
    
    public AgentListen(){
    }
//    public AgentListen(int p) throws IOException{
//        port = p;        
//        thread = new Thread(this);
//        thread.start();
//    }        
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
            pwdAsterisk = uti.readInfor(filename, "Asterisk_pwd");
            userAsterisk = uti.readInfor(filename, "Asterisk_user");
            hostAsterisk = uti.readInfor(filename, "Asterisk_server");                            
            aserver = new ServerSocket(port);
            /*connect to asterisk server*/
            connectAsterisk(hostAsterisk, userAsterisk, pwdAsterisk);
            manager.login();            
            manager.addEventListener(this);		
            manager.sendAction(new StatusAction());		                                 
            if( manager != null ){
                uti.writeAsteriskLog("- SYSTE  - Connect to Asterisk Server Successful");
                System.out.println("Connect to Asterisk Server Successful");
                while(true){
                    System.out.println("start agent_listen");
                    Socket clientsocket = aserver.accept();                    
                    agent = new ManagerAgent(this, clientsocket, mdb_agent);
                    String add = clientsocket.getInetAddress().getHostAddress().toString();
                    uti.writeAgentLog("- AGENT - Accept connect from address"+"\t"+add);							
                    System.out.println("acept connect from agent ");
                }
            }else{
                uti.writeAsteriskLog("- SYSTE  - Connect to Asterisk Server Fail");
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
                uti.writeAsteriskLog("- SYSTE  - Reconnect to Asterisk Server");
            }

            /*A DisconnectEvent is triggered when the connection to the asterisk server is lost.*/
            if(event instanceof DisconnectEvent){
                uti.writeAsteriskLog("- SYSTE  - Disconnect to Asterisk Server\r\n");
            }

            /* A ReloadEvent is triggerd when the reload console command is executed or the Asterisk server is started.  */
            if(event instanceof ReloadEvent) {
                uti.writeAsteriskLog("- SYSTE  - Reload Asterisk Server");
            }                

            /* A ShutdownEvent is triggered when the asterisk server is shut down or restarted.*/
            if(event instanceof ShutdownEvent) {
                uti.writeAsteriskLog("- SYSTE  - Shutdown Asterisk Server");
            }                        
        } catch (Exception e) {
                e.printStackTrace();
        }
    }        
}

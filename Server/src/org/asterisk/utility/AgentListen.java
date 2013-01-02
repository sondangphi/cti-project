/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
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
    
//    public  ManagerConnection manager;
    public  ManagerConnection managerSystem;
//    public  ManagerConnection managerEvent;
    private String astServer = "10.0.8.149";
    private String astUserSystem = "manager";
    private String astPwdSystem = "123456";
    private String astUserEvent = "manager";
    private String astPwdEvent = "123456";    
    private int port;
    private ServerSocket aserver;
    private Utility uti;
    private String filename = "infor.properties";    
    private Thread thread;
    private Managerdb mdb_agent;
    private int TIME_OUT = 60000;
    public ArrayList<Socket> lists = new ArrayList<Socket>();
    
    public AgentListen(){
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
            astServer = uti.readInfor(filename, "astServer");
            astPwdSystem = uti.readInfor(filename, "astPwdSystem");
            astUserSystem = uti.readInfor(filename, "astUserSystem");                                                    
            astPwdEvent = uti.readInfor(filename, "astPwdEvent");
            astUserEvent = uti.readInfor(filename, "astUserEvent");             
            aserver = new ServerSocket(port);
            /*connect to asterisk server*/            
            connectAsterisk(astServer, astUserSystem, astPwdSystem);                        
            managerSystem.login();            
            managerSystem.addEventListener(this);		
            managerSystem.sendAction(new StatusAction());            
            if( managerSystem != null ){
                uti.writeAsteriskLog("- SYSTE  - Connect to Asterisk Server Successful");
                System.out.println("Connect to Asterisk Server Successful");
                while(true){                    
                    Socket clientsocket = new Socket();
                    clientsocket = aserver.accept();                 
                    System.out.println("socket timeout\t"+clientsocket.getSoTimeout()); 
                    System.out.println("socket localport\t"+clientsocket.getPort());
                    new ManagerAgent(managerSystem, clientsocket, mdb_agent);
                    String add = clientsocket.getInetAddress().getHostAddress().toString();
                    uti.writeAgentLog("- AGENT - Accept connect "+"\t"+add);							
                    System.out.println("acept connect from agent\t"+add);
                }
            }else{
                uti.writeAsteriskLog("- SYSTE  - Connect to Asterisk Server Fail");
                System.out.println("Connect to Asterisk Server Fail");
                System.out.println("Interrup Connection.");     
                managerSystem.logoff();
            }                                    
        }catch(Exception e){            
            System.out.println("AgentListen thread exception\r\n"+e);
        }      
        catch(Throwable e){            
            System.out.println("AgentListen thread exception\r\n"+e);
        } 
    }
    
    public void connectAsterisk(String host, String username, String password){
        try{
            ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
            managerSystem = factory.createManagerConnection();
        }catch(Exception e){
        }
    }  
    public void connectAsterisk(String host, String username, String password, ManagerConnection mana){
        try{
            ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
            mana = factory.createManagerConnection();
        }catch(Exception e){
        }
    }         
    
    public void checkSocket(){
        System.out.println("size: "+lists.size());
        for(Socket s: lists){
            if(s != null){
                System.out.println("port: "+s.getPort());
            }
        }
    }
    @Override
    public void onManagerEvent(ManagerEvent event){
        // TODO Auto-generated method stub		
        try {
            /* A ConnectEvent is triggered after successful login to the Asterisk server.*/
            if(event instanceof ConnectEvent){       
                uti.writeAsteriskLog("- SYSTE  - Reconnect to Asterisk Server");
            }else 

            /*A DisconnectEvent is triggered when the connection to the asterisk server is lost.*/
            if(event instanceof DisconnectEvent){
                uti.writeAsteriskLog("- SYSTE  - Disconnect to Asterisk Server\r\n");
            }else 

            /* A ReloadEvent is triggerd when the reload console command is executed or the Asterisk server is started.  */
            if(event instanceof ReloadEvent) {
                uti.writeAsteriskLog("- SYSTE  - Reload Asterisk Server");
            }else                 

            /* A ShutdownEvent is triggered when the asterisk server is shut down or restarted.*/
            if(event instanceof ShutdownEvent) {
                uti.writeAsteriskLog("- SYSTE  - Shutdown Asterisk Server");
            }                        
        } catch (Exception e) {
                e.printStackTrace();
        }
    }        
}

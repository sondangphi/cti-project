package org.asterisk.utility;


import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asterisk.main.*;
import org.asterisk.model.AgentObject;

public class Agent implements Runnable{

	/**
	 * @param args
	 */
	private boolean running = true;
	private Thread mainThread;
	public Socket clientSocket;
        private AgentObject agentObject;
        private LoginForm loginform;        
        public static MainForm mainForm;
        
        private String filename = "infor.properties";                         		
        private String Mysql_server = "172.168.10.208";      
        private String Mysql_dbname = "ast_callcenter";
	private String Mysql_user = "callcenter";
	private String Mysql_pwd  = "callcenter";   
        private ConnectDatabase con;
        private Utility uti = new Utility();
        private boolean close = true;        
        private TimerClock clockWorktime = null;
        private TimerClock clockDialin = null;
        private TimerClock clockDialout = null;
        private BufferedReader infromServer;
        private PrintWriter outtoServer;        
        private String com;  
        private boolean dialout = false;
        private KeepAlive keepAlive;
        private Thread keep_alive; 
        private Object synObject = new Object();
                
//        private int vitualPort = 1234;
        
//        DataInputStream in;
//        DataOutputStream out;        
        
	public Agent(){
		
	}
        //new thread for client
        public Agent(Socket s, LoginForm login, AgentObject agentO, String command)throws Exception{
            try{
                clientSocket = s;
                loginform = login;
                agentObject = agentO;
                com = command;
                mainThread = new Thread(this);
                mainThread.start();                
            }catch(Exception e){
                System.out.println("Exception agent thread: "+e);
            }                
	}      	
	@Override
	public void run() {
            // TODO Auto-generated method stub
            try{
//                clientSocket.setKeepAlive(true);
                String command = "";
                CODE code;
                Mysql_dbname = uti.readInfor(filename, "MySql_database");
                Mysql_server = uti.readInfor(filename, "MySql_server");
                Mysql_user = uti.readInfor(filename, "MySql_user");
                Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
                outtoServer = new PrintWriter(clientSocket.getOutputStream());
                infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));                 
//                in = new DataInputStream(clientSocket.getInputStream());
//                out = new DataOutputStream(clientSocket.getOutputStream());                
                sendtoServer(com);
                while(running){                    
                    command = infromServer.readLine();
//                    command = in.readUTF();
                    System.out.println("***listen from server***");
                    System.out.println("***receive from server: "+command);
                    if(command == null){
                        System.out.println("null value from server");
                        agentLogout();
                    }                    
                    if (keep_alive != null) {
                        if (keep_alive.isAlive()) {
                            keep_alive.interrupt();
                        }
                    }
                    keep_alive = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(10000);
                                System.out.println("server interrupt1: " +clientSocket.getRemoteSocketAddress().toString());
                                agentLogout();
                            } catch (InterruptedException ex) { }  
                        }
                    });
                    keep_alive.start();                                       
                    ArrayList<String> cmdList = getList(command);							
                    code = CODE.valueOf(cmdList.get(0).toUpperCase());
                    switch(code){
                    case LOGINSUCC: //result LOGIN SUCCESS
                        System.out.println("LOGIN SUCCESS");
                        agentObject.setAgentName(cmdList.get(1));
                        agentObject.setSession(cmdList.get(2));
                        mainForm = new MainForm(this, agentObject);
                        mainForm.setVisible(true);
                        loginform.dispose();
                        clockWorktime = new TimerClock(mainForm, false);
                        clockWorktime.start();
                        uti.writelog("LOGIN SUCCESS\t"+agentObject.getAgentId());
                        keepAlive  = new KeepAlive(this);
                        
                    break;
                    case LOGINFAIL: //result LOGIN FAIL                                                     
                        try {
                            uti.writelog("LOGIN FAIL\t"+agentObject.getAgentId());
                            System.out.println("LOGIN FAIL");
                            loginform.lb_status.setText(cmdList.get(1));
                            closeConnect();                            
                        } catch (Throwable ex) {
                            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    break;
                    case LOGOUTSUCC: //result LOGOUT SUCCESS              
                        try {
                            synchronized(synObject){     
                                if(close){
                                    mainForm.setVisible(false);
                                    mainForm.dispose();
                                    mainForm = null;
                                    close = false;
                                    clockWorktime.stop();                            
                                    closeConnect();  
                                    new LoginForm().setVisible(true);
                                    System.out.println("LOGOUT SUCCESS");                                
                                    uti.writelog("LOGOUT SUCCESS\t"+agentObject.getAgentId()+"\r\n");
                                }
                            }                            
                        } catch (Exception ex) {
                            System.out.println("LOGOUTSUCC " +ex);
                        }
                        
                    break;
                    case LOGOUTFAIL: //result LOGOUT FAIL
                        System.out.println("LOGOUT FAIL\t"+agentObject.getAgentId());
                        uti.writelog("LOGOUT FAIL");
                    break;	            	
                    case PAUSESUCC: //result PAUSE
                        if(clockWorktime != null)
                            clockWorktime.pause();
                        mainForm.btn_logout.setEnabled(false);
                        mainForm.setAllEnable(false);
                        mainForm.lb_status.setText("Not Ready");
                        mainForm.btn_pause.setSelected(true);
                        mainForm.setPauseIcon(true);
                        System.out.println("PAUSESUCC");
                        uti.writelog("PAUSE SUCCESS\t"+agentObject.getAgentId());
                    break;
                    case PAUSEFAIL: //result PAUSE
                        System.out.println("PAUSEFAIL");
                        uti.writelog("PAUSE FAIL\t"+agentObject.getAgentId());
                    break;
                    case UNPAUSESUCC: //result UNPAUSESUCC
                        clockWorktime.resume();
                        mainForm.btn_logout.setEnabled(true);
                        mainForm.setAllEnable(true);
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setSelected(false);  
                        mainForm.setPauseIcon(false);
                        System.out.println("UNPAUSESUCC");
                        uti.writelog("UNPAUSE SUCCESS\t"+agentObject.getAgentId());
                    break;
                    case UNPAUSEFAIL: //result UNPAUSE
                        System.out.println("UNPAUSEFAIL");
                        uti.writelog("UNPAUSE FAIL\t"+agentObject.getAgentId());
                    break;
                    case TRANSSUCC: //result TRANSFER	            			
                    break;
                    case TRANSFAIL: //result TRANSFER	            			
                    break;
                    case HOLDSUCC: //result HOLD
                    break;
                    case HOLDFAIL: //result HOLD
                    break;
                    case CHANGEPWD: //CHANGEPWD
                        mainForm.chanpwdform.showDialog("Change Password Success");                        
                        agentObject.setPass(cmdList.get(1));
                        System.out.println("change pass success");
                        uti.writelog("CHANGE PASSWORD SUCCESS\t"+agentObject.getAgentId());
                    break;                        
                    case CHANGEPWDFAIL: //CHANGEPWD
                        mainForm.chanpwdform.showDialog("Change Password Fail");                        
                        System.out.println("change pass fail");
                        uti.writelog("CHANGE PASSWORD FAIL\t"+agentObject.getAgentId());
                    break;                                
                    case RINGING: //EVENT RINGING
                        try{
                            String callerNum = cmdList.get(1);
                            System.out.println("callerNum\t"+callerNum);
                            mainForm.lb_status.setText("Ringing...");
                            mainForm.lb_callduration.setText("00:00:00");
                            mainForm.btn_pause.setEnabled(false);       
                            mainForm.setAllEnable(false);  
                            mainForm.lb_callernumber.setText(callerNum);
                            mainForm.btn_hangup.setEnabled(true);
                            uti.writelog("RINGING\t"+callerNum);
                        }catch(Exception e){
                            System.out.println("RINGING: "+e);
                        }
                    break;
                    case CONNECTED://connected incoming call
                        System.out.println("CONNECTED incoming call");
                        mainForm.lb_status.setText("Busy"); 
                        if(clockDialin != null)
                            clockDialin.stop();
                        clockDialin = new TimerClock(mainForm, true);
                        clockDialin.start();
                        uti.writelog("CONNECTED\t"+mainForm.lb_callernumber.getText());
                    break;
                    case COMPLETED://connected incoming call
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true); 
                        mainForm.btn_hangup.setEnabled(false);
                        if(clockDialin != null){
                            clockDialin.stop(); 
                        }
                        uti.writelog("COMPLETED\t"+mainForm.lb_callernumber.getText());
                    break;
                    case RINGNOANWSER: 
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true);
                        mainForm.btn_hangup.setEnabled(false);
                        uti.writelog("RING NOANWSER\t"+mainForm.lb_callernumber.getText());
                    break;                        
                    case DIALOUT: //result 	
                        System.out.println("DIALOUT");  
                        uti.writelog("DIALOUT\t"+mainForm.dialNumber);
                        dialout = true;
                        mainForm.lb_status.setText("Dialing...");
                        mainForm.lb_callduration.setText("00:00:00");
                        mainForm.lb_callernumber.setText("");
                        mainForm.btn_pause.setEnabled(false);       
                        mainForm.setAllEnable(false);      
                        mainForm.btn_hangup.setEnabled(true);
                    break;                        
                    case DIALOUTFAIL: 
                        uti.writelog("DIALOUT FAIL\t"+mainForm.dialNumber);
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true);
                        mainForm.btn_hangup.setEnabled(false);
                    break;                                
                    case CONNECTEDDIALOUT: 
                        uti.writelog("CONNECTED DIALOUT\t"+mainForm.dialNumber);
                        if(clockDialout != null)
                            clockDialout.stop();
                        clockDialout = new TimerClock(mainForm, true);
                        clockDialout.start();                        
                        mainForm.lb_status.setText("Busy");
                        System.out.println("Connected dialout");
                    break;
                    case HANGUPDIALOUT:              
                        uti.writelog("HANGUP DIALOUT\t"+mainForm.dialNumber);
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true); 
                        mainForm.btn_hangup.setEnabled(false);
                        if(clockDialout != null){
                            clockDialout.stop();
                        }
                        System.out.println("hangup dialout");
                    break;                          
                    case HANGUPABANDON: 
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true);      
                        mainForm.btn_hangup.setEnabled(false);
                    break;                        
                    case HANGUPSUCCESS: //
                        System.out.println("HANGUPSUCCESS");
                        mainForm.btn_hangup.setEnabled(false);
                    break;      
                    case HANGUPFAIL: //EVENT ANSWER CALL	  
                        System.out.println("HANGUPFAIL");
                    break;             
                    case PING:
                        System.out.println("PING from server...");
                        uti.writelog("PING from server\t"+agentObject.getAgentId());
                    break;                        
                    default: 
                        System.out.println("Default value...");
                    break;
                    }
                }
            }  
            catch(SocketException e){
                synchronized(synObject){     
                    if(close){
                        try {
                            System.out.println("Socket exception client: "+e);
                            uti.writelog("DISCONNECT SERVER(interupt)\t"+agentObject.getAgentId()+"\r\n");
                            mainForm.setVisible(false);
                            mainForm.dispose();
                            mainForm = null;
                            close = false;
                            closeConnect();
                            new LoginForm().setVisible(true);
                        } catch (Exception ex) {
                            System.out.println("socket exception2: "+ex);
                        }                        
                    }
                }                                                                                                       
            }
            catch (IllegalArgumentException e) {
                    e.printStackTrace();
            } 
            catch (IllegalStateException e) {
                    e.printStackTrace();
            }             
            catch (IOException e){

            }       
            catch (SQLException e){

            } 
            catch (Exception e){

            }

	}
        
        public boolean agentLogout(){
            synchronized(synObject){     
                if(close){
                    try{
                        mainForm.setVisible(false);
                        mainForm.dispose();
                        mainForm = null;
                        close = false;
                        clockWorktime.stop();                            
                        closeConnect();  
                        new LoginForm().setVisible(true);                
                    }catch(Exception e){
                        System.out.println("agentLogout: "+e);
                        return false;
                    }            
                }
            }  
            return true;
        }
        
        void printinfor(){
            System.out.println("getAgentId "+agentObject.getAgentId());
            System.out.println("getAgentName "+agentObject.getAgentName());
            System.out.println("getInterface "+agentObject.getInterface());
            System.out.println("getPass "+agentObject.getPass());
            System.out.println("getQueueId "+agentObject.getQueueId());
            System.out.println("getQueueName "+agentObject.getQueueName());
            System.out.println("getRole "+agentObject.getRole());
            System.out.println("getSesion "+agentObject.getSesion());
            System.out.println("getPenalty "+agentObject.getPenalty());            
        }                
        //send request to server - string
	public void sendtoServer(String t){
//            try{
//                if(clientSocket != null && in != null && out != null){   
//                    out.writeUTF(t);
//                    out.flush();
//                    System.out.println("send to server: "+t);
//                }else{
//                    System.out.println("socket is close: "+t);
//                }
//            }catch(Exception e){
//                System.out.println("Exception(sendtoServer): "+e);
//            }            
            try{
                if(clientSocket != null){   
                    outtoServer.println(t);
                    outtoServer.flush();
                    System.out.println("send to server: "+t);
                }else{
                    System.out.println("socket is close: "+t);
                }
            }catch(Exception e){
                System.out.println("Exception(sendtoServer): "+e);
            }            
	}        
        //close Socket & Thread for client
	public void closeConnect(){
//            try{
//                System.out.println("start close session");
//                if(clientSocket != null && in != null && out!= null){
//                    running = false;
//                    in.close();
//                    out.close();
//                    clientSocket.close(); 
//                    System.out.println("close socket");                    
//                }                  
//                if(mainThread !=  null){
//                    mainThread.interrupt();
//                    System.out.println("finish interrupt mainThread");                
//                }
//                System.out.println("finish close session"); 
//            }catch(Exception e){
//                System.out.println("closeConnect Exception: "+e); 
//            } 
            
            try{
                System.out.println("start close session");
                running = false;
                if(clientSocket != null){                    
                    clientSocket.close(); 
                    System.out.println("close socket");                    
                }else
                    System.out.println("close null");                    
                if(mainThread !=  null){
                    mainThread.interrupt();
                    System.out.println("close thread");                
                }else
                    System.out.println("thread null");                    
                System.out.println("finish close session"); 
                keepAlive.interrupt();
                if (keep_alive != null) {
                    if (keep_alive.isAlive()) {
                        keep_alive.interrupt();
                    }
                }                
            }catch(Exception e){
                System.out.println("closeConnect Exception: "+e); 
            } 
	}                        
	public static ArrayList<String> getList(String cmd){
            ArrayList<String> list =  new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens())
        	list.add(st.nextToken());            
            return list;
	}	
        
	public enum CODE{
            LOGINSUCC, LOGINFAIL, LOGOUTSUCC, LOGOUTFAIL,		
            PAUSESUCC, PAUSEFAIL,
            HOLDSUCC, HOLDFAIL,
            TRANSSUCC, TRANSFAIL,
            UNPAUSESUCC, UNPAUSEFAIL,
            HANGUPSUCCESS,HANGUPFAIL,
            AVAIL, BUSY, READY, RESULT, UP,HANGUP,
            CHANGEPWD,CHANGEPWDFAIL,
            RINGING,RINGNOANWSER,CONNECTED, COMPLETED,HANGUPABANDON,
            DIALOUT,DIALOUTFAIL,CONNECTEDDIALOUT,HANGUPDIALOUT,
            PING,
	}
}

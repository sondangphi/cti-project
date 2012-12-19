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
import org.asteriskjava.manager.TimeoutException;

public class Agent implements Runnable{

	/**
	 * @param args
	 */
	private boolean running = true;
	private Thread mainThread;
	private Socket clientSocket;
        private AgentObject agentObject;
        private LoginForm loginform;        
        private MainForm mainForm;
        
        private String filename = "infor.properties";                         		
        private String Mysql_server = "172.168.10.208";      
        private String Mysql_dbname = "ast_callcenter";
	private String Mysql_user = "callcenter";
	private String Mysql_pwd  = "callcenter";   
        private ConnectDatabase con;
        private Utility uti = new Utility();
        private boolean close = true;        
        private TimerClock worktime;
        private TimerClock clock = null;
        private TimerClock clockdialout = null;
        private BufferedReader infromServer;
        private PrintWriter outtoServer;        
        private String com;  
        private boolean dialout = false;
        
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
                String command = "";
                CODE code;
                Mysql_dbname = uti.readInfor(filename, "MySql_database");
                Mysql_server = uti.readInfor(filename, "MySql_server");
                Mysql_user = uti.readInfor(filename, "MySql_user");
                Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
                outtoServer = new PrintWriter(clientSocket.getOutputStream());
                infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));              
                sendtoServer(com);
                while(running){                    
                    command = infromServer.readLine();
                    System.out.println("***listen from server***");
                    System.out.println("***receive from server: "+command);
                    ArrayList<String> cmdList = getList(command);							
                    code = CODE.valueOf(cmdList.get(0).toUpperCase());
                    switch(code){
                    case LOGINSUCC: //result LOGIN SUCCESS
                        System.out.println("LOGIN SUCCESS");
                        agentObject.setAgentName(cmdList.get(1));
                        agentObject.setSession(cmdList.get(2));
                        mainForm = new MainForm(this, agentObject);
                        mainForm.setVisible(true);
                        loginform.setVisible(false);
                        worktime = new TimerClock(mainForm, false);
                        worktime.start();
                    break;
                    case LOGINFAIL: //result LOGIN FAIL                                                     
                        try {
                            System.out.println("LOGIN FAIL");
                            loginform.lb_status.setText(cmdList.get(1));
                            closeConnect();
                        } catch (Throwable ex) {
                            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    break;
                    case LOGOUTSUCC: //result LOGOUT SUCCESS              
                        try {
                            mainForm.setVisible(false);
                            mainForm.dispose(); 
                            close = false;
                            loginform.setVisible(true);
                            loginform.lb_status.setText("");
                            closeConnect();  
                            System.out.println("LOGOUT SUCCESS");
                        } catch (Exception ex) {
                            System.out.println("LOGOUTSUCC " +ex);
                        }
                        
                    break;
                    case LOGOUTFAIL: //result LOGOUT FAIL
                        System.out.println("LOGOUT FAIL");
                    break;	            	
                    case PAUSESUCC: //result PAUSE
                        worktime.pause();
                        System.out.println("PAUSESUCC");
                    break;
                    case PAUSEFAIL: //result PAUSE
                        System.out.println("PAUSEFAIL");
                    break;
                    case UNPAUSESUCC: //result UNPAUSESUCC
                        worktime.resume();
                        System.out.println("UNPAUSESUCC");
                    break;
                    case UNPAUSEFAIL: //result UNPAUSE
                        System.out.println("UNPAUSEFAIL");
                    break;
                    case TRANSSUCC: //result TRANSFER	            			
                    break;
                    case TRANSFAIL: //result TRANSFER	            			
                    break;
                    case HOLDSUCC: //result HOLD
                    break;
                    case HOLDFAIL: //result HOLD
                    break;
                    case RESULT: //result ?
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
                            dialout = false;
                        }catch(Exception e){
                        }
                    break;
                    case DIALOUT: //result 	
                        System.out.println("DIALOUT");                        
                        mainForm.lb_status.setText("Dialing...");
                        dialout = true;
                    break;
                    case BUSY: 
                    break;
                    case READY: 
                    break;
                    case HANGUP: 
                        if(dialout){
                            dialout = false;
                            mainForm.lb_status.setText("Ready");
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.setAllEnable(true); 
                            if(clockdialout != null){
                                clockdialout.stop();
                                clockdialout = null;
                            }
                        }else{
                            mainForm.lb_status.setText("Ready");
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.setAllEnable(true); 
                            if(clock != null){
                                clock.stop(); 
                                clock = null;
                                System.out.println("finish clock");
                            }
                        }          
                    break;
                    case UP: //EVENT ANSWER CALL	  
                        if(dialout){
                            clockdialout = new TimerClock(mainForm, true);
                            clockdialout.start();
                            System.out.println("Dialout");
                            mainForm.lb_status.setText("Busy");
                            System.out.println("start clock");
                        }else{
                            System.out.println("ANSWER");
                            mainForm.lb_status.setText("Busy");
                            System.out.println("start clock");
                            clock = new TimerClock(mainForm, true);
                            clock.start();
                        }
                    break;
                    default: 
                    break;
                    }
                }
            }  
            catch(SocketException e){
                if(close){
                    try {
                        System.out.println("Socket exception client: "+e);
                        System.out.println("logout & new login form");
                        mainForm.setVisible(false);
                        loginform.setVisible(true);
                        loginform.lb_status.setText("");
                        closeConnect();
                    } catch (Exception ex) {
                        Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                                                                                                        
            }
            catch (IllegalArgumentException e) {
                    e.printStackTrace();
            } catch (IllegalStateException e) {
                    e.printStackTrace();
            }             
            catch (IOException e){

            }       
            catch (SQLException e){

            } 
            catch (Exception e){

            }

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
	public void sendtoServer(String t) throws IOException{
            try{
                if(clientSocket != null && outtoServer != null && infromServer != null){   
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
	public void closeConnect()throws Exception{
            try{
                System.out.println("start close session");
                if(clientSocket != null && infromServer != null && outtoServer!= null){
                    running = false;
                    infromServer.close();
                    outtoServer.close();
                    clientSocket.close(); 
                    System.out.println("close socket");                    
                }                      
                System.out.println("finish close session"); 
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
            DIALOUT, RINGING, AVAIL, BUSY, READY, RESULT, UP,HANGUP
	}
}

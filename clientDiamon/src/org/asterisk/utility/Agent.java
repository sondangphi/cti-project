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
        private TimerClock clockWorktime;
        private TimerClock clockDialin;
        private TimerClock clockDialout;
//        private BufferedReader infromServer;
//        private PrintWriter outtoServer;        
        private String com;  
        private boolean dialout = false;
        
        DataInputStream in;
        DataOutputStream out;        
        
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
//                outtoServer = new PrintWriter(clientSocket.getOutputStream());
//                infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));     
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());                
                sendtoServer(com);
                while(running){                    
//                    command = infromServer.readLine();
                    command = in.readUTF();
                    System.out.println("***listen from server***");
                    System.out.println("***receive from server: "+command);
                    if(command == null){
                        System.out.println("null value from server");
                        try {
                            close = false;
                            System.out.println("Server interupt! Please wait...");
                            mainForm.setVisible(false);
                            mainForm.dispose();                             
                            new LoginForm().setVisible(true);
                            closeConnect();  
                            break;                            
                        } catch (Exception ex) {
                            System.out.println("Server is close! Please wait... " +ex);
                        } 
                    }                    
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
                            clockWorktime.stop();
                            new LoginForm().setVisible(true);
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
                        if(clockWorktime != null)
                            clockWorktime.pause();
                        mainForm.btn_logout.setEnabled(false);
                        mainForm.setAllEnable(false);
                        mainForm.lb_status.setText("Not Ready");
                        mainForm.btn_pause.setSelected(true);
                        System.out.println("PAUSESUCC");
                    break;
                    case PAUSEFAIL: //result PAUSE
                        System.out.println("PAUSEFAIL");
                    break;
                    case UNPAUSESUCC: //result UNPAUSESUCC
                        clockWorktime.resume();
                        mainForm.btn_logout.setEnabled(true);
                        mainForm.setAllEnable(true);
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setSelected(false);                        
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
                    case CHANGEPWD: //CHANGEPWD
                        mainForm.chanpwdform.showDialog("Change Password Success");                        
                        agentObject.setPass(cmdList.get(1));
                        System.out.println("change pass success");
                    break;                        
                    case CHANGEPWDFAIL: //CHANGEPWD
                        mainForm.chanpwdform.showDialog("Change Password Fail");                        
                        System.out.println("change pass fail");
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
                        }catch(Exception e){
                            System.out.println("RINGING: "+e);
                        }
                    break;
                    case CONNECTED://connected incoming call
                        System.out.println("CONNECTED incoming call");
                        mainForm.lb_status.setText("Busy"); 
                        clockDialin = new TimerClock(mainForm, true);
                        clockDialin.start();
                    break;
                    case COMPLETED://connected incoming call
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true); 
                        if(clockDialin != null){
                            clockDialin.stop(); 
                        }
                    break;
                    case RINGNOANWSER: 
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true);
                    break;                        
                    case DIALOUT: //result 	
                        System.out.println("DIALOUT");    
                        dialout = true;
                        mainForm.lb_status.setText("Dialing...");
                        mainForm.lb_callduration.setText("00:00:00");
                        mainForm.lb_callernumber.setText("");
                        mainForm.btn_pause.setEnabled(false);       
                        mainForm.setAllEnable(false);                        
                    break;                        
                    case DIALOUTFAIL: 
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true);
                    break;                                
                    case CONNECTEDDIALOUT: 
                        clockDialout = new TimerClock(mainForm, true);
                        clockDialout.start();                        
                        mainForm.lb_status.setText("Busy");
                        System.out.println("Connected dialout");
                    break;
                    case HANGUPDIALOUT:                         
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true); 
                        if(clockDialout != null){
                            clockDialout.stop();
                        }
                        System.out.println("hangup dialout");
                    break;                          
                    case HANGUPABANDON: 
                        mainForm.lb_status.setText("Ready");
                        mainForm.btn_pause.setEnabled(true);
                        mainForm.setAllEnable(true);          
                    break;                        
                    case UP: //EVENT ANSWER CALL	  
                    break;                        
                    default: 
                        System.out.println("Break...");
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
                        mainForm.dispose();
                        new LoginForm().setVisible(true);
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
	public void sendtoServer(String t){
            try{
                if(clientSocket != null && in != null && out != null){   
                    out.writeUTF(t);
                    out.flush();
                    System.out.println("send to server: "+t);
                }else{
                    System.out.println("socket is close: "+t);
                }
            }catch(Exception e){
                System.out.println("Exception(sendtoServer): "+e);
            }            
//            try{
//                if(clientSocket != null && outtoServer != null && infromServer != null){   
//                    outtoServer.println(t);
//                    outtoServer.flush();
//                    System.out.println("send to server: "+t);
//                }else{
//                    System.out.println("socket is close: "+t);
//                }
//            }catch(Exception e){
//                System.out.println("Exception(sendtoServer): "+e);
//            }            
	}        
        //close Socket & Thread for client
	public void closeConnect(){
            try{
                System.out.println("start close session");
                if(clientSocket != null && in != null && out!= null){
                    running = false;
                    in.close();
                    out.close();
                    clientSocket.close(); 
                    System.out.println("close socket");                    
                }                  
                if(mainThread !=  null){
                    mainThread.interrupt();
                    System.out.println("finish interrupt mainThread");                
                }
                System.out.println("finish close session"); 
            }catch(Exception e){
                System.out.println("closeConnect Exception: "+e); 
            } 
            
//            try{
//                System.out.println("start close session");
//                if(clientSocket != null && infromServer != null && outtoServer!= null){
//                    running = false;
//                    infromServer.close();
//                    outtoServer.close();
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
            AVAIL, BUSY, READY, RESULT, UP,HANGUP,
            CHANGEPWD,CHANGEPWDFAIL,
            RINGING,RINGNOANWSER,CONNECTED, COMPLETED,HANGUPABANDON,
            DIALOUT,DIALOUTFAIL,CONNECTEDDIALOUT,HANGUPDIALOUT,
	}
}

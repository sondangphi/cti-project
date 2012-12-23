package org.asterisk.utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;
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
	private  Thread mainThread;
	private  Socket clientSocket;
        private  AgentObject agentObject;
        private LoginForm loginform;        
        private BufferedReader infromServer;
        private PrintWriter outtoServer;            
        private MainForm2 mainForm2 = null;
        private boolean close = true;
	int data;
	
	public Agent(){
		
	}
        public Agent(Socket soc){
            clientSocket = soc;
            mainThread = new Thread(this);
            mainThread.start();
	}
        public Agent(Socket soc, LoginForm login)throws Exception{
            clientSocket = soc;
            loginform = login;
            agentObject = loginform.agentObject;
            mainThread = new Thread(this);
            mainThread.start();                
	}

	public Agent(String address, int port, String cmd){
            try{
                clientSocket = new Socket(address, port);	
                mainThread = new Thread(this);
                mainThread.start();                                
            }catch(Exception ex){
            }
	}	

	@Override
	public void run() {
            // TODO Auto-generated method stub
            try{
                String command = null;
                CODE code;
                outtoServer = new PrintWriter(clientSocket.getOutputStream());
                infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                sendtoServer(loginform.cmd);
                while(running){                    
                    command = infromServer.readLine();
                    ArrayList<String> cmdList = getList(command);							
                    code = CODE.valueOf(cmdList.get(0).toUpperCase());
                    switch(code){
                    case LOGINSUCC: //result LOGIN SUCCESS
                        System.out.println("LOGIN SUCCESS");
                        agentObject.setAgentName(cmdList.get(1));
                        agentObject.setSession(cmdList.get(2));
                        mainForm2 = new MainForm2(this, agentObject);
                        mainForm2.setVisible(true);
                        loginform.setVisible(false);
                    break;
                    case LOGINFAIL: //result LOGIN FAIL                                 
                        try {
                            System.out.println("LOGIN FAIL");
                            loginform.lb_status.setText(cmdList.get(2)); 
                            closeConnect();
                            this.finalize();
                        } catch (Throwable ex) {
                            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    break;
                    case LOGOUTSUCC: //result LOGOUT SUCCESS                                        
                        try {
                            System.out.println("logout");
                            mainForm2.setVisible(false);
                            mainForm2.dispose();
                            loginform.setVisible(true);
                            loginform.lb_status.setText("");
                            close = false;
                            closeConnect();  
                            this.finalize();
                        } catch (Throwable ex) {
                            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("LOGOUT SUCCESS");
                    break;
                    case LOGOUTFAIL: //result LOGOUT FAIL
                        System.out.println("LOGOUT FAIL");
                    break;	            	
                    case PAUSESUCC: //result PAUSE
                    break;
                    case PAUSEFAIL: //result PAUSE
                    break;
                    case UNPAUSESUCC: //result UNPAUSE
                    break;
                    case UNPAUSEFAIL: //result UNPAUSE
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
                        System.out.println("RINGING");
                        String callerNum = cmdList.get(1);
                        mainForm2.lb_callerid.setText(callerNum);
                        mainForm2.lb_status.setText("Ringing...");
                        mainForm2.btn_logout.setEnabled(false);
                        mainForm2.btn_pause.setEnabled(false);
                        mainForm2.btn_dial.setEnabled(false);
                        mainForm2.MenuItem_logout.setEnabled(false);
                        mainForm2.MenuItem_exit.setEnabled(false);
                    break;
                    case DIALOUT: //result 	
                        System.out.println("DIALOUT");                        
                        mainForm2.lb_status.setText("Dial Out...");
                    break;
                    case BUSY: 
                    break;
                    case READY: 
                    break;
                    case HANGUP: 
                        System.out.println("HANGUP");
                        mainForm2.lb_status.setText("Ready");
                        mainForm2.lb_callerid.setText("");
                        mainForm2.btn_logout.setEnabled(true);
                        mainForm2.btn_pause.setEnabled(true);
                        mainForm2.MenuItem_logout.setEnabled(true);
                        mainForm2.MenuItem_exit.setEnabled(true);
                        mainForm2.btn_dial.setEnabled(true);
                    break;
                    case UP: //EVENT ANSWER CALL	     
                        System.out.println("ANSWER");
                        mainForm2.lb_status.setText("Connected");
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
                        mainForm2.setVisible(false);
                        mainForm2.dispose();
                        loginform.setVisible(true);
                        loginform.lb_status.setText(""); 
                        closeConnect();  
                    } catch (Throwable ex) {
                        Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Interupt connection by Server");                     
                }               
            }
            catch (IllegalArgumentException e) {
                    e.printStackTrace();
            } catch (IllegalStateException e) {
                    e.printStackTrace();
            }             
            catch (IOException e){

            }            

	}
        //send request to server - string
	public void sendtoServer(String t) throws IOException{
            if(clientSocket != null && outtoServer != null){
                outtoServer.println(t);
                outtoServer.flush();
            }
	}
	public static ArrayList<String> getList(String cmd){
            ArrayList<String> list =  new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens())
        	list.add(st.nextToken());            
            return list;
	}	
        //close Socket & Thread for client
	public void closeConnect()throws Exception{
            System.out.println("start close session");
            if(clientSocket != null && outtoServer != null && infromServer != null){
                running = false;
                outtoServer.close();
                infromServer.close();
                clientSocket.close();                
                System.out.println("close socket");
            }             
            System.out.println("finish close session");            
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

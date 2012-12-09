package org.asterisk.utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
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
	static boolean running = true;
	static Thread mainThread;
	static Socket clientSocket;
        static AgentObject agentObject;
        private static LoginForm loginform;        
        private static BufferedReader infromServer = null;
        MainForm2 mainForm2 = null;
	int data;
        
        private static String filename = "infor.properties";                         		
        private static String Mysql_server = "127.0.0.1";      
        private static String Mysql_dbname = "cti_database";
	private static String Mysql_user = "callcenter";
	private static String Mysql_pwd  = "callcenter";   
        private static ConnectDatabase con;
        private static Utility uti;
	
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
            sendtoServer(loginform.cmd);                
	}

	public Agent(String address, int port, String cmd){
            try{
                clientSocket = new Socket(address, port);	
                mainThread = new Thread(this);
                mainThread.start();
                sendtoServer(cmd);                
            }catch(Exception ex){
            }
	}	

	@Override
	public void run() {
            // TODO Auto-generated method stub
            try{
                String command = null;
                CODE code;
                uti = new Utility();
//                MainForm mainForm = null;
//                MainForm2 mainForm2 = null;
                while(clientSocket.isConnected()){
                    infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    command = infromServer.readLine();
                    ArrayList<String> cmdList = getList(command);							
                    code = CODE.valueOf(cmdList.get(0).toUpperCase());
                    switch(code){
                    case LOGINSUCC: //result LOGIN SUCCESS
                        System.out.println("LOGIN SUCCESS");
                        agentObject.setAgentName(cmdList.get(1));
                        mainForm2 = new MainForm2(this, agentObject);
                        mainForm2.setVisible(true);
                        loginform.setVisible(false);
                        loginform.dispose();
                        loginform = null;
                    break;
                    case LOGINFAIL: //result LOGIN FAIL
                        System.out.println("LOGIN FAIL");
                        loginform.lb_status.setText("Login Fail! Please check information again.");
//                        connected = false;                                           
                        try {
                            closeConnect();
                            this.finalize();
                        } catch (Throwable ex) {
                            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    break;
                    case LOGOUTSUCC: //result LOGOUT SUCCESS
                        System.out.println("logout");
                        new LoginForm().setVisible(true);
                        mainForm2.setVisible(false);
                        mainForm2.dispose();                                        
                        try {
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
                        try{
                            System.out.println("RINGING");
                            String callerNum = cmdList.get(1);
                            Mysql_dbname = uti.readInfor(filename, "MySql_database");
                            Mysql_server = uti.readInfor(filename, "MySql_server");
                            Mysql_user = uti.readInfor(filename, "MySql_user");
                            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");  
                            //open connect to database
                            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
                            if(con.isConnect()){
                                String sql = "SELECT * FROM customer WHERE phone ='"+callerNum+"'";
                                ResultSet rs = con.executeQuery(sql);
                                if(rs.next()){
                                    //get information of already custom and fill in data
                                }else{
                                    //new customer information, insert into database
                                }
                            }
                            //close connect
                            con.closeConnect();
                            mainForm2.lb_callerid.setText(callerNum);
                            mainForm2.lb_status.setText("Ringing...");
                            mainForm2.btn_logout.setEnabled(false);
                            mainForm2.btn_pause.setEnabled(false);
                            mainForm2.btn_dial.setEnabled(false);
                            mainForm2.MenuItem_logout.setEnabled(false);
                            mainForm2.MenuItem_exit.setEnabled(false);                            
                        }catch(Exception e){
                        }
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
                System.out.println("Socket exception client: "+e);
                System.out.println("logout");
//                new LoginForm().setVisible(true);   
//                mainForm2.setVisible(false);
//                mainForm2.dispose();                                                     
//                try {
//                    closeConnect();  
//                    this.finalize();
//                } catch (Throwable ex) {
//                    Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                System.out.println("Interupt connection by Server");                
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
	public static void sendtoServer(String t) throws IOException{
            PrintWriter outtoServer;
            outtoServer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outtoServer.println(t);
            outtoServer.flush();
	}
	public static ArrayList<String> getList(String cmd){
            ArrayList<String> list =  new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens())
        	list.add(st.nextToken());            
            return list;
	}	
        //close Socket & Thread for client
	public static void closeConnect()throws Exception{
            System.out.println("start close session");
            if(mainThread!=null){
                mainThread.interrupt();
                mainThread = null;
                System.out.println("close thread");
            }
            if(!clientSocket.isClosed()){
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                clientSocket.close();                
                System.out.println("close socket");
            }
            infromServer = null;            
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

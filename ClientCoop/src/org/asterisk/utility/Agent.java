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
import org.asterisk.model.CustomerObject;
import org.asterisk.model.TableModel;
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
        private String Mysql_server = "172.168.10.202";      
        private String Mysql_dbname = "ast_callcenter";
	private String Mysql_user = "callcenter";
	private String Mysql_pwd  = "callcenter";   
        private ConnectDatabase con;
        private Utility uti;
        
        public CustomerObject customer;
        
        private boolean close = true;
        private TimerClock clockDialin;
        private TimerClock clockDialout;
        private TimerClock worktime;
        private boolean campaign = false;
        private boolean dialout = false;
        private BufferedReader infromServer;
        private PrintWriter outtoServer;        
        private String com;          
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
                uti = new Utility();
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
                    if(command == null){
                        System.out.println("null value from server");
                        try {
                            close = false;
                            System.out.println("Server is close! Please wait...");
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
                        loginform.setVisible(false);
                        loginform.dispose();
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
                    case CHANGEPWD: //result
                        mainForm.chanpwdform.showDialog();
                        System.out.println("password have changed: "+cmdList.get(1));
                        agentObject.setPass(cmdList.get(1));                        
                    break;
                    case RINGING: //EVENT RINGING
                        try{
                            dialout = false;
                            String callerNum = cmdList.get(1);
                            mainForm.lb_status.setText("Ringing...");
                            mainForm.lb_callduration.setText("00:00:00");
                            mainForm.btn_pause.setEnabled(false);       
                            mainForm.btn_feedback.setEnabled(true);
                            mainForm.setAllEnable(false);
                            //open connect to database
                            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
                            if(con.isConnect()){
                                System.out.println("get information of customer"); 
                                String sql = "SELECT * FROM customer WHERE mobilephone ='"+callerNum+"'";
                                ResultSet rs = con.executeQuery(sql);
                                if(rs.next()){
                                    //get information of already custom and fill in data   
                                    System.out.println("callerNum: "+callerNum);
                                    System.out.println("already information");
                                    customer = new CustomerObject(callerNum);
                                    customer.setName(rs.getString("fullname"));                                    
                                    customer.setAddress(rs.getString("address"));
                                    customer.setEmail(rs.getString("email"));
                                    customer.setGender(rs.getString("gender"));
                                    customer.setPhone1(rs.getString("homephone1"));
                                    customer.setId(String.valueOf(rs.getObject("id")));                                                                         
                                    mainForm.txt_add.setText(customer.getAddress());
                                    mainForm.txt_name.setText(customer.getName());
                                    mainForm.txt_email.setText(customer.getEmail());
                                    mainForm.txt_mobile.setText(customer.getPhone());
                                    mainForm.txt_phone1.setText(customer.getPhone1());
                                    String gender = customer.getGender();
                                    if(gender.equalsIgnoreCase("1"))
                                        mainForm.cb_gender.setSelectedIndex(0);
                                    else
                                        mainForm.cb_gender.setSelectedIndex(1);
                                    mainForm.btn_new.setEnabled(false);
                                    mainForm.btn_update.setEnabled(true);
                                    mainForm.btn_feedback.setEnabled(true);
                                    sql = "SELECT * FROM feedback_history WHERE mobile = '"+callerNum+"'";
                                    rs = con.executeQuery(sql);
                                    if(rs != null)
                                        displayHistory(rs);                                                                                                                        
                                }else{
                                    //new customer information, insert into database
                                    System.out.println("callerNum: "+callerNum);
                                    System.out.println("not ready information");  
                                    mainForm.txt_mobile.setText(callerNum);
                                    mainForm.btn_new.setEnabled(true);
                                    mainForm.btn_update.setEnabled(false);
                                    mainForm.btn_feedback.setEnabled(true);
                                    mainForm.txt_add.setText("");                                    
                                    mainForm.txt_name.setText("");
                                    mainForm.txt_email.setText("");                                    
                                    mainForm.txt_phone1.setText("");  
                                    String colname[] = {"No","Date","Full Name","Phone", "Agent","Type","Content","Result"};
                                    int count = colname.length;
                                    Vector col = new Vector(count);
                                    Vector row = new  Vector();
                                    for (int i = 0; i <count; i++) 
                                        col.addElement(colname[i].toString());
                                    mainForm.table_report.setModel(new TableModel(col, row));
                                }
                            }
                            //close connect database
                            con.closeConnect();                                                       
                        }catch(Exception e){
                            con.closeConnect();  
                        }
                    break;
                    case DIALOUT: //result 	
                        System.out.println("DIALOUT");                        
                        mainForm.lb_status.setText("Dialing...");
                        mainForm.lb_callduration.setText("00:00:00");
                        dialout = true;
                    break;
                    case BUSY: 
                    break;
                    case READY: 
                    break;
                    case HANGUP: 
                        if(dialout){
                            mainForm.lb_status.setText("Ready");
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.setAllEnable(true); 
                            if(clockDialout != null){
                                clockDialout.stop();
                            }
                        }else{
                            mainForm.lb_status.setText("Ready");
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.setAllEnable(true);      
                            if(clockDialin != null){
                                clockDialin.stop();                            
                            }                                                      
                        }                                           
                    break;
                    case UP: //EVENT ANSWER CALL
                        if(dialout){
                            System.out.println("ANSWER dial out");
                            mainForm.lb_status.setText("Busy");
                            clockDialout = new TimerClock(mainForm, true);
                            clockDialout.start();
                        }else{
                            System.out.println("ANSWER dial in");
                            mainForm.lb_status.setText("Busy");
                            clockDialin = new TimerClock(mainForm, true);
                            clockDialin.start();                            
                        }
                    break;
                    default: 
                        System.out.println("default values from server\t"+command);
                    break;
                    }
                }
            }  
            catch(SocketException e){
                if(close){
                    System.out.println("Socket exception client: "+e);
                    System.out.println("logout & new login form");
                    mainForm.setVisible(false);
                    mainForm.dispose();
                    new LoginForm().setVisible(true);
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
        
        //xem lai doan nay, bi outofmemmory
        public void displayHistory(ResultSet rs)throws Exception{
            String colname[] = {"No","Date","Full Name","Phone", "Agent","Type","Content","Result"};
//            mainForm.table_report = null;
            int count = colname.length;
            Vector col = new Vector(count);
            Vector row = new  Vector();
            String [] temp = null;
            ArrayList<String[]> data = new ArrayList<String[]>();
            int t = 1;
            while(rs.next()){
                int j = 0;                                                                                    
                temp  = new String[count];
                temp[ j++ ] = String.valueOf(t++);
                temp[ j++ ] = String.valueOf(rs.getObject("datetime"));//rs.getString("datetime");
                temp[ j++ ] = String.valueOf(rs.getObject("name"));//rs.getString("name");
                temp[ j++ ] = String.valueOf(rs.getObject("mobile"));//rs.getString("mobile");
                temp[ j++ ] = String.valueOf(rs.getObject("agentid"));//rs.getString("agentid");
                temp[ j++ ] = String.valueOf(rs.getObject("type"));//rs.getString("type");
                temp[ j++ ] = String.valueOf(rs.getObject("content"));//rs.getString("content");
                temp[ j++ ] = String.valueOf(rs.getObject("results"));//rs.getString("result");
                data.add(temp);
            }
            for(int i = 0;i<data.size();i++){                
                Vector dataRow = new Vector(count);
                temp = new String[count];
                for(int j=0;j<count;j++){
                    temp = data.get(i);
                    if(temp[j] != null)
                        dataRow.addElement(temp[j]);
                    else
                        dataRow.addElement("");
                }
                row.addElement(dataRow);
            }
            for (int i = 0; i <count; i++) 
                col.addElement(colname[i].toString());
            mainForm.table_report.setModel(new TableModel(col, row));                         
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
            DIALOUT, RINGING, AVAIL, BUSY, READY, RESULT, UP,HANGUP,
            CHANGEPWD,
	}
}
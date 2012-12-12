package org.asterisk.utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	static boolean running = true;
	static Thread mainThread;
	static Socket clientSocket;
        static AgentObject agentObject;
        private static LoginForm loginform;        
        private static BufferedReader infromServer = null;
        MainForm2 mainForm2 = null;
	int data;
        
        private static String filename = "infor.properties";                         		
        private static String Mysql_server = "172.168.10.208";      
        private static String Mysql_dbname = "cti_database";
	private static String Mysql_user = "cti";
	private static String Mysql_pwd  = "123456";   
        private static ConnectDatabase con;
        private static Utility uti;
        
        public CustomerObject customer;
	
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
                Mysql_dbname = uti.readInfor(filename, "MySql_database");
                Mysql_server = uti.readInfor(filename, "MySql_server");
                Mysql_user = uti.readInfor(filename, "MySql_user");
                Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
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
                            mainForm2.lb_callerid.setText(callerNum);
                            mainForm2.lb_status.setText("Ringing...");
                            mainForm2.btn_logout.setEnabled(false);
                            mainForm2.btn_pause.setEnabled(false);
                            mainForm2.btn_dial.setEnabled(false);
                            mainForm2.MenuItem_logout.setEnabled(false);
                            mainForm2.MenuItem_exit.setEnabled(false);        
                            mainForm2.btn_feedback.setEnabled(true);
                            //open connect to database
                            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
                            if(con.isConnect()){
                                String sql = "SELECT * FROM customer WHERE mobilephone ='"+callerNum+"'";
                                ResultSet rs = con.executeQuery(sql);
                                if(rs.next()){
                                    //get information of already custom and fill in data   
                                    System.out.println("callerNum: "+callerNum);
                                    customer = new CustomerObject(callerNum);
                                    customer.setName(rs.getString("fullname"));                                    
                                    customer.setAddress(rs.getString("address"));
                                    customer.setEmail(rs.getString("email"));
                                    customer.setGender(rs.getString("gender"));
                                    customer.setPhone1(rs.getString("homephone1"));
                                    customer.setId(rs.getString("id"));
                                    String gender = customer.getGender();
                                    System.out.println("already information"); 
                                    mainForm2.txt_add.setText(customer.getAddress());
                                    mainForm2.txt_name.setText(customer.getName());
                                    mainForm2.txt_email.setText(customer.getEmail());
                                    mainForm2.txt_mobile.setText(customer.getMobile());
                                    mainForm2.txt_phone1.setText(customer.getPhone1());
                                    if(gender.equalsIgnoreCase("1"))
                                        mainForm2.cb_gioitinh.setSelectedIndex(0);
                                    else
                                        mainForm2.cb_gioitinh.setSelectedIndex(1);
                                    mainForm2.btn_new.setEnabled(false);
                                    sql = "SELECT * FROM feedback WHERE mobile ='"+callerNum+"'";
                                    rs = con.executeQuery(sql);
                                    if(rs != null){
                                        addTable(rs);
                                    }                                                                                    
                                }else{
                                    //new customer information, insert into database
                                    System.out.println("callerNum: "+callerNum);
                                    System.out.println("not ready information");  
                                    mainForm2.txt_mobile.setText(callerNum);
                                    mainForm2.btn_new.setEnabled(true);
                                    mainForm2.txt_add.setText("");
                                    mainForm2.txt_email.setText("");
                                    mainForm2.txt_name.setText("");
                                    mainForm2.txt_email.setText("");                                    
                                    mainForm2.txt_phone1.setText("");  
//                                    mainForm2.table_report.removeAll();
//                                    mainForm2.table_report.setModel(null);
                                    String colname[] = {"No","Date","Full Name","Phone", "Agent","Type","Content","Result"};
                                    int count = colname.length;
                                    Vector col = new Vector(count);
                                    Vector row = new  Vector();
                                    for (int i = 0; i <count; i++) 
                                        col.addElement(colname[i].toString());
                                    mainForm2.table_report.setModel(new TableModel(col, row));
                                }
                            }
                            //close connect
                            con.closeConnect();                                                       
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
                System.out.println("Interupt connection by Server");                
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
        
        public void addTable(ResultSet rs)throws Exception{
            String colname[] = {"No","Date","Full Name","Phone", "Agent","Type","Content","Result"};
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
            mainForm2.table_report.setModel(new TableModel(col, row));                         
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

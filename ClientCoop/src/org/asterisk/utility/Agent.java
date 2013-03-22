package org.asterisk.utility;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.TableColumn;

import nano.data.securities.SwapCode;
import org.asterisk.main.*;
import org.asterisk.model.AgentObject;
import org.asterisk.model.CustomerObject;
import org.asterisk.model.TableModel;

public class Agent implements Runnable{
	public boolean running = true;
	private Thread mainThread;
	public Socket clientSocket;
        private AgentObject agentObject;
        private LoginForm loginform;        
        public static MainForm mainForm;
        
        private String filename = "infor.properties";                         		
        private String Mysql_server = "172.168.10.202";      
        private String Mysql_dbname = "ast_callcenter";
	private String Mysql_user = "callcenter";
	private String Mysql_pwd  = "callcenter";   
        private ConnectDatabase con;
        private Utility uti;        
        public CustomerObject customer;        
        private boolean closed = true;
        private TimerClock clockDialin;
        private TimerClock clockDialout;
        private TimerClock worktime;
        private boolean campaign = false;
        private boolean dialout = false;
        private String com;          
        private KeepAlive keepAlive;
        private Thread keep_alive; 
        private Object synObject = new Object();        
        private String fromServer;
        private DataInputStream in;
        private DataOutputStream out;
        private CODE code;
        
        private String command;
        private SwapCode secure;
        private String KEY_STRING = "123";
        
        private final Object object=new Object();
        
	public Agent(){
		
	}
        //new thread for client
        public Agent(Socket s, LoginForm login, AgentObject agentO, String command){
            try{
                clientSocket = s;
                loginform = login;
                agentObject = agentO;
                com = command;
                secure = new SwapCode(KEY_STRING.getBytes("UTF-8"));
                mainThread = new Thread(this,"agent");
                mainThread.start();                
            }catch(Exception e){
                System.out.println("Exception agent thread: "+e);
            }
	}      	
	@Override
	public void run() {            
            try{                
                uti = new Utility();
                Mysql_dbname = uti.readInfor(filename, "MySql_database");
                Mysql_server = uti.readInfor(filename, "MySql_server");
                Mysql_user = uti.readInfor(filename, "MySql_user");
                Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());  
//                KEY_STRING = "~!@#$%^&*()_+`1234567890-=";                 
                sendtoServer(com);
                System.err.println("___START RECEIVE DATA___");
            }catch (Exception e){}                
            
            while( running ){
                try{
                    if((fromServer = in.readUTF()) == null)
                        break;
                    fromServer = new String (secure.decode(fromServer.getBytes("ISO-8859-1")), "UTF-8");                        
                    System.out.println("Receive from server: "+fromServer);
                    final ArrayList<String> cmdList = getList(fromServer);							
                    code = CODE.valueOf(cmdList.get(0).toUpperCase());
                    switch(code){
                        case LOGINSUCC: //result LOGIN SUCCESS
                            System.out.println("LOGIN SUCCESS");
                            agentObject.setAgentName(cmdList.get(1));
                            agentObject.setSession(cmdList.get(2));
                            mainForm = new MainForm(Agent.this, agentObject);
                            loginform.txt_img_wait.setText("Login success");
                            loginform.txt_img_wait.setIcon(null);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
//                                        Thread.sleep(2000);
                                        mainForm.setVisible(true);                              
                                        loginform.setVisible(false);
                                        loginform.dispose();
                                        worktime = new TimerClock(mainForm, false);
                                        worktime.start();
                                        keepAlive = new KeepAlive(Agent.this);
                                        sendtoServer("222");
                                    } catch (Exception ex) {}
                                }
                            },"login succ").start();
                        break;
                        case LOGINFAIL: //result LOGIN FAIL                                                     
                            System.out.println("LOGIN FAIL");
                            loginform.lb_status.setText(cmdList.get(1));
                            loginform.tx_agent.setEnabled(true);
                            loginform.btn_login.setEnabled(true);
                            loginform.btn_clear.setEnabled(true);
                            loginform.tx_iface.setEnabled(true);
                            loginform.cb_queue.setEnabled(true);
                            loginform.lb_option.setEnabled(true);
                            loginform.txt_img_wait.setVisible(false);   
                            loginform.pwd.setEnabled(true);
                            synchronized(synObject){
                                if(closed == true){
                                    agentLogout();
                                    closeConnect();
                                }
                            }
                        break;
                        case LOGOUTSUCC: //result LOGOUT SUCCESS
                            synchronized(synObject){
                                agentLogout();
                                new LoginForm().setVisible(true);
                                closeConnect();                                                                                           
                            }
                        break;
                        case LOGOUTFAIL: //result LOGOUT FAIL
                            System.out.println("LOGOUT FAIL");
                        break;	            	
                        case PAUSESUCC: //result PAUSE
                            mainForm.btn_logout.setEnabled(false);
                            mainForm.setAllEnable(false);      
                            mainForm.lb_status.setText("Not Ready");
                            mainForm.setPauseIcon(true);
                            mainForm.btn_pause.setSelected(true);  
                            mainForm.lb_status.setForeground(Color.red);
                            worktime.pause();
                            System.out.println("PAUSESUCC");
                        break;
                        case PAUSEFAIL: //result PAUSE
                            System.out.println("PAUSEFAIL");
                        break;
                        case UNPAUSESUCC: //result UNPAUSESUCC
                            worktime.resume();
                            mainForm.btn_logout.setEnabled(true);
                            mainForm.setAllEnable(true);      
                            mainForm.setPauseIcon(false);
                            mainForm.lb_status.setText("Ready");
                            mainForm.btn_pause.setSelected(false);
                            mainForm.btn_hangup.setEnabled(false);
                            mainForm.lb_status.setForeground(Color.green);
                            System.out.println("UNPAUSESUCC");
                        break;
                        case UNPAUSEFAIL: //result UNPAUSE
                            System.out.println("UNPAUSEFAIL");
                            mainForm.btn_pause.setSelected(true);
                        break;
                        case TRANSFERSUCCESS: //result transfer success   
                            System.out.println("TRANSFERSUCCESS");                            
                        break;
                        case TRANSFERFAIL: //result TRANSFER fail	   
                            System.out.println("TRANSFERFAIL");  
                        break;
                        case HOLDSUCC: //result HOLD
                        break;
                        case HOLDFAIL: //result HOLD
                        break;                        
                        case CHANGEPWD: //CHANGEPWD
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mainForm.chanpwdform.showDialog("Change Password Success");                        
                                }
                            },"changepwd").start();
                            agentObject.setPass(cmdList.get(1));
                            System.out.println("change pass success");
                        break;                        
                        case CHANGEPWDFAIL: //CHANGEPWD
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mainForm.chanpwdform.showDialog("Change Password Fail");  
                                }
                            },"CHANGEPWDFAIL").start();                                                                      
                            System.out.println("change pass fail");
                        break;                        
                        case RINGING: //EVENT RINGING
                            try{
                                dialout = false;
                                String callerNum = cmdList.get(1);
                                mainForm.clearInfo();
                                mainForm.setAllEnable(false);
                                mainForm.lb_status.setText("Ringing...");
                                mainForm.lb_callduration.setText("00:00:00");
                                mainForm.btn_pause.setEnabled(false);       
                                mainForm.btn_feedback.setEnabled(false);
//                                    mainForm.btn_update.setEnabled(false);
                                mainForm.btn_hangup.setEnabled(true);     
//                                mainForm.btn_transfer.setEnabled(false);
                                mainForm.btnViewFB.setEnabled(false);
                                //open connect to database
                                con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
                                if(con.isConnect()){
                                    System.out.println("get information of customer"); 
                                    String sql = "SELECT c.*,t.* FROM customer c LEFT JOIN cus_type t "
                                            + "on c.type=t.id_type  WHERE phone1 ='"+callerNum+"'";
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

                                        customer.setType(rs.getString("desc"));
                                        customer.setId(String.valueOf(rs.getObject("id")));   
                                        customer.setBirth(String.valueOf(rs.getObject("birthday"))); 
                                        customer.setReg(String.valueOf(rs.getObject("registration"))); 
                                        mainForm.txt_add.setText(customer.getAddress());
                                        mainForm.txt_name.setText(customer.getName());
                                        mainForm.txt_email.setText(customer.getEmail());
                                        mainForm.txt_mobile.setText(customer.getPhone());
                                        mainForm.txt_makh.setText(customer.getId());
                                        mainForm.txt_birthday.setText(customer.getBirth());
                                        mainForm.txt_reg.setText(customer.getReg());

                                        String gender="";
                                        if(Integer.parseInt(customer.getGender())==0)
                                        {
                                            gender="n?";
                                        }
                                        else
                                        {
                                            gender="nam";
                                        }
                                        mainForm.txt_gender.setText(gender);
                                        mainForm.txt_type.setText(customer.getType());

                                        sql = "SELECT f.*,a.name as `a_name`,a.email "
                                                + "FROM feedback_history f LEFT OUTER JOIN feedback_assign a ON f.assign=a.id"
                                                + " WHERE mobile = '"+callerNum+"' "
                                                + " ORDER BY f.datetime DESC "
                                                + " LIMIT 0,10 ";
                                        rs = con.executeQuery(sql);
                                        if(rs != null)
                                        {  
                                            displayHistory(rs);  
                                        }
                                    }else{
                                        //new customer information, insert into database
                                        System.out.println("callerNum: "+callerNum);
                                        System.out.println("not ready information");  
                                        mainForm.txt_mobile.setText(callerNum);
                                        mainForm.btn_feedback.setEnabled(false);
//                                            mainForm.btn_new.setEnabled(true);
                                        mainForm.txt_add.setText("");                                    
                                        mainForm.txt_name.setText("");
                                        mainForm.txt_email.setText("");                                    
//                                            mainForm.txt_birthday.setText("");
                                        String colname[] = {"No","Date", "Agent","Type","Content","Result","Assign "};
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
                        case CONNECTED://connected incoming call
                            System.out.println("CONNECTED incoming call");
                            mainForm.lb_status.setText("Busy");
//                            mainForm.btn_transfer.setEnabled(true);
                             mainForm.btn_feedback.setEnabled(true);
//                                mainForm.btn_update.setEnabled(true);
                            clockDialin = new TimerClock(mainForm, true);
                            clockDialin.start();
                        break;
                        case COMPLETED://connected incoming call
                            mainForm.lb_status.setText("Ready");
                            mainForm.setAllEnable(true); 
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.btn_hangup.setEnabled(false);   
//                            mainForm.btn_transfer.setEnabled(false);
                            if(clockDialin != null){
                                clockDialin.stop(); 
                            }
                        break;
                        case RINGNOANWSER: //ko ai nghe 
                            mainForm.lb_status.setText("Ready");
                            mainForm.setAllEnable(true);
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.btn_hangup.setEnabled(false);    
//                            mainForm.btn_transfer.setEnabled(false);
                        break;                                                
                        case DIALOUT: //result 	//goi ra
                            System.out.println("DIALOUT");                        
                            mainForm.lb_status.setText("Dialing...");
                            mainForm.lb_callduration.setText("00:00:00");
                            mainForm.setAllEnable(false);  
                            mainForm.btn_pause.setEnabled(false);    
                            mainForm.btn_hangup.setEnabled(true);                                                      
                            dialout = true;
                        break;
                        case CONNECTEDDIALOUT: //talking outbou
                            clockDialout = new TimerClock(mainForm, true);
                            clockDialout.start();                        
                            mainForm.lb_status.setText("Busy");
                            mainForm.btn_feedback.setEnabled(true);
                            System.out.println("Connected dialout");
                        break;
                        case HANGUPDIALOUT:         //kt                
                            mainForm.lb_status.setText("Ready");
                            mainForm.setAllEnable(true); 
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.btn_hangup.setEnabled(false);   
                            mainForm.btn_feedback.setEnabled(false);
                            if(clockDialout != null){
                                clockDialout.stop();
                            }
                            System.out.println("hangup dialout");
                        break;       
                        case HANGUPABANDON: //rot cuoc  goi
                            mainForm.lb_status.setText("Ready");
                            mainForm.setAllEnable(true); 
                            mainForm.btn_pause.setEnabled(true);
                            mainForm.btn_hangup.setEnabled(false);
//                            mainForm.btn_transfer.setEnabled(false);
                        break;       
                        case HANGUPSUCCESS://
                            System.out.println("HANGUPSUCCESS");
                            mainForm.btn_hangup.setEnabled(false);
//                            mainForm.btn_transfer.setEnabled(false);
                        break;
                        case HANGUPFAIL: 
                            System.out.println("HANGUPFAIL\t");
                        break;                          
                        case PING:                                 
                            if (keep_alive != null && keep_alive.isAlive())
                                keep_alive.stop();                                
                            keep_alive = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(10000);                                   
                                        synchronized(synObject){
                                            System.out.println("Time out detect");
                                            agentLogout();
                                            new LoginForm().setVisible(true);
                                            closeConnectTimeout();                                                
                                        }
                                        keep_alive.stop();
                                    } catch (Exception ex) {}  
                                }
                            },"PING");
                            keep_alive.start();      
                            break;                           
                    case CHAT:                            
//                                System.err.println("chat1");
//                                System.err.println("chat: "+command);
//                                if (MainForm.itemchat == null || !MainForm.itemchat.isVisible()) {
//                                    MainForm.itemchat=new ListItemChat(agentObject.getAgentId(), this);
//                                    MainForm.itemchat.popup(cmdList.get(1), cmdList.get(2));
//                                }
//                                else {
//                                    MainForm.itemchat.receive(cmdList.get(1), cmdList.get(2));
//                                }     

                        new Thread(new Runnable() {
                                @Override
                                public void run() {
                                   
                                        synchronized (object){
                                            if (mainForm.tabPChat.getTabCount() ==0||!mainForm.tabPChat.isVisible()) 
                                            {
                                                mainForm.popup(cmdList.get(1), cmdList.get(2));
                                            }
                                            else {
                                                mainForm.receive(cmdList.get(1), cmdList.get(2));
                                            }
                                        }
                                   
                                }
                            },"chat").start();
                            
                            break;
                                
                           default: 
//                             System.out.println("default values from server\t"+command);
                               break;
                              
                        }

                }
                catch(Exception ex){
                    System.out.println("Exception client: "+ex);
                    if(closed == true){
                        System.out.println("SocketException: " +ex);
                        agentLogout();
                        new LoginForm().setVisible(true);
                        closeConnect();                        
                    }
                }
            }                    
            synchronized(synObject){
                System.err.println("___end while loop___");
                if(closed == true){
                    System.out.println("end while loop, read null value from buffer");
                    agentLogout();                    
                    new LoginForm().setVisible(true);
                    closeConnect();                        
                }
            }
        }        
        public void displayHistory(ResultSet rs){
            try{
            String colname[] = {"No","Date","Agent","Type","Categories","Content_type","Content","Solution","Result","Assign"};
          //  mainForm.btn_feedback.setEnabled(true);
            mainForm.table_report.getTableHeader().setReorderingAllowed(false);
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
                
                SimpleDateFormat spdt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date c_day = spdt.parse(rs.getObject("datetime").toString());

                // *** same for the format String below
                SimpleDateFormat spdt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String cr_day=spdt1.format(c_day);
                temp[ j++ ] =cr_day;
//                temp[ j++ ] = String.valueOf(rs.getObject("datetime"));//rs.getString("datetime");
                temp[ j++ ] = String.valueOf(rs.getObject("agentid"));//rs.getString("agentid");
                temp[ j++ ] = String.valueOf(rs.getObject("type"));//rs.getString("type");
                temp[ j++ ] = String.valueOf(rs.getObject("categories"));//rs.getString("categories");
                temp[ j++ ] = String.valueOf(rs.getObject("content_type"));//rs.getString("content_type");
                temp[ j++ ] = String.valueOf(rs.getObject("content"));//rs.getString("content");
                temp[ j++ ] = String.valueOf(rs.getObject("solution"));//rs.getString("solution");
                temp[ j++ ] = String.valueOf(rs.getObject("results"));//rs.getString("results");
                temp[ j++ ] = String.valueOf(rs.getString("a_name"));//rs.getString("assign");                                                                                
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

            TableColumn column = null;
                for (int k = 0;k <  mainForm.table_report.getColumnCount(); k++) 
                {
                    column =  mainForm.table_report.getColumnModel().getColumn(k);

                    if (k == 0) 
                    {
                        column.setPreferredWidth(50);
                    } 

                    else if (k==1)
                    {
                        column.setPreferredWidth(150);
                    }
                    else
                    {
                        column.setPreferredWidth(100);
                    }
                }                
            }catch(Exception ex){}
        }
        public void agentLogout(){
            try{            
                closed = false;
                running = false;
                if(mainForm != null && mainForm.isVisible()){
                    mainForm.setVisible(false);
                    mainForm.dispose();
                    mainForm = null;                    
                } 
                if(worktime != null)
                    worktime.stop();        
            }catch(Exception e){
                System.out.println("agentLogout: "+e);
            }             
        }        
        
        //send request to server - string
	public void sendtoServer(String data) {
            try{
                if(clientSocket != null && !clientSocket.isClosed()){
                    System.out.println("send to server: "+data);
                    String encryptData = new String (secure.encode(data.getBytes("UTF-8")),"ISO-8859-1");
                    out.writeUTF(encryptData);
                    out.flush();                    
                }
            }catch(Exception e){
                System.out.println("Exception(sendtoServer): "+e);
            }            
	}        
        //close Socket & Thread for client
	public void closeConnect(){
            try{
                System.out.println("start close session");                
                if(keepAlive != null && keepAlive.isAlive())
                    keepAlive.stop();
                System.out.println("close keepp alive1"); 
                if (keep_alive != null && keep_alive.isAlive()) {
                    System.out.println("close keepp alive2");
                    keep_alive.stop();                     
                }                
                if(clientSocket != null && !clientSocket.isClosed()){
                    System.out.println("close socket");
                    clientSocket.close();                                         
                }                                                        
                if(mainThread != null && mainThread.isAlive()){                    
                    System.out.println("close main thread"); 
                    System.out.println("finish close session");
                    mainThread.stop();
                }                
            }catch(Exception e){
                System.out.println("closeConnect Exception: "+e); 
            }            
	}
        
	public void closeConnectTimeout(){
            try{
                System.out.println("start close session");                
                if(keepAlive.isAlive())
                    keepAlive.stop();
                System.out.println("close keepp alive1"); 
                if (keep_alive != null && keep_alive.isAlive()) {
                    System.out.println("interrupt keepp alive2");
                    keep_alive.interrupt();                     
                }                
                if(clientSocket != null && !clientSocket.isClosed()){
                    System.out.println("close socket");
                    clientSocket.close();                                         
                }                                                        
                System.out.println("close main thread");
                if(mainThread != null && mainThread.isAlive()){                                         
                    System.out.println("finish close session");
                    mainThread.stop();
                }                
            }catch(Exception e){
                System.out.println("closeConnect Exception: "+e); 
            }            
	}        
        
	public static ArrayList<String> getList(String cmd){
            ArrayList<String> list =  new ArrayList();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens()){
        	list.add(st.nextToken());            
            }
            return list;
	}	
        
	public enum CODE{
            LOGINSUCC, LOGINFAIL, LOGOUTSUCC, LOGOUTFAIL,		
            PAUSESUCC, PAUSEFAIL,
            HOLDSUCC, HOLDFAIL,
            TRANSFERSUCCESS, TRANSFERFAIL,
            UNPAUSESUCC, UNPAUSEFAIL,
            AVAIL, BUSY, READY, RESULT, UP,HANGUP,
            CHANGEPWD,CHANGEPWDFAIL,
            RINGING,RINGNOANWSER,CONNECTED, COMPLETED,HANGUPABANDON,HANGUPFAIL,
            DIALOUT,CONNECTEDDIALOUT,HANGUPDIALOUT,HANGUPSUCCESS,
            PING,CHAT
	}
}

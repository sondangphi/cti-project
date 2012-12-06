package org.asterisk.main;

import java.io.File;
import java.io.FileInputStream;
import org.asterisk.utility.AgentListen;
import org.asterisk.utility.Managerdb;
import org.asterisk.utility.QueueListen;
import org.asterisk.utility.Utility;

public class Server{
	private static Utility uti;	
	private static int Port_agent = 22222;
	private static int Port_queue = 33333;
//        private static String hostdb = "172.168.10.208";      
        private static String Mysql_server = "127.0.0.1";      
        private static String Mysql_dbname = "cti_database";
	private static String Mysql_user = "callcenter";
	private static String Mysql_pwd  = "callcenter";
        private static String Asterisk_server = "127.0.0.1";
        private static String Asterisk_user = "manager";
        private static String Asterisk_pwd = "123456";
	private static Managerdb mdb_agent;
        private static QueueListen qlisten;  
        private static AgentListen alisten; 
        static String filename = "infor.properties";                         		
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
            // TODO Auto-generated method stub
            uti = new Utility();	
            //create configuration file if not exist
            File f = new File(filename);
            if(!f.exists())
                f.createNewFile();            
            FileInputStream fis = new FileInputStream(f);    
            if(fis.available() == 0 ){                
                uti.writeInfor(filename, "MySql_database", Mysql_dbname);
                uti.writeInfor(filename, "MySql_server", Mysql_server);
                uti.writeInfor(filename, "MySql_user", Mysql_user);
                uti.writeInfor(filename, "MySql_pwd", Mysql_pwd);
                uti.writeInfor(filename, "Port_agent", Integer.toString(Port_agent));
                uti.writeInfor(filename, "Port_queue", Integer.toString(Port_queue));                
                uti.writeInfor(filename, "Asterisk_pwd", Asterisk_pwd);
                uti.writeInfor(filename, "Asterisk_user", Asterisk_user);
                uti.writeInfor(filename, "Asterisk_server", Asterisk_server);
            }
            //Read configuration file
            System.out.println("Read configuration file!"); 
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            Port_agent = Integer.parseInt(uti.readInfor(filename, "Port_agent"));
            Port_queue = Integer.parseInt(uti.readInfor(filename, "Port_queue"));      
            //connect to Database server - MYSQL
            mdb_agent = new Managerdb(Mysql_dbname,Mysql_user,Mysql_pwd,Mysql_server);
            if(mdb_agent.isConnect()){
                uti.writeAsteriskLog("- SYSTE  - Connect to Database Successful");
                System.out.println("Connect to Database Successful");
                //start thread AgentListen
                alisten = new AgentListen( Port_agent, mdb_agent );
                //start thread QueueListen
                qlisten = new QueueListen( Port_queue, mdb_agent );                
            } else {
                uti.writeAsteriskLog("- SYSTE  - Connect to Database Fail");
                System.out.println("Connect to Database Fail");
                System.out.println("Interrup Connection.");                
            }
	}
}

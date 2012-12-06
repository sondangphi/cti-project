package org.asterisk.main;

import java.io.File;
import java.io.FileInputStream;
import org.asterisk.utility.AgentListen;
import org.asterisk.utility.Managerdb;
import org.asterisk.utility.QueueListen;
import org.asterisk.utility.Utility;

public class ctiServer{
	private static Utility uti;	
	private static int aport = 22222;
	private static int qport = 33333;
//        private static String hostdb = "172.168.10.208";      
        private static String hostdb = "10.0.8.149";      
        private static String dbname = "cti_database";
	private static String userSql = "callcenter";
	private static String pwdSql  = "callcenter";
        private static String hostAsterisk = "10.0.8.149";
        private static String userAsterisk = "manager";
        private static String pwdAsterisk = "123456";
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
            File f = new File(filename);
            if(!f.exists())
                f.createNewFile();            
            FileInputStream fis = new FileInputStream(f);    
            if(fis.available() == 0 ){                
                uti.writeInfor(filename, "MySql_database", dbname);
                uti.writeInfor(filename, "MySql_server", hostdb);
                uti.writeInfor(filename, "MySql_user", userSql);
                uti.writeInfor(filename, "MySql_pwd", pwdSql);
                uti.writeInfor(filename, "Port_agent", Integer.toString(aport));
                uti.writeInfor(filename, "Port_queue", Integer.toString(qport));
                ////////////////////////////////////////////////////////////
                uti.writeInfor(filename, "Asterisk_pwd", pwdAsterisk);
                uti.writeInfor(filename, "Asterisk_user", userAsterisk);
                uti.writeInfor(filename, "Asterisk_server", hostAsterisk);
            }
            System.out.println("Read configuration file!"); 
            dbname = uti.readInfor(filename, "MySql_database");
            hostdb = uti.readInfor(filename, "MySql_server");
            userSql = uti.readInfor(filename, "MySql_user");
            pwdSql = uti.readInfor(filename, "MySql_pwd");
            aport = Integer.parseInt(uti.readInfor(filename, "Port_agent"));
            qport = Integer.parseInt(uti.readInfor(filename, "Port_queue"));                                       
            mdb_agent = new Managerdb(dbname,userSql,pwdSql,hostdb);
            if(mdb_agent.isConnect()){
                uti.writeAsteriskLog("- SYSTE  - Connect to Database Successful");
                System.out.println("Connect to Database Successful");
                alisten = new AgentListen( aport, mdb_agent );
                qlisten = new QueueListen( qport, mdb_agent );                
            } else {
                uti.writeAsteriskLog("- SYSTE  - Connect to Database Fail");
                System.out.println("Connect to Database Fail");
                System.out.println("Interrup Connection.");                
            }
	}
}

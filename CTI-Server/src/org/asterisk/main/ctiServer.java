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
        private static String hostdb = "172.168.10.208";      
        private static String dbname = "cti_database";
	private static String userSql = "cti";
	private static String pwdSql  = "123456";
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
                uti.writeInfor(filename, "dbname", dbname);
                uti.writeInfor(filename, "hostdb", hostdb);
                uti.writeInfor(filename, "userSql", userSql);
                uti.writeInfor(filename, "pwdSql", pwdSql);
                uti.writeInfor(filename, "aport", Integer.toString(aport));
                uti.writeInfor(filename, "qport", Integer.toString(qport));
            }
            System.out.println("Read configuration file!"); 
            dbname = uti.readInfor(filename, "dbname");
            hostdb = uti.readInfor(filename, "hostdb");
            userSql = uti.readInfor(filename, "userSql");
            pwdSql = uti.readInfor(filename, "pwdSql");
            aport = Integer.parseInt(uti.readInfor(filename, "aport"));
            qport = Integer.parseInt(uti.readInfor(filename, "qport"));                                       
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

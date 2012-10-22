package org.asterisk.main;

//import java.net.ServerSocket;
//import java.net.Socket;
//import javax.net.ssl.SSLServerSocket;
import java.io.File;
import java.io.FileInputStream;
import org.asterisk.utility.AgentListen;

//import org.asterisk.utility.ManagerAgent;
import org.asterisk.utility.Managerdb;
//import org.asterisk.utility.QueueInfo;
import org.asterisk.utility.QueueListen;
import org.asterisk.utility.Utility;
//import org.asteriskjava.manager.ManagerConnection;
//import org.asteriskjava.manager.ManagerConnectionFactory;
//import org.asteriskjava.manager.ManagerEventListener;
//import org.asteriskjava.manager.action.StatusAction;
//import org.asteriskjava.manager.event.ConnectEvent;
//import org.asteriskjava.manager.event.DisconnectEvent;
//import org.asteriskjava.manager.event.ManagerEvent;
//import org.asteriskjava.manager.event.ReloadEvent;
//import org.asteriskjava.manager.event.ShutdownEvent;

public class ctiServer{
//public class ctiServer implements ManagerEventListener{    
	private static Utility uti;	
	private static int aport = 22222;
	private static int qport = 33333;
        private static String hostdb = "172.168.10.208";
//        private static String host = "127.0.0.1";
//        private static String hostAsterisk = "172.168.10.208";
//        private static String userAsterisk = "manager";
//        private static String pwdAsterisk = "123456";        
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
                System.out.println("file is empty"); 
                uti.writeInfor(filename, "dbname", dbname);
                uti.writeInfor(filename, "hostdb", hostdb);
                uti.writeInfor(filename, "userSql", userSql);
                uti.writeInfor(filename, "pwdSql", pwdSql);
                uti.writeInfor(filename, "aport", Integer.toString(aport));
                uti.writeInfor(filename, "qport", Integer.toString(qport));
                System.out.println("write file"); 
            }
            System.out.println("read file"); 
            dbname = uti.readInfor(filename, "dbname");
            hostdb = uti.readInfor(filename, "hostdb");
            userSql = uti.readInfor(filename, "userSql");
            pwdSql = uti.readInfor(filename, "pwdSql");
            aport = Integer.parseInt(uti.readInfor(filename, "aport"));
            qport = Integer.parseInt(uti.readInfor(filename, "qport"));                
                       
            mdb_agent = new Managerdb(dbname,userSql,pwdSql,hostdb);
            if(mdb_agent.isConnect()){
                uti.writeAsteriskLog("Connect to Database Successfull");
                System.out.println("Connect to Database Successfull");
                alisten = new AgentListen( aport, mdb_agent );
                qlisten = new QueueListen( qport, mdb_agent );                
            } else {
                uti.writeAsteriskLog("Connect to Database Fail");
                System.out.println("Connect to Database Fail");
                System.out.println("Interrup Connection.");                
            }
	}
        
	
//	@Override
//	public void onManagerEvent(ManagerEvent event){
//	}
		

}

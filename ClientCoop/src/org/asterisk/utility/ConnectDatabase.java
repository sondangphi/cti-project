package org.asterisk.utility;



import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.asterisk.model.QueueObject;



public class ConnectDatabase {

	private  Connection connection = null;
	private  String user = "callcenter";
	private  String pwd = "callcenter";
	private  String database = "ast_callcenter";
	private  String driverName = "com.mysql.jdbc.Driver";
	private  String serverName = "172.168.10.202:3306/";
    	
	public ConnectDatabase() throws ClassNotFoundException, SQLException{
		Class.forName(driverName);
		connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database+"?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true",user,pwd);
	}
	
	public ConnectDatabase(String db, String username, String pass,String host) throws IOException {
            try{
                System.out.println("start managerdb");
                database = db;
                user = username;
                pwd  = pass;
                serverName = host;
                Class.forName(driverName);		
                connection = DriverManager.getConnection("jdbc:mysql://"+serverName+":3306/"+database+"?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true",user,pwd); 
                connection.setAutoCommit(true); 
                System.out.println("end managerdb");
            }catch(Exception e){
                System.out.println("exception managerdb\r\n"+e);
            }

	}        
        
        public boolean isConnect()throws SQLException{         
            if(connection.isClosed())
                return false;
            return true;            
	}
	
	public boolean closeConnect() {
            try{
                connection.close();
            }catch(Exception e){
                return false;
            }
            return true;		
	}        
        
	/*ket noi csdl mysql bang jdbc*/
	public  ResultSet executeQuery(String sqlCom) throws ClassNotFoundException, SQLException{		
//		Class.forName(driverName);
//		connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);			
		Statement stm = connection.createStatement();		
		ResultSet rs = stm.executeQuery(sqlCom);//thi hanh lenh select va tra ve ket qua
		return rs;
	}
	public  int executeUpdate(String sql)throws ClassNotFoundException, SQLException{
		
//		Class.forName(driverName);
//		connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);
		int result = 0;
		Statement stm = connection.createStatement();	
		result  = stm.executeUpdate(sql);
		return result;//neu thanh cong tra ve so thu tu hang thuc thi
										//neu that bai tra ve 0
	}	
        
        public ArrayList<QueueObject> listQueue(){
            ArrayList<QueueObject> list = new ArrayList<QueueObject>();
            try{
                String sqlCom = "SELECT extension,descr FROM queues_config";
                ResultSet rs = executeQuery(sqlCom);
                while(rs.next()){
                    QueueObject q = new QueueObject();
                    q.setQueueId(rs.getString("extension"));
                    q.setQueueName(rs.getString("descr"));
                    list.add(q);
                }
            }catch(SQLException e){
                System.out.println("SQLException\t"+e);
            }catch(ClassNotFoundException e){
                System.out.println("ClassNotFoundException\t"+e);
            }             
            return list;            
        }   
}

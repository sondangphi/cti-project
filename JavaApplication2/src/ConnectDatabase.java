



import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;




public class ConnectDatabase {

	private static Connection connection = null;
	private static String user = "cti";
	private static String pwd = "123456";
	private static String database = "cti_database";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String serverName = "172.168.10.208:3306/";
	
	public ConnectDatabase() throws ClassNotFoundException, SQLException{
		Class.forName(driverName);
		connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);
	}
	
	public ConnectDatabase(String db, String username, String pass,String host) throws IOException {
            try{
                System.out.println("start managerdb");
                database = db;
                user = username;
                pwd  = pass;
                serverName = host;
                Class.forName(driverName);		
                connection = DriverManager.getConnection("jdbc:mysql://"+serverName+":3306/"+database,user,pwd); 
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
		
		Class.forName(driverName);
//		connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);			
		Statement stm = connection.createStatement();		
		ResultSet rs = stm.executeQuery(sqlCom);//thi hanh lenh select va tra ve ket qua
		return rs;
	}
	public  int executeUpdate(String sql)throws ClassNotFoundException, SQLException{
		
		Class.forName(driverName);
//		connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);
		int result = 0;
		Statement stm = connection.createStatement();	
		result  = stm.executeUpdate(sql);
		return result;//neu thanh cong tra ve so thu tu hang thuc thi
										//neu that bai tra ve 0
	}	
       
}

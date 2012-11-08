

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;



public class ConnectDatabase {

	private static Connection con = null;
	private static String user = "cti";
	private static String pass = "123456";
	private static String database = "cti_database";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String serverName = "172.168.10.208:3306/";
	
	public ConnectDatabase() throws ClassNotFoundException, SQLException{
		Class.forName(driverName);
		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);
	}
	
	/*ket noi csdl mysql bang jdbc*/
	public static ResultSet executeQuery(String sqlCom) throws ClassNotFoundException, SQLException{
		
		Class.forName(driverName);
		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);			
		Statement stm = con.createStatement();		
		ResultSet rs = stm.executeQuery(sqlCom);//thi hanh lenh select va tra ve ket qua
		return rs;
	}
	public static int executeUpdate(String sql)throws ClassNotFoundException, SQLException{
		
		Class.forName(driverName);
		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);
		int result = 0;
		Statement stm = con.createStatement();	
		result  = stm.executeUpdate(sql);
		con.close();
		return result;//neu thanh cong tra ve so thu tu hang thuc thi
										//neu that bai tra ve 0
	}	
//        
//        public static void main(String[] args) throws Exception {
//            String sql = "select agent_id,datetime_login,timediff(datetime_logout,datetime_login) from login_action group by datetime_login,agent_id order by id";
//            ResultSet rs = null;
//            rs = executeQuery(sql);
//            while(rs.next()){
//                String date = rs.getDate("datetime_login").toString();
//                if(date.equals("2012-11-02")){
//                    System.out.print(""+rs.getString("agent_id"));
//                    System.out.print("\t"+rs.getString("datetime_login"));
//                    System.out.println("\t"+rs.getString("timediff(datetime_logout,datetime_login)"));
//                }
//                System.out.println("\t"+rs.getTimestamp("datetime_login"));
//            }            
//        }
}

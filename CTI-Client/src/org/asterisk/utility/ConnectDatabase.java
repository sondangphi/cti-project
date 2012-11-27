package org.asterisk.utility;



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
		return result;//neu thanh cong tra ve so thu tu hang thuc thi
										//neu that bai tra ve 0
	}	
}

package org.asterisk.utility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.asterisk.utility.*;

public class ConnectDatabase {

	private static Connection con = null;
	private static String user = "test";
	private static String pass = "123456";
	private static String database = "test";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String serverName = "172.168.10.100:3306/";
	private Utility uti = new Utility();
	
	public ConnectDatabase() throws ClassNotFoundException, SQLException{
//		Class.forName(driverName);
//		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);
	}
	
	/*ket noi csdl mysql bang jdbc*/
	public static ResultSet executeQuery(String sqlCom) throws ClassNotFoundException, SQLException{
		
		Class.forName(driverName);
		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);			
		Statement stm = con.createStatement();		
		ResultSet rs = stm.executeQuery(sqlCom);//thi hanh lenh select va tra ve ket qua
		con.close();
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
	public boolean checkDatabase() throws ClassNotFoundException, SQLException, IOException{
		try{
			uti.writeAsteriskLog("connect to database");
			Class.forName(driverName);		
			con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);
			con.close();
		}catch(Exception ex){
			return false;
		}
		uti.writeAsteriskLog("connect to database successfull");
		return true;
	}
}

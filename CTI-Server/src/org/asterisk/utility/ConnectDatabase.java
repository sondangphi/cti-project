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
	private static String user = "cti";
	private static String pwd = "123456";
	private static String database = "test";
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String serverName = "172.168.10.205:3306/";
	private Utility uti = new Utility();
	
	public ConnectDatabase() throws ClassNotFoundException, SQLException{
//		Class.forName(driverName);
//		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pass);
	}
	
	/*ket noi csdl mysql bang jdbc*/
	public static ResultSet executeQuery(String sqlCom) throws ClassNotFoundException, SQLException{
		
		Class.forName(driverName);
		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);			
		Statement stm = con.createStatement();		
		ResultSet rs = stm.executeQuery(sqlCom);//thi hanh lenh select va tra ve ket qua
//		con.close();
		return rs;
	}
	public static int executeUpdate(String sql)throws ClassNotFoundException, SQLException{
		
		Class.forName(driverName);
		con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);
		int result = 0;
		Statement stm = con.createStatement();	
		result  = stm.executeUpdate(sql);
//		con.close();
		return result;//neu thanh cong tra ve so thu tu hang thuc thi
										//neu that bai tra ve 0
	}	
	
	public static boolean checkLogin(String agent, String pass) throws ClassNotFoundException, SQLException{
		String sqlCom = "SELECT * FROM AgentLogin where AgentName = '"+agent+"'and Password = '"+pass+"'";
		ResultSet rs = executeQuery(sqlCom);
		return rs.next() ? true : false;
	}
	
	public static boolean checkStatus(String iface) throws ClassNotFoundException, SQLException{
		String sqlCom = "SELECT * FROM AgentLogin where Interface = '"+iface+"'";
		ResultSet rs = executeQuery(sqlCom);
		return rs.next() ? false : true;
	}
	
	public static int updateStatus(String agent, String iface, String queue) throws ClassNotFoundException, SQLException{
		String sql = "UPDATE AgentLogin SET Interface ='"+iface+"',Queue ='"+queue+"' WHERE AgentName = '"+agent+"'" ;
		executeUpdate(sql);
//		return (executeUpdate(sql) > 0) ? 1 : 0;
		return executeUpdate(sql);
	}
	
	public static void writeAction(String agent, String iface, String act, String queue) throws ClassNotFoundException, SQLException{
		String sql = "INSERT INTO AgentAction (AgentName, Interface, Action, Queue) " +
				"VALUES ('"+agent+"','"+iface+"','"+act+"','"+queue+"')";
		executeUpdate(sql);
	}
	
	public boolean checkDatabase() throws ClassNotFoundException, SQLException, IOException{
		try{			
			Class.forName(driverName);		
			con = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);
			con.close();
		}catch(Exception ex){
			return false;
		}
		return true;
	}
}

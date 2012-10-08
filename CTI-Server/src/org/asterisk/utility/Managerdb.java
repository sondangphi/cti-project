package org.asterisk.utility;

//import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

//import org.asterisk.utility.*;

public class Managerdb {

	private static Connection connection = null;
	private static String user = "";
	private static String pwd = "";
	private static String serverName = "";
	private static String database = "test";
	private static String driverName = "com.mysql.jdbc.Driver";
	
//	private Utility uti = new Utility();
	
	public Managerdb(String address, String username, String pass) throws ClassNotFoundException, SQLException{
		serverName = address+":3306/";
		user = username;
		pwd  = pass;
	}
	public Managerdb(){
		
	}
	public boolean connect(){
		try{
			Class.forName(driverName);		
			connection = DriverManager.getConnection("jdbc:mysql://"+serverName+database,user,pwd);
		}catch(Exception e){
			return false;
		}
		return true;

	}
	
	public boolean close() {
		try{
			connection.close();
		}catch(Exception e){
			return false;
		}
		return true;		
	}
	
	/*ket noi csdl mysql bang jdbc*/
	public static ResultSet executeQuery(String sqlCom) throws ClassNotFoundException, SQLException{		
		Statement stm = connection.createStatement();		
		ResultSet rs = stm.executeQuery(sqlCom);
		return rs;
		//thi hanh lenh select va tra ve ket qua ResultSet
	}
	public static int executeUpdate(String sql)throws ClassNotFoundException, SQLException{
		int result = 0;
		Statement stm = connection.createStatement();	
		result  = stm.executeUpdate(sql);
		return result;	
		/* neu thanh cong tra ve so thu tu hang thuc thi
		 * neu that bai tra ve 0
		 */
	}	
	
	public boolean checkLogin(String agent, String pass) throws ClassNotFoundException, SQLException{
		String sqlCom = "SELECT * FROM AgentLogin where AgentName = '"+agent+"'and Password = '"+pass+"'";
		ResultSet rs = executeQuery(sqlCom);
		return rs.next() ? true : false;
	}
	
	public boolean checkStatus(String iface) throws ClassNotFoundException, SQLException{
		String sqlCom = "SELECT * FROM AgentLogin where Interface = '"+iface+"'";
		ResultSet rs = executeQuery(sqlCom);
		return rs.next() ? false : true;
	}
	
	public int updateStatus(String agent, String iface, String queue) throws ClassNotFoundException, SQLException{
		String sql = "UPDATE AgentLogin SET Interface ='"+iface+"',Queue ='"+queue+"' WHERE AgentName = '"+agent+"'" ;
		executeUpdate(sql);
		return executeUpdate(sql);
	}
	
	public void writeAction(String agent, String iface, String act, String queue) throws ClassNotFoundException, SQLException{
		String sql = "INSERT INTO AgentAction (AgentName, Interface, Action, Queue) " +
				"VALUES ('"+agent+"','"+iface+"','"+act+"','"+queue+"')";
		executeUpdate(sql);
	}		
}

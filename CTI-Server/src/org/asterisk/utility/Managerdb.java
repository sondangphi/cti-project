package org.asterisk.utility;

//import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.asterisk.model.QueueObject;

//import org.asterisk.utility.*;

public class Managerdb {

	private static Connection connection = null;
	private static String user = "cti";
	private static String pwd = "123456";
	private static String serverName = "172.168.10.208";
	private static String database = "";
	private static String driverName = "com.mysql.jdbc.Driver";
	
//	private Utility uti = new Utility();
	
	public Managerdb(String address, String username, String pass) throws ClassNotFoundException, SQLException{
		serverName = address;
                database = "";
		user = username;
		pwd  = pass;
	}
        public Managerdb(String address, String username, String pass, String db) throws ClassNotFoundException, SQLException{
		serverName = address;
                database = db;
		user = username;
		pwd  = pass;
	}
	public Managerdb(){
		
	}
        public Managerdb(String db){
            database = db;
	}
	public boolean connect(){
            try{
                Class.forName(driverName);		
                connection = DriverManager.getConnection("jdbc:mysql://"+serverName+":3306/"+database,user,pwd);
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
	
	public boolean checkLogin(String agent, String pass, String role) throws ClassNotFoundException, SQLException{
		String sqlCom = "SELECT * FROM agent_login where agent_id = '"+agent+"'and password = '"+pass+"' and role ='"+role+"'";
		ResultSet rs = executeQuery(sqlCom);
		return rs.next() ? true : false;
	}
	
	public boolean checkStatus(String iface) throws ClassNotFoundException, SQLException{
		String sqlCom = "SELECT agent_id FROM agent_status where interface = '"+iface+"'";
		ResultSet rs = executeQuery(sqlCom);
		return rs.next() ? false : true;
	}
	
	public int updateStatus(String agentid, String iface, String queue) throws ClassNotFoundException, SQLException{
		String sql = "UPDATE agent_status SET interface ='"+iface+"',queue ='"+queue+"' WHERE agent_id = '"+agentid+"'" ;
		executeUpdate(sql);
		return executeUpdate(sql);
	}
	
	public void loginAction(String agent, String iface, String act, String queue) throws ClassNotFoundException, SQLException{
		String sql = "INSERT INTO login_action (agent_id, interface, queue) " +
				"VALUES ('"+agent+"','"+iface+"','"+queue+"')";
		executeUpdate(sql);
	}
        public int logoutAction(String agentid, String iface, String queue) throws ClassNotFoundException, SQLException{
            String datetime = "";
            String sql = "UPDATE agent_status SET interface ='"+iface+"',queue ='"+queue+"' WHERE agent_id = '"+agentid+"'" ;
            executeUpdate(sql);
            return executeUpdate(sql);
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
                System.out.println("e\t"+e);
            }catch(ClassNotFoundException e){
                System.out.println("e2\t"+e);
            }             
            return list;            
        }
}

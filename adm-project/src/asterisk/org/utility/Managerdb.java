package asterisk.org.utility;

//import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Managerdb {

	private static Connection connection = null;
	private static String user = "cti";
	private static String pwd = "123456";
	private static String serverName = "172.168.10.208";
	public static String database = "asterisk";
	private static String driverName = "com.mysql.jdbc.Driver";
        String tablename = "";
	public Managerdb(String db, String username, String pass, String host) throws ClassNotFoundException, SQLException{
            database = db;
            user = username;
            pwd  = pass;
            serverName = host;
            Class.forName(driverName);		
            connection = DriverManager.getConnection("jdbc:mysql://"+serverName+":3306/"+database,user,pwd); 
	}        
	public Managerdb(){
		
	}
        public boolean isConnect()throws SQLException{         
            if(connection.isClosed())
                return false;
            return true;            
	}
	
	public boolean close() {
            try{
                connection.close();
                connection = null;
            }catch(Exception e){return false;}
            return true;		
	}
	
	/*ket noi csdl mysql bang jdbc*/
	public static ResultSet sqlQuery(String sqlCom) throws ClassNotFoundException, SQLException{		
            Statement stm = connection.createStatement();		
            ResultSet rs = stm.executeQuery(sqlCom);
            return rs;
	}
	public static int sqlExecute(String sql)throws ClassNotFoundException, SQLException{
            int result = 0;
            Statement stm = connection.createStatement();	
            result  = stm.executeUpdate(sql);
            return result;	
	}		
        
        public boolean getInfor(String phone) throws ClassNotFoundException, SQLException{
            String sqlCom = "SELECT * FROM cus where phone = '"+phone+"'";
            ResultSet rs = sqlQuery(sqlCom);
            return rs.next() ? true : false;
	}	
}

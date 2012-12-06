package org.asterisk.utility;

//import java.io.IOException;
import java.io.IOException;
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
	private static String user = "";
	private static String pwd = "";
	private static String serverName = "";
//        private static String serverName = "localhost";
	public static String database = "";
	private static String driverName = "com.mysql.jdbc.Driver";
        private Utility uti = new Utility();	
	
	public Managerdb(String db, String username, String pass,String host) throws IOException {
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
                uti.writeAsteriskLog("Can't connect to Database - Exit program");
                System.out.println("exception managerdb\r\n"+e);
            }

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
            }catch(Exception e){
                return false;
            }
            return true;		
	}
	
	/*ket noi csdl mysql bang jdbc*/
	public static ResultSet sqlQuery(String sqlCom) throws ClassNotFoundException, SQLException{		
            Statement stm = connection.createStatement();		
            ResultSet rs = stm.executeQuery(sqlCom);
            return rs;
            //thi hanh lenh select va tra ve ket qua ResultSet
	}
	public static int sqlExecute(String sql)throws ClassNotFoundException, SQLException{
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
            ResultSet rs = sqlQuery(sqlCom);
            return rs.next() ? true : false;
	}
        
        public String getAgentName(String agent)throws ClassNotFoundException, SQLException{            
            String name = "";
            String sqlCom = "SELECT agentName FROM agent_login where agent_id = '"+agent+"'";
            ResultSet rs = sqlQuery(sqlCom);
            if(rs.next())
                name = rs.getString("agentName");
            return name;
        }
	
//        public String getAgentSession(String session)throws ClassNotFoundException, SQLException{            
//            String name = "";
//            String sqlCom = "SELECT agentName FROM agent_login where session = '"+session+"'";
//            ResultSet rs = sqlQuery(sqlCom);
//            if(rs.next())
//                name = rs.getString("agentName");
//            return name;
//        }        
        
	public boolean checkStatus(String iface) throws ClassNotFoundException, SQLException{
            String sqlCom = "SELECT * FROM agent_status where interface = '"+iface+"'";
            ResultSet rs = sqlQuery(sqlCom);
            return rs.next() ? false : true;
	}
	
	public int updateStatus(String agentid, String iface, String queue) throws ClassNotFoundException, SQLException{
            String sql = "UPDATE agent_status SET interface ='"+iface+"',queue ='"+queue+"' WHERE agent_id = '"+agentid+"'" ;
            sqlExecute(sql);
            return sqlExecute(sql);
	}
	
	public void loginAction(String ses, String agent, String iface, String queue) throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO login_action (session, agent_id, interface, queue) " +
                            "VALUES ('"+ses+"','"+agent+"','"+iface+"','"+queue+"')";
            sqlExecute(sql);
	}
        public int logoutAction(String ses,String agentid) throws ClassNotFoundException, SQLException{
            String datetime = uti.getDatetime();
            String sql = "UPDATE login_action SET datetime_logout ='"+datetime+"'"
                        + " WHERE session = '"+ses+"' and agent_id ='"+agentid+"'" ;
            sqlExecute(sql);
            return sqlExecute(sql);
	}
        
	public void pauseAction(String ses, String agent) throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO pause_action (session, agent_id) VALUES "
                    + "('"+ses+"','"+agent+"')";
            sqlExecute(sql);
	}
        public int unpauseAction(String ses,String agentid) throws ClassNotFoundException, SQLException{
            String datetime = uti.getDatetime();
            String sql = "UPDATE pause_action SET datetime_unpause ='"+datetime+"'"
                        + " WHERE session = '"+ses+"' and agent_id ='"+agentid+"'" ;
            sqlExecute(sql);
            return sqlExecute(sql);
	}        
        
        public void writeDialLog(String ses, String agentid, String iface, String queue, String event)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dial_event (session, agent_id, interface, queue, event) VALUES "
                    + "('"+ses+"','"+agentid+"','"+iface+"','"+queue+"','"+event+"')";
            sqlExecute(sql);            
        }
        
        public void enterQueue(String ses, String agentid, String iface, String queue, String event, String caller)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dial_event (session, agent_id, interface, queue, event, note1) VALUES "
                    + "('"+ses+"','"+agentid+"','"+iface+"','"+queue+"','"+event+"','"+caller+"')";
            sqlExecute(sql);            
        }      
        public void connectQueue(String ses, String agentid, String iface, String queue, String event, String ringtime)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dial_event (session, agent_id, interface, queue, event, note1) VALUES "
                    + "('"+ses+"','"+agentid+"','"+iface+"','"+queue+"','"+event+"','"+ringtime+"')";
            sqlExecute(sql);            
        }         
        public void ringNoans(String ses, String agentid, String iface, String queue, String event, String ringtime)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dial_event (session, agent_id, interface, queue, event, note1) VALUES "
                    + "('"+ses+"','"+agentid+"','"+iface+"','"+queue+"','"+event+"','"+ringtime+"')";
            sqlExecute(sql);            
        }        
        //chua bik dau lenh sql
        public void abandon(String ses, String agentid, String iface, String queue, String event, String ringtime)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dial_event (session, agent_id, interface, queue, event, note1) VALUES "
                    + "('"+ses+"','"+agentid+"','"+iface+"','"+queue+"','"+event+"','"+ringtime+"')";
            sqlExecute(sql);            
        } 
        public void completeCall(String ses, String agentid, String iface, String queue, String event, String talktime)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO dial_event (session, agent_id, interface, queue, event, note1) VALUES "
                    + "('"+ses+"','"+agentid+"','"+iface+"','"+queue+"','"+event+"','"+talktime+"')";
            sqlExecute(sql);            
        }        
        public boolean changePwd(String oldPwd, String newPwd, String agent) throws ClassNotFoundException, SQLException{            
            String sqlcom = "UPDATE agent_login SET password ='"+newPwd+"' WHERE agent_id ='"+agent+"'";                        
            return sqlExecute(sqlcom)>0 ? true : false;            
        }
                
        
        
        public ArrayList<QueueObject> listQueue(){
            ArrayList<QueueObject> list = new ArrayList<QueueObject>();
            try{
                String sqlCom = "SELECT extension,descr FROM queues_config";
                ResultSet rs = sqlQuery(sqlCom);
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

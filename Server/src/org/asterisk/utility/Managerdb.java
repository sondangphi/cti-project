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

	private Connection connection = null;
	private String user = "";
	private String pwd = "";
	private String serverName = "";
	public String database = "";
	private String driverName = "com.mysql.jdbc.Driver";
        private Utility uti = new Utility();	
        private String filename = "infor.properties";
	
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
                uti.writeAsteriskLog("- MYSQL - Can't connect to Database - Exit program");
                System.out.println("exception managerdb\r\n"+e);
            }

	}        
	public Managerdb(){
		
	}
        public void checkConnect()throws SQLException, ClassNotFoundException, Exception{
            if(!isConnect()){
                database = uti.readInfor(filename, "MySql_database");
                serverName = uti.readInfor(filename, "MySql_server");
                user = uti.readInfor(filename, "MySql_user");
                pwd = uti.readInfor(filename, "MySql_pwd");
                Class.forName(driverName);		
                connection = DriverManager.getConnection("jdbc:mysql://"+serverName+":3306/"+database,user,pwd); 
                connection.setAutoCommit(true);
            }            
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
	public ResultSet sqlQuery(String sqlCom) throws ClassNotFoundException, SQLException{		
            Statement stm = connection.createStatement();		
            ResultSet rs = stm.executeQuery(sqlCom);
            return rs;
            //thi hanh lenh select va tra ve ket qua ResultSet
	}
	public int sqlExecute(String sql)throws ClassNotFoundException, SQLException{
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
        //su dung cho 1 agent/1 interface/ 1 queue
	public boolean checkStatus(String agentid, String iface, String queueid) throws ClassNotFoundException, SQLException{
            String sqlCom2 = "SELECT * FROM agent_status where interface = '"+iface+"'";
            String sqlCom = "SELECT * FROM agent_status where agent_id = '"+agentid+"'";
            ResultSet rs = sqlQuery(sqlCom);            
            if(rs.next()){
                String inface = rs.getString("interface");
                String queue = rs.getString("queue");
                System.out.println("inface: "+inface+"\t"+"queue: "+queue);
                System.out.println("iface: "+iface+"\t"+"queueid: "+queueid);
                if("0".equalsIgnoreCase(inface) && "0".equalsIgnoreCase(queue)){
                    System.out.println("inface: "+inface+"\t"+"queue: "+queue);
                    rs = sqlQuery(sqlCom2);
                    if(rs.next())
                        return false;
                    return true;                
                }else if(iface.equalsIgnoreCase(inface) && queueid.equalsIgnoreCase(queue))//same iface, same queue ->false
                    return false;
                else if(iface.equalsIgnoreCase(inface) && !queueid.equalsIgnoreCase(queue))//same iface, diff queue ->false
                    return false;
                else if(!iface.equalsIgnoreCase(inface) && queueid.equalsIgnoreCase(queue))//diff iface, same queue ->false
                    return false;
                else if(!iface.equalsIgnoreCase(inface) && !queueid.equalsIgnoreCase(queue))//diff iface, same queue ->false
                    return false;               
            }
            return false ;
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
	public void pauseAction(String ses, String agent, String iface, String queueId, String loginSession) throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO pause_action (session, agent_id, interface, queueId, loginSession) VALUES "
                    + "('"+ses+"','"+agent+"','"+iface+"','"+queueId+"','"+loginSession+"')";
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
        public boolean changePwd(String newPwd, String agent) throws ClassNotFoundException, SQLException{            
            String sqlcom = "UPDATE agent_login SET password ='"+newPwd+"' WHERE agent_id ='"+agent+"'";                        
            return sqlExecute(sqlcom)>0 ? true : false;            
        }
                
        public void finishDialout(String session, String talktime, String status)throws ClassNotFoundException, SQLException{
            String sql = "UPDATE agent_dialout SET talktime ='"+talktime+"', status ='"+status+"'"
                        + " WHERE session = '"+session+"'";
            sqlExecute(sql); 
        }
        
       public void startDialout(String agentid, String iface, String queueid, String number, String loginSes, String session)throws ClassNotFoundException, SQLException{
            String sql = "INSERT INTO agent_dialout (agentid, interface, queueid, dialoutnumber, loginSession, session) VALUES "
                    + "('"+agentid+"','"+iface+"','"+queueid+"','"+number+"','"+loginSes+"','"+session+"')";
            sqlExecute(sql); 
        }        
        
        public void checkSessionLogout()throws Exception{
            uti.writeAsteriskLog("- SYSTE  - Check DateTime Agent unLogout");
            String date = uti.getDate();            
            String sql = "SELECT * FROM login_action WHERE CAST(datetime_login AS DATE) >=  '"+date+"'";
            ResultSet rs = sqlQuery(sql);
            String datenow = uti.getDatetime();
            while(rs.next()){
                String datelogout = String.valueOf(rs.getObject("datetime_logout"));               
                if(datelogout.equalsIgnoreCase("null")){
                    String agentid = String.valueOf(rs.getObject("agent_id"));     
                    String iface = String.valueOf(rs.getObject("interface")); 
                    String queue = String.valueOf(rs.getObject("queue")); 
                    String session = rs.getString("session");    
                    updateStatus(agentid, "NULL", "NULL");
                    logoutAction(session, agentid);
                    uti.writeAsteriskLog("- SYSTE  - Update datetime agent unlogout\t"+agentid+"\t"+session);
                    System.out.println("update success logout\t"+session);
                    
//                    sql = "UPDATE login_action SET datetime_logout ='"+datenow+"'"
//                        + " WHERE session = '"+session+"'" ;                    
//                    int rs2 = sqlExecute(sql);
//                    if(rs2 != 0)
//                        uti.writeAsteriskLog("- SYSTE  - Update datetime agent unlogout\t"+agentid+"\t"+session);
//                        System.out.println("update success\t"+session);
//                    String sql2 = "UPDATE agent_status SET interface =null,queue=null WHERE agent_id = '"+agentid+"'" ;
//                    rs2 = sqlExecute(sql2);
                }
            }
        }
        
        public void checkSessionPause()throws Exception{
            uti.writeAsteriskLog("- SYSTE  - Check DateTime Agent Pause");
            String date = uti.getDate();            
            String sql = "SELECT * FROM pause_action WHERE CAST(datetime_pause AS DATE) >=  '"+date+"'";
            ResultSet rs = sqlQuery(sql);
            String datenow = uti.getDatetime();
            while(rs.next()){
                String datepause = String.valueOf(rs.getObject("datetime_unpause"));               
                if(datepause.equalsIgnoreCase("null")){
                    String agentid = String.valueOf(rs.getObject("agent_id"));               
                    String session = rs.getString("session");  
                    unpauseAction(session, agentid);
                    uti.writeAsteriskLog("- SYSTE  - Update Datetime Agent Pause\t"+agentid+"\t"+session);
                    System.out.println("update success pause\t"+session);
                }
            }
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

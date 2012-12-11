
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
public class newclass123 {
     public static void main(String args[]) throws Exception{
             String colname[] = {"No","Date","Full Name","Phone", "Agent","Type","Content","Result"};
            int count = colname.length;
//            Vector col = new Vector(count);
//            Vector row = new  Vector();
            String [] temp = null;
            ArrayList<String[]> data = new ArrayList<String[]>();
            String sql = "SELECT * FROM feedback WHERE mobile ='8002'";
            sql = "INSERT INTO customer (fullname,email,address,mobilephone,gender,homephone1) VALUES ('1','1','1','0','1','3')";
            ConnectDatabase con = new ConnectDatabase();    
            
            int t = con.executeUpdate(sql);
//            ResultSet rs = con.executeQuery(sql);
//            int t = 1;
//            while(rs.next()){
//                int j = 0;                                                                                    
//                temp  = new String[count];
//                temp[ j++ ] = String.valueOf(t++);
//                temp[ j++ ] = String.valueOf(rs.getObject("datetime"));//rs.getString("datetime");
//                temp[ j++ ] = String.valueOf(rs.getObject("name"));//rs.getString("name");
//                temp[ j++ ] = String.valueOf(rs.getObject("mobile"));//rs.getString("mobile");
//                temp[ j++ ] = String.valueOf(rs.getObject("agentid"));//rs.getString("agentid");
//                temp[ j++ ] = String.valueOf(rs.getObject("type"));//rs.getString("type");
//                temp[ j++ ] = String.valueOf(rs.getObject("content"));//rs.getString("content");
//                temp[ j++ ] = String.valueOf(rs.getObject("results"));//rs.getString("result");
//                data.add(temp);
//            }
            
            con.closeConnect();
     }
}

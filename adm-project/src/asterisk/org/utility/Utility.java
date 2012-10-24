package asterisk.org.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

public class Utility {
        Properties prop = new Properties();    
        String filename = "infor.properties"; 
        String logname = "logfile.log";

        public Utility() throws Exception {
            File f = new File(logname);
            if(!f.exists())
                f.createNewFile();
//            f = new File(logname);
//            if(!f.exists())
//                f.createNewFile();
        }
        
	public String getDatetime(){		
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String datetime = dateFormat.format(cal.getTime()).toString();	
            return datetime;
	}
	
	public void writeLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter(logname,true));
            output.append(getDatetime()+"\t"+log);
            output.append("\r\n");
            output.close();
	}
	
	public void writeLog(String log,String file) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter(file,true));
            output.append(getDatetime()+"\t"+log+"\r\n");
            output.close();
	}
	
	public void writeAsteriskLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter("AsteriskLogFile.log",true));
            output.append(getDatetime() + "\t" +log+ "\r\n");
            output.close();
	}
	
	public void writeAgentLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter("AgentLogFile.log",true));
            output.append(getDatetime() + "\t" + log + "\r\n");
            output.close();
	}
	
	public ArrayList<String> getList(String cmd){
            ArrayList<String> list =  new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens())
                list.add(st.nextToken());            
            return list;
	}
        
        public String getSession(){
            String temps = "QWERTYUIOPASDFGHJKLZXCVBNMabcdefghijklmnopqrstuvwxyz1234567890";
            Random rnd = new Random();
            StringBuilder sb = new StringBuilder( 10 );
            for( int i = 0; i < 10; i++ ) 
                sb.append( temps.charAt( rnd.nextInt(temps.length()) ) );
            return sb.toString();           
        }
        
        public void writeInfor(String fname,String key, String value)throws Exception{
            prop.setProperty(key, value);
            prop.store(new FileOutputStream(fname), null);
        }
        public String readInfor(String fname, String key)throws Exception{
            String value = "";
            prop.load(new FileInputStream(fname));
            value = prop.getProperty(key);
            return value;
        }
        public boolean checkAgent(String t){
            return t.matches("([a-zA-Z0-9_./\\-]+)");
        }
        public boolean checkPwd(String t){
            return t.matches("([a-zA-Z0-9]+)");
        }
        public boolean checkIface(String t){
            return t.matches("([0-9]+)");
        }        

}

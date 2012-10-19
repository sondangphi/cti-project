package org.asterisk.utility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.StringTokenizer;

public class Utility {
	
	public String getDatetime(){		
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String datetime = dateFormat.format(cal.getTime()).toString();	
            return datetime;
	}
	
	public void writeLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter("logfile.log",true));
            output.append(getDatetime()+"\t"+log);
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

}

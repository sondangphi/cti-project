package org.asterisk.utility;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

public class Utility {
        Properties prop = new Properties();
    
        String filename = "infor.properties"; 
	public String getDatetimeNow(){		
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            String datetime = dateFormat.format(cal.getTime()).toString();	
            return datetime;
	}
        public String getDateNow(){		
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            String datetime = dateFormat.format(cal.getTime()).toString();	
            return datetime;
	}
	public void writeLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter("logfile.log",true));
            output.append(getDatetimeNow()+"\t"+log);
            output.close();
	}
	
	public void writeLog(String log,String file) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter(file,true));
            output.append(getDatetimeNow()+"\t"+log+"\r\n");
            output.close();
	}
	
	public void writeAsteriskLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter("AsteriskLogFile.log",true));
            output.append(getDatetimeNow() + "\t" +log+ "\r\n");
            output.close();
	}
	
	public void writeAgentLog(String log) throws IOException{
            Writer output;
            output = new BufferedWriter(new FileWriter("AgentLogFile.log",true));
            output.append(getDatetimeNow() + "\t" + log + "\r\n");
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
        
        public String getFeedbackId(){
            String temps = "QWERTYUIOPASDFGHJKLZXCVBNMabcdefghijklmnopqrstuvwxyz1234567890";
            Random rnd = new Random();
            StringBuilder sb = new StringBuilder( 7 );
            for( int i = 0; i < 7; i++ ) 
                sb.append( temps.charAt( rnd.nextInt(temps.length()) ) );
            return sb.toString();           
        }
        
        public void writeInfor(String fname,String key, String value)throws Exception{
//            System.out.println("write file"); 
            prop.setProperty(key, value);
            prop.store(new FileOutputStream(fname), null);
        }
        public String readInfor(String fname, String key)throws Exception{
//            System.out.println("read file"); 
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
        public boolean checkNumber(String t){
            return t.matches("([0-9]+)");
        }        
        public static String getTime(long ses){
            long hour ;
            long minute;
            long second;         
            hour = ses/3600;
            ses = ses%3600;
            minute = ses/60;
            second = ses%60;
            String time = String.valueOf(hour)+":"+String.valueOf(minute)+":"+String.valueOf(second);      
            return time;
        }
    
        public static long sumTime(String begin, String end){
            ArrayList<String> list1 = new ArrayList<String>();
            ArrayList<String> list2 = new ArrayList<String>();
            StringTokenizer st;
            st = new StringTokenizer(begin,":");
            while(st.hasMoreTokens())
                list1.add(st.nextToken());
            st = new StringTokenizer(end,":");
            while(st.hasMoreTokens())
                list2.add(st.nextToken());  
            long t1 = mili(Long.parseLong(list1.get(0)),Long.parseLong(list1.get(1)),Long.parseLong(list1.get(2)));
            long t2 = mili(Long.parseLong(list2.get(0)),Long.parseLong(list2.get(1)),Long.parseLong(list2.get(2)));        
            return t1+t2;
        }     
        public static long divtime(String time2, String time1) throws ParseException{
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            long difference = date2.getTime() - date1.getTime(); 
            return difference/1000;
        }

        static long mili(long h, long m, long s){        
            return s+m*60+h*3600;
        }
    
//    static long divtime(String begin, String end) throws ParseException{
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//        Date date1 = format.parse(begin);
//        Date date2 = format.parse(end);
//        long difference = date2.getTime() - date1.getTime(); 
//        System.out.println(difference);
//        return difference/1000;
////        System.out.println(difference);
//    }    
    
        public static long sumTime2(String begin, String end) throws ParseException{
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(begin);
            Date date2 = format.parse(end);
            long difference = date2.getTime() + date1.getTime(); 
            System.out.println(difference/1000);
            return difference/1000;    
        }        

}

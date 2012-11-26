
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
public class caculatortime {
    caculatortime(){}
//    public static void main(String[] args) throws ParseException {
////        long times = 123;
////        long second;
////        long hour ;
////        long hour2;
////        long minute = 0;
////        long temp = 0;
////        hour = times/3600;
////        hour2 = times%3600;
////        System.out.println(hour);
////        System.out.println(hour2);        
////        minute = hour2/60;
////        second = hour2%60;
////        String time = String.valueOf(hour)+":"+String.valueOf(minute)+":"+String.valueOf(second);
////        System.out.println(time);
////        System.out.println(getTime(123));
//        
////        time(3600);
//        
//        String t1 ="07:58:02";
//        String t2 = "11:58:10"; 
//        String t3 ="17:00:02";
//        String t4 = "20:55:10"; 
//        long dtime1 = divtime(t1,t2);
//        String s1 = getTime(dtime1);        
//        System.out.println("ca 1 lam: \t"+s1);
//        long dtime2 = divtime(t3,t4);
//        String s2 = getTime(dtime2);
//        System.out.println("ca 2 lam: \t"+s2);   
//        
//        
//        long stime = sumTime(s1,s2);
//        System.out.println("tong thoi gian lam trong ngay\t"+getTime(stime));
//    }
    
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
    
    public static void time(long s){
        Time t = new Time(s);
        System.out.println(t.getHours());
        System.out.println(t.getMinutes());
        System.out.println(t.getSeconds());
        
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
//        System.out.println(difference);
    }    
}

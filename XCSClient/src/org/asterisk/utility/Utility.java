package org.asterisk.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {    
        Properties prop = new Properties();    
        String fileName = "infor.properties"; 
        String logFolder = "log";  
        String logFile = "client.log"; 
        String soundFolder = "data/sounds/";
        
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
            File root = new File(logFolder);
            root.mkdirs();
            File child = new File(root, logFile);
            child.createNewFile();            
            Writer output;
            output = new BufferedWriter(new FileWriter(child,true));
            output.append(getDatetimeNow() + "\t" +log+ "\r\n");
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
            ArrayList<String> list =  new ArrayList();
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
            prop.setProperty(key, value);
            prop.store(new FileOutputStream(fname), null);
        }
        public String readInfor(String fname, String key)throws Exception{
            String value = "";
            prop.load(new FileInputStream(fname));
            value = prop.getProperty(key);
            return value;
        }
        public boolean writeConfig(String fname,String key, String value){
            try{
                File file = new File(fname);
                if(file.exists()){
                    FileInputStream input = new FileInputStream(file);
                    byte [] readbyte = new byte[input.available()];                
                    input.read(readbyte);                                                                            
                    String jsonString = new String(readbyte,"UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    jsonObject.remove(key);
                    jsonObject.put(key, value);                    
                    String out = jsonObject.toString();                                        
                    FileOutputStream output = new FileOutputStream(file);
                    output.write(out.getBytes("UTF-8"));  
                    return true;
                }
            }catch(IOException | JSONException ex){
            }
            return false;
        }
        public String readConfig(String fname, String key)throws Exception{
            try{
                File file = new File(fname);
                if(file.exists()){
                    FileInputStream input = new FileInputStream(file);
                    byte [] readbyte = new byte[input.available()];                
                    input.read(readbyte);                                                                            
                    String jsonString = new String(readbyte,"UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String value = jsonObject.getString(key);
                    return value;  
                }                                                                              
            }catch(IOException | JSONException ex){
            }
            return null;
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
        
        public void playSounds(){
            try{
                File soundFile = new File(soundFolder+"snd_keypress.wav");
                if(soundFile != null && soundFile.canRead()){
                    AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audio);
                    clip.start();                                  
                }
            }catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
                System.out.println("Can't play sound");
            }
        }
        public void playSounds(String path){
            try{
                File soundFile = new File(path);
                if(soundFile != null && soundFile.canRead()){
                    AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audio);
                    clip.start();                
                }
            }catch(Exception e){
                System.out.println("Can't play sound");
            }            
        }        

}

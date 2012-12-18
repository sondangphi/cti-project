package org.asterisk.model;

public class IncomingCallObject {
	
//	String callerId;
	String callerName;
	String callerNumber;
	String desName;
	String desNumber;
//	String state;
//	String srcUnique;
//        String desUnique;
//	String context;
	String talkTime;
//        String stateCall;
        String session;
//	boolean isAvail;
        
        public IncomingCallObject(){}
        public IncomingCallObject(String cNum, String cName){            
            callerName = cNum;
            callerNumber = cName;          
        }
        public IncomingCallObject(String cNum, String cName, String dName, String dNumber){
            callerName = cName;
            callerNumber = cNum;                    
            desName = dName;
            desNumber = dNumber;
        }                
         
        public String getcallerName(){return callerName;}
        public void setcallerName(String t){callerName = t;}
        
        public String getcallerNumber(){return callerNumber;}
        public void setcallerNumber(String t){callerNumber = t;}         
         
        public String getdesName(){return desName ;}
        public void setdesName(String t){desName = t;}
        
        public String getdesNum(){return desNumber ;}
        public void setdesNum(String t){desNumber = t;}        
        
//        public String getsrcUniqueid(){return srcUnique;}
//        public void setsrcUniqueid(String t){srcUnique = t;}
        
//        public String getdesUniqueid(){return desUnique;}
//        public void setdesUniqueid(String t){desUnique = t;}        
         
//        public String getcontext(){return context;}
//        public void setcontext(String t){context = t;}
        
        public String gettalkTime(){return talkTime;}
        public void settalkTime(String t){talkTime = t;}
         
//        public String getstateCall(){return stateCall;}
//        public void setstateCall(String t){stateCall = t;}
        
        public String getsession(){return session;}
        public void setsession(String t){session = t;}
        
//        public boolean getisAvail(){return isAvail;}
//        public void setisAvail(boolean b){isAvail = b;}
         
//        public String getCallerName(){return callerName;}
//        public void setcallerName(String id){callerName = id;}        
	

}

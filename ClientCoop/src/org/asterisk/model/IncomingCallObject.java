package org.asterisk.model;

public class IncomingCallObject {
	
	String callerId;
	String callerName;
	String callerNumber;
	String srcChannel;
	String desChannel;
	String state;
	String srcUnique;
        String desUnique;
	String context;
	String appData;
        String stateCall;
	boolean isAvail;
        
        public String getcallerId(){return callerId;}
        public void setcallerId(String t){callerId = t;}
         
        public String getcallerName(){return callerName;}
        public void setcallerName(String t){callerName = t;}
        
        public String getcallerNumber(){return callerNumber;}
        public void setcallerNumber(String t){callerNumber = t;}
         
        public String getsrcChannel(){return srcChannel;}
        public void setsrcChannel(String id){srcChannel = id;}  
        
        public String getdesChannel(){return desChannel;}
        public void setdesChannel(String t){desChannel = t;}
         
        public String getstate(){return state;}
        public void setstate(String t){state = t;}
        
        public String getsrcUniqueid(){return srcUnique;}
        public void setsrcUniqueid(String t){srcUnique = t;}
        
        public String getdesUniqueid(){return desUnique;}
        public void setdesUniqueid(String t){desUnique = t;}        
         
        public String getcontext(){return context;}
        public void setcontext(String t){context = t;}
        
        public String getappData(){return appData;}
        public void setappData(String t){appData = t;}
         
        public String getstateCall(){return stateCall;}
        public void setstateCall(String t){stateCall = t;}
        
        public boolean getisAvail(){return isAvail;}
        public void setisAvail(boolean b){isAvail = b;}
         
//        public String getCallerName(){return callerName;}
//        public void setcallerName(String id){callerName = id;}        
	

}

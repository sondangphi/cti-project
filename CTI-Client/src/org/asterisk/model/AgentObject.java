package org.asterisk.model;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class AgentObject implements Serializable{
	
	Socket socket;
	String agentId;
        String agentName;
        String queueId;
        String queueName;
	String pass;
	String iface;	
	int penalty;
        String role;
        String session;
        
	
	public AgentObject() {
		
	}
	
	public AgentObject(Socket client,String user, String pwd, String inface, String queuename){
		socket = client;
		agentId = user;
		pass = pwd;
		iface = inface;
		queueId = queuename;
	}
	public AgentObject(Socket client,String user, String pwd, String inface, String queuename, int pen){
		socket = client;
		agentId = user;
		pass = pwd;
		iface = inface;
		queueId = queuename;
                penalty = pen;
	}
	public Socket getSocket(){
            return socket;
	}
	
	public void setSocket(Socket s){
		socket = s;
	}
	public String getAgentId(){
		return agentId;
	}
	
	public void setAgentId(String a){
		agentId = a;
	}
	public String getAgentName(){
		return agentName;
	}
	
	public void setAgentName(String name){
		agentName = name;
	}        
        
	public String getPass(){
		return pass;
	}
	
	public void setPass(String p){
		pass = p;
	}
	public String getInterface(){
		return iface;
	}
	
	public void setInterface(String inface){
		iface = inface;
	}

	public String getQueueId(){
		return queueId;
	}
	
	public void setQueueId(String q){
		queueId  = q;
	}
	
	public String getQueueName(){
		return queueName;
	}
	
	public void setQueueName(String name){
		queueName  = name;
	}        
        
	public int getPenalty(){
		return penalty;
	}
	public void setPenalty(int pen){
		penalty = pen;
	}
        
        public String getRole(){
		return role;
	}
	public void setRole(String rl){
		role = rl;
	}
        public String getSesion(){
		return session;
	}
	public void setSession(String ses){
		session = ses;
	}        
	public void closeSocket(Socket s) throws IOException{
		socket = s;
		if(!socket.isClosed())
			socket.close();
	}	
}

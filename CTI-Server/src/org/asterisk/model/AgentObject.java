package org.asterisk.model;

import java.io.IOException;
import java.net.Socket;

public class AgentObject {
	
	Socket socket;
	String agent;
	String pass;
	String iface;
	String queue;
	int penalty;
	
	public AgentObject() {
		
	}
	
	public AgentObject(Socket client,String user, String pwd, String inface, String queuename){
		socket = client;
		agent = user;
		pass = pwd;
		iface = inface;
		queue = queuename;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public void setSocket(Socket s){
		socket = s;
	}
	public String getAgent(){
		return agent;
	}
	
	public void setAgent(String a){
		agent = a;
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

	public String getQueue(){
		return queue;
	}
	
	public void setQueue(String q){
		queue  = q;
	}
	
	public int getPenalty(){
		return penalty;
	}
	public void setPenalty(int pen){
		penalty = pen;
	}
	public void closeSocket(Socket s) throws IOException{
		socket = s;
		if(!socket.isClosed())
			socket.close();
	}	
}

package org.asterisk.model;

import java.io.IOException;
import java.net.Socket;

public class AgentObject {
	
	Socket socket;
	String agent;
	String pass;
	String extension;
	String queue;
	int penalty;
	
	public AgentObject() {
		// TODO Auto-generated constructor stub
	}
	
	public AgentObject(Socket client,String user, String pwd, String exten, String queuename){
		socket = client;
		agent = user;
		pass = pwd;
		extension = exten;
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
	public String getExtension(){
		return extension;
	}
	
	public void setExtension(String e){
		extension = e;
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

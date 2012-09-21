package org.asterisk.utility;


import java.io.IOException;
import java.net.Socket;

public class ConnectServer {

	/**
	 * @param args
	 */

	public ConnectServer(Socket client, String address, int port){
		try{
			client = new Socket(address, port);			
			System.out.println("connect to server: "+client.getInetAddress().getHostAddress().toString());
		}catch(Exception ex){
			System.out.println("Cannot connect to server. Plz check again->"+ex.toString());
		}
	}
	
	public void closeSocket(Socket client) throws IOException{
		client.close();
	}

	
}

package org.asterisk.utility;

import java.io.BufferedReader;
import java.net.Socket;

public class receiveThread implements Runnable{

	/**
	 * @param args
	 */
	Socket clientSocket;
	boolean running = true;
	BufferedReader infromServer;
	
	receiveThread(Socket client) {
	    clientSocket = client;
	}
	
	@Override
	public void run() {
		try{
		}catch(Exception ex){
		}
		
	}	
	
	
}

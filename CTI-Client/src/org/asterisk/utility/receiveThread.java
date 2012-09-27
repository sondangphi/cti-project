package org.asterisk.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
	
	public void run() {
		try{
		}catch(Exception ex){
		}
		
	}	
	
	
}

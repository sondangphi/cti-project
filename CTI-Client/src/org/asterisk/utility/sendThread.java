package org.asterisk.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class sendThread extends Thread{

	/**
	 * @param args
	 */
	int i=0;
	Socket clientSocket;
	boolean running = true;
	BufferedReader inputLine ;
	String sen;
	sendThread(Socket client) {
	    clientSocket = client;
	}
	@Override
	public void run() {
		
		try{
			//data gui den server
			DataOutputStream outToserver = new DataOutputStream(clientSocket.getOutputStream());
			PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("begin send data to server ");
			while(running && (clientSocket.isClosed()==false)){
				System.out.print("input: ");
				sen = inputLine.readLine();
				if(sen.equalsIgnoreCase("quit"))
					running = true;
				out.println(sen);
				out.flush();
			}							
			out.close();
			clientSocket.close();
			System.out.println("stop send data to server ");
		}catch(Exception ex){
			System.out.println("Ex of send thread is : "+ex.toString());
		}

	}

}

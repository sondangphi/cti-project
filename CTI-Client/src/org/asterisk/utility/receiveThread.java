package org.asterisk.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class receiveThread extends Thread{

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
			String modified = null;
			infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println("begin receive data from server ");
			while(running && (clientSocket.isClosed()==false)){				
				modified = infromServer.readLine();
		        StringTokenizer st = new StringTokenizer(modified,"@");
		        ArrayList<String> list = new ArrayList<String>();
		        while(st.hasMoreTokens()){
		        	list.add(st.nextToken());
		        }
				int data = Integer.parseInt(list.get(0));
				switch(data){
		           case 100:  System.out.println("Log in");
                   			break;
		           case 101:  System.out.println("select");
                   			break;
		           case 102:  System.out.println("insert");
                   			break;
		           case 103:  System.out.println("update");
                   			break;
		           case 111:  System.out.println("logout");
          		 		running = false;
          		 		break;
		           default: System.out.println("invalid");
                   		break;
				}
				if(clientSocket.isClosed())
					running = false;
				else
					System.out.println("Server: "+modified);
			}			
			if(clientSocket.isClosed()==false)							
				clientSocket.close();			
			infromServer.close();
			System.out.println("stop receive data from server & close server socket");
		}catch(Exception ex){
			System.out.println("Ex of receive thread is : "+ex.toString());
			try {
				infromServer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}	
	
	
}

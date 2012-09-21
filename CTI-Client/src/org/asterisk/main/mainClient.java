package org.asterisk.main;

import java.io.IOException;
import java.net.Socket;

import org.asterisk.utility.*;

public class mainClient {
	
	static Socket clientSocket;
	static String address;
	static int port;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		try {
//			new ConnectServer(clientSocket, address, port);
////			String login = "100@username@password@extension@queue";
//			String login = "100@username@password@extension@queue";
//			String result = sendData(login);
			System.out.println("client running");
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

	}
	
	public static String sendData(String data){
		String result = "";
		
		return result;
	}

}

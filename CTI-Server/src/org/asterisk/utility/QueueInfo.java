package org.asterisk.utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.asterisk.model.QueueObject;

public class QueueInfo implements Runnable{

	ServerSocket server;
	Socket client;
	Thread thread;
	int port;
	String queueid;
	String queuename;
	boolean running = true;	
	public QueueInfo(int p) {
		thread = new Thread(this);
		thread.start();
		port = p;		
	}		
	@Override
	public void run() {
		try {			
			server = new ServerSocket(port);
			while(running){
				client = server.accept();
				OutputStream os = client.getOutputStream();  
				ObjectOutputStream oos = new ObjectOutputStream(os);  
				//add data to queueid, queuename
				QueueObject qObject = new QueueObject(queueid,queuename);  
				oos.writeObject(qObject);  
				oos.flush();
				os.close();
				oos.close();
				client.close();
			}
		} catch (IOException e) {		
		}
	}

}

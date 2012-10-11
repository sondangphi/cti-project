package org.asterisk.utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.asterisk.model.QueueObject;

public class QueueInfo implements Runnable{

	private Socket client;
	private Thread thread;
	private boolean running = true;	
        private ArrayList<QueueObject> list;
        private Managerdb db;
        private String database = "asterisk";
	public QueueInfo(Socket clientS) {
            thread = new Thread(this);
            thread.start();
            client = clientS;		
	}        
        public QueueInfo() {
	
	}
	@Override
	public void run() {
            try {			
                db = new Managerdb(database);
                db.connect();
                while(running){
                    list = db.listQueue();
                    OutputStream os = null;  
                    ObjectOutputStream oos = null; 
                    os = client.getOutputStream();  
                    oos = new ObjectOutputStream(os);   
                    oos.writeObject(list);  
                    oos.flush();
                    running = false;
                    closeConnect();
                }
            } catch (Exception e) {	
            }
	}
        
        private void closeConnect()throws Exception{
            if(client != null)
                client.close();
            if(thread != null)
                thread.interrupt();
            db.close();
        }

}

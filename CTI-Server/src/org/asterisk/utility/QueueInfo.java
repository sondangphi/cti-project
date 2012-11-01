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
        private ArrayList<QueueObject> list;
        private Managerdb mdb_agent;
	public QueueInfo(Socket clientS, Managerdb dbase) {
            mdb_agent = dbase;
            client = clientS;
            thread = new Thread(this);
            thread.start();            		
	}   
	public QueueInfo(Socket clientS) {
            client = clientS;
            thread = new Thread(this);
            thread.start();            		
	}        
        public QueueInfo() {
	
	}
	@Override
	public void run() {
            try {			
                list = mdb_agent.listQueue();
                OutputStream os = null;  
                ObjectOutputStream oos = null; 
                os = client.getOutputStream();  
                oos = new ObjectOutputStream(os);   
                oos.writeObject(list);  
                oos.flush();
                closeConnect();
            } catch (Exception e) {	
            }
	}
        
        private void closeConnect()throws Exception{
            if(client != null)
                client.close();
            if(thread != null)
                thread.interrupt();
        }

}

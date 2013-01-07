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
        private OutputStream os;  
        private ObjectOutputStream oos; 
	public QueueInfo(Socket clientS, Managerdb dbase) {
            mdb_agent = dbase;
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
                os = client.getOutputStream();  
                oos = new ObjectOutputStream(os);   
                oos.writeObject(list);  
                oos.flush();
                os.close();
                oos.close();
                client.close();
                thread.interrupt();
            } catch (Exception e) {	
            }
	}
}

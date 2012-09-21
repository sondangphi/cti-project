package org.asterisk.utility;

import java.io.IOException;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.QueueAddAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;

public class ConnectAsterisk implements Runnable, ManagerEventListener{
        private ManagerConnection managerConnection;
        private Thread t;
        private Utility uti = new Utility();
        public ConnectAsterisk(){
        	
        }
        
        public ConnectAsterisk(String address, String username, String password)throws Exception {
        	
        	ManagerConnectionFactory factory = new ManagerConnectionFactory(address, username, password);
        	this.managerConnection = factory.createManagerConnection();
        	t = new Thread(this);
        	t.start();
        	connect();
        	addManagerEventListener(this);
        	sendAction(new StatusAction());
        }

        public void connect() throws Exception {
                managerConnection.login();
        }

        public void disconnect() {
                managerConnection.logoff();
        }

        public ManagerConnection getManagerConnection() {
                return managerConnection;
        }

        public void addManagerEventListener(ManagerEventListener listener) {
                managerConnection.addEventListener(listener);
        }

        public void removeManagerEventListener(ManagerEventListener listener) {
                managerConnection.removeEventListener(listener);
        }

        public String sendAction(ManagerAction action) throws Exception {
                ManagerResponse response = managerConnection.sendAction(action);
                return response.getResponse().toString();
        }
        
        public String send(QueueAddAction action)throws Exception{
        	ManagerResponse originateResponse = managerConnection.sendAction(action,20000);
        	return originateResponse.getResponse();
        }
        

		@Override
		public void onManagerEvent(ManagerEvent event) {
			// TODO Auto-generated method stub
			try {
				System.out.println("event:  "+event);
				uti.writeLog(event.toString()+"\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
//			while(t!=null){
//				System.out.println("t!=null");
//			}
		}

}

package org.asterisk.utility;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.asterisk.main.*;

public class Agent implements Runnable{

	/**
	 * @param args
	 */
	static boolean running = true;
	static boolean connected = true;
	static Thread mainThread;
	static Socket clientSocket;
	static BufferedReader infromServer;
	static PrintWriter outtoServer;
        private static LoginForm loginform;
        private static MainForm mainForm;
	int data;
	
	public Agent(){
		
	}
        public Agent(Socket soc){
            clientSocket = soc;
            mainThread = new Thread(this);
            mainThread.start();
	}
            public Agent(Socket soc, LoginForm login){
            clientSocket = soc;
            loginform = login;
            mainThread = new Thread(this);
            mainThread.start();
	}

	public Agent(String address, int port, String cmd){
            try{
                clientSocket = new Socket(address, port);	
                mainThread = new Thread(this);
                mainThread.start();
                sendtoServer(cmd);
            }catch(Exception ex){

            }
	}	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
                    String command = null;
                    CODE code;
                    while(connected && clientSocket.isConnected()){
                        infromServer =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        command = infromServer.readLine();
                        ArrayList<String> cmdList = getList(command);							
                        code = CODE.valueOf(cmdList.get(0).toUpperCase());
                        switch(code){
	            	case LOGINSUCC: //result LOGIN SUCCESS
                            System.out.println("LOGIN SUCCESS");
                            loginform.lb_status.setText("LOGIN SUCCESS");
                            loginform.setVisible(false);
                            mainForm = new MainForm();
                            mainForm.setVisible(true);
	            	break;
	            	case LOGINFAIL: //result LOGIN FAIL
                            System.out.println("LOGIN FAIL");
                            loginform.lb_status.setText("LOGIN FAIL");
                            connected = false;
                            closeConnect();
		            break;
	            	case LOGOUTSUCC: //result LOGOUT SUCCESS
                            connected = false;
                            closeConnect();
                            mainForm.setVisible(false);
                            LoginForm lgform = new LoginForm();
//                            lgform.
                            lgform.setVisible(true);                            
                            System.out.println("LOGOUT SUCCESS");
	            	break;
	            	case LOGOUTFAIL: //result LOGOUT FAIL
	            		System.out.println("LOGOUT FAIL");
//	            		mainform.lblStatus.setText("LOGIOUT FAIL");
		            break;	            	
	            	case PAUSESUCC: //result PAUSE
	            	break;
	            	case PAUSEFAIL: //result PAUSE
		            break;
	            	case UNPAUSESUCC: //result UNPAUSE
	            	break;
	            	case UNPAUSEFAIL: //result UNPAUSE
		            break;
	            	case TRANSSUCC: //result TRANSFER	            			
	            	break;
	            	case TRANSFAIL: //result TRANSFER	            			
		            break;
	            	case HOLDSUCC: //result HOLD
	            	break;
	            	case HOLDFAIL: //result HOLD
		            break;
	            	case RESULT: //result ?
	            	break;
	            	case RINGING: //EVENT RINGING
	            		System.out.println("RINGING");
//	            		mainform.lblStatus.setText("RINGING");
	            	break;
	            	case AVAIL: //result 	            			
	            	break;
	            	case BUSY: 
	            	break;
	            	case READY: 
	            	break;
	            	case HANGUP: 
	            		System.out.println("HANGUP");
//	            		mainform.lblStatus.setText("HANGUP");
		            break;
	            	case UP: //EVENT ANSWER CALL	     
	            		System.out.println("ANSWER");
//	            		mainform.lblStatus.setText("ANSWER");
	            	break;
		            default: 
	                break;
				}
			}
		} catch (IOException e){
			
		} catch (Exception e) {

		}
	}

        //send request to server - string
	public static void sendtoServer(String t) throws IOException{
            outtoServer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            outtoServer.println(t);
            outtoServer.flush();
	}
	public static ArrayList<String> getList(String cmd){
            ArrayList<String> list =  new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(cmd,"@");
            while(st.hasMoreTokens()){
        	list.add(st.nextToken());
            }
            return list;
	}
	
        //close Socket & Thread for client
	public static void closeConnect()throws Exception{
            System.out.println("start close session");
            if(mainThread!=null){
                mainThread.interrupt();
                mainThread = null;
                System.out.println("close thread");
            }
            if(!clientSocket.isClosed()){
                clientSocket.close();
                outtoServer.close();
                infromServer.close();
                System.out.println("close socket");
            }
            System.out.println("finish close session");
	}
	public enum CODE{
		LOGINSUCC, LOGINFAIL, LOGOUTSUCC, LOGOUTFAIL,		
		PAUSESUCC, PAUSEFAIL,
		HOLDSUCC, HOLDFAIL,
		TRANSSUCC, TRANSFAIL,
		UNPAUSESUCC, UNPAUSEFAIL,
		DIAL, RINGING, AVAIL, BUSY, READY, RESULT, UP,HANGUP
	}
}

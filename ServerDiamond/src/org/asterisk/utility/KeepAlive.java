/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

/**
 *KEEP CONNECT TO SERVER
 *
 */
public class KeepAlive implements Runnable{

    private ManagerAgent agent;
    public Thread thread;
    public int COUNT = 0;
    public boolean run = true;
    public KeepAlive(ManagerAgent a) {
        agent = a;
        thread = new Thread(this);
        thread.start();
    }    

    @Override
    public void run() {
        try{
            while(run){  
                try{
                    Thread.sleep(10000);       
                    if(COUNT >2){//logout and stop keepalive
                        agent.agentLogout();
                        run = false;
                    }else if(agent.clientSocket.isClosed()){//return if socket is closed 
                        run = false;   
                    }else{
                        COUNT ++; 
                    }                   
                }catch(Exception ex){
                    System.out.println("keepalive  "+ex);
                }               
            }    
            System.out.println("finish keepalive  ");
        }catch(Exception e){
            
        }
    }
    
}

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
    private Thread thread;
    public int COUNT = 0;
    public KeepAlive(ManagerAgent a) {
        agent = a;
        thread = new Thread(this);
        thread.start();
    }    

    @Override
    public void run() {
       try{
           Thread.sleep(5000);
           while(true){               
               Thread.sleep(10000);       
               if(COUNT >= 3){//logout and stop keepalive
                   if(agent.agentLogout())
                       return;
               }               
               if(agent.clientSocket.isClosed())//return if socket is closed
                   return;         
               agent.sendToAgent("PING");
               COUNT ++;
           }
       }catch(Exception e){
       }
    }
    
}

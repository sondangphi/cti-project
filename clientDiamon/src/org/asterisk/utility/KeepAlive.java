/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.net.ServerSocket;

/**
 *
 * @author leehoa
 */
public class KeepAlive implements Runnable{
    private Agent agent;
    private Thread thread;
    public int COUNT = 0;
    public KeepAlive(Agent a) {
        agent = a;
        thread = new Thread(this);
        thread.start();
    }    

    @Override
    public void run() {
       try{
           while(true){
               Thread.sleep(10000);
               if(COUNT >= 4){
                   if(agent.agentLogout())
                       return;
               }               
               if(agent.clientSocket.isClosed())
                   return;      
               agent.sendtoServer("222");
               COUNT ++;
               System.out.println("count is : "+COUNT);
                       
           }
       }catch(Exception e){
       }
    }
    
}

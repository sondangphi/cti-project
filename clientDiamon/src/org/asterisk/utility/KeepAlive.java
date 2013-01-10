/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

/**
 *
 * @author leehoa
 */
public class KeepAlive implements Runnable{

    Agent agent;
    Thread thread;
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
               agent.sendtoServer("222");
               if(agent.clientSocket.isClosed())
                   return;                              
           }
       }catch(Exception e){
       }
    }
    
}

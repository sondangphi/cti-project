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

    ManagerAgent agent;
    Thread thread;
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
               agent.sendToAgent("PING");
               if(agent.clientSocket.isClosed())
                   return;               
           }
       }catch(Exception e){
       }
    }
    
}

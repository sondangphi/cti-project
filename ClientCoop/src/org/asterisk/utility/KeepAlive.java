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

    public KeepAlive(Agent a) {
        agent = a;
        thread = new Thread(this);
        thread.start();
    }    

    public void stop() {
        if (thread != null) {
            if (thread.isAlive()) {
                thread.stop();
            }
        }
    }
    
    @Override
    public void run() {
       try{
           while(agent.clientSocket.isConnected() && agent.running){
               Thread.sleep(5000);
               agent.sendtoServer("222");
           }
           if(thread.isAlive())
               thread.stop();           
       }catch(Exception e){}
    }
    
}

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

    public void interrupt() {
        if (thread != null) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
    }
    
    @Override
    public void run() {
       try{
           agent.sendtoServer("222");
           Thread.sleep(2000);
           while(true){               
               agent.sendtoServer("222");
               Thread.sleep(5000);
           }
       }catch(Exception e){}
    }
    
}

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

    public KeepAlive(ManagerAgent a) {
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
            while (true) {
                Thread.sleep(5000);
                agent.sendToAgent("PING");
            }
        }catch(Exception e) {}
    }
    
}

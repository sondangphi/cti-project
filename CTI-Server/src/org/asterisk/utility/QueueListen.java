/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author leehoa
 */
public class QueueListen implements Runnable{
    
    private static ServerSocket server;
    private static int port;
    Thread thread;
    public QueueListen(int p) throws IOException{
        port = p;
        server = new ServerSocket(port);
        thread = new Thread(this);
        thread.start();
    }
    public QueueListen() throws IOException{
        thread = new Thread(this);
        thread.start();
    }


    public void run() {
        try{
            QueueInfo qInfor = null;
            while(true){
                System.out.println("start queue_listen");
                Socket client = server.accept();                
                qInfor = new QueueInfo(client);
                System.out.println("send list queue for client");
            }
        }catch(Exception e){
        }            
    }
    
}

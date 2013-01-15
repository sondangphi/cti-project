/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Frame;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import org.asterisk.*;
import org.asterisk.utility.Agent;

/**
 *
 * @author leehoa
 */
public class MainClass {
    
    private static int virtualPort = 1234;
    private static ServerSocket virtualServer = null;
    private static Thread thread;    
    public static void main(String[] args) throws IOException{
        try{
            if(virtualServer == null){
                virtualServer = new ServerSocket(virtualPort);
                new LoginForm().setVisible(true);
            }            
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while(true){                            
                            virtualServer.accept();
                            if(Agent.mainForm != null){
                                Agent.mainForm.setAlwaysOnTop(true);
                                Agent.mainForm.setVisible(true);
                                Agent.mainForm.setFocusable(true);
                                Agent.mainForm.setFocusableWindowState(true);
                                Agent.mainForm.requestFocus();                                 
                                Agent.mainForm.setAlwaysOnTop(false);
                                System.out.println("warming!!!\r\n");
                            }                                                            
                        }                        
                    }catch(Exception e){}
                    
                }
            });
            thread.start();            
        }catch(Exception ex){
            System.out.println("already open\r\n"+ex);
            Socket vclient = new Socket();
            vclient.connect(new InetSocketAddress("127.0.0.1", virtualPort));
            vclient.close();
            System.exit(0);
        }
    }
    
    
}



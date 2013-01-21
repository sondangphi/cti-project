/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

/**
 *
 * @author leehoa
 */
public class QueueListen implements Runnable {

    private ServerSocket qserver;
    private int port;
    private Thread thread;
    private Managerdb mdb_queue;

    public QueueListen(int p) throws IOException {
        port = p;
        thread = new Thread(this);
        thread.start();
    }

    public QueueListen(int p, Managerdb mdb) throws IOException {
        mdb_queue = mdb;
        port = p;
        thread = new Thread(this);
        thread.start();
    }

    public QueueListen() throws IOException {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            qserver = new ServerSocket(port);
            while (true) {
                System.out.println("start queue_listen");
                Socket client = qserver.accept();
                new QueueInfo(client, mdb_queue);
                System.out.println("send list queue for client");
            }
        } catch (Exception e) {
            System.out.println("QueueListen thread exception\r\n" + e);
        }
    }
}

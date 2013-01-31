package org.asterisk.main;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import javax.net.ssl.SSLSocket;
import javax.swing.ImageIcon;
import org.asterisk.model.AgentObject;
import org.asterisk.model.QueueObject;
import org.asterisk.utility.Agent;
import org.asterisk.utility.Utility;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
public class LoginForm extends javax.swing.JFrame {
    	
	private String host = "localhost";
        public AgentObject agentObject;
	private int qport = 33333;
	private int aport = 22222;
	private ArrayList <QueueObject>  listQueue;	
        private String role ="1";
        
        private Utility uti;
        private String agentId = "";
        private String pass = "";
        private String iface = "";
        private String queueId = "";
        public String cmd;
        public String queueName = "";
        public Agent agent;
        private String filename = "infor.properties";
        private String Mysql_server = "172.168.10.208";      
        private String Mysql_dbname = "cti_database";
	private String Mysql_user = "cti";
	private String Mysql_pwd  = "123456"; 
        
        private static int virtualPort = 1234;
        private static ServerSocket virtualServer;
        private static Thread thr;
    /**
     * Creates new form LoginForm
     */
    ConfigForm configform;
    public LoginForm() {        
        initComponents();
        Image image = Toolkit.getDefaultToolkit().getImage("images/icon_login.png");
        this.setIconImage(image);
        try{            
            uti = new Utility();		
            File f = new File(filename);
            if(!f.exists())
                f.createNewFile();            
            FileInputStream fis = new FileInputStream(f);  
            if(fis.available() == 0 ){
                System.out.println("file is empty"); 
                uti.writeInfor(filename, "host", host);
                uti.writeInfor(filename, "aport", Integer.toString(aport));
                uti.writeInfor(filename, "qport", Integer.toString(qport));
                
                uti.writeInfor(filename, "MySql_database", Mysql_dbname);
                uti.writeInfor(filename, "MySql_server", Mysql_server);
                uti.writeInfor(filename, "MySql_user", Mysql_user);
                uti.writeInfor(filename, "MySql_pwd", Mysql_pwd);
                System.out.println("write file"); 
            }            
            System.out.println("read file configuration"); 
            host = uti.readInfor(filename, "host");
            aport = Integer.parseInt(uti.readInfor(filename, "aport"));
            qport = Integer.parseInt(uti.readInfor(filename, "qport"));  
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            listQueue = new ArrayList<QueueObject>();
            getListQueue();      
            queueId = listQueue.get(cb_queue.getSelectedIndex()).getQueueId();
            if(queueId == null){
                lb_notify_queue.setText("(*)");
                lb_status.setText("Check network connection");
            }                
            else 
                lb_notify_queue.setText("");
        }catch(Exception e){
        }
        setLocationRelativeTo(null);
        Color LightSkyBlue2 = new Color(164, 211, 238);
        Color white = new Color(255,255,255);    
        Color yellow2 = new Color(238, 238, 0);        
        Color Khaki1 = new Color( 255, 246, 143);                           
        this.getContentPane().setBackground(white);
        panel_1.setBackground(white);
        mn_main.setBackground(white);
        submn_config.setBackground(white);
        submn_quit.setBackground(white);
        cb_queue.setBackground(Khaki1);
        tx_agent.setBackground(Khaki1);
        tx_iface.setBackground(Khaki1);
        pwd.setBackground(Khaki1);        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        panel_1 = new javax.swing.JPanel();
        btn_login = new javax.swing.JButton();
        lb_agent = new javax.swing.JLabel();
        lb_pwd = new javax.swing.JLabel();
        lb_iface = new javax.swing.JLabel();
        lb_queue = new javax.swing.JLabel();
        tx_agent = new javax.swing.JTextField();
        tx_iface = new javax.swing.JTextField();
        cb_queue = new javax.swing.JComboBox();
        pwd = new javax.swing.JPasswordField();
        btn_clear = new javax.swing.JButton();
        lb_notify_iface = new javax.swing.JLabel();
        lb_notify_pwd = new javax.swing.JLabel();
        lb_notify_agent = new javax.swing.JLabel();
        lb_notify_queue = new javax.swing.JLabel();
        lb_pic = new javax.swing.JLabel();
        lb_status = new javax.swing.JLabel();
        mn_main = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        submn_config = new javax.swing.JMenuItem();
        submn_quit = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Agent Desktop Login");
        setFont(new java.awt.Font(".Vn3DH", 1, 10)); // NOI18N
        setForeground(new java.awt.Color(121, 163, 240));
        setName("main_frame"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        panel_1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Desktop"));

        btn_login.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_login.setText("Login");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });
        btn_login.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_loginKeyPressed(evt);
            }
        });

        lb_agent.setBackground(new java.awt.Color(204, 255, 204));
        lb_agent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_agent.setText("AgentID");

        lb_pwd.setBackground(new java.awt.Color(204, 255, 204));
        lb_pwd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_pwd.setText("Password");

        lb_iface.setBackground(new java.awt.Color(204, 255, 204));
        lb_iface.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_iface.setText("Extension");

        lb_queue.setBackground(new java.awt.Color(204, 255, 204));
        lb_queue.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_queue.setText("Queue");

        tx_agent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tx_agent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tx_agentKeyPressed(evt);
            }
        });

        tx_iface.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tx_iface.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tx_ifaceKeyPressed(evt);
            }
        });

        cb_queue.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cb_queueKeyPressed(evt);
            }
        });

        pwd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pwd.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pwd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdKeyPressed(evt);
            }
        });

        btn_clear.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });
        btn_clear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_clearKeyPressed(evt);
            }
        });

        lb_notify_iface.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_iface.setForeground(new java.awt.Color(255, 0, 0));

        lb_notify_pwd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_pwd.setForeground(new java.awt.Color(255, 0, 0));

        lb_notify_agent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_agent.setForeground(new java.awt.Color(255, 0, 0));

        lb_notify_queue.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_queue.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout panel_1Layout = new javax.swing.GroupLayout(panel_1);
        panel_1.setLayout(panel_1Layout);
        panel_1Layout.setHorizontalGroup(
            panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_iface, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_pwd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_agent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_1Layout.createSequentialGroup()
                        .addComponent(btn_login)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tx_agent)
                    .addComponent(tx_iface)
                    .addComponent(cb_queue, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_notify_iface, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lb_agent, lb_iface, lb_pwd, lb_queue});

        panel_1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cb_queue, pwd, tx_agent, tx_iface});

        panel_1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_clear, btn_login});

        panel_1Layout.setVerticalGroup(
            panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_agent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tx_iface, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_iface, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_iface, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lb_agent, lb_iface, lb_pwd, lb_queue});

        panel_1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cb_queue, pwd, tx_agent, tx_iface});

        lb_pic.setBackground(new java.awt.Color(255, 0, 0));
        lb_pic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/stock_lock.png"))); // NOI18N
        lb_pic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lb_picMouseClicked(evt);
            }
        });

        lb_status.setForeground(new java.awt.Color(255, 0, 0));

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        submn_config.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        submn_config.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submn_config.setText("Configuration");
        submn_config.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submn_configActionPerformed(evt);
            }
        });
        jMenu1.add(submn_config);

        submn_quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        submn_quit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submn_quit.setText("Exit");
        submn_quit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submn_quitMouseClicked(evt);
            }
        });
        submn_quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submn_quitActionPerformed(evt);
            }
        });
        jMenu1.add(submn_quit);

        mn_main.add(jMenu1);

        setJMenuBar(mn_main);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lb_pic, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_pic, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // TODO add your handling code here:
        tx_agent.setText("");
        tx_iface.setText("");
        pwd.setText("");                
    }//GEN-LAST:event_btn_clearActionPerformed

    void printinfor(){
        System.out.println("getAgentId "+agentObject.getAgentId());
        System.out.println("getAgentName "+agentObject.getAgentName());
        System.out.println("getInterface "+agentObject.getInterface());
        System.out.println("getPass "+agentObject.getPass());
        System.out.println("getQueueId "+agentObject.getQueueId());
        System.out.println("getQueueName "+agentObject.getQueueName());
        System.out.println("getRole "+agentObject.getRole());
        System.out.println("getSesion "+agentObject.getSesion());
        System.out.println("getPenalty "+agentObject.getPenalty());            
    }
    
    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        // TODO add your handling code here:   
        agentObject = new AgentObject();
        char [] p = pwd.getPassword();
        pass     = new String(p);
        agentId = tx_agent.getText();        
        iface  = tx_iface.getText();
        queueId = listQueue.get(cb_queue.getSelectedIndex()).getQueueId();
        queueName = listQueue.get(cb_queue.getSelectedIndex()).getQueueName();
        agentObject.setAgentId(agentId);
        agentObject.setPass(pass);
//        agentObject.setInterface("SIP/"+iface);        
        agentObject.setInterface(iface);
        agentObject.setRole(role);
        agentObject.setQueueId(queueId);
        agentObject.setQueueName(queueName);
        lb_notify_queue.setText("");                            
        lb_notify_iface.setText("");
        lb_notify_pwd.setText("");
        lb_notify_agent.setText("");        
        lb_status.setText("");      
        try{
            //kiem tra thong tin nguoi dung nhap vao
            if(!agentId.equalsIgnoreCase("") && uti.checkAgent(agentId)){                
                if(!pass.equalsIgnoreCase("") && uti.checkPwd(pass)){                    
                    if(!iface.equalsIgnoreCase("") && uti.checkIface(iface)){                        
                        if(!queueId.equalsIgnoreCase("")){
                            cmd = "100@"+agentId+"@"+pass+"@SIP/"+iface+"@"+queueId+"@"+role;
                            Socket clientSocket = new Socket(host, aport);
//                            clientSocket.setKeepAlive(true);
                            System.out.println("socket timeout\t"+clientSocket.getSoTimeout()); 
                            if(clientSocket != null){
                                System.out.println("connect to server "+clientSocket.getInetAddress().toString());
                                agent = new Agent(clientSocket, this, agentObject, cmd);
                            }
                        }else {   
                            lb_notify_queue.setText("(*)");  
                            lb_status.setText("Check network connection");
                        }
                    }else {
                        lb_status.setText("Extension not appropriate");
                        lb_notify_iface.setText("(*)");
                    }
                }else {
                    lb_status.setText("Password not appropriate");
                    lb_notify_pwd.setText("(*)");
                }
            }else {
                lb_status.setText("Agent Id not appropriate");                
                lb_notify_agent.setText("(*)");       
            }
            
        }catch(Exception e){
            System.out.println("btn_loginActionPerformed\t"+e); 
        }
        
    }//GEN-LAST:event_btn_loginActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void tx_agentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_agentKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 10){
             System.out.println("tx_iface\t"+evt.getKeyCode());
             btn_loginActionPerformed(null);
         }
    }//GEN-LAST:event_tx_agentKeyPressed

    private void tx_ifaceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_ifaceKeyPressed
        // TODO add your handling code here:         
         if(evt.getKeyCode() == 10){
             System.out.println("tx_iface\t"+evt.getKeyCode());
             btn_loginActionPerformed(null);
         }
    }//GEN-LAST:event_tx_ifaceKeyPressed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:                 
//         if (thr != null) {
//             if (thr.isAlive()) {
//                 thr.interrupt();
//                 System.out.println("close loginform");
//             }
//         }
    }//GEN-LAST:event_formWindowClosed

    private void lb_picMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lb_picMouseClicked
        // TODO add your handling code here:     
    }//GEN-LAST:event_lb_picMouseClicked

    private void pwdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 10){
             System.out.println("tx_iface\t"+evt.getKeyCode());
             btn_loginActionPerformed(null);
         }        
    }//GEN-LAST:event_pwdKeyPressed

    private void cb_queueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cb_queueKeyPressed
        // TODO add your handling code here:
//         if(evt.getKeyCode() == 10){
//             System.out.println("tx_iface\t"+evt.getKeyCode());
//             btn_loginActionPerformed(null);
//         }        
    }//GEN-LAST:event_cb_queueKeyPressed

    private void btn_loginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_loginKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 10){
             System.out.println("tx_iface\t"+evt.getKeyCode());
             btn_loginActionPerformed(null);
         }        
    }//GEN-LAST:event_btn_loginKeyPressed

    private void btn_clearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_clearKeyPressed
        // TODO add your handling code here:
        tx_agent.setText("");
        tx_iface.setText("");
        pwd.setText("");         
    }//GEN-LAST:event_btn_clearKeyPressed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void submn_quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submn_quitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_submn_quitActionPerformed

    private void submn_quitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submn_quitMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_submn_quitMouseClicked

    private void submn_configActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submn_configActionPerformed
        // TODO add your handling code here:
        configform = new ConfigForm(this);
        configform.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_submn_configActionPerformed

    /**
     * @param args the command line arguments
     */
    public static  void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
//                try {                
                    new LoginForm().setVisible(true);
//                } catch (IOException ex) {
//                    Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        });
                        
    }
    
    private void getListQueue(){
        try{            
            Socket soc = new Socket(host, qport);            
            if(soc != null){                    		
                InputStream is = soc.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);  
                listQueue = (ArrayList<QueueObject>)ois.readObject(); 
                ois.close();
                is.close();
                soc.close();
                for(QueueObject q : listQueue){
                    if(q != null){
                        String temp = q.getQueueId()+" - "+q.getQueueName();
                        cb_queue.addItem(temp.toUpperCase());
                    }                                    
                }
            }                        
        }catch(Exception e){
            System.out.println("getListQueue\t"+e);
            lb_notify_queue.setText("(*)");
        }        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_login;
    private static javax.swing.JComboBox cb_queue;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JLabel lb_agent;
    private javax.swing.JLabel lb_iface;
    private javax.swing.JLabel lb_notify_agent;
    private javax.swing.JLabel lb_notify_iface;
    private javax.swing.JLabel lb_notify_pwd;
    public static javax.swing.JLabel lb_notify_queue;
    private javax.swing.JLabel lb_pic;
    private javax.swing.JLabel lb_pwd;
    private javax.swing.JLabel lb_queue;
    public static javax.swing.JLabel lb_status;
    private javax.swing.JMenuBar mn_main;
    private javax.swing.JPanel panel_1;
    private javax.swing.JPasswordField pwd;
    private javax.swing.JMenuItem submn_config;
    private javax.swing.JMenuItem submn_quit;
    private javax.swing.JTextField tx_agent;
    private javax.swing.JTextField tx_iface;
    // End of variables declaration//GEN-END:variables
}

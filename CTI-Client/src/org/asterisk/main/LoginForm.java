package org.asterisk.main;

import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import org.asterisk.model.QueueObject;
import org.asterisk.utility.Agent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
public class LoginForm extends javax.swing.JFrame {

    	private Agent agentClient ;
	private static String host = "localhost";
	private static int qport = 33333;
	private static int aport = 22222;
	private static ArrayList <QueueObject>  listQueue;
	private static Socket clientSoc;
        private String role ="1";
//        private QueueObject qObject;
    
    
    /**
     * Creates new form LoginForm
     */
    ConfigForm configform;
    public LoginForm() {      
        initComponents();
        listQueue = new ArrayList<QueueObject>();
        getListQueue();        
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
        setTitle("Login form for agent");
        setForeground(new java.awt.Color(121, 163, 240));
        setName("main_frame"); // NOI18N
        setResizable(false);

        btn_login.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_login.setText("Login");
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        lb_agent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_agent.setText("AgentID");

        lb_pwd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_pwd.setText("Password");

        lb_iface.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_iface.setText("Extension");

        lb_queue.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_queue.setText("Queue");

        tx_agent.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        tx_iface.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tx_iface.setText("8001");

        pwd.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pwd.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        btn_clear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        lb_notify_iface.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_iface.setForeground(new java.awt.Color(255, 0, 0));

        lb_notify_pwd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_pwd.setForeground(new java.awt.Color(255, 0, 0));

        lb_notify_agent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_agent.setForeground(new java.awt.Color(255, 0, 0));
        lb_notify_agent.setText(",,,");

        lb_notify_queue.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lb_notify_queue.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout panel_1Layout = new javax.swing.GroupLayout(panel_1);
        panel_1.setLayout(panel_1Layout);
        panel_1Layout.setHorizontalGroup(
            panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_1Layout.createSequentialGroup()
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panel_1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lb_iface, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_pwd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_agent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tx_agent)
                            .addComponent(tx_iface)
                            .addComponent(cb_queue, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_notify_iface, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_notify_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lb_agent, lb_iface, lb_pwd, lb_queue});

        panel_1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_clear, btn_login});

        panel_1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cb_queue, pwd, tx_agent, tx_iface});

        panel_1Layout.setVerticalGroup(
            panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
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
                .addGap(24, 24, 24)
                .addGroup(panel_1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lb_agent, lb_iface, lb_pwd, lb_queue});

        panel_1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cb_queue, pwd, tx_agent, tx_iface});

        lb_pic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/ntt2.png"))); // NOI18N

        lb_status.setText("status");

        jMenu1.setText("File");

        submn_config.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        submn_config.setText("Configuration");
        submn_config.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                submn_configActionPerformed(evt);
            }
        });
        jMenu1.add(submn_config);

        submn_quit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        submn_quit.setText("Exit");
        submn_quit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
                submn_quitMouseClicked(evt);
            }
        });
        submn_quit.addActionListener(new java.awt.event.ActionListener() {
            @Override
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
                .addGap(34, 34, 34)
                .addComponent(lb_pic)
                .addGap(70, 70, 70)
                .addComponent(panel_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_pic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submn_quitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submn_quitMouseClicked
        // TODO add your handling code here:
//        System.exit(0);
    }//GEN-LAST:event_submn_quitMouseClicked

    private void submn_quitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submn_quitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_submn_quitActionPerformed

    private void submn_configActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submn_configActionPerformed
        // TODO add your handling code here:
        configform = new ConfigForm();
        configform.setVisible(true);
//        configform.
        
        
    }//GEN-LAST:event_submn_configActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // TODO add your handling code here:
        tx_agent.setText("");
        tx_iface.setText("");
        pwd.setText("");        
        
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed
        // TODO add your handling code here:        
        char [] p = pwd.getPassword();
        String pwd     = new String(p);
        String agentid = tx_agent.getText();        
        String iface  = tx_iface.getText();
        String queue = listQueue.get(cb_queue.getSelectedIndex()).getQueueId();
        try{
            if(!agentid.equalsIgnoreCase("") && !pwd.equalsIgnoreCase("") 
                                                && !iface.equalsIgnoreCase("") && !queue.equalsIgnoreCase("")){
                String cmd = "100@"+agentid+"@"+pwd+"@SIP/"+iface+"@"+queue+"@"+role;
                lb_status.setText(cmd);
                clientSoc = new Socket(host, aport);
                if(clientSoc != null){
                    System.out.println("connect to server localhost");
                    agentClient = new Agent(clientSoc, this);
                    Agent.sendtoServer(cmd);
                }
            }                        
        }catch(Exception e){
            System.out.println("btn_loginActionPerformed\t"+e);                        
        }
        
    }//GEN-LAST:event_btn_loginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static  void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new LoginForm().setVisible(true);
                listQueue = new ArrayList<QueueObject>();
                getListQueue();                 
            }
        });
                        
    }
    
    public void windowClosing(WindowEvent e) {
        System.out.println("close window");
    }
    
    private static void getListQueue(){
        try{            
            clientSoc = new Socket(host, qport);            
            if(clientSoc!=null){                    		
                InputStream is = clientSoc.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);  
                listQueue = (ArrayList<QueueObject>)ois.readObject(); 
                ois.close();
                is.close();
                clientSoc.close();
                clientSoc = null;  
                for(QueueObject q:listQueue){
                    if(q!=null){
                        String temp = q.getQueueId()+" - "+q.getQueueName();
                        cb_queue.addItem(temp.toUpperCase());
                    }                                    
                }
            }                        
        }catch(Exception e){
            System.out.println("getListQueue\t"+e);
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

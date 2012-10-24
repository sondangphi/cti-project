/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asterisk.org.main;

import asterisk.org.utility.Managerdb;
import asterisk.org.utility.Utility;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.HangupEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewStateEvent;

/**
 *
 * @author leehoa
 */
public class mainForm extends javax.swing.JFrame implements ManagerEventListener{
    TrayIcon trayIcon;
    SystemTray stray;
    private static ManagerConnection manager;
    private static String asteriskAdd = "172.168.10.208";
//    private static String host = "127.0.0.1";
    private static String asteriskUser = "manager";
    private static String asteriskPwd = "123456"; 
    private static String callerid ;
    private static String sqluser = "cti";
    private static String sqlpwd = "123456";
    private static String sqlhost = "172.168.10.208";
    private static String sqldbname = "asterisk";
    private static String sqltbname = "cus";
    private static String iface = "";
//    private static int count = 0;
    private static Utility uti;
    private static String filename = "infor.conf";
    /**
     * Creates new form mainForm
     */
    public mainForm() throws Exception{
        initComponents();      
        uti = new Utility(); 
        if (SystemTray.isSupported()) {
            System.out.println("system tray supported");
            stray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting....");
                    try {                    
                        uti.writeLog("Exit ADM program");
                    } catch (IOException ex) {
                        Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem = new MenuItem("Configuration");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stray.remove(trayIcon);
                    setVisible(true);
                    System.out.println("Tray icon removed");
                    lb_status.setText("");
                    if(manager != null){
                        manager.logoff();
                        manager = null;
                        try {
                            uti.writeLog("Disconnect to Asterisk server - stop receive call");
                        } catch (IOException ex) {
                            Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                        
                }
            });
            popup.add(defaultItem);
            trayIcon = new TrayIcon(image, iface, popup);
            trayIcon.setImageAutoSize(true);
        } else {
            System.out.println("system tray not supported");
        }        
        
        
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));                
        uti.writeLog("Start ADM program");
        writeConfiguration();
        readConfiguration();
        tx_serveras.setText(asteriskAdd);            
        tx_asteriskuser.setText(asteriskUser);
        pwd_asterisk.setText(asteriskPwd);
        tx_serverdb.setText(sqlhost);
        tx_dbuser.setText(sqluser);
        pwd_db.setText(sqlpwd);
        tx_iface.setText(iface);
        
        tx_asteriskuser.setEnabled(false);
        tx_serveras.setEnabled(false);
        tx_dbuser.setEnabled(false);
        tx_serverdb.setEnabled(false);
        pwd_asterisk.setEnabled(false);
        pwd_db.setEnabled(false);    
        lb_status.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainpanel = new javax.swing.JPanel();
        bt_ok = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tx_serveras = new javax.swing.JTextField();
        tx_dbuser = new javax.swing.JTextField();
        tx_serverdb = new javax.swing.JTextField();
        tx_asteriskuser = new javax.swing.JTextField();
        pwd_asterisk = new javax.swing.JPasswordField();
        pwd_db = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        tx_iface = new javax.swing.JTextField();
        lb_status = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("main form");
        setAlwaysOnTop(true);
        setResizable(false);

        mainpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainpanelMouseClicked(evt);
            }
        });
        mainpanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mainpanelKeyPressed(evt);
            }
        });

        bt_ok.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bt_ok.setText("Finish");
        bt_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_okActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Server Asterisk");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Username");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Password");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Server Database");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Username db");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Password db");

        tx_serveras.setText("jTextField1");
        tx_serveras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tx_serverasMouseClicked(evt);
            }
        });
        tx_serveras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_serverasActionPerformed(evt);
            }
        });
        tx_serveras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tx_serverasKeyPressed(evt);
            }
        });

        tx_dbuser.setText("jTextField1");
        tx_dbuser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tx_dbuserMouseClicked(evt);
            }
        });
        tx_dbuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_dbuserActionPerformed(evt);
            }
        });

        tx_serverdb.setText("jTextField1");
        tx_serverdb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tx_serverdbMouseClicked(evt);
            }
        });
        tx_serverdb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_serverdbActionPerformed(evt);
            }
        });

        tx_asteriskuser.setText("jTextField1");
        tx_asteriskuser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tx_asteriskuserMouseClicked(evt);
            }
        });
        tx_asteriskuser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_asteriskuserActionPerformed(evt);
            }
        });

        pwd_asterisk.setText("jPasswordField1");
        pwd_asterisk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pwd_asteriskMouseClicked(evt);
            }
        });
        pwd_asterisk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwd_asteriskActionPerformed(evt);
            }
        });

        pwd_db.setText("jPasswordField1");
        pwd_db.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pwd_dbMouseClicked(evt);
            }
        });
        pwd_db.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwd_dbActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Extension");

        tx_iface.setText("jTextField1");
        tx_iface.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tx_ifaceActionPerformed(evt);
            }
        });
        tx_iface.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tx_ifaceKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout mainpanelLayout = new javax.swing.GroupLayout(mainpanel);
        mainpanel.setLayout(mainpanelLayout);
        mainpanelLayout.setHorizontalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tx_serveras, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_dbuser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_serverdb, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_asteriskuser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_asterisk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_db, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_iface, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(bt_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainpanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        mainpanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {pwd_asterisk, pwd_db, tx_asteriskuser, tx_dbuser, tx_serveras, tx_serverdb});

        mainpanelLayout.setVerticalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_serveras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_asteriskuser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwd_asterisk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_serverdb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tx_dbuser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(pwd_db, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tx_iface, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(bt_ok)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainpanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        mainpanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pwd_asterisk, pwd_db, tx_asteriskuser, tx_dbuser, tx_serveras, tx_serverdb});

        lb_status.setForeground(new java.awt.Color(255, 0, 0));
        lb_status.setText("jLabel8");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_okActionPerformed
        // TODO add your handling code here:
         try {
             int count = 0;
//            stray.add(trayIcon);            
            updateConfiguration();
            readConfiguration();            
            while(count != 5 && manager == null){
                connectAsterisk(asteriskAdd, asteriskUser, asteriskPwd);
                manager.login();
                manager.addEventListener(this);		
                manager.sendAction(new StatusAction());
                count++;
                if(manager == null){
                    Thread.sleep(3000);
                    uti.writeLog("Connect to Asterisk Fail - reconnect - "+asteriskAdd);
                    lb_status.setText("Connect to Asterisk Fail - check information aganin ");
                }                    
            }            
            if(manager != null){
                stray.add(trayIcon);  
                uti.writeLog("Connect to Asterisk Successfull - "+asteriskAdd);
                this.setVisible(false); 
            }else{
                lb_status.setText("Connect to Asterisk Fail - check information aganin ");
                uti.writeLog("Connect to Asterisk Fail - "+asteriskAdd);
            }                            
        } catch (AWTException ex) {
            System.out.println("unable to add to tray");
            stray.remove(trayIcon);  
        }  catch (Exception e) {
            try {
                uti.writeLog("Connect to Asterisk Fail - "+asteriskAdd);
                lb_status.setText("Connect to Asterisk Fail - check information aganin ");
                manager = null;
            } catch (IOException ex) {
                Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }      
    }//GEN-LAST:event_bt_okActionPerformed

    private void tx_serverasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_serverasActionPerformed
        // TODO add your handling code here:
        if(!tx_asteriskuser.isEnabled())
            tx_asteriskuser.setEnabled(true);
//        else
//            tx_asteriskuser.setEnabled(true);
    }//GEN-LAST:event_tx_serverasActionPerformed

    private void mainpanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainpanelMouseClicked
        // TODO add your handling code here:
        tx_asteriskuser.setEnabled(false);
        tx_serveras.setEnabled(false);
        tx_dbuser.setEnabled(false);
        tx_serverdb.setEnabled(false);
        pwd_asterisk.setEnabled(false);
        pwd_db.setEnabled(false);
    }//GEN-LAST:event_mainpanelMouseClicked

    private void tx_asteriskuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_asteriskuserActionPerformed
        // TODO add your handling code here:
        if(!tx_asteriskuser.isEnabled())
            tx_asteriskuser.setEditable(true);
    }//GEN-LAST:event_tx_asteriskuserActionPerformed

    private void pwd_asteriskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwd_asteriskActionPerformed
        // TODO add your handling code here:
        if(!pwd_asterisk.isEnabled())
            pwd_asterisk.setEnabled(true);
    }//GEN-LAST:event_pwd_asteriskActionPerformed

    private void tx_serverdbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_serverdbActionPerformed
        // TODO add your handling code here:
        if(!tx_serverdb.isEnabled())
            tx_serverdb.setEnabled(true);        
    }//GEN-LAST:event_tx_serverdbActionPerformed

    private void tx_dbuserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_dbuserActionPerformed
        // TODO add your handling code here:
        if(!tx_dbuser.isEnabled())
            tx_dbuser.setEnabled(true);         
    }//GEN-LAST:event_tx_dbuserActionPerformed

    private void pwd_dbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwd_dbActionPerformed
        // TODO add your handling code here:
        if(!pwd_db.isEnabled())
            pwd_db.setEnabled(true); 
    }//GEN-LAST:event_pwd_dbActionPerformed

    private void tx_ifaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tx_ifaceActionPerformed
        // TODO add your handling code here:
        if(!tx_iface.isEnabled())
            tx_iface.setEnabled(true); 
    }//GEN-LAST:event_tx_ifaceActionPerformed

    private void tx_ifaceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_ifaceKeyPressed
        // TODO add your handling code here:
        
            System.out.println("keycode\\t"+evt.getKeyCode());
            if(evt.getKeyCode() == 10)
                bt_okActionPerformed(null);
        
    }//GEN-LAST:event_tx_ifaceKeyPressed

    private void mainpanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mainpanelKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            System.out.println("enter");
        }
    }//GEN-LAST:event_mainpanelKeyPressed

    private void tx_serverasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_serverasKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tx_serverasKeyPressed

    private void tx_serverasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tx_serverasMouseClicked
        // TODO add your handling code here:
        tx_serveras.setEnabled(true);
    }//GEN-LAST:event_tx_serverasMouseClicked

    private void tx_asteriskuserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tx_asteriskuserMouseClicked
        // TODO add your handling code here:
        tx_asteriskuser.setEnabled(true);
    }//GEN-LAST:event_tx_asteriskuserMouseClicked

    private void pwd_asteriskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pwd_asteriskMouseClicked
        // TODO add your handling code here:
        pwd_asterisk.setEnabled(true);
    }//GEN-LAST:event_pwd_asteriskMouseClicked

    private void tx_serverdbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tx_serverdbMouseClicked
        // TODO add your handling code here:
        tx_serverdb.setEnabled(true);
    }//GEN-LAST:event_tx_serverdbMouseClicked

    private void tx_dbuserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tx_dbuserMouseClicked
        // TODO add your handling code here:
        tx_dbuser.setEnabled(true);
    }//GEN-LAST:event_tx_dbuserMouseClicked

    private void pwd_dbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pwd_dbMouseClicked
        // TODO add your handling code here:
        pwd_db.setEnabled(true);
    }//GEN-LAST:event_pwd_dbMouseClicked
    public static void connectAsterisk(String host, String username, String password){
        ManagerConnectionFactory factory = new ManagerConnectionFactory(host, username, password);
        manager = factory.createManagerConnection();
    } 
    
    public static void readConfiguration()throws Exception{
        asteriskAdd = uti.readInfor(filename, "hostAsterisk");
        asteriskUser = uti.readInfor(filename, "userAsterisk");
        asteriskPwd = uti.readInfor(filename, "pwdAsterisk");
        sqluser = uti.readInfor(filename, "sqluser");
        sqlpwd = uti.readInfor(filename, "sqlpwd");
        sqlhost = uti.readInfor(filename, "sqlhost");
        sqldbname = uti.readInfor(filename, "sqldbname");
        sqltbname = uti.readInfor(filename, "sqltbname");            
        iface = uti.readInfor(filename, "iface");             
        uti.writeLog("Read Configuration");
    }
    public static void writeConfiguration()throws Exception{
        File f = new File(filename);
        if(!f.exists())
            f.createNewFile();            
        FileInputStream fis = new FileInputStream(f);    
        if(fis.available() == 0 ){
            uti.writeInfor(filename, "hostAsterisk", asteriskAdd);
            uti.writeInfor(filename, "userAsterisk", asteriskUser);
            uti.writeInfor(filename, "pwdAsterisk", asteriskPwd);
            uti.writeInfor(filename, "sqluser", sqluser);
            uti.writeInfor(filename, "sqlpwd", sqlpwd);
            uti.writeInfor(filename, "sqlhost", sqlhost);
            uti.writeInfor(filename, "sqldbname", sqldbname);
            uti.writeInfor(filename, "sqltbname", sqltbname);
            uti.writeInfor(filename, "iface", iface);
            uti.writeLog("Create & Write Configuration");
        }                    
    }    
    public static void updateConfiguration()throws Exception{
          
        char [] p = null;
        p = pwd_asterisk.getPassword();
        String pwdas = new String(p);
        p = pwd_db.getPassword();
        String pwddb = new String (p);        
        uti.writeInfor(filename, "hostAsterisk", tx_serveras.getText());
        uti.writeInfor(filename, "userAsterisk", tx_asteriskuser.getText());
        uti.writeInfor(filename, "pwdAsterisk", pwdas);
        uti.writeInfor(filename, "sqluser", tx_dbuser.getText());
        uti.writeInfor(filename, "sqlpwd", pwddb);
        uti.writeInfor(filename, "sqlhost", tx_serverdb.getText());
        uti.writeInfor(filename, "iface", tx_iface.getText());
        uti.writeLog("Update Configuration");  
    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run(){
                try {
                    new mainForm().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(mainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_ok;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private static javax.swing.JLabel lb_status;
    private javax.swing.JPanel mainpanel;
    private static javax.swing.JPasswordField pwd_asterisk;
    private static javax.swing.JPasswordField pwd_db;
    private static javax.swing.JTextField tx_asteriskuser;
    private static javax.swing.JTextField tx_dbuser;
    private static javax.swing.JTextField tx_iface;
    private static javax.swing.JTextField tx_serveras;
    private static javax.swing.JTextField tx_serverdb;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onManagerEvent(ManagerEvent me) {
        if(me instanceof NewStateEvent){
            NewStateEvent stateEvent = (NewStateEvent)me;
            System.out.println("***********************\t NewStateEvent\t ***********************");  
            String state = stateEvent.getChannelStateDesc();
            String channel = stateEvent.getChannel();
            channel = channel.substring(0, channel.indexOf("-"));  
            if(state.equalsIgnoreCase("RINGING") && channel.equalsIgnoreCase("SIP/"+iface)){
                try{
                    Managerdb mdb = new Managerdb( sqldbname, sqluser, sqlpwd, sqlhost);
//                    System.out.println("channel\t "+channel);                        
                    String callerNum = stateEvent.getCallerIdNum();
                    System.out.println("callerNum\t "+callerNum);       
                    if(mdb.getInfor(callerNum)){
                        System.out.println("callerNum map\t "+callerNum);  
                        String temp = "https://www.google.com.vn/search?q="+callerNum;
                        System.out.println("url\t "+temp);
                        URI url = new URI(temp);
                        Desktop.getDesktop().browse(url);
                        uti.writeLog("Receive a old customer call from "+callerNum);
                    }
                    else{
                        System.out.println("callerNum not map\t "+callerNum);  
                        uti.writeLog("Receive a new customer call from "+callerNum);
                    }                    
                    mdb.close();
                }catch(Exception e){}                        
            }
        }
    }
}

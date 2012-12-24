/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import com.jniwrapper.win32.ie.Browser;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;

/**
 *
 * @author leehoa
 */
public class MainForm extends javax.swing.JFrame {
    
    private LoginForm loginform;
    private Agent agentClient;
    private TrayIcon trayIcon;
    private SystemTray stray;    
    static AgentObject agentObject;
    private Utility uti;
    private String dialNumber = "";
    public String callDuration = "00:00:00";

    private  String filename = "infor.properties";
    private  String Mysql_server = "172.168.10.208";      
    private  String Mysql_dbname = "cti_database";
    private  String Mysql_user = "cti";
    private  String Mysql_pwd  = "123456"; 
    private ConnectDatabase con;
    private FeedbackForm feedback;
    Browser bro = null;    
    LocateMap locate = new LocateMap();
    public ChangepwdForm chanpwdform;
//    JFrame frame = new JFrame("Locate Maps");

    /**
     * Creates new form MainForm2
     */
    public MainForm() {
        initComponents();
        txt_phonenum.setHorizontalAlignment(javax.swing.JLabel.RIGHT);        
    }
    
    public MainForm(Agent agent, AgentObject aOb) {
//        jPanel9.set
        initComponents();        
        uti = new Utility();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );        
        agentClient = agent;
        agentObject = aOb;        
        Image image = Toolkit.getDefaultToolkit().getImage("people.png");
        this.setIconImage(image);
        if (SystemTray.isSupported()) {
            MouseListener mouseListener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Tray Icon – Mouse clicked!");
                    setVisible(true);
                    stray.remove(trayIcon);
                }
                public void mouseEntered(MouseEvent e) {
//                System.out.println("Tray Icon – Mouse entered!");
                }
                public void mouseExited(MouseEvent e) {
//                System.out.println("Tray Icon – Mouse exited!");
                }
                public void mousePressed(MouseEvent e) {
//                System.out.println("Tray Icon – Mouse pressed!");
                }
                public void mouseReleased(MouseEvent e) {
//                System.out.println("Tray Icon – Mouse released!");
                }
            };                                    
            stray = SystemTray.getSystemTray();                        
            trayIcon = new TrayIcon(image, "CTI Client");
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(mouseListener);     
//
        } else {
            System.out.println("system tray not supported");
        }        
        lb_agentid.setText(agentObject.getAgentId());
        lb_agentname.setText(agentObject.getAgentName());
        lb_extension.setText(agentObject.getInterface());
        lb_queue.setText(agentObject.getQueueId()+" - "+agentObject.getQueueName());
        lb_logintime.setText(uti.getDatetimeNow());         
        lb_version.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        lb_version.setVerticalAlignment(javax.swing.JLabel.CENTER);
        txt_phonenum.setHorizontalAlignment(javax.swing.JLabel.RIGHT);  
        try{
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
        }catch(Exception e){
        }
        
        this.setTitle("Desktop Agent _ "+agentObject.getAgentId()+" _ "+agentObject.getAgentName());
//        this.lb_locate.setToolTipText("Locate Map");
        setLocationRelativeTo(null);
    }    

    public void updateNumber(){
        txt_phonenum.setText(dialNumber);
    }
    
    public void deleteNumber(){
        dialNumber  = txt_phonenum.getText();
        if(dialNumber != ""){
            dialNumber = dialNumber.substring(0, dialNumber.length()-1);        
            txt_phonenum.setText(dialNumber);            
        }

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btn_logout = new javax.swing.JButton();
        btn_pause = new javax.swing.JToggleButton();
        jLabel13 = new javax.swing.JLabel();
        lb_workTime = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_logintime = new javax.swing.JLabel();
        main_tab = new javax.swing.JTabbedPane();
        Panel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_makh = new javax.swing.JTextField();
        txt_add = new javax.swing.JTextField();
        txt_mobile = new javax.swing.JTextField();
        txt_phone1 = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        cb_gender = new javax.swing.JComboBox();
        btn_new = new javax.swing.JButton();
        btn_feedback = new javax.swing.JButton();
        txt_name = new javax.swing.JTextField();
        btn_update = new javax.swing.JButton();
        btn_clear2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_email1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        cb_type = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        txt_phone2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_report = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lb_agentid = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lb_extension = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lb_agentname = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lb_queue = new javax.swing.JLabel();
        lb_status = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lb_callduration = new javax.swing.JLabel();
        panel_dial = new javax.swing.JPanel();
        panel_number = new javax.swing.JPanel();
        btn_1 = new javax.swing.JButton();
        btn_2 = new javax.swing.JButton();
        btn_3 = new javax.swing.JButton();
        btn_4 = new javax.swing.JButton();
        btn_5 = new javax.swing.JButton();
        btn_6 = new javax.swing.JButton();
        btn_7 = new javax.swing.JButton();
        btn_8 = new javax.swing.JButton();
        btn_9 = new javax.swing.JButton();
        btn_0 = new javax.swing.JButton();
        btn_11 = new javax.swing.JButton();
        btn_12 = new javax.swing.JButton();
        txt_phonenum = new javax.swing.JTextField();
        btn_back = new javax.swing.JButton();
        btn_dial = new javax.swing.JButton();
        btn_clear = new javax.swing.JButton();
        lb_version = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuItem_logout = new javax.swing.JMenuItem();
        MenuItem_changepwd = new javax.swing.JMenuItem();
        MenuItem_setting = new javax.swing.JMenuItem();
        MenuItem_exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MenuItem_about = new javax.swing.JMenuItem();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Desktop Agent");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/logout.png"))); // NOI18N
        btn_logout.setText("Logout");
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        btn_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/pause.png"))); // NOI18N
        btn_pause.setText("Pause");
        btn_pause.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pauseActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Work Time");

        lb_workTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_workTime.setText("00:00:00");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Login at");

        lb_logintime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_logintime.setText("2012-12-12 08:00:00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btn_pause, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_logintime, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_workTime)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_workTime)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_logintime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_pause, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_logout, btn_pause});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel13, lb_workTime});

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.Color.black));

        jLabel4.setText("Customer ID");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Name");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setText("Address");
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setText("Gender");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setText("Phone");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setText("Phone 2");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });

        cb_gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nam", "Nữ" }));

        btn_new.setText("Submit");
        btn_new.setEnabled(false);
        btn_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newActionPerformed(evt);
            }
        });

        btn_feedback.setText("Feedback");
        btn_feedback.setEnabled(false);
        btn_feedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_feedbackActionPerformed(evt);
            }
        });

        btn_update.setText("Update");
        btn_update.setEnabled(false);
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        btn_clear2.setText("Reset");
        btn_clear2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clear2ActionPerformed(evt);
            }
        });

        jLabel16.setText("Email");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setText("Birthday");
        jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setText("Type");
        jLabel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cb_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "type1", "type2", "type3" }));

        jLabel19.setText("registration");
        jLabel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/map_magnify.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_type, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_phone2)
                        .addGap(170, 170, 170))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addComponent(txt_makh, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cb_gender, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_email1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(txt_mobile, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_phone1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_email))))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(btn_new, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_feedback)
                                .addGap(18, 18, 18)
                                .addComponent(btn_clear2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel16, jLabel17});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_makh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(txt_add)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(txt_email1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_mobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_phone1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_phone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton3)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(btn_new, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_feedback, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_clear2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cb_gender, cb_type, jLabel10, jLabel11, jLabel14, jLabel18, jLabel19, jLabel4, jLabel9, txt_add, txt_makh, txt_name, txt_phone2});

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "History", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        table_report.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(table_report);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        main_tab.addTab("Mini CRM", Panel1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );

        main_tab.addTab("Promotions", jPanel1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 426, Short.MAX_VALUE)
        );

        main_tab.addTab("Co.op Systems", jPanel3);

        jButton1.setText("dial");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        main_tab.addTab("Campaigns", jPanel5);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agent Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("AgentID");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel3.setPreferredSize(new java.awt.Dimension(74, 19));

        lb_agentid.setBackground(new java.awt.Color(255, 255, 204));
        lb_agentid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_agentid.setText("8001");
        lb_agentid.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Extension");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel5.setMaximumSize(new java.awt.Dimension(75, 20));
        jLabel5.setPreferredSize(new java.awt.Dimension(74, 19));

        lb_extension.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_extension.setText("6789");
        lb_extension.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Agent Name");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lb_agentname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_agentname.setText("Nguyen Van A");
        lb_agentname.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Queue");
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lb_queue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_queue.setText("8888 - Ban Hang");
        lb_queue.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lb_status.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lb_status.setForeground(new java.awt.Color(0, 204, 0));
        lb_status.setText("Ready");
        lb_status.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Call Duration");

        lb_callduration.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lb_callduration.setForeground(new java.awt.Color(255, 0, 0));
        lb_callduration.setText("00:00:00");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(7, 7, 7)))
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(lb_agentid, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(lb_extension, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lb_callduration, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(33, 33, 33))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                    .addComponent(lb_agentid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(lb_extension, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_callduration, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel5, jLabel6, jLabel7, lb_agentid, lb_agentname, lb_extension, lb_queue});

        panel_dial.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agent Dial Out", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        panel_number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                panel_numberKeyPressed(evt);
            }
        });

        btn_1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_1.setText("1");
        btn_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_1ActionPerformed(evt);
            }
        });
        btn_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_1KeyPressed(evt);
            }
        });

        btn_2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_2.setText("2");
        btn_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_2ActionPerformed(evt);
            }
        });
        btn_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_2KeyPressed(evt);
            }
        });

        btn_3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_3.setText("3");
        btn_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_3ActionPerformed(evt);
            }
        });
        btn_3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_3KeyPressed(evt);
            }
        });

        btn_4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_4.setText("4");
        btn_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_4ActionPerformed(evt);
            }
        });
        btn_4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_4KeyPressed(evt);
            }
        });

        btn_5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_5.setText("5");
        btn_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_5ActionPerformed(evt);
            }
        });
        btn_5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_5KeyPressed(evt);
            }
        });

        btn_6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_6.setText("6");
        btn_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_6ActionPerformed(evt);
            }
        });
        btn_6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_6KeyPressed(evt);
            }
        });

        btn_7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_7.setText("7");
        btn_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_7ActionPerformed(evt);
            }
        });
        btn_7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_7KeyPressed(evt);
            }
        });

        btn_8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_8.setText("8");
        btn_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_8ActionPerformed(evt);
            }
        });
        btn_8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_8KeyPressed(evt);
            }
        });

        btn_9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_9.setText("9");
        btn_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_9ActionPerformed(evt);
            }
        });
        btn_9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_9KeyPressed(evt);
            }
        });

        btn_0.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_0.setText("0");
        btn_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_0ActionPerformed(evt);
            }
        });
        btn_0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_0KeyPressed(evt);
            }
        });

        btn_11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_11.setText("*");
        btn_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_11ActionPerformed(evt);
            }
        });

        btn_12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_12.setText("#");
        btn_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_12ActionPerformed(evt);
            }
        });

        txt_phonenum.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_phonenum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_phonenumKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout panel_numberLayout = new javax.swing.GroupLayout(panel_number);
        panel_number.setLayout(panel_numberLayout);
        panel_numberLayout.setHorizontalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_numberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_3))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_6))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addComponent(btn_11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_0))
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addComponent(btn_7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_9)
                            .addComponent(btn_12)))
                    .addComponent(txt_phonenum))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_0, btn_1, btn_11, btn_12, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9});

        panel_numberLayout.setVerticalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                .addComponent(txt_phonenum, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_0, btn_1, btn_11, btn_12, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9});

        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/123070_42045_16_back_icon.png.gif"))); // NOI18N
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        btn_dial.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_dial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/com.mmmooo.SpeedDial_icon.png"))); // NOI18N
        btn_dial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dialActionPerformed(evt);
            }
        });

        btn_clear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_clear.setText("Clear");
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_dialLayout = new javax.swing.GroupLayout(panel_dial);
        panel_dial.setLayout(panel_dialLayout);
        panel_dialLayout.setHorizontalGroup(
            panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_dialLayout.createSequentialGroup()
                .addComponent(panel_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_dialLayout.setVerticalGroup(
            panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_dialLayout.createSequentialGroup()
                .addGroup(panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_dialLayout.createSequentialGroup()
                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panel_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lb_version.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        lb_version.setText("Build 0.0.1");
        lb_version.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setText("File");

        MenuItem_logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        MenuItem_logout.setText("Logout");
        MenuItem_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_logoutActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItem_logout);

        MenuItem_changepwd.setText("Change Password");
        MenuItem_changepwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_changepwdActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItem_changepwd);

        MenuItem_setting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        MenuItem_setting.setText("Settings");
        MenuItem_setting.setEnabled(false);
        jMenu1.add(MenuItem_setting);

        MenuItem_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_MASK));
        MenuItem_exit.setText("Exit");
        MenuItem_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_exitActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItem_exit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Help");

        MenuItem_about.setText("About");
        MenuItem_about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_aboutActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItem_about);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panel_dial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(main_tab)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_version, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(main_tab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_version, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pauseActionPerformed
        // TODO add your handling code here:        
        try{
            String cmd = "";
            if(btn_pause.isSelected() && btn_pause.isEnabled()){
                btn_pause.setText("UNPAUSE");
                cmd  = "104@off";
                agentClient.sendtoServer(cmd);
                btn_logout.setEnabled(false);
                setAllEnable(false);
//                lb_status.
                lb_status.setText("Not Ready");
            }else{
                btn_pause.setText("PAUSE");    
                cmd  = "104@on";
                agentClient.sendtoServer(cmd);  
                btn_logout.setEnabled(true);
                setAllEnable(true);
                lb_status.setText("Ready");
            }
        }catch(Exception e){
            
        }         
        
    }//GEN-LAST:event_btn_pauseActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
        // TODO add your handling code here:
        try{
            String cmd  = "102";
            agentClient.sendtoServer(cmd);
            System.out.println("Logout program");
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println("Exception(btn_logout): "+e);
        }
    }//GEN-LAST:event_btn_logoutActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        try{
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
            stray.add(trayIcon); 
            setVisible(false);
            System.out.println("Close Main Form");  
        }catch(Exception e){
            System.out.println("Exception(formWindowClosing): "+e);
        }      
    }//GEN-LAST:event_formWindowClosing

    private void MenuItem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_exitActionPerformed
        // TODO add your handling code here:
        try{
            String cmd  = "112";
            agentClient.sendtoServer(cmd);
            agentClient.closeConnect();
            System.out.println("Exit CTI CLIENT"); 
            System.exit(0);
        }catch(Exception e){
        }        
    }//GEN-LAST:event_MenuItem_exitActionPerformed

    private void MenuItem_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_logoutActionPerformed
        // TODO add your handling code here:
        try{
            btn_logoutActionPerformed(null);           
        }catch(Exception e){
        }        
    }//GEN-LAST:event_MenuItem_logoutActionPerformed

    private void btn_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_1ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_1.getText();
        updateNumber();
    }//GEN-LAST:event_btn_1ActionPerformed

    private void btn_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_2ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_2.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_2ActionPerformed

    private void btn_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_3ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_3.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_3ActionPerformed

    private void btn_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_4ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_4.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_4ActionPerformed

    private void btn_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_5ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_5.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_5ActionPerformed

    private void btn_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_6ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_6.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_6ActionPerformed

    private void btn_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_7ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_7.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_7ActionPerformed

    private void btn_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_8ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_8.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_8ActionPerformed

    private void btn_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_9ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_9.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_9ActionPerformed

    private void btn_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_0ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_0.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_0ActionPerformed

    private void btn_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_11ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_11.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_11ActionPerformed

    private void btn_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_12ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_12.getText();
        updateNumber();        
    }//GEN-LAST:event_btn_12ActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        // TODO add your handling code here:
        deleteNumber();
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        // TODO add your handling code here:
        dialNumber = "";
        txt_phonenum.setText(dialNumber);
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_dialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dialActionPerformed
        // TODO add your handling code here:
        try{
            dialNumber = txt_phonenum.getText();
            if(dialNumber != "" && uti.checkNumber(dialNumber)){
                String cmd  = "108@"+dialNumber;
                agentClient.sendtoServer(cmd);
                System.out.println("Dial number: "+dialNumber);
            }else
                System.out.println("not allow: "+dialNumber);
        }catch(Exception e){
        }        
    }//GEN-LAST:event_btn_dialActionPerformed

    private void btn_feedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_feedbackActionPerformed
        // TODO add your handling code here:
        System.out.println("txt_name: "+this.txt_name.getText());
        System.out.println("txt_add: "+this.txt_add.getText());
        System.out.println("txt_mobile: "+this.txt_mobile.getText());
        System.out.println("txt_phone1: "+this.txt_phone1.getText());
        System.out.println("txt_email: "+this.txt_email.getText());
        System.out.println("txt_add: "+this.txt_add.getText());
        feedback = new FeedbackForm(agentObject,agentClient);
        feedback.setVisible(true);     
    }//GEN-LAST:event_btn_feedbackActionPerformed

    private void btn_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newActionPerformed
        // TODO add your handling code here:
        try{
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                String name = txt_name.getText();
                String email = txt_email.getText();
                String mobile =txt_mobile.getText();                
                String add = txt_add.getText();
                String homephone1=txt_phone1.getText();
                String gender = "";
                if(cb_gender.getSelectedIndex() == 0)
                    gender = "1";
                else if (cb_gender.getSelectedIndex() == 1)
                    gender = "0";                                
                String sql = "INSERT INTO customer (fullname,email,address,mobilephone,gender,homephone1) "
                        + "VALUES ('"+name+"','"+email+"','"+add+"','"+mobile+"','"+gender+"','"+homephone1+"')";
                con.executeUpdate(sql);                                                
                System.out.println("new customer information success.");
                btn_new.setEnabled(false);
            }
            con.closeConnect();
        }catch(Exception e){
            System.out.println(""+e);
        }
        
    }//GEN-LAST:event_btn_newActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        // TODO add your handling code here:
        try{
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                String name = txt_name.getText();
                String email = txt_email.getText();
                String mobile = txt_mobile.getText();                
                String add = txt_add.getText();
                String homephone1=txt_phone1.getText();
                String gender = "";
                if(cb_gender.getSelectedIndex() == 0)
                    gender = "1";
                else if (cb_gender.getSelectedIndex() == 1)
                    gender = "0";                                
                String sql = "UPDATE customer SET fullname ='"+name+"',email='"+email+"',mobilephone='"+mobile+"',address='"+add+"',homephone1='"+homephone1+"',gender='"+gender+"'"
                        + " WHERE id = '"+agentClient.customer.getId()+"'" ;
                con.executeUpdate(sql);                                                
                System.out.println("update information success.");
                btn_update.setEnabled(false);
            }
            con.closeConnect();            
        }catch(Exception e){
            System.out.println(""+e);
        }        
        
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_clear2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clear2ActionPerformed
        // TODO add your handling code here:
        txt_add.setText("");
        txt_email.setText("");
        txt_makh.setText("");
        txt_mobile.setText("");
        txt_name.setText("");
        txt_phone1.setText("");
        btn_feedback.setEnabled(false);
        btn_update.setEnabled(false);
        btn_new.setEnabled(false);
    }//GEN-LAST:event_btn_clear2ActionPerformed

    private void txt_phonenumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_phonenumKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 10){
             btn_dialActionPerformed(null);
         }        
    }//GEN-LAST:event_txt_phonenumKeyPressed

    private void btn_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_1KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }
    }//GEN-LAST:event_btn_1KeyPressed

    private void btn_2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_2KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_2KeyPressed

    private void btn_3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_3KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_3KeyPressed

    private void btn_4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_4KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_4KeyPressed

    private void btn_5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_5KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_5KeyPressed

    private void btn_6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_6KeyPressed

    private void btn_7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_7KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_7KeyPressed

    private void btn_8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_8KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_8KeyPressed

    private void btn_9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_9KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_9KeyPressed

    private void btn_0KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_0KeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_0KeyPressed

    private void panel_numberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panel_numberKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_panel_numberKeyPressed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

    private void MenuItem_changepwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_changepwdActionPerformed
        // TODO add your handling code here:        
        chanpwdform = new ChangepwdForm(agentClient, agentObject);
        chanpwdform.setVisible(true);
    }//GEN-LAST:event_MenuItem_changepwdActionPerformed

    private void MenuItem_aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_aboutActionPerformed
        // TODO add your handling code here:
        AboutForm about = new AboutForm();
        about.setVisible(true);
    }//GEN-LAST:event_MenuItem_aboutActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        try{            
            if(txt_add.getText().equalsIgnoreCase("")){
                System.out.println("null data");
            }else{
                String add =txt_add.getText();
//                String link = "http://maps.google.com/maps?q="+add;
//                link = link.replaceAll(" ", "%20");
//                System.out.println("ulr: "+link);                      
//                URI url = new URI(link);
//                Desktop.getDesktop().browse(url);     
                            
            String url = "http://maps.google.com/maps?q="+add;
            //            bro.navigate(url);

            //            locate.setVisible(true);
            bro = new Browser();
            bro.navigate(url);
            Image image = Toolkit.getDefaultToolkit().getImage("map_magnify.png");                        
            JPanel contentPane;
            contentPane = new JPanel(new GridLayout(1,9));
            contentPane.add(bro);
            JFrame frame = new JFrame("Locate Maps");
            frame.setSize(700, 500);
            frame.setIconImage(image);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(contentPane, BorderLayout.CENTER);       
            frame.setVisible(true);
            frame.add(contentPane);
            
            
            
        }
        }catch(Exception e){
        }         
    }//GEN-LAST:event_jButton3ActionPerformed

    public void setAllEnable(boolean flag){
        btn_0.setEnabled(flag);
        btn_1.setEnabled(flag);
        btn_2.setEnabled(flag);
        btn_3.setEnabled(flag);
        btn_4.setEnabled(flag);
        btn_5.setEnabled(flag);
        btn_6.setEnabled(flag);
        btn_7.setEnabled(flag);
        btn_8.setEnabled(flag);
        btn_9.setEnabled(flag);
        btn_11.setEnabled(flag);
        btn_12.setEnabled(flag);
        btn_back.setEnabled(flag);        
        btn_dial.setEnabled(flag);
        btn_clear.setEnabled(flag);
        btn_logout.setEnabled(flag);        
        txt_phonenum.setEnabled(flag);        
        MenuItem_exit.setEnabled(flag);
        MenuItem_logout.setEnabled(flag);
        MenuItem_changepwd.setEnabled(flag);
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
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MenuItem_about;
    private javax.swing.JMenuItem MenuItem_changepwd;
    public javax.swing.JMenuItem MenuItem_exit;
    public javax.swing.JMenuItem MenuItem_logout;
    private javax.swing.JMenuItem MenuItem_setting;
    private javax.swing.JPanel Panel1;
    private javax.swing.JButton btn_0;
    private javax.swing.JButton btn_1;
    private javax.swing.JButton btn_11;
    private javax.swing.JButton btn_12;
    private javax.swing.JButton btn_2;
    private javax.swing.JButton btn_3;
    private javax.swing.JButton btn_4;
    private javax.swing.JButton btn_5;
    private javax.swing.JButton btn_6;
    private javax.swing.JButton btn_7;
    private javax.swing.JButton btn_8;
    private javax.swing.JButton btn_9;
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_clear2;
    public javax.swing.JButton btn_dial;
    public javax.swing.JButton btn_feedback;
    public javax.swing.JButton btn_logout;
    public javax.swing.JButton btn_new;
    public javax.swing.JToggleButton btn_pause;
    public javax.swing.JButton btn_update;
    public javax.swing.JComboBox cb_gender;
    public javax.swing.JComboBox cb_type;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lb_agentid;
    private javax.swing.JLabel lb_agentname;
    public javax.swing.JLabel lb_callduration;
    private javax.swing.JLabel lb_extension;
    public javax.swing.JLabel lb_logintime;
    private javax.swing.JLabel lb_queue;
    public javax.swing.JLabel lb_status;
    private javax.swing.JLabel lb_version;
    public javax.swing.JLabel lb_workTime;
    private javax.swing.JTabbedPane main_tab;
    private javax.swing.JPanel panel_dial;
    private javax.swing.JPanel panel_number;
    public javax.swing.JTable table_report;
    public javax.swing.JTextField txt_add;
    public javax.swing.JTextField txt_email;
    public javax.swing.JTextField txt_email1;
    public javax.swing.JTextField txt_makh;
    public javax.swing.JTextField txt_mobile;
    public javax.swing.JTextField txt_name;
    public javax.swing.JTextField txt_phone1;
    public javax.swing.JTextField txt_phone2;
    private javax.swing.JTextField txt_phonenum;
    // End of variables declaration//GEN-END:variables
}
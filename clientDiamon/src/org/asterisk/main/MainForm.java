/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

//import com.jniwrapper.win32.ie.Browser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    public  Agent agentClient;
    private TrayIcon trayIcon;
    private SystemTray stray;    
    public  AgentObject agentObject;
    private Utility uti;
    public String dialNumber = "";
    public String callDuration = "00:00:00";

    private  String filename = "infor.properties";
    private  String Mysql_server = "172.168.10.208";      
    private  String Mysql_dbname = "cti_database";
    private  String Mysql_user = "cti";
    private  String Mysql_pwd  = "123456"; 
    private ConnectDatabase con;
    public ChangepwdForm chanpwdform;
//    private FeedbackForm feedback;

    /**
     * Creates new form MainForm2
     */
    public MainForm() {
        initComponents();
        txt_phonenum.setHorizontalAlignment(javax.swing.JLabel.RIGHT);   
        Image image = Toolkit.getDefaultToolkit().getImage("images/icon_main.png");
        this.setIconImage(image);
        Color green2 = new Color(0, 238, 0);
        Color royalBlue = new Color(65, 105, 225);
        Color Blue1 = new Color(0, 0, 225);
        btn_dial.setBackground(royalBlue);
        btn_clear.setToolTipText("Clear");
        btn_back.setToolTipText("Back");        
    }
    
    public MainForm(Agent agent, AgentObject aOb) {
        initComponents();        
        uti = new Utility();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );        
        agentClient = agent;
        agentObject = aOb;        
        Image image = Toolkit.getDefaultToolkit().getImage("images/icon_main.png");
        this.setIconImage(image);
//        setUndecorated(true);
//        if (SystemTray.isSupported()) {
//            MouseListener mouseListener = new MouseListener() {
//                public void mouseClicked(MouseEvent e) {
//                    System.out.println("Tray Icon – Mouse clicked!");
//                    setVisible(true);
//                    stray.remove(trayIcon);
//                }
//                public void mouseEntered(MouseEvent e) {
////                System.out.println("Tray Icon – Mouse entered!");
//                }
//                public void mouseExited(MouseEvent e) {
////                System.out.println("Tray Icon – Mouse exited!");
//                }
//                public void mousePressed(MouseEvent e) {
////                System.out.println("Tray Icon – Mouse pressed!");
//                }
//                public void mouseReleased(MouseEvent e) {
////                System.out.println("Tray Icon – Mouse released!");
//                }
//            };                                    
//            stray = SystemTray.getSystemTray();                        
//            trayIcon = new TrayIcon(image, "CTI Client");
//            trayIcon.setImageAutoSize(true);
//            trayIcon.addMouseListener(mouseListener);     
////
//        } else {
//            System.out.println("system tray not supported");
//        }        
        lb_agentid.setText(agentObject.getAgentId());
        lb_agentname.setText(agentObject.getAgentName());
        lb_extension.setText(agentObject.getInterface());
        lb_queue.setText(agentObject.getQueueId()+" - "+agentObject.getQueueName());
        lb_logintime.setText(uti.getDatetimeNow());         
        lb_version.setHorizontalAlignment(javax.swing.JLabel.RIGHT);
        lb_version.setVerticalAlignment(javax.swing.JLabel.CENTER);
        txt_phonenum.setHorizontalAlignment(javax.swing.JLabel.RIGHT);  
        Color green2 = new Color(0, 238, 0);
        Color white = new Color(255, 255, 255);
        Color royalBlue = new Color(65, 105, 225);
        Color red1 = new Color(255, 0, 0);        
        Color Blue1 = new Color(0, 0, 225);
//        btn_hangup.setBackground(white);
//        btn_dial.setBackground(white);        
        btn_clear.setToolTipText("Clear");
        btn_back.setToolTipText("Back");
        Image image2 = Toolkit.getDefaultToolkit().getImage("images/icon_pause.png");
        Icon ic = new ImageIcon(image2);
        btn_pause.setIcon(ic);         
        try{
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
        }catch(Exception e){
        }
        
        this.setTitle("Agent Desktop .::. "+agentObject.getAgentId()+" _ "+agentObject.getAgentName());
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
        jLabel13 = new javax.swing.JLabel();
        lb_workTime = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_logintime = new javax.swing.JLabel();
        btn_pause = new javax.swing.JToggleButton();
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
        jLabel4 = new javax.swing.JLabel();
        lb_callernumber = new javax.swing.JLabel();
        panel_dial = new javax.swing.JPanel();
        panel_number = new javax.swing.JPanel();
        btn_1 = new javax.swing.JButton();
        btn_2 = new javax.swing.JButton();
        btn_4 = new javax.swing.JButton();
        btn_5 = new javax.swing.JButton();
        btn_7 = new javax.swing.JButton();
        btn_8 = new javax.swing.JButton();
        btn_0 = new javax.swing.JButton();
        btn_11 = new javax.swing.JButton();
        txt_phonenum = new javax.swing.JTextField();
        btn_3 = new javax.swing.JButton();
        btn_6 = new javax.swing.JButton();
        btn_9 = new javax.swing.JButton();
        btn_12 = new javax.swing.JButton();
        btn_back = new javax.swing.JButton();
        btn_dial = new javax.swing.JButton();
        btn_clear = new javax.swing.JButton();
        btn_hangup = new javax.swing.JButton();
        lb_version = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuItem_logout = new javax.swing.JMenuItem();
        MenuItem_setting = new javax.swing.JMenuItem();
        MenuItem_changepwd = new javax.swing.JMenuItem();
        MenuItem_exit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        MenuItem_info = new javax.swing.JMenuItem();

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
        setTitle("Agent Desktop");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btn_logout.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/logoutbtn.png"))); // NOI18N
        btn_logout.setPreferredSize(new java.awt.Dimension(115, 45));
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Work Time");

        lb_workTime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_workTime.setText("00:00:00");
        lb_workTime.setPreferredSize(new java.awt.Dimension(51, 15));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Login at");

        lb_logintime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_logintime.setText("2012-12-12 08:00:00");

        btn_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/icon_unpause.png"))); // NOI18N
        btn_pause.setPreferredSize(new java.awt.Dimension(115, 45));
        btn_pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pauseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_pause, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_logintime, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lb_workTime, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_logintime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_workTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_pause, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_logout, btn_pause});

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agent Status", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("AgentID");
        jLabel3.setPreferredSize(new java.awt.Dimension(74, 19));

        lb_agentid.setBackground(new java.awt.Color(255, 255, 204));
        lb_agentid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_agentid.setText("8001");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Extension");
        jLabel5.setMaximumSize(new java.awt.Dimension(75, 20));
        jLabel5.setPreferredSize(new java.awt.Dimension(74, 19));

        lb_extension.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_extension.setText("6789");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Agent Name");

        lb_agentname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_agentname.setText("Nguyen Van A");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Queue");

        lb_queue.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_queue.setText("8888 - Ban Hang");

        lb_status.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lb_status.setForeground(new java.awt.Color(0, 204, 0));
        lb_status.setText("Ready");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Call Duration");

        lb_callduration.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lb_callduration.setForeground(new java.awt.Color(255, 0, 0));
        lb_callduration.setText("00:00:00");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Caller Number");

        lb_callernumber.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lb_callernumber.setForeground(new java.awt.Color(51, 51, 255));

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
                        .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(7, 7, 7)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lb_agentid, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lb_extension, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_queue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_callduration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_callernumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_extension, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_callduration, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_callernumber, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, lb_agentid, lb_agentname, lb_callernumber, lb_extension, lb_queue});

        panel_dial.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agent Dial Out", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        btn_1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_1.setText("1");
        btn_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_1ActionPerformed(evt);
            }
        });

        btn_2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_2.setText("2");
        btn_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_2ActionPerformed(evt);
            }
        });

        btn_4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_4.setText("4");
        btn_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_4ActionPerformed(evt);
            }
        });

        btn_5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_5.setText("5");
        btn_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_5ActionPerformed(evt);
            }
        });

        btn_7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_7.setText("7");
        btn_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_7ActionPerformed(evt);
            }
        });

        btn_8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_8.setText("8");
        btn_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_8ActionPerformed(evt);
            }
        });

        btn_0.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_0.setText("0");
        btn_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_0ActionPerformed(evt);
            }
        });

        btn_11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_11.setText("*");
        btn_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_11ActionPerformed(evt);
            }
        });

        txt_phonenum.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txt_phonenum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_phonenumKeyPressed(evt);
            }
        });

        btn_3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_3.setText("3");
        btn_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_3ActionPerformed(evt);
            }
        });

        btn_6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_6.setText("6");
        btn_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_6ActionPerformed(evt);
            }
        });

        btn_9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_9.setText("9");
        btn_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_9ActionPerformed(evt);
            }
        });

        btn_12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_12.setText("#");
        btn_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_numberLayout = new javax.swing.GroupLayout(panel_number);
        panel_number.setLayout(panel_numberLayout);
        panel_numberLayout.setHorizontalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_numberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_phonenum)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addComponent(btn_1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_2))
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addComponent(btn_4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_5))
                            .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panel_numberLayout.createSequentialGroup()
                                    .addComponent(btn_11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btn_0))
                                .addGroup(panel_numberLayout.createSequentialGroup()
                                    .addComponent(btn_7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(btn_8))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btn_9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_12, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_0, btn_1, btn_11, btn_12, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9});

        panel_numberLayout.setVerticalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_phonenum, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_0, btn_1, btn_11, btn_12, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9});

        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/back_icon.png.gif"))); // NOI18N
        btn_back.setPreferredSize(new java.awt.Dimension(50, 25));
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        btn_dial.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_dial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/callbtn3.png"))); // NOI18N
        btn_dial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dialActionPerformed(evt);
            }
        });

        btn_clear.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_clear.setText("C");
        btn_clear.setPreferredSize(new java.awt.Dimension(50, 25));
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        btn_hangup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/endcall.png"))); // NOI18N
        btn_hangup.setEnabled(false);
        btn_hangup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hangupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_dialLayout = new javax.swing.GroupLayout(panel_dial);
        panel_dial.setLayout(panel_dialLayout);
        panel_dialLayout.setHorizontalGroup(
            panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_dialLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(panel_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_dialLayout.createSequentialGroup()
                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 48, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btn_dial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_dialLayout.setVerticalGroup(
            panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_dialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(panel_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        lb_version.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        lb_version.setText("version 1.0.0 (Build 10012013)");
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

        MenuItem_setting.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        MenuItem_setting.setText("Settings");
        MenuItem_setting.setEnabled(false);
        jMenu1.add(MenuItem_setting);

        MenuItem_changepwd.setText("Change Password");
        MenuItem_changepwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_changepwdActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItem_changepwd);

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
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        MenuItem_info.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        MenuItem_info.setText("Information");
        MenuItem_info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItem_infoActionPerformed(evt);
            }
        });
        jMenu2.add(MenuItem_info);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lb_version, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panel_dial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_dial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_version, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
//        try{
//            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
//            stray.add(trayIcon); 
//            setVisible(false);
//            System.out.println("Close Main Form");  
//        }catch(Exception e){
//            System.out.println("Exception(formWindowClosing): "+e);
//        }      
    }//GEN-LAST:event_formWindowClosing

    private void MenuItem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_exitActionPerformed
        // TODO add your handling code here:
        try{
            String cmd  = "112";
            agentClient.sendtoServer(cmd);            
            System.out.println("Exit CTI CLIENT"); 
            System.exit(0);
            agentClient.closeConnect();
        }catch(Exception e){
        }        
    }//GEN-LAST:event_MenuItem_exitActionPerformed

    private void MenuItem_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_logoutActionPerformed
        // TODO add your handling code here:
        try{
            String cmd  = "102";
            agentClient.sendtoServer(cmd);
            Thread.sleep(3000);
            System.out.println("Logout program");   
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
        if(!"".equalsIgnoreCase(dialNumber))
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
                JOptionPane.showMessageDialog(this,"Just use number. Please!");                
//            if(uti.checkNumber(dialNumber)){
//                String cmd  = "108@"+dialNumber;
//                agentClient.sendtoServer(cmd);
//                System.out.println("Dial number: "+dialNumber);                                
//            }else
//                JOptionPane.showMessageDialog(this,"Just use number!");
        }catch(Exception e){
        }        
    }//GEN-LAST:event_btn_dialActionPerformed

    private void txt_phonenumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_phonenumKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            btn_dialActionPerformed(null);
        }
        
    }//GEN-LAST:event_txt_phonenumKeyPressed

    private void MenuItem_infoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_infoActionPerformed
        // TODO add your handling code here:        
        AboutForm about = new AboutForm();
        about.setVisible(true);
    }//GEN-LAST:event_MenuItem_infoActionPerformed

    private void MenuItem_changepwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_changepwdActionPerformed
        // TODO add your handling code here:
        chanpwdform = new ChangepwdForm(agentClient, agentObject);
        chanpwdform.setVisible(true);
    }//GEN-LAST:event_MenuItem_changepwdActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
        System.out.println("About form open");
         AboutForm about = new AboutForm();
         about.setVisible(true);
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void btn_hangupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hangupActionPerformed
        // TODO add your handling code here:
        try{
            String command = "106";
            agentClient.sendtoServer(command);
        }catch(Exception e){
        
        }
    }//GEN-LAST:event_btn_hangupActionPerformed

    private void btn_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pauseActionPerformed
        // TODO add your handling code here:
        try{
            String cmd = "";
            if(btn_pause.isSelected() && btn_pause.isEnabled()){
//                btn_pause2.setText("UNPAUSE");
                cmd  = "104@off";
                agentClient.sendtoServer(cmd);
            }else{
//                btn_pause2.setText("PAUSE");    
                cmd  = "104@on";
                agentClient.sendtoServer(cmd);  
                btn_logout.setEnabled(true);
                setAllEnable(true);
                lb_status.setText("Ready");
            }
        }catch(Exception e){
            
        }         
    }//GEN-LAST:event_btn_pauseActionPerformed

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
        btn_dial.setEnabled(flag);
//        btn_dial2.setEnabled(flag);
        btn_clear.setEnabled(flag);
        txt_phonenum.setEnabled(flag);
        btn_logout.setEnabled(flag);
        btn_back.setEnabled(flag);
//            MenuItem_setting.setEnabled(flag);
//        MenuItem_exit.setEnabled(flag);
        MenuItem_logout.setEnabled(flag);
        MenuItem_changepwd.setEnabled(flag);
    }
    
    public void setPauseIcon(boolean flag){
        if(flag){
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_unpause.png");
            Icon ic = new ImageIcon(image);
            btn_pause.setIcon(ic);            
        }else{
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_pause.png");
            Icon ic = new ImageIcon(image);
            btn_pause.setIcon(ic);            
        }
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
    private javax.swing.JMenuItem MenuItem_changepwd;
    public javax.swing.JMenuItem MenuItem_exit;
    private javax.swing.JMenuItem MenuItem_info;
    public javax.swing.JMenuItem MenuItem_logout;
    private javax.swing.JMenuItem MenuItem_setting;
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
    public javax.swing.JButton btn_dial;
    public javax.swing.JButton btn_hangup;
    public javax.swing.JButton btn_logout;
    public javax.swing.JToggleButton btn_pause;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lb_agentid;
    private javax.swing.JLabel lb_agentname;
    public javax.swing.JLabel lb_callduration;
    public javax.swing.JLabel lb_callernumber;
    private javax.swing.JLabel lb_extension;
    public javax.swing.JLabel lb_logintime;
    private javax.swing.JLabel lb_queue;
    public javax.swing.JLabel lb_status;
    private javax.swing.JLabel lb_version;
    public javax.swing.JLabel lb_workTime;
    private javax.swing.JPanel panel_dial;
    private javax.swing.JPanel panel_number;
    public javax.swing.JTextField txt_phonenum;
    // End of variables declaration//GEN-END:variables
}

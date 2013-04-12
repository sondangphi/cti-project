/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import nttnetworks.com.controls.IPanelTabEvent;
import nttnetworks.com.controls.TabCloseIcon;
import nttnetworks.com.controls.panelTab1;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskQueueEntry;
import org.asteriskjava.live.AsteriskQueueListener;
import org.asteriskjava.live.AsteriskQueueMember;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.AsteriskServerListener;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.MeetMeUser;
import org.asteriskjava.live.internal.AsteriskAgentImpl;


/**
 *
 * @author leehoa
 */
public class MainForm extends javax.swing.JFrame implements AsteriskServerListener, AsteriskQueueListener{
     
    private LoginForm loginform;
    private Agent agentClient;
    private TrayIcon trayIcon;
    private SystemTray stray;    
    static AgentObject agentObject;
    private Utility uti;
    private String dialNumber = "";
    public String callDuration = "00:00:00";

    private  String filename = "infor.properties";
    private  String Mysql_server = "172.168.10.202";      
    private  String Mysql_dbname = "ast_callcenter";
    private  String Mysql_user = "callcenter";
    private  String Mysql_pwd  = "callcenter"; 
    private ConnectDatabase con;

    private String CallPhone;
    private JComboBox out;
    LocateMap locate = new LocateMap();
    public ChangepwdForm chanpwdform;
    private final String EXIT = "112";
    private final String PAUSE = "104@off";
    private final String UNPAUSE = "104@on";
    private AsteriskServer asteriskServer;
    public HashMap <String,panelTab1> mapAgent=new HashMap<>();
    private String Agent_loged;
    private String imagesFolder = "data/images/";
    
    public MainForm() {
        initComponents();
        txt_phonenum.setHorizontalAlignment(javax.swing.JLabel.RIGHT);   
    }
    
    public MainForm(Agent agent, AgentObject aOb) {
        initComponents(); 

        uti = new Utility();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );        
        agentClient = agent;
        agentObject = aOb;        
        Image image = Toolkit.getDefaultToolkit().getImage(imagesFolder+"icon_main.png");
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
            System.out.println("connect database");
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
        }catch(Exception e){
        }        
        this.setTitle("Xorcom Agent Desktop _ "+agentObject.getAgentId()+" _ "+agentObject.getAgentName());
        setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.white);      

        showAgent();
       
         NativeInterface.open();
         
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JComponent c=createBrowserUnknowTab();
                c.setSize(jPanel18.getWidth() ,jPanel18.getHeight());
                jPanel18.add(c);
                
                jPanel18.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent ce) {
                        c.setSize(jPanel18.getWidth() ,jPanel18.getHeight());
                    }
                });
            }
          });

        
//     
      addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent ce) {
                jPanel18.setSize(main_tab.getWidth(), main_tab.getHeight());
            }
      });
       
    }    

    public void updateNumber(){
        txt_phonenum.setText(dialNumber);
    }
    
    public void deleteNumber(){
        System.out.println("del: "+dialNumber);
        dialNumber  = txt_phonenum.getText();
        if(!"".equalsIgnoreCase(dialNumber)){
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
        main_tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
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
        jLabel12 = new javax.swing.JLabel();
        lb_logintime = new javax.swing.JLabel();
        btn_pause = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        lb_callduration1 = new javax.swing.JLabel();
        btn_logout = new javax.swing.JButton();
        lb_version = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        function_tab = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
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
        btn_clear = new javax.swing.JButton();
        btn_dial = new javax.swing.JButton();
        btn_hangup = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        list_transfer = new javax.swing.JList();
        btn_transfercall = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tblShowAgent = new javax.swing.JTable();
        tabPChat = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
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
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        main_tab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                main_tabStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 823, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        main_tab.addTab("Inbound Call", jPanel1);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 819, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        jButton1.setText("Call");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Status :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)))
        );

        main_tab.addTab("Outbound Call", jPanel3);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 823, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 570, Short.MAX_VALUE)
        );

        main_tab.addTab("Knowledge Base", jPanel18);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Information"));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("AgentID");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jLabel3.setPreferredSize(new java.awt.Dimension(74, 19));

        lb_agentid.setBackground(new java.awt.Color(255, 255, 204));
        lb_agentid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_agentid.setText("800001");
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

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Login at");

        lb_logintime.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_logintime.setText("2012-12-12 08:00:00");

        btn_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/pausebtn.png"))); // NOI18N
        btn_pause.setToolTipText("pause");
        btn_pause.setPreferredSize(new java.awt.Dimension(147, 46));
        btn_pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pauseActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Call Duration");

        lb_callduration1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lb_callduration1.setForeground(new java.awt.Color(255, 0, 0));
        lb_callduration1.setText("00:00:00");

        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/logoutbtn.png"))); // NOI18N
        btn_logout.setOpaque(false);
        btn_logout.setPreferredSize(new java.awt.Dimension(147, 46));
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_queue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_agentname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lb_extension, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lb_agentid, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(lb_logintime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_callduration1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_pause, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_logout, btn_pause});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_agentid)
                    .addComponent(lb_status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_extension, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_logintime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_callduration1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_pause, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        lb_version.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        lb_version.setText("Version 1.0.0 Build 10012013");
        lb_version.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        function_tab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                function_tabStateChanged(evt);
            }
        });

        jPanel10.setOpaque(false);

        panel_number.setOpaque(false);
        panel_number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                panel_numberKeyPressed(evt);
            }
        });

        btn_1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/1.png"))); // NOI18N
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
        btn_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/2.png"))); // NOI18N
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
        btn_3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/3.png"))); // NOI18N
        btn_3.setOpaque(false);
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
        btn_4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/4.png"))); // NOI18N
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
        btn_5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/5.png"))); // NOI18N
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
        btn_6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/6.png"))); // NOI18N
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
        btn_7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/7.png"))); // NOI18N
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
        btn_8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/8.png"))); // NOI18N
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
        btn_9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/9.png"))); // NOI18N
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
        btn_0.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/0.png"))); // NOI18N
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
        btn_11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/11.png"))); // NOI18N
        btn_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_11ActionPerformed(evt);
            }
        });

        btn_12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/12.png"))); // NOI18N
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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_phonenumKeyTyped(evt);
            }
        });

        btn_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/123070_42045_16_back_icon.png.gif"))); // NOI18N
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        btn_clear.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/clear.png"))); // NOI18N
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });

        btn_dial.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_dial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/callbtn3.png"))); // NOI18N
        btn_dial.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_dial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_dialActionPerformed(evt);
            }
        });

        btn_hangup.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_hangup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/endcall.png"))); // NOI18N
        btn_hangup.setEnabled(false);
        btn_hangup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hangupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_numberLayout = new javax.swing.GroupLayout(panel_number);
        panel_number.setLayout(panel_numberLayout);
        panel_numberLayout.setHorizontalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panel_numberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txt_phonenum, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_numberLayout.createSequentialGroup()
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn_11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel_numberLayout.createSequentialGroup()
                                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_numberLayout.setVerticalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_phonenum, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btn_3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(btn_2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btn_12, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(btn_11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_0, btn_8});

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        function_tab.addTab("Keypad", jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        list_transfer.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane9.setViewportView(list_transfer);

        btn_transfercall.setText("Transfer");
        btn_transfercall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transfercallActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addContainerGap())
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btn_transfercall)
                .addContainerGap(86, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_transfercall, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        function_tab.addTab("Transfer", jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        tblShowAgent.setModel(new javax.swing.table.DefaultTableModel(
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
        tblShowAgent.setShowHorizontalLines(false);
        tblShowAgent.setShowVerticalLines(false);
        tblShowAgent.getTableHeader().setResizingAllowed(false);
        tblShowAgent.getTableHeader().setReorderingAllowed(false);
        tblShowAgent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShowAgentMouseClicked(evt);
            }
        });
        tblShowAgent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblShowAgentKeyReleased(evt);
            }
        });
        jScrollPane10.setViewportView(tblShowAgent);

        tabPChat.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
            .addComponent(tabPChat)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPChat, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
        );

        function_tab.addTab("Live Chat", jPanel12);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(function_tab)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(function_tab)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jMenuBar1.setOpaque(false);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_version, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(main_tab)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_version, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(main_tab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
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
        try{
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
            stray.add(trayIcon); 
            setVisible(false);
            disconnectAsterisk();
            System.out.println("Close Main Form");  
        }catch(Exception e){
            System.out.println("Exception(formWindowClosing): "+e);
        }      
    }//GEN-LAST:event_formWindowClosing

    private void MenuItem_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_exitActionPerformed
        try{
            new Thread(new Runnable() {
                @Override
                public void run() {                    
                    int option = showDialog("Comfirm", "Do you realy want to EXIT program?");
                    if(option == 0){
                        try {                            
                            agentClient.sendtoServer(EXIT);
                            agentClient.agentLogout();
                            agentClient.closeConnect();
                            System.exit(0);
                        } catch (Exception ex) {                           
                        }                                 
                    }else if(option == 1){
                        System.out.println("CANCEL: "+option);
                    }else{
                        System.out.println("CLOSE: "+option);
                    }                    
                }
            },"MenuItem_exit").start();
        }catch(Exception e){
        }        
    }//GEN-LAST:event_MenuItem_exitActionPerformed

    private void MenuItem_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_logoutActionPerformed
        try{
            btn_logoutActionPerformed(null);           
        }catch(Exception e){
        }        
    }//GEN-LAST:event_MenuItem_logoutActionPerformed

    private void btn_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_1ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "1";
        updateNumber();
        uti.playSounds();        
    }//GEN-LAST:event_btn_1ActionPerformed

    private void btn_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_2ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "2";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_2ActionPerformed

    private void btn_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_3ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "3";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_3ActionPerformed

    private void btn_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_4ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "4";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_4ActionPerformed

    private void btn_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_5ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "5";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_5ActionPerformed

    private void btn_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_6ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "6";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_6ActionPerformed

    private void btn_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_7ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "7";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_7ActionPerformed

    private void btn_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_8ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "8";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_8ActionPerformed

    private void btn_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_9ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "9";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_9ActionPerformed

    private void btn_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_0ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += "0";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_0ActionPerformed

    private void btn_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_11ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_11.getText();
        updateNumber(); 
        uti.playSounds();
    }//GEN-LAST:event_btn_11ActionPerformed

    private void btn_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_12ActionPerformed
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_12.getText();
        updateNumber();        
        uti.playSounds();
    }//GEN-LAST:event_btn_12ActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        deleteNumber();
//        uti.playSounds();
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        dialNumber = "";
        txt_phonenum.setText(dialNumber);
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_dialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_dialActionPerformed
        try{
            dialNumber = txt_phonenum.getText();
            if(dialNumber != "" && uti.checkNumber(dialNumber)){
                String cmd  = "108@"+dialNumber;
                agentClient.sendtoServer(cmd);
                System.out.println("Dial number: "+dialNumber);
                uti.playSounds();
            }else{
                System.out.println("not allow: "+dialNumber);
                new Thread(new Runnable() {
                    @Override
                    public void run() {   
                        showDialog("Just use number.");
                    }
                },"btn_dial").start();                
            }
        }catch(Exception e){
        }                       
    }//GEN-LAST:event_btn_dialActionPerformed
    
    public void showDialog(String t){
        JOptionPane.showMessageDialog(this, t);
    }
    
    public int showDialog(String name, String data){
        int option = JOptionPane.showConfirmDialog(this,data,name,JOptionPane.YES_NO_OPTION);     
        return option;
    }    
    
    private void txt_phonenumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_phonenumKeyPressed
        if(evt.getKeyCode() == 10){
            btn_dialActionPerformed(null);
        }  
    }//GEN-LAST:event_txt_phonenumKeyPressed

    private void btn_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_1KeyPressed
        if(evt.getKeyCode() == 8){
             deleteNumber();
        }
    }//GEN-LAST:event_btn_1KeyPressed

    private void btn_2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_2KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_2KeyPressed

    private void btn_3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_3KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_3KeyPressed

    private void btn_4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_4KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_4KeyPressed

    private void btn_5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_5KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_5KeyPressed

    private void btn_6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_6KeyPressed
        
    }//GEN-LAST:event_btn_6KeyPressed

    private void btn_7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_7KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_7KeyPressed

    private void btn_8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_8KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_8KeyPressed

    private void btn_9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_9KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_9KeyPressed

    private void btn_0KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_0KeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_btn_0KeyPressed

    private void panel_numberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_panel_numberKeyPressed
         if(evt.getKeyCode() == 8){
             deleteNumber();
         }        
    }//GEN-LAST:event_panel_numberKeyPressed

    private void MenuItem_changepwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_changepwdActionPerformed
        chanpwdform = new ChangepwdForm(agentClient, agentObject);
        chanpwdform.setVisible(true);
    }//GEN-LAST:event_MenuItem_changepwdActionPerformed

    private void MenuItem_aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItem_aboutActionPerformed
        AboutForm about = new AboutForm();
        about.setVisible(true);
    }//GEN-LAST:event_MenuItem_aboutActionPerformed

    private void btn_hangupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hangupActionPerformed
        try{
            String command = "106";
            agentClient.sendtoServer(command);
            uti.playSounds();
        }catch(Exception e){
        
        }          
    }//GEN-LAST:event_btn_hangupActionPerformed

    private void btn_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pauseActionPerformed
        try{
            if(btn_pause.isSelected() && btn_pause.isEnabled()){                            
                agentClient.sendtoServer(PAUSE);
            }else{                                
                agentClient.sendtoServer(UNPAUSE);  
            }
        }catch(Exception e){
            
        }         
    }//GEN-LAST:event_btn_pauseActionPerformed

    public void setPauseIcon(boolean flag){
        if(flag){
            Image image = Toolkit.getDefaultToolkit().getImage(imagesFolder+"icon_unpause.png");
            Icon ic = new ImageIcon(image);
            btn_pause.setIcon(ic);            
        }else{
            Image image = Toolkit.getDefaultToolkit().getImage(imagesFolder+"icon_pause.png");
            Icon ic = new ImageIcon(image);
            btn_pause.setIcon(ic);            
        }
    }    
    
    private void txt_phonenumKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_phonenumKeyTyped
       int key=evt.getKeyChar();
        String st=txt_phonenum.getText();
        String stTest="0123456789";
        if(key != KeyEvent.VK_BACK_SPACE &&key !=KeyEvent.VK_DELETE &&key !=KeyEvent.VK_ENTER)
        {
            int flag=0;
            if(stTest.indexOf(evt.getKeyChar())==-1)
            {
                flag++;
//                System.out.println("Must enter number only");
            }
            if (st.length()>10)
            {
                flag++;
//                System.out.println("Lenght 10 charater only");
            }
            if (flag>0)
            { 
                evt.consume();
            }
        }
    }//GEN-LAST:event_txt_phonenumKeyTyped

    private void function_tabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_function_tabStateChanged
        int index = function_tab.getSelectedIndex();
        if(index == 1){
            System.out.println("get transfer list");
            connectAsterisk();
            queueListener();
            agentAvailable();
        }else{
            System.out.println("get other function");
            disconnectAsterisk();
        }
    }//GEN-LAST:event_function_tabStateChanged

    private void btn_transfercallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transfercallActionPerformed
        try{
            String temp = (String)list_transfer.getSelectedValue();
            if(temp != null){
                String value = "";
                String status = "";
                value = temp.substring(0, temp.indexOf("-"));
                status = temp.substring(temp.indexOf("-") +1 , temp.length());
                System.out.println("value is: "+value);
                if(status.equalsIgnoreCase("READY"))
                {   agentClient.sendtoServer("114@"+value); }   
                else
                {   System.out.println("status is: "+status);}
            }
        }catch(Exception ex){
        }        
    }//GEN-LAST:event_btn_transfercallActionPerformed

    private void tblShowAgentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShowAgentMouseClicked
        int i=tblShowAgent.getSelectedRow();
        if(i>=0)
        {
            createPopupMenu();
            if(evt.getClickCount()==2)
            {
                show_chat();
            }
        }
    }//GEN-LAST:event_tblShowAgentMouseClicked

    private void tblShowAgentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblShowAgentKeyReleased
        if(evt.getKeyCode() == 10){
            show_chat();
        }
    }//GEN-LAST:event_tblShowAgentKeyReleased

    private void main_tabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_main_tabStateChanged
      
    }//GEN-LAST:event_main_tabStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    public void setAllEnable(boolean flag){
        Component comNumber [] = panel_number.getComponents();
        for(Component com : comNumber){
            com.setEnabled(flag);
        }
//        btn_0.setEnabled(flag);
//        btn_1.setEnabled(flag);
//        btn_2.setEnabled(flag);
//        btn_3.setEnabled(flag);
//        btn_4.setEnabled(flag);
//        btn_5.setEnabled(flag);
//        btn_6.setEnabled(flag);
//        btn_7.setEnabled(flag);
//        btn_8.setEnabled(flag);
//        btn_9.setEnabled(flag);
//        btn_11.setEnabled(flag);
//        btn_12.setEnabled(flag);
//        btn_back.setEnabled(flag);        
//        btn_dial.setEnabled(flag);
//        btn_hangup.setEnabled(flag);
//        btn_clear.setEnabled(flag);
        btn_logout.setEnabled(flag);
//        txt_phonenum.setEnabled(flag);        
        MenuItem_logout.setEnabled(flag);
        MenuItem_changepwd.setEnabled(flag);        
    }
    
  
  
    
    public String GetStatus(String t)
    {
        return t;
    }
    
    
    public static JComponent createBrowserUnknowTab() {  
        final JPanel p=new JPanel(new BorderLayout());
        final JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(false);
        webBrowser.navigate("http://support.microsoft.com/kb/307759/en-us");
        p.add(webBrowser, BorderLayout.CENTER);
        p.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent ce) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {}
                webBrowser.setSize(p.getWidth(), p.getHeight());
            }
        });
        return p;
  }
  
    private void show_chat()
    {
          try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

            if(con.isConnect())
            {
                Agent_loged=lb_agentid.getText();
                int row=tblShowAgent.getSelectedRow();
                final String col1=""+tblShowAgent.getValueAt(row,1); 
                for(int i=0;i<tabPChat.getTabCount();i++)
                {
                    if(tabPChat.getTitleAt(i).equals(col1))
                    {
                        tabPChat.setSelectedIndex(i);
                        return;
                    }

                }
                final panelTab1 tab=new panelTab1();
                tab.events = new IPanelTabEvent() {
                    @Override
                    public void send() {
                        try {
                            String txt = tab.getText().trim();
                            while(txt.indexOf("  ")>=0) {
                                txt=txt.replace("  ", " ");
                            }
                            
                            if(!"".equals(txt)){
                                //send
                                
                                agentClient.sendtoServer("120@"+Agent_loged+"@"+col1+"@"+txt);
                                tab.showMessage(Agent_loged, tab.getText());
                                tab.send();
                            }
                        } catch (Exception ex) {}
                    }
                };
                if(tabPChat.getTabCount()<5)
                {
                     tabPChat.addTab(col1,new TabCloseIcon(), tab);
                }    
                int display=tabPChat.getTabCount()-1;
                tabPChat.setSelectedIndex(display);
                mapAgent.put(col1, tab);
            }
            con.closeConnect();
        }
        catch(Exception e)
        {

        }
    }
    public void receive(String from, String message) {
        Agent_loged=lb_agentid.getText();
        panelTab1 tabs = mapAgent.get(from);
        if (tabs == null) {
            
           final String agent=from;

                final panelTab1 tab=new panelTab1();
                tab.events = new IPanelTabEvent() {
                    @Override
                    public void send() {
                        try {
                            if(!"".equals(tab.getText())){
                                //send
                                agentClient.sendtoServer("120@"+Agent_loged+"@"+agent+"@"+tab.getText().replace("@", "&#64;"));
                                tab.showMessage(Agent_loged, tab.getText());
                                tab.send();
                            }
                        } catch (Exception ex) {
                            System.out.println("send paneltab "+ex);
                        }
                    }
                };

               
                tabPChat.addTab(agent,new TabCloseIcon(), tab);
                int display= tabPChat.getTabCount()-1;
                tabPChat.setSelectedIndex(display);

                mapAgent.put(agent, tab);
                tabs = tab;
       }
       
       tabs.showMessage(from, message);
   }
    class PopupListener extends MouseAdapter {
        JPopupMenu popup; 
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
 
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
     public void createPopupMenu() {
        JMenuItem menuItem;
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent ae) {
                show_chat();
               
             }
         });
        popup.add(menuItem);
        menuItem = new JMenuItem("...");
        popup.add(menuItem);
        MouseListener popupListener = new PopupListener(popup);
        tblShowAgent.addMouseListener(popupListener);
    }
   private void showAgent()
    {
      
       try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

            if(con.isConnect())
            {
                ResultSet result=null;
                String sql="SELECT * FROM `agent_status`";
//                String sql="SELECT * FROM `agent_status` AS s LEFT JOIN `agent_login` AS l"
//                                        + " ON s.`agent_id`=l.`agent_id` WHERE role='2'";
                result = con.executeQuery(sql);
                tblShowAgent.getTableHeader().setReorderingAllowed(false);
                String strHeader[]={"","","",""};
                final DefaultTableModel  dt=new DefaultTableModel(strHeader,0)
                {
                    @Override
                    public boolean isCellEditable(int i, int i1) {
                        return false;
                    }
                };
                ArrayList<Vector> st_online = new ArrayList<>();
                ArrayList<Vector> st_offline = new ArrayList<>();
                int i=0;
                int m=0;
                while (result.next()) { 
                    i++;
                    String agent=result.getString("agent_id");
                    String iface=result.getString("interface");
                    int queue=Integer.parseInt(result.getString("queue"));
                    
                    String showOnline="";
                    Pattern pattern = Pattern.compile("\\d*");
                    Matcher matcher = pattern.matcher(iface); 

                    if (matcher.matches())
                    { 
                          if(queue==0) 
                          {
                              showOnline=" now offline";
                              m=0;
                          }
                    } 
                    else
                    { 
                          showOnline=" now online";
                          m=1;
                    } 
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(agent);
                    rowdata.add(showOnline);
                    rowdata.add(Integer.toString(m));
                   
                    if(Integer.parseInt((String)rowdata.get(3)) == 1)     
                    {
                        st_online.add(rowdata);

                    } else       
                    {
                         st_offline.add(rowdata);
                    }
                   
                }
                for (int j=0; j<st_online.size(); j++) 
                {
                    dt.addRow(st_online.get(j));
                }
                for (int j=0; j<st_offline.size(); j++) 
                {
                    dt.addRow(st_offline.get(j));
                }
                tblShowAgent.setModel(dt);
                
                tblShowAgent.setDefaultRenderer(Object.class, new TableCellRenderer()
                {
                    final int currentRow = -1;
                    @Override
                    public Component getTableCellRendererComponent(JTable table, 
                                                                        Object value, 
                                                                        boolean isSelected, 
                                                                        boolean hasFocus, 
                                                                        int row, int column) 
                    {
                        JLabel out=new JLabel();
                        if (column == 0) 
                        {
                            out.setText(Integer.toString(row+1));
                        } 
                        else 
                        {
                            out.setText((String)value);
                        }
                        if (Integer.parseInt((String)dt.getValueAt(row, 3)) == 1)
                        {
                            out.setForeground(Color.red);
                        } 
                        else
                        {
                            out.setForeground(new Color(0x88, 0x88, 0x88, 0xff));
                        }
                        if (isSelected) 
                        {
                            out.setBackground(new Color(0x88, 0x88, 0x88, 0x88));
                            out.setOpaque(true);
                        }
                        return out;
                    }
                });
                TableColumn column = null;
                for (int k = 0;k < tblShowAgent.getColumnCount(); k++) 
                {
                    column = tblShowAgent.getColumnModel().getColumn(k);
                    if (k == 0) 
                    {
                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);
                    }
                    if (k == 3) 
                    {
                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);
                    } 
                }
                
               result.close(); 
            }
          con.closeConnect();
        
        }catch(Exception e)
        {
            
        }
    }
   public void popup(String Agent, String message)
   {
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect())
            {
                Agent_loged=lb_agentid.getText();
                final String agent=Agent;
                final panelTab1 tab=new panelTab1();
                tab.events = new IPanelTabEvent() {
                    @Override
                    public void send() {
                        try {
                            if(!"".equals(tab.getText())){
                                //send
                                agentClient.sendtoServer("120@"+Agent_loged+"@"+agent+"@"+tab.getText().replace("@", "&#64;"));
                                tab.showMessage(Agent_loged, tab.getText());
                                tab.send();
                            }
                        } catch (Exception ex) { }
                    }
                };
                if(tabPChat.getTabCount()<5)
                {
                    tabPChat.addTab(agent,new TabCloseIcon(), tab);
                }
                int display= tabPChat.getTabCount()-1;
                tabPChat.setSelectedIndex(display);
                function_tab.setSelectedIndex(2);
                mapAgent.put(agent, tab);
                tab.showMessage(agent, message);
            }
            con.closeConnect();
        }
        catch(Exception e)
        {

        }
   }
   
    private void connectAsterisk(){
        try{
            System.out.println("connect asterisk");
            asteriskServer = new DefaultAsteriskServer("172.168.10.202", "manager", "123456");
            asteriskServer.addAsteriskServerListener(this);             
        }catch(Exception ex){}
    }
    private void disconnectAsterisk(){         
        if(asteriskServer != null){
            System.out.println("disconnect asterisk");
            asteriskServer.removeAsteriskServerListener(this);   
            asteriskServer.shutdown();
            asteriskServer = null;
        }
    }    
    private void queueListener(){
        System.out.println("queueListener");
        for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()){
            asteriskQueue.addAsteriskQueueListener(this);
        }        
    }    
    private void agentAvailable(){
        System.out.println("agentAvailable");
        DefaultListModel listModel = new DefaultListModel();
        String iface = "SIP/"+agentObject.getInterface();
        if(list_transfer != null)
            list_transfer.removeAll();
        for(AsteriskQueue queue:asteriskServer.getQueues()){
            String QUEUE_NAME = queue.getName();
            int QUEUE_SIZE = queue.getMembers().size();
            if(!"default".equalsIgnoreCase(QUEUE_NAME) && QUEUE_SIZE > 0){
                for(AsteriskQueueMember member : queue.getMembers()){    
//                    System.out.println(member.getLocation());
//                    System.out.println(iface);                    
                    if(!iface.equalsIgnoreCase(member.getLocation())){ 
                        String status = "RINGING";
                        if(member.getState()!=null){
                            status = member.getState().name();
                            switch(status){
                                case "DEVICE_NOT_INUSE":
                                    status = "READY";
                                    break;
                                case "DEVICE_INUSE":
                                    status = "TALKING";
                                    break;   
                                case "DEVICE_UNAVAILABLE":
                                    status = "UNAVAILABLE";
                                    break;   
                                case "DEVICE_BUSY":
                                    status = "BUSY";
                                    break;
                                case "DEVICE_INVALID":
                                    status = "INVALID";
                                    break;    
                                case "DEVICE_UNKNOWN":
                                    status = "UNKNOWN";
                                    break;                                    
                                default:
                                    break;
                            }
                            if(member.isPaused()){
                                status = "PAUSE";
                            }                            
                            listModel.addElement(member.getLocation()+"-"+status);
                        }else{
                            listModel.addElement(member.getLocation()+"-"+status);
                        }                                                                                                   
                    }                        
                }           
            }            
        } 
        list_transfer.setModel(listModel);      
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
    //        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Windows".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
       
    
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }
    //<editor-fold defaultstate="collapsed" desc=" Variables declaration - do not modify       ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MenuItem_about;
    private javax.swing.JMenuItem MenuItem_changepwd;
    public javax.swing.JMenuItem MenuItem_exit;
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
    private javax.swing.JButton btn_transfercall;
    private javax.swing.JTabbedPane function_tab;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lb_agentid;
    private javax.swing.JLabel lb_agentname;
    public javax.swing.JLabel lb_callduration1;
    private javax.swing.JLabel lb_extension;
    public javax.swing.JLabel lb_logintime;
    private javax.swing.JLabel lb_queue;
    public javax.swing.JLabel lb_status;
    private javax.swing.JLabel lb_version;
    private javax.swing.JList list_transfer;
    private javax.swing.JTabbedPane main_tab;
    private javax.swing.JPanel panel_number;
    public javax.swing.JTabbedPane tabPChat;
    private javax.swing.JTable tblShowAgent;
    private javax.swing.JTextField txt_phonenum;
    // End of variables declaration//GEN-END:variables
 //</editor-fold>

    //AsteriskServerListener
    @Override
    public void onNewAsteriskChannel(AsteriskChannel channel) {
        System.out.println("onNewAsteriskChannel");
        agentAvailable();
    }

    @Override
    public void onNewMeetMeUser(MeetMeUser user) {
        System.out.println("onNewMeetMeUser");
    }

    @Override
    public void onNewAgent(AsteriskAgentImpl agent) {
        System.out.println("onNewAgent");
    }

    @Override
    public void onNewQueueEntry(AsteriskQueueEntry entry) {
        System.out.println("onNewQueueEntry");
        agentAvailable();
    }
    //AsteriskServerListener//

    //AsteriskQueueListener
    @Override
    public void onNewEntry(AsteriskQueueEntry entry) {
        System.out.println("onNewEntry");
        agentAvailable();
    }

    @Override
    public void onEntryLeave(AsteriskQueueEntry entry) {
        System.out.println("onEntryLeave");
//        agentAvailable();
    }

    @Override
    public void onMemberStateChange(AsteriskQueueMember member) {
        System.out.println("onMemberStateChange");
        agentAvailable();
    }

    @Override
    public void onEntryServiceLevelExceeded(AsteriskQueueEntry entry) {
        System.out.println("onEntryServiceLevelExceeded");
    }

    @Override
    public void onMemberAdded(AsteriskQueueMember member) {
        System.out.println("onMemberAdded");
        agentAvailable();
    }

    @Override
    public void onMemberRemoved(AsteriskQueueMember member) {
        System.out.println("onMemberRemoved");
        agentAvailable();
    }
    //AsteriskQueueListener//
}

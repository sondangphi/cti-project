/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import az.encoding.html.HtmlCoding;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import nttnetworks.com.controls.IPanelTabEvent;
import nttnetworks.com.controls.TabCloseIcon;
import nttnetworks.com.controls.panelTab1;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.DatePicker;
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
    public static FeedbackForm feedback;
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
    
    public MainForm() {
        initComponents();
        txt_phonenum.setHorizontalAlignment(javax.swing.JLabel.RIGHT);   
    }
    
    public MainForm(Agent agent, AgentObject aOb) {
        initComponents(); 
        tblCustom.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);                   
        uti = new Utility();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );        
        agentClient = agent;
        agentObject = aOb;        
        Image image = Toolkit.getDefaultToolkit().getImage("images/icon_main.png");
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
        showCampaign();  
        showAgent();
        for(int i=1;i<3;i++){
            main_tab.remove(1);
        }
         main_tab.setEnabledAt(main_tab.getTabCount()-1, false);
         NativeInterface.open();
         
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JComponent c=createBrowserUnknowTab();
                c.setSize(jPanel18.getWidth() ,jPanel18.getHeight());
                jPanel18.add(c);
            }
          });

        
//     
      
       
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
        jPanel2 = new javax.swing.JPanel();
        btn_logout = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        lb_workTime = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lb_logintime = new javax.swing.JLabel();
        btn_pause = new javax.swing.JToggleButton();
        main_tab = new javax.swing.JTabbedPane();
        Panel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_makh = new javax.swing.JTextField();
        txt_add = new javax.swing.JTextField();
        txt_mobile = new javax.swing.JTextField();
        txt_email = new javax.swing.JTextField();
        btn_feedback = new javax.swing.JButton();
        txt_name = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnMap = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_reg = new javax.swing.JTextField();
        txt_gender = new javax.swing.JTextField();
        txt_type = new javax.swing.JTextField();
        txt_birthday = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_report = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnViewFB = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnShowPro = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPromotions = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtContent = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        btnChooseStart = new javax.swing.JButton();
        btnChooseEnd = new javax.swing.JButton();
        txtTimeEnd = new javax.swing.JTextField();
        txtTimeStart = new javax.swing.JTextField();
        txtAutoSearchPro = new javax.swing.JTextField();
        chkTime = new javax.swing.JCheckBox();
        ChkName = new javax.swing.JCheckBox();
        btnClearPro = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        txtResultPro = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        txtResultCoop = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtAutoSearchCoop = new javax.swing.JTextField();
        btnClearCoop = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblCoop = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDetail = new javax.swing.JTextArea();
        btnShowCoop = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnDial = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCamp = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblCustom = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
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
        lb_version = new javax.swing.JLabel();
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

        jPanel2.setOpaque(false);

        btn_logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/logoutbtn.png"))); // NOI18N
        btn_logout.setOpaque(false);
        btn_logout.setPreferredSize(new java.awt.Dimension(147, 46));
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
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

        btn_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/pausebtn.png"))); // NOI18N
        btn_pause.setToolTipText("pause");
        btn_pause.setPreferredSize(new java.awt.Dimension(147, 46));
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
                .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btn_pause, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_logintime, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(lb_workTime)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_logout, btn_pause});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_logintime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lb_workTime))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btn_logout, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, Short.MAX_VALUE)
                        .addComponent(btn_pause, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel13, lb_workTime});

        main_tab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                main_tabStateChanged(evt);
            }
        });

        Panel1.setOpaque(false);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.Color.black));
        jPanel8.setOpaque(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Customer ID");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Name");
        jLabel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Address");
        jLabel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Gender");
        jLabel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Phone");
        jLabel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txt_makh.setEditable(false);
        txt_makh.setBackground(new java.awt.Color(255, 255, 255));
        txt_makh.setEnabled(false);

        txt_add.setEditable(false);
        txt_add.setBackground(new java.awt.Color(255, 255, 255));

        txt_mobile.setEditable(false);
        txt_mobile.setBackground(new java.awt.Color(255, 255, 255));

        txt_email.setEditable(false);
        txt_email.setBackground(new java.awt.Color(255, 255, 255));

        btn_feedback.setText("Feedback");
        btn_feedback.setEnabled(false);
        btn_feedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_feedbackActionPerformed(evt);
            }
        });

        txt_name.setEditable(false);
        txt_name.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Email");
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Birthday");
        jLabel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnMap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/map32x32.png"))); // NOI18N
        btnMap.setText("Map");
        btnMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMapActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("Type");
        jLabel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setText("Registration");
        jLabel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txt_reg.setEditable(false);
        txt_reg.setBackground(new java.awt.Color(255, 255, 255));

        txt_gender.setEditable(false);
        txt_gender.setBackground(new java.awt.Color(255, 255, 255));

        txt_type.setEditable(false);
        txt_type.setBackground(new java.awt.Color(255, 255, 255));

        txt_birthday.setEditable(false);
        txt_birthday.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_type)
                            .addComponent(txt_mobile, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel19)
                        .addGap(20, 20, 20)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_reg)
                            .addComponent(txt_birthday)
                            .addComponent(txt_email)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_makh, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                            .addComponent(txt_gender))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(35, 35, 35)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(btn_feedback)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                                .addComponent(btnMap, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel17});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_makh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(txt_birthday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_mobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_reg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMap, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(btn_feedback, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnMap, btn_feedback, jLabel10, jLabel11, jLabel14, jLabel18, jLabel19, jLabel4, jLabel9, txt_add, txt_birthday, txt_email, txt_gender, txt_makh, txt_mobile, txt_name, txt_reg, txt_type});

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
        table_report.setOpaque(false);
        table_report.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_reportMouseClicked(evt);
            }
        });
        table_report.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                table_reportKeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(table_report);

        jLabel1.setText("Call History");

        btnViewFB.setText("View");
        btnViewFB.setEnabled(false);
        btnViewFB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewFBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 741, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, Panel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnViewFB)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewFB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap())
        );

        main_tab.addTab("Mini CRM", Panel1);

        btnShowPro.setText("Show Data");
        btnShowPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowProActionPerformed(evt);
            }
        });

        tblPromotions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblPromotions.setEnabled(false);
        tblPromotions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPromotionsMouseClicked(evt);
            }
        });
        tblPromotions.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblPromotionsKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(tblPromotions);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Content"));

        txtContent.setEditable(false);
        txtContent.setColumns(20);
        txtContent.setLineWrap(true);
        txtContent.setRows(5);
        txtContent.setWrapStyleWord(true);
        jScrollPane7.setViewportView(txtContent);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Search by"));

        btnSearch.setText("Search");
        btnSearch.setEnabled(false);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnChooseStart.setText("Start Day");
        btnChooseStart.setEnabled(false);
        btnChooseStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseStartActionPerformed(evt);
            }
        });

        btnChooseEnd.setText("End Day");
        btnChooseEnd.setEnabled(false);
        btnChooseEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseEndActionPerformed(evt);
            }
        });

        txtTimeEnd.setEditable(false);
        txtTimeEnd.setEnabled(false);

        txtTimeStart.setEditable(false);
        txtTimeStart.setEnabled(false);

        txtAutoSearchPro.setEnabled(false);
        txtAutoSearchPro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAutoSearchProKeyTyped(evt);
            }
        });

        chkTime.setText(" Time");
        chkTime.setEnabled(false);
        chkTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chkTimeMouseClicked(evt);
            }
        });

        ChkName.setSelected(true);
        ChkName.setText("Search by Name");
        ChkName.setEnabled(false);
        ChkName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ChkNameMouseClicked(evt);
            }
        });

        btnClearPro.setText("Clear");
        btnClearPro.setEnabled(false);
        btnClearPro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearProActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(chkTime)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(ChkName)
                        .addGap(18, 18, 18)
                        .addComponent(txtAutoSearchPro))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTimeStart, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(txtTimeEnd))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(btnChooseStart)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(btnChooseEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnClearPro, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ChkName)
                    .addComponent(txtAutoSearchPro, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(chkTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimeStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseStart)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimeEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChooseEnd)
                    .addComponent(btnClearPro))
                .addContainerGap())
        );

        jLabel24.setText("Results were found");

        txtResultPro.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnShowPro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(18, 18, 18)
                        .addComponent(txtResultPro, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtResultPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowPro, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        main_tab.addTab("Promotions", jPanel1);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Field"));

        txtResultCoop.setEditable(false);
        txtResultCoop.setEnabled(false);

        jLabel20.setText("Results were found");

        txtAutoSearchCoop.setEnabled(false);
        txtAutoSearchCoop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAutoSearchCoopKeyTyped(evt);
            }
        });

        btnClearCoop.setText("Clear");
        btnClearCoop.setEnabled(false);
        btnClearCoop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearCoopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtAutoSearchCoop)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(9, 9, 9)
                        .addComponent(txtResultCoop, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnClearCoop, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(txtAutoSearchCoop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtResultCoop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClearCoop)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tblCoop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblCoop.setEnabled(false);
        tblCoop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCoopMouseClicked(evt);
            }
        });
        tblCoop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCoopKeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblCoop);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail"));

        txtDetail.setEditable(false);
        txtDetail.setColumns(20);
        txtDetail.setLineWrap(true);
        txtDetail.setRows(5);
        txtDetail.setWrapStyleWord(true);
        txtDetail.setEnabled(false);
        jScrollPane5.setViewportView(txtDetail);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btnShowCoop.setText("Show Data");
        btnShowCoop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCoopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnShowCoop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnShowCoop, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        main_tab.addTab("Co.op Systems", jPanel3);

        btnDial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/btn_dial.png"))); // NOI18N
        btnDial.setEnabled(false);
        btnDial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialActionPerformed(evt);
            }
        });

        tblCamp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblCamp.setRowHeight(20);
        tblCamp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCampMouseClicked(evt);
            }
        });
        tblCamp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCampKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblCamp);

        tblCustom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblCustom.setRowHeight(20);
        tblCustom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustomMouseClicked(evt);
            }
        });
        tblCustom.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblCustomKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(tblCustom);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/view-refresh.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDial, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDial, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        main_tab.addTab("Campaigns", jPanel5);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 772, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 483, Short.MAX_VALUE)
        );

        main_tab.addTab("Knowledge Base", jPanel18);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 772, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 483, Short.MAX_VALUE)
        );

        main_tab.addTab("Google Map", jPanel19);

        jPanel4.setOpaque(false);

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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Information"));
        jPanel6.setOpaque(false);

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
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lb_extension, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lb_agentid, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lb_callduration, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lb_queue, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_agentname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_agentid)
                    .addComponent(lb_status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_extension, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_queue, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_callduration, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lb_version.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        lb_version.setText("Version 1.0.0 Build 10012013");
        lb_version.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

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
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_numberLayout.createSequentialGroup()
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
                                .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txt_phonenum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_numberLayout.setVerticalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_phonenum)
                    .addComponent(btn_back, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_0, btn_8});

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_number, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_number, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(114, Short.MAX_VALUE))
        );

        function_tab.addTab("Keypad", jPanel10);

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
                .addContainerGap(96, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_transfercall, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        function_tab.addTab("Transfer", jPanel11);

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
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
            .addComponent(tabPChat)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPChat, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE))
        );

        function_tab.addTab("Live Chat", jPanel12);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_version, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(323, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(function_tab, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(main_tab, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(function_tab))
                    .addComponent(main_tab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lb_version, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_unpause.png");
            Icon ic = new ImageIcon(image);
            btn_pause.setIcon(ic);            
        }else{
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_pause.png");
            Icon ic = new ImageIcon(image);
            btn_pause.setIcon(ic);            
        }
    }    
    
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        showCampaign(); 
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void tblCustomKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCustomKeyReleased
        ShowButtonDial();
    }//GEN-LAST:event_tblCustomKeyReleased

    private void tblCustomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustomMouseClicked
        try{ 
            int row=tblCustom.getSelectedRow();
            String phone=(String)this.tblCustom.getValueAt(row, 6);
            if(phone==null)
            {
                 btnDial.setEnabled(false);
            }
            else
            {
                ShowButtonDial();
            }
        }catch(Exception e){}
        
    }//GEN-LAST:event_tblCustomMouseClicked

    private void tblCampKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCampKeyReleased
        showCustomer();
        btnDial.setEnabled(false);
    }//GEN-LAST:event_tblCampKeyReleased

    private void tblCampMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCampMouseClicked
        showCustomer();
        btnDial.setEnabled(false);
    }//GEN-LAST:event_tblCampMouseClicked

    private void btnDialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialActionPerformed
        Dial();
        try {
            agentClient.sendtoServer("108@"+CallPhone);
        } catch (Exception ex) {}
    }//GEN-LAST:event_btnDialActionPerformed

    private void btnShowCoopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCoopActionPerformed
        ShowCoopAction();
    }//GEN-LAST:event_btnShowCoopActionPerformed

    private void tblCoopKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCoopKeyReleased
        ClickTableCoop();
    }//GEN-LAST:event_tblCoopKeyReleased

    private void tblCoopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCoopMouseClicked
        ClickTableCoop();
    }//GEN-LAST:event_tblCoopMouseClicked

    private void btnClearCoopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearCoopActionPerformed
       txtAutoSearchCoop.setText("");
        txtResultCoop.setText("");
        txtDetail.setText("");
        ShowCoopAction();
    }//GEN-LAST:event_btnClearCoopActionPerformed

    private void txtAutoSearchCoopKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutoSearchCoopKeyTyped
        try{
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                String sql="SELECT * FROM coopmart_name WHERE name LIKE '%"+txtAutoSearchCoop.getText()+"%'";
                ResultSet result=con.executeQuery(sql);
                tblCoop.getTableHeader().setReorderingAllowed(false);
                String strHeader[]={"Number","Name","Address","Phone","Fax","Date of Establishment"};
                DefaultTableModel dt=new DefaultTableModel(strHeader,0)
                {
                    @Override
                    public boolean isCellEditable(int i, int i1) {
                        return false;
                    }
                };
                int i=0;
                while (result.next()) {
                    i++;
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(result.getString("name"));
                    rowdata.add(result.getString("address"));
                    rowdata.add(result.getString("phone"));
                    rowdata.add(result.getString("fax"));
                    rowdata.add(result.getString("date_establishment"));
                    dt.addRow(rowdata);
                }
                tblCoop.setModel(dt);
                TableColumn column = null;
                for (int k = 0;k < tblCoop.getColumnCount(); k++) {
                    column = tblCoop.getColumnModel().getColumn(k);
                    if (k == 0) 
                        column.setPreferredWidth(50);                    
                    else 
                        column.setPreferredWidth(100);                   
                }
                int j=dt.getRowCount();
                if(j==0){
                    txtResultCoop.setForeground(Color.RED);
                    txtResultCoop.setText("no record found");
                }else{
                    txtResultCoop.setForeground(Color.BLACK);
                    txtResultCoop.setText(Integer.toString(j));
                }
                result.close();
            }
            con.closeConnect();
        }
        catch(Exception e)
        {
            System.out.println("Error txtAutoSearchCoop :"+ e.toString());
        }
    }//GEN-LAST:event_txtAutoSearchCoopKeyTyped

    private void btnClearProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearProActionPerformed
        txtContent.setText("");
        txtAutoSearchPro.setText("");
        txtTimeStart.setText("");
        txtTimeEnd.setText("");
        ShowProAction();
    }//GEN-LAST:event_btnClearProActionPerformed

    private void ChkNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChkNameMouseClicked
        if(ChkName.isSelected())
        {
            btnClearPro.setEnabled(true);
            txtAutoSearchPro.setEnabled(true);
        }
        else
        {
            txtAutoSearchPro.setEnabled(false);
            if(chkTime.isSelected())
            {
            }
            else
            {
                btnClearPro.setEnabled(false);
            }
        }
    }//GEN-LAST:event_ChkNameMouseClicked

    private void chkTimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chkTimeMouseClicked
        if(chkTime.isSelected())
        {
            txtTimeStart.setEnabled(true);
            txtTimeEnd.setEnabled(true);
            btnChooseStart.setEnabled(true);
            btnChooseEnd.setEnabled(true);
            btnSearch.setEnabled(true);
            if(ChkName.isSelected())
            {
            }
            else
            {
                btnClearPro.setEnabled(true);
            }
        }
        else
        {
            txtTimeStart.setEnabled(false);
            txtTimeEnd.setEnabled(false);
            btnChooseStart.setEnabled(false);
            btnChooseEnd.setEnabled(false);
            btnSearch.setEnabled(false);
            if(ChkName.isSelected())
            {
                btnClearPro.setEnabled(true);
            }
            else
            {
                btnClearPro.setEnabled(false);
            }
        }
    }//GEN-LAST:event_chkTimeMouseClicked

    private void txtAutoSearchProKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutoSearchProKeyTyped
        try{
            tblPromotions.getTableHeader().setReorderingAllowed(false);
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                if(ChkName.isSelected())
                {
                    String sql="SELECT * FROM promotions WHERE name LIKE'%"+txtAutoSearchPro.getText()+"%'";
                    ResultSet result=con.executeQuery(sql);
                    String strHeader[]={"Number","Name","Time Start","Time End","Content","Location"};
                    DefaultTableModel dt =new DefaultTableModel(strHeader,0)
                    {
                        @Override
                        public boolean isCellEditable(int i, int i1) {
                            return false;
                        }
                    };
                    int i=0;
                    while (result.next()) {
                        i++;
                        Vector rowdata = new Vector();
                        rowdata.add(Integer.toString(i));
                        rowdata.add(result.getString("name"));
                        rowdata.add(result.getString("time_start"));
                        rowdata.add(result.getString("time_end"));
                        rowdata.add(result.getString("content"));
                        rowdata.add(result.getString("location"));
                        dt.addRow(rowdata);
                    }
                    tblPromotions.setModel(dt);
                    TableColumn column = null;
                    for (int k = 0;k < tblPromotions.getColumnCount(); k++) {
                        column = tblPromotions.getColumnModel().getColumn(k);
                        if (k == 0) {
                            column.setPreferredWidth(50);
                        }
                        else 
                        {
                            column.setPreferredWidth(100);
                        }
                    }
                    int j=dt.getRowCount();
                    if(j==0)
                    {
                        txtResultPro.setForeground(Color.RED);
                        txtResultPro.setText("no record found");
                    }
                    else
                    {
                        txtResultPro.setForeground(Color.BLACK);
                        txtResultPro.setText(Integer.toString(j));
                    }
                    result.close();
                }
            }
            con.closeConnect();
        }
        catch(Exception e)
        {
            System.out.println("Error txtAutoSearchPro :"+ e);
        }
    }//GEN-LAST:event_txtAutoSearchProKeyTyped

    private void btnChooseEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseEndActionPerformed
        String s=txtTimeStart.getText();
        if(s.length()==0)
        {
            JOptionPane.showMessageDialog(null,"No Start Day");
        }
        else
        {
            JFrame f = new JFrame();
            String e=new DatePicker(f).setPickedDate();
            if("".equals(e)) {
                JOptionPane.showMessageDialog(null,"Please Choose True Day");
            }
            else
            {
                String sStr[]=s.split("-");
                String eStr[]=e.split("-");
                String s1=sStr[0]+sStr[1]+sStr[2];
                String e1=eStr[0]+eStr[1]+eStr[2];
                int s2=Integer.parseInt(s1);
                int e2=Integer.parseInt(e1);
                if(s2>=e2)
                {
                    JOptionPane.showMessageDialog(null,"End Day must be larger Start Day");
                    txtTimeEnd.setText("");
                }
                else
                {
                    txtTimeEnd.setText(e);
                }
            }
        }
    }//GEN-LAST:event_btnChooseEndActionPerformed

    private void btnChooseStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseStartActionPerformed
        Image image = Toolkit.getDefaultToolkit().getImage("images/icon_main.png");
        JFrame f = new JFrame();
        f.setDefaultLookAndFeelDecorated(true);
        DatePicker d=new DatePicker(f);
        f.setIconImage(new ImageIcon(image).getImage());
    }//GEN-LAST:event_btnChooseStartActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try{
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                String sql="SELECT * FROM promotions ";
                String kt="";
                if(ChkName.isSelected()&&chkTime.isSelected())
                {
                    if(txtAutoSearchPro.getText().equals(kt)||txtTimeStart.getText().equals(kt)
                                                            ||txtTimeStart.getText().equals(kt)
                                                            ||txtTimeEnd.getText().equals(kt)
                                                            ||txtTimeEnd.getText().equals("Time End Field"))
                    {
                        JOptionPane.showMessageDialog(null, "Please insert full data");
                    }
                    else
                    {
                        sql+="WHERE name LIKE '%"+txtAutoSearchPro.getText()
                                            +"%' AND time_start >= '"+txtTimeStart.getText()
                                            +"' AND time_start <= '"+txtTimeEnd.getText()
                                            +"' AND time_end >= '"+txtTimeStart.getText()
                                            +"' AND time_end <= '"+txtTimeEnd.getText()+"'";
                    }
                }
                else if(chkTime.isSelected())
                {
                    if(txtTimeStart.getText().equals(kt)||txtTimeEnd.getText().equals(kt))
                    {
                        JOptionPane.showMessageDialog(null, "Please insert full data");
                    }
                    else
                    {
                        sql+="WHERE time_start >= '"+txtTimeStart.getText()
                            +"' AND time_start <= '"+txtTimeEnd.getText()
                            +"' AND time_end >= '"+txtTimeStart.getText()
                            +"' AND time_end <= '"+txtTimeEnd.getText()+"'";
                    }
                }
                ResultSet result=con.executeQuery(sql);
                tblPromotions.getTableHeader().setReorderingAllowed(false);
                String strHeader[]={"Number","Name","Time Start","Time End","Content","Location"};
                DefaultTableModel dt =new DefaultTableModel(strHeader,0)
                {
                    @Override
                    public boolean isCellEditable(int i, int i1) {
                        return false;
                    }
                };
                int i=0;
                while (result.next()) {
                    i++;
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(result.getString("name"));
                    rowdata.add(result.getString("time_start"));
                    rowdata.add(result.getString("time_end"));
                    rowdata.add(result.getString("content"));
                    rowdata.add(result.getString("location"));
                    dt.addRow(rowdata);
                }
                tblPromotions.setModel(dt);
                TableColumn column = null;
                for (int k = 0;k < tblPromotions.getColumnCount(); k++) {
                    column = tblPromotions.getColumnModel().getColumn(k);
                    if (k == 0) {
                        column.setPreferredWidth(50);
                    }
                    else 
                    {
                        column.setPreferredWidth(100);
                    }
                }
                int j=dt.getRowCount();
                if(j==0)
                {
                    txtResultPro.setForeground(Color.RED);
                    txtResultPro.setText("no record found");
                }
                else
                {
                    txtResultPro.setForeground(Color.BLACK);
                    txtResultPro.setText(Integer.toString(j));
                }
                result.close();
            }
            con.closeConnect();
        } catch (Exception ex) {
            System.out.println("Error btnSearch :"+ ex);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblPromotionsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPromotionsKeyReleased
        ClickTablePro();
    }//GEN-LAST:event_tblPromotionsKeyReleased

    private void tblPromotionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPromotionsMouseClicked
        ClickTablePro();
    }//GEN-LAST:event_tblPromotionsMouseClicked

    private void btnShowProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowProActionPerformed
        ShowProAction();
    }//GEN-LAST:event_btnShowProActionPerformed

    private void btnMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapActionPerformed
        NativeInterface.open();
        if("".equals(txt_add.getText()))
        {
            JOptionPane.showMessageDialog(null,"No Address Input");
        }
        else
        {
            main_tab.setEnabledAt(main_tab.getTabCount()-1, true);
            main_tab.setSelectedIndex(main_tab.getTabCount()-1);
            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JComponent c=createContentGoogle();
                c.setSize(jPanel19.getWidth() ,jPanel19.getHeight());
                jPanel19.add(c);
            }
          });
        }
         
        
    }//GEN-LAST:event_btnMapActionPerformed

    private void btn_feedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_feedbackActionPerformed
     
            System.out.println("txt_name.getText() : "+txt_name.getText());
            feedback = new FeedbackForm(this, agentObject,agentClient);
            feedback.setVisible(true); 
        
    }//GEN-LAST:event_btn_feedbackActionPerformed

    private void btnViewFBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewFBActionPerformed
        feedback = new FeedbackForm(this,agentObject,agentClient);
        feedback.setVisible(true);
        int row=table_report.getSelectedRow();
        String col3=""+this.table_report.getValueAt(row,3);
        String col4=""+this.table_report.getValueAt(row,4);
        String col5=""+this.table_report.getValueAt(row,5);
        String col6=""+this.table_report.getValueAt(row,6);
        String col7=""+this.table_report.getValueAt(row,7);
        String col8=""+this.table_report.getValueAt(row,8);
        for (int i=0; i<feedback.cb_feedback_type.getItemCount(); i++) {
             if (col3.toLowerCase().equals(feedback.cb_feedback_type.getItemAt(i).toString().toLowerCase())) {
                 feedback.cb_feedback_type.setSelectedIndex(i); 
                 int index = feedback.cb_feedback_type.getItemCount() - 1;
                 if(feedback.cb_feedback_type.getSelectedIndex() == index)
                 {
                 }
                 else
                 {
                    feedback.cb_content_type.setVisible(false);
                    feedback.jLabel17.setVisible(false);
                 }
                 break;
             }
        }
        for (int j=0; j<feedback.cb_catlogies.getItemCount(); j++) {
             if (col4.toLowerCase().equals(feedback.cb_catlogies.getItemAt(j).toString().toLowerCase())) {
                 feedback.cb_catlogies.setSelectedIndex(j);
                 break;
             }
        }
        for (int j=0; j<feedback.cb_content_type.getItemCount(); j++) {
            if (col5.toLowerCase().equals(feedback.cb_content_type.getItemAt(j).toString().toLowerCase())) {
                feedback.cb_content_type.setSelectedIndex(j);
                break;
            }
        }
        feedback.text_content.setText(col6);  
        feedback.text_solution.setText(col7);  
        for (int i=0; i<feedback.cb_result.getItemCount(); i++) {
            if (col8.toLowerCase().equals(feedback.cb_result.getItemAt(i).toString().toLowerCase())) {
               feedback.cb_result.setSelectedIndex(i);
               int index = feedback.cb_result.getItemCount() - 1;
               if(feedback.cb_result.getSelectedIndex() == index)
               {
                   feedback.txtAsTo.setVisible(true);
                   feedback.cb_assign.setEditable(false);
               }
               else
               {
                   feedback.txtAsTo.setVisible(false);
                   feedback.cb_assign.setVisible(false);
               }
                break;
            }
        }    
       feedback.cb_catlogies.setEnabled(false);
       feedback.cb_content_type.setEnabled(false);
       feedback.cb_feedback_type.setEnabled(false);
       feedback.cb_result.setEnabled(false);
       feedback.cb_assign.setEnabled(false);
       feedback.text_content.setEnabled(false);
       feedback.text_solution.setEnabled(false);
       feedback.btn_save.setVisible(false);
       feedback.cb_catlogies.setBackground(Color.white);
       feedback.cb_content_type.setBackground(Color.white);
       feedback.cb_feedback_type.setBackground(Color.white);
       feedback.cb_result.setBackground(Color.white);
       feedback.text_content.setBackground(Color.white);
       feedback.text_solution.setBackground(Color.white);
    }//GEN-LAST:event_btnViewFBActionPerformed

    private void table_reportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_reportMouseClicked
       btnViewFB.setEnabled(true);
    }//GEN-LAST:event_table_reportMouseClicked

    private void table_reportKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_reportKeyReleased
       btnViewFB.setEnabled(true);
    }//GEN-LAST:event_table_reportKeyReleased

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
                    agentClient.sendtoServer("114@"+value);    
                else
                    System.out.println("status is: "+status);
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
    
     public void ClickTableCoop(){        
        int row=tblCoop.getSelectedRow();        
        String col2=""+this.tblCoop.getValueAt(row,1);
        String col3=""+this.tblCoop.getValueAt(row,2);
        String col4=""+this.tblCoop.getValueAt(row,3);
        String col5=""+this.tblCoop.getValueAt(row,4);
        String col6=""+this.tblCoop.getValueAt(row,5);
        txtDetail.setText("Name : "+col2+"\n"
                         +"Address : "+col3+"\n"
                         +"Phone Number : "+col4+"\n"
                         +"Fax : "+col5+"\n"
                         +"Day of Establishment : "+col6+"\n");            
    }
    /**/
    public void ClickTablePro(){
        int row=tblPromotions.getSelectedRow();
        String col1=""+this.tblPromotions.getValueAt(row,1);
        String col2=""+this.tblPromotions.getValueAt(row,2);
        String col3=""+this.tblPromotions.getValueAt(row,3);
        String col4=""+this.tblPromotions.getValueAt(row,4);       
        txtContent.setText("Promotions : "+col1+"\n"
                          +"Time Start : "+col2+"\n"
                          +"Time End : "+col3+"\n"
                          +"Content : \n"+col4);
     
    }   
    
    private void ShowProAction(){
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect())
            {
                String sql="SELECT * FROM promotions";
                ChkName.setEnabled(true);
                chkTime.setEnabled(true);
                txtAutoSearchPro.setEnabled(true);
                btnClearPro.setEnabled(true);
                tblPromotions.setEnabled(true);
                tblPromotions.getTableHeader().setReorderingAllowed(false);//dont't move column
                tblPromotions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//don't auto resize
                ResultSet result=con.executeQuery(sql);
                String strHeader[]={"Number","Name","Time Start","Time End","Content","Location"};
                DefaultTableModel dt =new DefaultTableModel(strHeader,0){
                    @Override
                    public boolean isCellEditable(int i, int i1) {
                         return false;
                    }
                };
                int i=0;
                while (result.next()) {
                    i++;
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(result.getString("name"));
                    rowdata.add(result.getString("time_start"));
                    rowdata.add(result.getString("time_end"));
                    rowdata.add(result.getString("content"));
                    rowdata.add(result.getString("location"));
                    dt.addRow(rowdata);
                }
                tblPromotions.setModel(dt);
                TableColumn column = null;
                for (int k = 0;k < tblPromotions.getColumnCount(); k++) 
                {
                    column = tblPromotions.getColumnModel().getColumn(k);
                    if (k == 0) {
                        column.setPreferredWidth(50);
                    } 
                    else {
                        column.setPreferredWidth(100);
                    }
                }
                int j=dt.getRowCount();
                txtResultPro.setText(Integer.toString(j));
                result.close();
            }
            con.closeConnect();
        }
        catch (Exception ex) {
            System.out.println("Error: showProAction  "+ex);
        }
    }
    private void ShowCoopAction()
    {
        try {            
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect())
            {
                txtAutoSearchCoop.setEnabled(true);
                txtResultCoop.setEnabled(true);
                txtDetail.setEnabled(true);
                tblCoop.setEnabled(true);
                btnClearCoop.setEnabled(true);
                tblCoop.getTableHeader().setReorderingAllowed(false);
                ResultSet result = con.executeQuery("SELECT * FROM coopmart_name");
                String strHeader[]={"Number","Name","Address","Phone","Fax","Date of Establishment"};
                DefaultTableModel  dt=new DefaultTableModel(strHeader,0)
                {
                    @Override
                    public boolean isCellEditable(int i, int i1) {
                        return false;
                    }
                };
                int i=0;
                while (result.next())
                {
                    i++;
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(result.getString("name"));
                    rowdata.add(result.getString("address"));
                    rowdata.add(result.getString("phone"));
                    rowdata.add(result.getString("fax"));
                    rowdata.add(result.getString("date_establishment"));
                    dt.addRow(rowdata);
                }
                tblCoop.setModel(dt);
                TableColumn column = null;
                for (int k = 0;k < tblCoop.getColumnCount(); k++)
                {
                    column = tblCoop.getColumnModel().getColumn(k);
                    if (k == 0)
                    {
                        column.setPreferredWidth(50);
                    } 
                    else 
                    {
                        column.setPreferredWidth(100);
                    }
                }
                int j=dt.getRowCount();
                txtResultCoop.setText(Integer.toString(j));
            }
            con.closeConnect();
        }
        catch (Exception ex)
        {
            System.out.println("Error: showCopAction ");
        }
      }
 
    private void showCampaign()
    {
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect())
            {
                String sql="SELECT * FROM ("
                                        + "SELECT c.id as campaign_id,c.name as name_camp,c.create_day as create_day,"
                                        + "c.start_day as start_day,c.end_day as end_day,c.desc as descript, "
                                        + "k.id as call_id, "
                                        + "a.agent_id as agent_id, a.agentName as agent_name "
                                        + "FROM ((_campaign_detail d INNER JOIN _campaign c ON d.camp_id = c.id) "
                                        + "INNER JOIN _call k ON k.camp_detail_id = d.id) "
                                        + "INNER JOIN agent_login a ON k.agent_id = a.id) "
                                        + "AS asd ";
                sql+=" WHERE agent_id= '"+lb_agentid.getText()+"' AND end_day > '"+lb_logintime.getText()+"' "
                                        + " GROUP BY campaign_id,create_day,start_day,end_day,agent_id"
                                        + " ORDER BY start_day ASC ";
                ResultSet result = con.executeQuery(sql);
                tblCamp.getTableHeader().setReorderingAllowed(false);
                String strHeader[]={"Number","Campaign name","Day Create","Start Day","End Day","Camp_id","Description"};
                DefaultTableModel  dt=new DefaultTableModel(strHeader,0)
                {
                    @Override
                    public boolean isCellEditable(int i, int i1) {
                        return false;
                    }
                };
                int i=0;
                while (result.next()) {
                    i++;
                    String name_camp= HtmlCoding.decode(result.getString("name_camp"));
                    String create_day=result.getString("create_day");
                    String startday=result.getString("start_day");
                    String end_day=result.getString("end_day");
                    String camp_id=result.getString("campaign_id");
                    String desc=HtmlCoding.decode(result.getString("descript"));
                    SimpleDateFormat spdt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date c_day = spdt.parse(create_day);
                    Date s_day=spdt.parse(startday);
                    Date e_day=spdt.parse(end_day);
                    SimpleDateFormat spdt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String cr_day=spdt1.format(c_day);
                    SimpleDateFormat spdt2 = new SimpleDateFormat("yyyy-MM-dd");
                    String st_day=spdt2.format(s_day);
                    String en_day=spdt2.format(e_day);
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(name_camp);
                    rowdata.add(cr_day);
                    rowdata.add(st_day);
                    rowdata.add(en_day);
                    rowdata.add(camp_id);
                    rowdata.add(desc);
                    dt.addRow(rowdata);
                }
                tblCamp.setModel(dt);
                TableColumn column = null;
                for (int k = 0;k < tblCamp.getColumnCount(); k++) 
                {
                    column = tblCamp.getColumnModel().getColumn(k);
                    if (k == 0) {
                        column.setPreferredWidth(50);
                    } 
                    else if(k==5)
                    {
                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);
                    }
                    else 
                    {
                        column.setPreferredWidth(100);
                    }
               }
            }
            con.closeConnect();
        }
        catch (Exception ex) {
            System.out.println("Error: show campain "+ex);
        }
    }
    
    private Hashtable<Integer, String> selPhone = new Hashtable<>();
    
    public void showCustomer()
    {
         try {
              try { 
                tblCustom.getCellEditor().stopCellEditing();
            } catch(Exception e) {
             
            }
            comboBox();
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            selPhone.clear();
            if(con.isConnect()){
                String sql="SELECT customer_id, name, gender, address, birthday, "
                + " GROUP_CONCAT(number)as number,status,id_status,detail_id,call_id,note, "
                        + "id_status = (SELECT COUNT(*) FROM _call_status) AS `is_success_status`"
                + " FROM ( "
                + " SELECT c.id AS campaign_id,"
                + " a.agent_id AS agent_id, a.agentname AS agent_name, "
                + " k.camp_detail_id AS detail_id,k.id as call_id,k.note as note, "
                + " i.id AS customer_id, i.name AS name, i.gender AS gender, i.address AS address, i.birthday AS birthday,"
                + " p.number AS number,s.desc AS status, s.id AS id_status"
                + " FROM (((((("
                + " _campaign_detail d INNER JOIN _campaign c ON d.camp_id = c.id)"
                + " LEFT JOIN _call k ON k.camp_detail_id = d.id) "
                + " LEFT JOIN agent_login a ON k.agent_id = a.id)"
                + " LEFT JOIN _customer_info i ON d.cus_id = i.id)"
                + " LEFT JOIN _phone p ON i.id = p.cus_id)"
                + " LEFT JOIN _call_status s ON s.id=k.status_id)"
                + ") AS asd";

                int row=tblCamp.getSelectedRow();
                String col5=""+this.tblCamp.getValueAt(row,5);
                sql+=" WHERE agent_id = '"+lb_agentid.getText()+"' and campaign_id = '"+col5+"'"
                              + " GROUP BY customer_id, name, gender, address, birthday ";
                final ResultSet result = con.executeQuery(sql);
                tblCustom.getTableHeader().setReorderingAllowed(false);
                String strHeader[]={"Number","Customer id","Name","Gender","Address",
                       "Birthday","Phone","Status","id_s","detail_id","call id","note",""};
                final DefaultTableModel  dt=new DefaultTableModel(strHeader,0)
                {
                    @Override
                    public boolean isCellEditable(int row, int col) {
                        if (col==6) {
                            return true;
                        }
                        return false;
                    }
                };
                ArrayList<Vector> st_init = new ArrayList<>();
                ArrayList<Vector> st_com = new ArrayList<>();
                int i=0;
                while (result.next()) {
                    Vector rowdata = new Vector();
                    rowdata.add("");                                            //0
                    rowdata.add(result.getString("customer_id"));               //1
                    rowdata.add(result.getString("name"));
                    int a=Integer.parseInt(result.getString("gender"));         //3
                    String a1="";
                    if(a==1)
                    {
                        a1="male";
                    }
                    else 
                    {
                        a1="female";
                    }
                    rowdata.add(a1);
                    rowdata.add(result.getString("address"));
                    rowdata.add(result.getString("birthday"));                  //5
                    rowdata.add(result.getString("number"));                              
                    rowdata.add(result.getString("status"));                    //7
                    rowdata.add(result.getString("id_status"));
                    rowdata.add(result.getString("detail_id"));                 //9
                    rowdata.add(result.getString("call_id"));
                    rowdata.add(result.getString("note"));                      //11
                    rowdata.add(result.getString("is_success_status"));                      //11
                    if(result.getBoolean("is_success_status"))       //hoan thanh
                    {
                        st_com.add(rowdata);
                    } else          // khong hoan thanh
                    {
                         st_init.add(rowdata);
                    }
                }
                for (int j=0; j<st_init.size(); j++) {
                    dt.addRow(st_init.get(j));
                }
                for (int j=0; j<st_com.size(); j++) {
                    dt.addRow(st_com.get(j));
                }
                tblCustom.setModel(dt);
                // <editor-fold defaultstate="collapsed" desc="older code">



                /**/
                 tblCustom.setDefaultRenderer(Object.class, new TableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, 
                                                                    Object value, 
                                                                    boolean isSelected, 
                                                                    boolean hasFocus, 
                                                                    int row, int column) {

                        if (column == 6) {
                           if(value!=null){
                                String[] phones = value.toString().split(",");
                                out = new JComboBox();
                                out.setModel(new DefaultComboBoxModel(phones));
                                out.setBackground(new Color(0xFFFFFFFF));

                                return out;
                           }
                        }

                        JLabel out=new JLabel();

                        if (column == 0) {
                            out.setText(Integer.toString(row+1));
                        } else {
                            out.setText((String)value);
                        }
                         System.out.println("value : "+(String)dt.getValueAt(row, 8));
                        try{
                         if (Integer.parseInt((String)dt.getValueAt(row, 12)) == 0) {
                            out.setForeground(Color.black);
                            if (isSelected) {
                                out.setBackground(new Color(0xff, 0xff, 0x88, 0xff));
                                out.setOpaque(true);
                            }

                            } else {
                                out.setForeground(new Color(0x88, 0x88, 0x88, 0xff));
                            }

                       }
                       catch(Exception e) {}
                        return out;
                    }
                });


                TableColumn column = null;
                 for (int k = 0;k < tblCustom.getColumnCount(); k++) {
                    column = tblCustom.getColumnModel().getColumn(k);

                    if (k == 0) {
                        column.setPreferredWidth(50);

                    } 
                     else if(k==1)
                    {

                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);

                    }
                    else if(k==8)
                    {

                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);

                    }
                    else if(k==9)
                    {

                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);

                    }
                    else if(k==10)
                    {

                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);

                    }
                     else if(k==11)
                    {

                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);

                    }
                     else if(k==12)
                    {

                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);

                    }
                    else {
                        column.setPreferredWidth(100);

                    }
                }
                 // </editor-fold>
                }
                con.closeConnect();
              }
              catch (Exception ex) {
                  System.out.println("Error: show custom"+ex);
              }
    }
    private void ShowButtonDial(){
        int row=tblCustom.getSelectedRow();
        String SStatus_id=""+this.tblCustom.getValueAt(row, 12);        
        int status_id=Integer.parseInt(SStatus_id);
        if(status_id==1)//complete        
            btnDial.setEnabled(false);        
        else        
            btnDial.setEnabled(true);                
    }
     
    public void clearInfo(){
        txt_add.setText("");
        txt_birthday.setText("");
        txt_email.setText("");
        txt_gender.setText("");
        txt_makh.setText("");
        txt_name.setText("");
        txt_phonenum.setText("");
        txt_reg.setText("");
        txt_type.setText("");
    }
     
  private void Dial(){
    try {
           con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

           if(con.isConnect())
           {
                
                int rw=tblCamp.getSelectedRow();
                int camp_id= Integer.parseInt(tblCamp.getValueAt(rw, 5).toString());

                int row=tblCustom.getSelectedRow();
                
                if(row>=0)
                {
                    int cus_id = Integer.parseInt((String)this.tblCustom.getValueAt(row, 1));
                    if (selPhone.containsKey(cus_id)) {
                        CallPhone = selPhone.get(cus_id);
                    } else {

                        CallPhone = this.tblCustom.getValueAt(row, 6).toString().split(",")[0];


                    }



                    final String Scus_name=""+this.tblCustom.getValueAt(row,2);

                    final String SGender=""+this.tblCustom.getValueAt(row,3);
                    final String SAddr=""+this.tblCustom.getValueAt(row, 4);
                    final String SBirth=""+this.tblCustom.getValueAt(row, 5);

                    final String SCall_id=""+this.tblCustom.getValueAt(row, 10);

                    int row1=tblCamp.getSelectedRow();
                    if(row1 <0)
                    {
                        return;
                    }
                    final String Scam_name=""+this.tblCamp.getValueAt(row1, 1);
                    final String Sdesc=""+this.tblCamp.getValueAt(row1, 6);


                    

                    Question_Camp quesF = new Question_Camp(camp_id,this,lb_agentid.getText(),SCall_id,Scam_name);
                    quesF.getTextCamp_Desc().setText(Sdesc);
                    quesF.getlblGender().setText(SGender); 
                    quesF.getlblCus_id().setText(Scus_name);
                    quesF.getlblAddr().setText(SAddr);
                    quesF.getlblBirth().setText(SBirth);
                    quesF.setVisible(true);
                    String col7=""+this.tblCustom.getValueAt(row,7);
                    String col11=""+this.tblCustom.getValueAt(row,11);
                    quesF.txtNote.setText(HtmlCoding.decode(col11)); 
                    for (int i=0; i<quesF.cbxStatus.getItemCount(); i++) {
                         if (col7.toLowerCase().equals(quesF.cbxStatus.getItemAt(i).toString().toLowerCase())) {
                             quesF.cbxStatus.setSelectedIndex(i); 
                         }
                    }
                }
           }
           con.closeConnect();
        }catch(Exception e){}
    }
    public String GetStatus(String t)
    {
        return t;
    }
    
    int cus_id;
    private void comboBox()
    {
        try{
            tblCustom.setDefaultEditor(Object.class, new TableCellEditor() {
                JComboBox jcom = new JComboBox();

                @Override
                public Component getTableCellEditorComponent(JTable jtable, Object va, boolean haf, 
                                                            int row, int col) {

                    cus_id = Integer.parseInt((String)tblCustom.getValueAt(row, 1));
                    String d=(String)tblCustom.getValueAt(row, col);
                    if(d!=null)
                    {
                        String[] phones = tblCustom.getValueAt(row, col).toString().split(",");
                        jcom.setModel(new DefaultComboBoxModel(phones));
                        jcom.addItemListener(new ItemListener() {

                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                if (e.getStateChange() == ItemEvent.SELECTED) {
                                    if (selPhone.containsKey(cus_id)) {
                                        selPhone.remove(cus_id);
                                    }
                                    selPhone.put(cus_id, (String)e.getItem());
                                }
                            }
                        });
                    }
                    return jcom;
                }

                @Override
                public Object getCellEditorValue() { return null; }

                @Override
                public boolean isCellEditable(EventObject eo) { return true; }

                @Override
                public boolean shouldSelectCell(EventObject eo) { return true; }

                @Override
                public boolean stopCellEditing() { 
                    jcom.setEditable(false);
                    jcom.setEnabled(false);
                    jcom.setEnabled(true);
                    return true;
                }

                @Override
                public void cancelCellEditing() {}

                @Override
                public void addCellEditorListener(CellEditorListener cl) {}

                @Override
                public void removeCellEditorListener(CellEditorListener cl) {}
            });
        }catch(Exception e)
        {}
    }
    
    public static JComponent createBrowserUnknowTab() {  
        JPanel p=new JPanel(new BorderLayout());
        final JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(false);
        webBrowser.navigate("http://support.microsoft.com/kb/307759/en-us");
        p.add(webBrowser, BorderLayout.CENTER);
        return p;
  }
     public JComponent createContentGoogle() {  
        JPanel p=new JPanel(new BorderLayout());
        final JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(false);
        try {
            webBrowser.setHTMLContent("<body onload='autoload()'>"
                    + "<a id='autoload' href='file:///"+new File(".").getCanonicalPath()+"/http/mycontext.html?diadiem="+ URLEncoder.encode(txt_add.getText()) +"'></a>"                                    
                    + "</body>"
                    + "<script>function autoload() {"
                    + " var auto = document.getElementById('autoload');"
                    + " auto.click();"
                    + "}</script>");
        } catch (Exception ex) {
        }        
        p.add( webBrowser, BorderLayout.CENTER);
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
    private javax.swing.JCheckBox ChkName;
    private javax.swing.JMenuItem MenuItem_about;
    private javax.swing.JMenuItem MenuItem_changepwd;
    public javax.swing.JMenuItem MenuItem_exit;
    public javax.swing.JMenuItem MenuItem_logout;
    private javax.swing.JMenuItem MenuItem_setting;
    private javax.swing.JPanel Panel1;
    private javax.swing.JButton btnChooseEnd;
    private javax.swing.JButton btnChooseStart;
    private javax.swing.JButton btnClearCoop;
    private javax.swing.JButton btnClearPro;
    private javax.swing.JButton btnDial;
    private javax.swing.JButton btnMap;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnShowCoop;
    private javax.swing.JButton btnShowPro;
    public javax.swing.JButton btnViewFB;
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
    public javax.swing.JButton btn_feedback;
    public javax.swing.JButton btn_hangup;
    public javax.swing.JButton btn_logout;
    public javax.swing.JToggleButton btn_pause;
    private javax.swing.JButton btn_transfercall;
    private javax.swing.JCheckBox chkTime;
    private javax.swing.JTabbedPane function_tab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lb_agentid;
    private javax.swing.JLabel lb_agentname;
    public javax.swing.JLabel lb_callduration;
    private javax.swing.JLabel lb_extension;
    public javax.swing.JLabel lb_logintime;
    private javax.swing.JLabel lb_queue;
    public javax.swing.JLabel lb_status;
    private javax.swing.JLabel lb_version;
    public javax.swing.JLabel lb_workTime;
    private javax.swing.JList list_transfer;
    private javax.swing.JTabbedPane main_tab;
    private javax.swing.JPanel panel_number;
    public javax.swing.JTabbedPane tabPChat;
    public javax.swing.JTable table_report;
    private javax.swing.JTable tblCamp;
    private javax.swing.JTable tblCoop;
    private javax.swing.JTable tblCustom;
    private javax.swing.JTable tblPromotions;
    private javax.swing.JTable tblShowAgent;
    private javax.swing.JTextField txtAutoSearchCoop;
    private javax.swing.JTextField txtAutoSearchPro;
    private javax.swing.JTextArea txtContent;
    private javax.swing.JTextArea txtDetail;
    private javax.swing.JTextField txtResultCoop;
    private javax.swing.JTextField txtResultPro;
    private javax.swing.JTextField txtTimeEnd;
    private javax.swing.JTextField txtTimeStart;
    public javax.swing.JTextField txt_add;
    public javax.swing.JTextField txt_birthday;
    public javax.swing.JTextField txt_email;
    public javax.swing.JTextField txt_gender;
    public javax.swing.JTextField txt_makh;
    public javax.swing.JTextField txt_mobile;
    public javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_phonenum;
    public javax.swing.JTextField txt_reg;
    public javax.swing.JTextField txt_type;
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

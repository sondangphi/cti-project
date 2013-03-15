/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

//import com.jniwrapper.win32.ie.Browser;
import az.encoding.html.HtmlCoding;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowOpeningEvent;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserWindowWillOpenEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.midi.Transmitter;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.CellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.DatePicker;
import org.asterisk.utility.Utility;
import org.w3c.dom.html.HTMLCollection;


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
    private  String Mysql_server = "172.168.10.202";      
    private  String Mysql_dbname = "ast_callcenter";
    private  String Mysql_user = "callcenter";
    private  String Mysql_pwd  = "callcenter"; 
    private ConnectDatabase con;
    public static FeedbackForm feedback;
    public static MessageForm messageform;
   
    private String CallPhone;
    private JComboBox out;
   
    public static ListItemChat itemchat;
    LocateMap locate = new LocateMap();
    public ChangepwdForm chanpwdform;
    private final String EXIT = "112";
    private final String PAUSE = "104@off";
    private final String UNPAUSE = "104@on";

    /**
     * Creates new form MainForm2
     */
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
        btn_transfer = new javax.swing.JButton();
        btnChat = new javax.swing.JButton();
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
        jButton3 = new javax.swing.JButton();
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
        btn_clear = new javax.swing.JButton();
        btn_dial = new javax.swing.JButton();
        btn_hangup = new javax.swing.JButton();
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

        btn_transfer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btn_transfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/btn_transfer.png"))); // NOI18N
        btn_transfer.setEnabled(false);
        btn_transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transferActionPerformed(evt);
            }
        });

        btnChat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/btn_livechat32x32.png"))); // NOI18N
        btnChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChatActionPerformed(evt);
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
                .addGap(49, 49, 49)
                .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btnChat, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
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
                        .addComponent(btnChat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_logout, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btn_pause, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel13, lb_workTime});

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
        txt_makh.setEnabled(false);

        txt_add.setEditable(false);
        txt_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_addActionPerformed(evt);
            }
        });

        txt_mobile.setEditable(false);

        txt_email.setEditable(false);
        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });

        btn_feedback.setText("Feedback");
        btn_feedback.setEnabled(false);
        btn_feedback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_feedbackActionPerformed(evt);
            }
        });

        txt_name.setEditable(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Email");
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Birthday");
        jLabel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/asterisk/image/map32x32.png"))); // NOI18N
        jButton3.setText("Map");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("Type");
        jLabel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setText("Registration");
        jLabel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txt_reg.setEditable(false);

        txt_gender.setEditable(false);

        txt_type.setEditable(false);

        txt_birthday.setEditable(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(35, 35, 35)
                        .addComponent(txt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addGap(26, 26, 26)
                                .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_feedback))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_type)
                            .addComponent(txt_mobile, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addGap(18, 18, 18)
                                .addComponent(txt_reg, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_birthday)
                                    .addComponent(txt_email))))))
                .addContainerGap())
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel17});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_makh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
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
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_add, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btn_feedback, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_feedback, jButton3, jLabel10, jLabel11, jLabel14, jLabel18, jLabel19, jLabel4, jLabel9, txt_add, txt_email, txt_makh, txt_mobile, txt_name, txt_reg});

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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(Panel1Layout.createSequentialGroup()
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
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
                .addContainerGap(31, Short.MAX_VALUE))
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
                .addContainerGap(19, Short.MAX_VALUE))
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
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
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
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDial, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        main_tab.addTab("Campaigns", jPanel5);

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                    .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                                .addComponent(lb_extension, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lb_callduration, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_agentname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        lb_version.setFont(new java.awt.Font("Tahoma", 2, 9)); // NOI18N
        lb_version.setText("Version 1.0.0 Build 10012013");
        lb_version.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        panel_dial.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agent Dial Out", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        panel_dial.setOpaque(false);

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
                                .addComponent(btn_11, javax.swing.GroupLayout.PREFERRED_SIZE, 49, Short.MAX_VALUE)
                                .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(btn_1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_0, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(btn_3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btn_12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(17, 17, 17))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(txt_phonenum)
                        .addGap(18, 18, 18)))
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_back)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btn_hangup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btn_dial, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_0, btn_1, btn_11, btn_12, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9});

        panel_numberLayout.setVerticalGroup(
            panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_numberLayout.createSequentialGroup()
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_phonenum, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btn_back, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_dial, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_hangup, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btn_6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_numberLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btn_9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_numberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_numberLayout.createSequentialGroup()
                        .addComponent(btn_2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_0, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        panel_numberLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_0, btn_11, btn_12, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9});

        javax.swing.GroupLayout panel_dialLayout = new javax.swing.GroupLayout(panel_dial);
        panel_dial.setLayout(panel_dialLayout);
        panel_dialLayout.setHorizontalGroup(
            panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_dialLayout.createSequentialGroup()
                .addComponent(panel_number, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panel_dialLayout.setVerticalGroup(
            panel_dialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_dialLayout.createSequentialGroup()
                .addComponent(panel_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lb_version, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel_dial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(main_tab, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panel_dial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(main_tab))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
            }).start();
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
        dialNumber += "1";
        updateNumber();
        uti.playSounds();        
    }//GEN-LAST:event_btn_1ActionPerformed

    private void btn_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_2ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "2";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_2ActionPerformed

    private void btn_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_3ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "3";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_3ActionPerformed

    private void btn_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_4ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "4";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_4ActionPerformed

    private void btn_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_5ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "5";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_5ActionPerformed

    private void btn_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_6ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "6";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_6ActionPerformed

    private void btn_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_7ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "7";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_7ActionPerformed

    private void btn_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_8ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "8";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_8ActionPerformed

    private void btn_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_9ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += "9";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_9ActionPerformed

    private void btn_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_0ActionPerformed
        // TODO add your handling code here:
        System.out.println("press");
        dialNumber = txt_phonenum.getText();
        dialNumber += "0";
        updateNumber();  
        uti.playSounds();
    }//GEN-LAST:event_btn_0ActionPerformed

    private void btn_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_11ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_11.getText();
        updateNumber(); 
        uti.playSounds();
    }//GEN-LAST:event_btn_11ActionPerformed

    private void btn_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_12ActionPerformed
        // TODO add your handling code here:
        dialNumber = txt_phonenum.getText();
        dialNumber += btn_12.getText();
        updateNumber();        
        uti.playSounds();
    }//GEN-LAST:event_btn_12ActionPerformed

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        // TODO add your handling code here:
        deleteNumber();
//        uti.playSounds();
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
                uti.playSounds();
            }else{
                System.out.println("not allow: "+dialNumber);
                new Thread(new Runnable() {
                    @Override
                    public void run() {   
                        showDialog("Just use number.");
                    }
                }).start();                
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

    private void btn_hangupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hangupActionPerformed
        // TODO add your handling code here:
        try{
            String command = "106";
            agentClient.sendtoServer(command);
            uti.playSounds();
        }catch(Exception e){
        
        }          
    }//GEN-LAST:event_btn_hangupActionPerformed

    private void btn_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pauseActionPerformed
        // TODO add your handling code here:
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
    
    private void btn_transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transferActionPerformed
        try{
//            String desTransfer = "";
////            if(cb_transfer.getItemCount()>0)
////                desTransfer = "SIP/"+(String)cb_transfer.getSelectedItem();
//            desTransfer = "SIP/"+txt_transfer.getText();
//            agentClient.sendtoServer("114@"+desTransfer);
//            System.out.println("transfer");
            btn_transfer.setEnabled(false);
            new TransferForm(agentClient, agentObject, this).setVisible(true);
        }catch(Exception e){
            System.out.println("btn_transferActionPerformed: "+e);
        }                
    }//GEN-LAST:event_btn_transferActionPerformed

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
            System.out.println(phone);
            if(phone==null)
            {
                System.out.println("null 1");
                 btnDial.setEnabled(false);
                 
            }
            else{
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
        // TODO add your handling code here:
        ShowCoopAction();
    }//GEN-LAST:event_btnShowCoopActionPerformed

    private void tblCoopKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCoopKeyReleased
        // TODO add your handling code here:
        ClickTableCoop();
    }//GEN-LAST:event_tblCoopKeyReleased

    private void tblCoopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCoopMouseClicked
        // TODO add your handling code here:
        ClickTableCoop();
    }//GEN-LAST:event_tblCoopMouseClicked

    private void btnClearCoopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearCoopActionPerformed
        // TODO add your handling code here:
        txtAutoSearchCoop.setText("");
        txtResultCoop.setText("");
        txtDetail.setText("");
        ShowCoopAction();
    }//GEN-LAST:event_btnClearCoopActionPerformed

    private void txtAutoSearchCoopKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAutoSearchCoopKeyTyped
        // TODO add your handling code here:
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

                    if (k == 0) {
                        column.setPreferredWidth(50);

                    }

                    else {
                        column.setPreferredWidth(100);

                    }
                }
                int j=dt.getRowCount();

                if(j==0)
                {
                    txtResultCoop.setForeground(Color.RED);
                    txtResultCoop.setText("no record found");

                }
                else
                {
                    txtResultCoop.setForeground(Color.BLACK);
                    txtResultCoop.setText(Integer.toString(j));

                }

                result.close();

            }
            con.closeConnect();

        }
        catch(IOException | SQLException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,"Error:"+ e.toString());
        }
    }//GEN-LAST:event_txtAutoSearchCoopKeyTyped

    private void btnClearProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearProActionPerformed
        // TODO add your handling code here:
        txtContent.setText("");
        txtAutoSearchPro.setText("");
        txtTimeStart.setText("");
        txtTimeEnd.setText("");
        ShowProAction();

    }//GEN-LAST:event_btnClearProActionPerformed

    private void ChkNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChkNameMouseClicked
        // TODO add your handling code here:

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
        // TODO add your handling code here:

        if(chkTime.isSelected())
        {
            txtTimeStart.setEnabled(true);
            txtTimeEnd.setEnabled(true);
            btnChooseStart.setEnabled(true);
            btnChooseEnd.setEnabled(true);
            btnSearch.setEnabled(true);
            if(ChkName.isSelected())
            {

                //btnClearPro.setEnabled(true);
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
        // TODO add your handling code here:
        try{
            tblPromotions.getTableHeader().setReorderingAllowed(false);
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            System.out.println("Mysql_server: "+Mysql_server);
            if(con.isConnect()){
                if(ChkName.isSelected())
                {
                    String sql="SELECT * FROM promotions WHERE name LIKE'%"+txtAutoSearchPro.getText()+"%'";

                    ResultSet result=con.executeQuery(sql);
                    //loadDataPro();
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

                        else {
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
        catch(IOException | SQLException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null,"Error:"+ e.toString());
        }
    }//GEN-LAST:event_txtAutoSearchProKeyTyped

    private void btnChooseEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseEndActionPerformed
        // TODO add your handling code here:

        String s=txtTimeStart.getText();

        if(s.length()==0)
        {
            JOptionPane.showMessageDialog(null,"No Start Day");
        }

        else{

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
                else{
                    txtTimeEnd.setText(e);
                }
            }
        }

    }//GEN-LAST:event_btnChooseEndActionPerformed

    private void btnChooseStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseStartActionPerformed
        // TODO add your handling code here:
        Image image = Toolkit.getDefaultToolkit().getImage("images/icon_main.png");
        JFrame f = new JFrame();
        f.setDefaultLookAndFeelDecorated(true);
       DatePicker d=new DatePicker(f);
       
       f.setIconImage(new ImageIcon(image).getImage());
//        String s=new DatePicker(f).setPickedDate();
//         f.setIconImage(image);
//        if("".equals(s)) {
//            JOptionPane.showMessageDialog(null,"Please Choose True Day");
//        }
//        else
//        {
//            txtTimeStart.setText(s);
//
//        }
    }//GEN-LAST:event_btnChooseStartActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        try{
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                String sql="SELECT * FROM promotions ";
                String kt="";

                try{
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

                        else {
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
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(null,"Error:"+ ex.toString());
                }

            }
            con.closeConnect();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error:"+ ex.toString());
        }

    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblPromotionsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPromotionsKeyReleased
        // TODO add your handling code here:
        ClickTablePro();
    }//GEN-LAST:event_tblPromotionsKeyReleased

    private void tblPromotionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPromotionsMouseClicked
        // TODO add your handling code here:
        ClickTablePro();
    }//GEN-LAST:event_tblPromotionsMouseClicked

    private void btnShowProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowProActionPerformed
        // TODO add your handling code here:
        ShowProAction();

    }//GEN-LAST:event_btnShowProActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        NativeInterface.open();
        if("".equals(txt_add.getText()))
        {
            JOptionPane.showMessageDialog(null,"No Address Input");

        }
        else{
            createContent();
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void btn_feedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_feedbackActionPerformed
        // TODO add your handling code here:
        
                System.out.println("txt_name: "+this.txt_name.getText());
                System.out.println("txt_add: "+this.txt_add.getText());
                System.out.println("txt_mobile: "+this.txt_mobile.getText());

                System.out.println("txt_email: "+this.txt_email.getText());
                System.out.println("txt_add: "+this.txt_add.getText());
                feedback = new FeedbackForm(this, agentObject,agentClient);
                feedback.setVisible(true);
                
            
    }//GEN-LAST:event_btn_feedbackActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

    private void txt_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_addActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_addActionPerformed

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
       
                 if(feedback.cb_feedback_type.getSelectedIndex() == index){
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
                System.out.println(feedback.cb_result.getItemAt(i).toString().toLowerCase());
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
     
        
        
        //setEnable
       feedback.cb_catlogies.setEnabled(false);
       feedback.cb_content_type.setEnabled(false);
       feedback.cb_feedback_type.setEnabled(false);
       feedback.cb_result.setEnabled(false);
       feedback.cb_assign.setEnabled(false);
       feedback.text_content.setEnabled(false);
       feedback.text_solution.setEnabled(false);
      // feedback.txt_email.setVisible(true);
       
       //set visible
     
       feedback.btn_save.setVisible(false);
       //setbackground
       feedback.cb_catlogies.setBackground(Color.white);
       feedback.cb_content_type.setBackground(Color.white);
       feedback.cb_feedback_type.setBackground(Color.white);
       feedback.cb_result.setBackground(Color.white);
       feedback.text_content.setBackground(Color.white);
       feedback.text_solution.setBackground(Color.white);
      
//       if(!feedback.cb_assign.isVisible()) {
//           feedback.setSize(feedback.getWidth(), feedback.jPanel2.getY()+feedback.jPanel2.getHeight()+10);
//       }
//       
    }//GEN-LAST:event_btnViewFBActionPerformed

    private void table_reportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_reportMouseClicked
       btnViewFB.setEnabled(true);
    }//GEN-LAST:event_table_reportMouseClicked

    private void table_reportKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_table_reportKeyReleased
       btnViewFB.setEnabled(true);
    }//GEN-LAST:event_table_reportKeyReleased


    private void btnChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChatActionPerformed
       messageform=new MessageForm(agentObject.getAgentId(), agentClient);
       messageform.setVisible(true);
        
//        itemchat=new ListItemChat(agentObject.getAgentId(), agentClient);
//       itemchat.setVisible(true);
    }//GEN-LAST:event_btnChatActionPerformed

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
    
     public void ClickTableCoop()
    {
        
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
                         +"Day of Establishment : "+col6+"\n"
                );
       
     
    }
    /**/
    public void ClickTablePro()
    {
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
    
    private void ShowProAction()
      {
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
        catch (IOException | SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Error:"+ex.toString());
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
            JOptionPane.showMessageDialog(null,"Error:"+ex.toString());
        }

      }
    
    private static byte[] stringToBytes(String str) {
        byte[] output = new byte[str.length()];
        for (int i=0; i<output.length; i++) {
            output[i] = (byte)((int)str.charAt(i) & 0xFF);
        }
        
        return output;
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
                        // *** same for the format String below
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
                    for (int k = 0;k < tblCamp.getColumnCount(); k++) {
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
                   
                    else {
                        column.setPreferredWidth(100);

                    }
               }
            }
            con.closeConnect();
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error:"+ex.toString());
        }
    }
    
    private Hashtable<Integer, String> selPhone = new Hashtable<>();
    
    public void showCustomer()
    {
         try {
            try { 
                tblCustom.getCellEditor().stopCellEditing();
            } catch(Exception e) {}

            comboBox();
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            selPhone.clear();
                
                if(con.isConnect()){
                    String sql="SELECT customer_id, name, gender, address, birthday, "
                    + " GROUP_CONCAT(number)as number,status,id_status,detail_id,call_id,note "
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
                    + " LEFT JOIN _call_status s ON s.id=k.status_id)) AS asd ";
                 
                    
                    int row=tblCamp.getSelectedRow();
        
                    String col5=""+this.tblCamp.getValueAt(row,5);
                   // String col2=""+this.tblCamp.getValueAt(row,2);
                      sql+=" WHERE agent_id = '"+lb_agentid.getText()+"' and campaign_id = '"+col5+"'"
                              + " GROUP BY customer_id, name, gender, address, birthday ";
                   ResultSet result = con.executeQuery(sql);
                 
                   tblCustom.getTableHeader().setReorderingAllowed(false);
                   String strHeader[]={"Number","Customer id","Name","Gender","Address",
                       "Birthday","Phone","Status","id_s","detail_id","call id","note"};
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
                            System.out.println(a1);
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
                       
                        if(Integer.parseInt((String)rowdata.get(8)) == 6)       //hoan thanh
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
                             System.out.println("value : "+Integer.parseInt((String)dt.getValueAt(row, 8)));
                            if (Integer.parseInt((String)dt.getValueAt(row, 8)) != 6) {
                                out.setForeground(Color.black);
                                if (isSelected) {
                                    out.setBackground(new Color(0xff, 0xff, 0x88, 0xff));
                                    out.setOpaque(true);
                                }
                                
                            } else {
                                out.setForeground(new Color(0x88, 0x88, 0x88, 0xff));
                            }
                            
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
                        else {
                            column.setPreferredWidth(100);

                        }
                    }
                     // </editor-fold>

                }
                
                con.closeConnect();
              }
              catch (Exception ex) {
                  JOptionPane.showMessageDialog(null,"Error:"+ex.toString());
              }
    }
    private void ShowButtonDial()
    {
        int row=tblCustom.getSelectedRow();
        String SStatus_id=""+this.tblCustom.getValueAt(row, 8);
        
        int status_id=Integer.parseInt(SStatus_id);
        if(status_id==6)//complete
        {

            btnDial.setEnabled(false);
        }
      
        else
        {

              btnDial.setEnabled(true);
        }
        
    }
     
     
  private void Dial()
   {
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
                   // quesF.getTextCamp_Desc().setText(quesF.injectSql(Sdesc));
                    quesF.getlblGender().setText(SGender); 
                    quesF.getlblCus_id().setText(Scus_name);
                    quesF.getlblAddr().setText(SAddr);
                    quesF.getlblBirth().setText(SBirth);


                    quesF.setVisible(true);
                    
                    /////
                    

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
                    System.out.println("null 4 :"+ d);
                    if(d!=null)
                    {
                        System.out.println("null 5 :"+ d);
                        
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
    

    public JComponent createContent() {
      
  
        main_tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        JWebBrowser webBrowser = new JWebBrowser();
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(true);
        try {
            webBrowser.setHTMLContent("<body onload='autoload()'>"
                    + "<a id='autoload' href='file:///"+new File(".").getCanonicalPath()+"/http/mycontext.html?diadiem="+ URLEncoder.encode(txt_add.getText()) +"'></a>"
                 
                   
                    + "</body>"
                    + "<script>function autoload() {"
                    + " var auto = document.getElementById('autoload');"
                    + " auto.click();"
                    + "}</script>");
        } catch (Exception ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     
        try { 
            main_tab.removeTabAt(4); 
        } catch (Exception e) {}
        
        if(main_tab.getTabCount()==4)
        {
            main_tab.addTab("Google Map", webBrowser);
            main_tab.setSelectedIndex(4);
        }
        
        return main_tab;
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
    private javax.swing.JButton btnChat;
    private javax.swing.JButton btnChooseEnd;
    private javax.swing.JButton btnChooseStart;
    private javax.swing.JButton btnClearCoop;
    private javax.swing.JButton btnClearPro;
    private javax.swing.JButton btnDial;
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
    public javax.swing.JButton btn_transfer;
    private javax.swing.JCheckBox chkTime;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
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
    private javax.swing.JTabbedPane main_tab;
    private javax.swing.JPanel panel_dial;
    private javax.swing.JPanel panel_number;
    public javax.swing.JTable table_report;
    private javax.swing.JTable tblCamp;
    private javax.swing.JTable tblCoop;
    private javax.swing.JTable tblCustom;
    private javax.swing.JTable tblPromotions;
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
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;

/**
 *
 * @author leehoa
 */
public class FeedbackForm extends javax.swing.JDialog {

        private static String filename = "infor.properties";                         		
        private static String Mysql_server = "172.168.10.202";      
        private static String Mysql_dbname = "ast_callcenter";
	private static String Mysql_user = "callcenter";
	private static String Mysql_pwd  = "callcenter";   
        private static ConnectDatabase con;
        private static Utility uti;
        public static MainForm mainform2 = null ;
        private Agent agentclient;
        public static WaitingForm wait_form;
        
        private static AgentObject agentObject = null;
    /**
     * Creates new form FeebackForm
     */
    public FeedbackForm() {
        initComponents();
        uti = new Utility();
        try{
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            setLocationRelativeTo(null);
            Color LightSkyBlue2 = new Color(164, 211, 238);
            Color white = new Color(255,255,255);
            this.getContentPane().setBackground(LightSkyBlue2);
            jPanel8.setBackground(LightSkyBlue2);
            cb_result.setBackground(white);
            cb_feedback_type.setBackground(white);
            cb_catlogies.setBackground(white);
            cb_content_type.setEnabled(true);
            txtEmail.setVisible(false);
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_feedback.gif");
            this.setIconImage(image); 
            showComboBox();
            System.out.println("message : 2");
        }catch(Exception e){
        }                
    }
  
    public FeedbackForm(final MainForm m, AgentObject agent, Agent agentc) {
        super(m);
        initComponents();
        
        m.setEnabled(false);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent we) {
                m.setEnabled(true);
                super.windowClosed(we);
            }
        });
        
        try{
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
            uti = new Utility();
            agentObject = agent;
            agentclient = agentc;
            mainform2 = m;
            if("".equals(m.txt_name.getText()))
            {
                lbl_Name.setText("Unknown");
                lb_mobile.setText(m.txt_mobile.getText());
            }
            else
            {
                lbl_Name.setText(agentclient.customer.getName());
                lb_mobile.setText(agentclient.customer.getPhone());
            }
            
            
           
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            setLocationRelativeTo(null);
            Color LightSkyBlue2 = new Color(164, 211, 238);
            Color white = new Color(255,255,255);
            this.getContentPane().setBackground(LightSkyBlue2);
            jPanel8.setBackground(LightSkyBlue2);

            cb_result.setBackground(white);
            cb_feedback_type.setBackground(white);
            cb_catlogies.setBackground(white);
            cb_content_type.setBackground(white);
            cb_content_type.setEnabled(false);
            cb_assign.setBackground(white);  
            txtAsTo.setVisible(false);
            cb_assign.setVisible(false);
            txtEmail.setVisible(false);
//             buttong
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_feedback.gif");
            this.setIconImage(image);
            showComboBox();
            this.setTitle("Feedback");
         
             
        }catch(Exception e){
            System.out.println("message : "+e.getMessage());
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

        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cb_catlogies = new javax.swing.JComboBox();
        cb_feedback_type = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        text_content = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        text_solution = new javax.swing.JTextArea();
        lb_mobile = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cb_content_type = new javax.swing.JComboBox();
        lbl_Name = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cb_result = new javax.swing.JComboBox();
        btn_save = new javax.swing.JButton();
        btn_close = new javax.swing.JButton();
        txtAsTo = new javax.swing.JLabel();
        cb_assign = new javax.swing.JComboBox();
        txtEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("FeedbackForm");
        setResizable(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Name  :");
        jLabel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Feeback Type  : ");
        jLabel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Catelogies : ");
        jLabel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Content :");
        jLabel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Solution :");
        jLabel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        cb_catlogies.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tiêu Dùng", "Văn Phòng Phẩm", "Nội Thất", "Điện Máy", "May Mặc" }));

        cb_feedback_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Phàn nàn", "Khen", "Khiếu nại" }));
        cb_feedback_type.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_feedback_typeItemStateChanged(evt);
            }
        });

        text_content.setColumns(20);
        text_content.setRows(5);
        text_content.setToolTipText("");
        text_content.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        text_content.setName(""); // NOI18N
        jScrollPane1.setViewportView(text_content);

        text_solution.setColumns(20);
        text_solution.setRows(5);
        jScrollPane2.setViewportView(text_solution);

        lb_mobile.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lb_mobile.setText("01234");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("Content Type  : ");
        jLabel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        cb_content_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dịch vụ", "Hàng hóa", "Khuyến mãi sự kiện", "Khác" }));

        lbl_Name.setText("Nguyen A");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Phone : ");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Result     :");
        jLabel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        cb_result.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Đồng Ý", "Không Đồng Ý", "Hoàn Toàn Đồng Ý", "Chuyển Tiếp" }));
        cb_result.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_resultItemStateChanged(evt);
            }
        });

        btn_save.setText("Save");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_close.setText("Close");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        txtAsTo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtAsTo.setText("Assign To :");

        cb_assign.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_assignItemStateChanged(evt);
            }
        });

        txtEmail.setEditable(false);
        txtEmail.setText("nguyenthi@hhh.com");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Name)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_mobile)
                            .addComponent(cb_catlogies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cb_content_type, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_feedback_type, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cb_assign, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(txtAsTo)
                                .addGap(23, 23, 23)))
                        .addComponent(cb_result, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_save, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_close, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmail)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Name))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lb_mobile, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_feedback_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_content_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_catlogies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_save)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_result, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_close)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAsTo)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_assign, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //this.setVisible(false);
    }//GEN-LAST:event_btn_closeActionPerformed

    private void cb_feedback_typeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_feedback_typeItemStateChanged
        // TODO add your handling code here:
        int index = cb_feedback_type.getItemCount() - 1;
       
        if(cb_feedback_type.getSelectedIndex() == index){
            cb_content_type.setEnabled(true);
        }else
            cb_content_type.setEnabled(false);
        
    }//GEN-LAST:event_cb_feedback_typeItemStateChanged

    private void cb_resultItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_resultItemStateChanged
        int index = cb_result.getItemCount() - 1;
       
        if(cb_result.getSelectedIndex() == index)
        {
            cb_assign.setVisible(true);
            String email=cb_assign.getSelectedItem().toString();
            txtEmail.setVisible(true);
            txtEmail.setText(getEmail(email));
            txtAsTo.setVisible(true);
            
        }else
        {
            txtAsTo.setVisible(false);
            cb_assign.setVisible(false);
             txtEmail.setVisible(false);
        }
    }//GEN-LAST:event_cb_resultItemStateChanged

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        try{
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
           if(con.isConnect()){
                System.out.println("begin write feedback!");
                String name = lbl_Name.getText();
                final String mobile = lb_mobile.getText();
                String type = (String)cb_feedback_type.getItemAt(cb_feedback_type.getSelectedIndex());
                String categories= (String)cb_catlogies.getItemAt(cb_catlogies.getSelectedIndex());
                String content_type= (String)cb_content_type.getItemAt(cb_content_type.getSelectedIndex());
                String content = text_content.getText();
                String solution = text_solution.getText();
                String result = (String)cb_result.getItemAt(cb_result.getSelectedIndex());
                
                String feedbackid = uti.getFeedbackId();
                int assign = 0;
                if(cb_result.getSelectedIndex() == 3)
                {
                    
                    for(int i=0;i<cb_assign.getItemCount();i++)
                    {
                        if(cb_assign.getSelectedIndex()==i)
                        {
                            assign = i+1;
                            
                        }
                        
                    }
                }
                else{  assign = 0;}
               
                String sql = "INSERT INTO feedback_history "
                        + "(feedbackid, name, mobile, agentid,type,categories,content_type,content,solution,results,assign) "
                        + "VALUES ('"+feedbackid+"','"+name+"','"+mobile+"','"
                                     +agentObject.getAgentId()+"','"+type+"','"
                                     +categories+"','"+content_type+"','"+content+"','"+solution+"','"+result+"', '"+Integer.toString(assign+1)+"')";
                con.executeUpdate(sql);
         
                String qry="SELECT * FROM feedback_assign where id = 1 ";
                ResultSet rsult=con.executeQuery(qry);
                 String from1="";
                 String password1="";
                while(rsult.next()){
                   from1= rsult.getString("email");
                    password1=rsult.getString("pass");
                    
                }
                final String from=from1;
                final String password=password1;
                final String email=cb_assign.getSelectedItem().toString();
                
                final String smtpServer="smtp.gmail.com";
                
                final String to=getEmail(email);
                
                System.out.println(to);
               
                final String subject="Feedback";
                final String body="Agent : "+agentObject.getAgentId()+"\n"+
                            "From : "+from+"\n"+
                            "to : "+to+"\n"+
                            "Name Customer : "+name+"\n"+
                            "Phone : "+mobile+"\n"+
                            "Feedback type : "+type+"\n"+
                            "Catelogies : "+categories+"\n"+
                            "Content_type : "+content_type+"\n"+
                            "Content : "+content+"\n"+
                            "Solution : "+solution+"\n" ;
              
           //     final String body="hajsdgajhgda";
                
               //waiting
                wait_form=new WaitingForm(this);
                wait_form.setVisible(true);
                
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            if(cb_result.getSelectedIndex() == 3)
                            {
                                send(smtpServer, to, from, password, subject, body);
                            }
                            System.out.println("Finish!");
           
                            wait_form.dispose();
                            
                            FeedbackForm.this.dispose();
                            JOptionPane.showMessageDialog(null, "save successful");
                            //reload table
                            ConnectDatabase con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
                            String query = "SELECT f.*,a.name as `a_name`,a.email FROM feedback_history f LEFT OUTER JOIN feedback_assign a ON f.assign=a.id"
                                               + " WHERE mobile = '"+mobile+"' and a.id<>'1'"
                                                    + " ORDER BY f.datetime DESC "
                                                    + " LIMIT 0,10 ";
                            ResultSet rs = con.executeQuery(query);
                            if(rs != null){
                                
                                agentclient.displayHistory(rs); 
//                                mainform2.table_report.setAutoCreateRowSorter(true);
//                                mainform2.table_report.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.DESCENDING)));
                            }
                            con.closeConnect();
                        } catch (Exception ex) {
                            Logger.getLogger(FeedbackForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                },"btnSave feedback").start();
               
                
                
            }
            con.closeConnect();
                      
        }catch(Exception e){
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void cb_assignItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_assignItemStateChanged
        String email=cb_assign.getSelectedItem().toString();
        txtEmail.setVisible(true);
        txtEmail.setText(getEmail(email));
     
                
    }//GEN-LAST:event_cb_assignItemStateChanged
    
    public String getEmail(String name)
    {
        try {
           con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
          
            if(con.isConnect()){
               String query="SELECT * FROM feedback_assign where name='"+name+"'";
               ResultSet rs2 = con.executeQuery(query);

               if(rs2.next())
               {
                  System.out.println(rs2.getString("email"));
                  String out=rs2.getString("email");
                  con.closeConnect();
                  return out;
               }
            }
            con.closeConnect();
        } catch (Exception ex) {
           Logger.getLogger(FeedbackForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public void showComboBox()
    {
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
           
             if(con.isConnect()){
                 //////////
                 String query1="SELECT * FROM feedback_assign WHERE id<>'1'";
                 ResultSet rs1 = con.executeQuery(query1);

                 ArrayList<String> cb_assin = new ArrayList<>();
                 while(rs1.next())
                 {
                     cb_assin.add(rs1.getString("name"));

                 }

                 cb_assign.setModel(new DefaultComboBoxModel(cb_assin.toArray()));
                 ///////
                 String query2="SELECT * FROM feedback_catelogies";
                 ResultSet rs2 = con.executeQuery(query2);

                 ArrayList<String> cb_catelories = new ArrayList<>();
                 while(rs2.next())
                 {
                     cb_catelories.add(rs2.getString("desc"));

                 }

                 cb_catlogies.setModel(new DefaultComboBoxModel(cb_catelories.toArray()));
                 //////////
                   String query3="SELECT * FROM feedback_results";
                 ResultSet rs3 = con.executeQuery(query3);

                 ArrayList<String> cb_rsult = new ArrayList<>();
                 while(rs3.next())
                 {
                     cb_rsult.add(rs3.getString("desc"));

                 }

                 cb_result.setModel(new DefaultComboBoxModel(cb_rsult.toArray()));
                 //////////
                 String query4="SELECT * FROM feedback_content_type";
                 ResultSet rs4 = con.executeQuery(query4);

                 ArrayList<String> cb_con_type = new ArrayList<>();
                 while(rs4.next())
                 {
                     cb_con_type.add(rs4.getString("desc"));

                 }

                 cb_content_type.setModel(new DefaultComboBoxModel(cb_con_type.toArray()));
                 ////////
                  String query5="SELECT * FROM feedback_type";
                 ResultSet rs5 = con.executeQuery(query5);

                 ArrayList<String> cb_type = new ArrayList<>();
                 while(rs5.next())
                 {
                     cb_type.add(rs5.getString("desc"));

                 }

                 cb_feedback_type.setModel(new DefaultComboBoxModel(cb_type.toArray()));
             }
             con.closeConnect();
        } catch (Exception ex) {
            Logger.getLogger(FeedbackForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public static void send(final String smtpServer, final String to,final String from,final String psw,
                                final String subject,final String body) throws Exception{
        // java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Properties props = System.getProperties();
            // –
            props.put("mail.smtp.host", smtpServer);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.starttls.enable","true");
            final String login = from;
            final String pwd = psw;
            Authenticator pa = null;
            if (login != null && pwd != null) {
                props.put("mail.smtp.auth", "true");
                pa = new Authenticator (){
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, pwd);
                    }
                };
            }//else: no authentication
            Session session = Session.getInstance(props, pa);
            // — Create a new message –
            Message msg = new MimeMessage(session);
            // — Set the FROM and TO fields –
            msg.setFrom(new InternetAddress(from));
            
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));           
           // — Set the subject and body text –
            msg.setSubject(subject);
            msg.setText(body);
            // — Set some other header information –
            msg.setHeader("X-Mailer", "LOTONtechEmail");
            msg.setSentDate(new Date());
            msg.saveChanges();
            // — Send the message –
            Transport.send(msg);
            System.out.println("Message sent OK.");

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
            java.util.logging.Logger.getLogger(FeedbackForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FeedbackForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FeedbackForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FeedbackForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FeedbackForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    public javax.swing.JButton btn_save;
    public javax.swing.JComboBox cb_assign;
    public javax.swing.JComboBox cb_catlogies;
    public javax.swing.JComboBox cb_content_type;
    public javax.swing.JComboBox cb_feedback_type;
    public javax.swing.JComboBox cb_result;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    public javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lb_mobile;
    public javax.swing.JLabel lbl_Name;
    public javax.swing.JTextArea text_content;
    public javax.swing.JTextArea text_solution;
    public javax.swing.JLabel txtAsTo;
    public javax.swing.JTextField txtEmail;
    // End of variables declaration//GEN-END:variables
}

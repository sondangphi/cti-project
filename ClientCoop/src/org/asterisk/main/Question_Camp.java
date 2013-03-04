/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;

/**
 *
 * @author PHUONGTRANG
 */
public class Question_Camp extends javax.swing.JDialog {
    
    
   private MainForm own;

   
    private  String filename = "infor.properties";       
    private Utility uti;
    private static String Mysql_server = "172.168.10.202";      
     private static String Mysql_dbname = "ast_callcenter";
    private static String Mysql_user = "callcenter";
    private static String Mysql_pwd  = "callcenter";   
    private ConnectDatabase con;
   
    ResultSet result;
    DefaultTableModel dt;
    Vector rowdata;
    String sql;
    
    String title_call;
    String Camp_name;
   
    public Question_Camp(int camp_id, MainForm own, String agent,String title_call, String Camp_name)  
    {
        super(own);
        this.own = own;
        this.own.setEnabled(false);
        this.title_call=title_call;
        this.Camp_name=Camp_name;
        initComponents();
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 11));
        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 11));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 11));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 11));
        
        try{
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
        }catch(Exception e){
        }
        setTitle("CALL : " +title_call);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Campaign name :  "+ Camp_name));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation(new Point((screenSize.width -this.getWidth()) / 2,
                              (screenSize.height-this.getHeight()) / 2));

       
        ShowContentComponent(camp_id, agent); 
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent ce) {
                ques.autoSize();
                super.componentShown(ce);
            }
        });
        showComboBox();
    }

    
   
    private void ShowContentComponent(int camp_id, String agent)
    {
        try 
        {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect())
            {
                
                sql="SELECT ques.*"+"\r\n"+
                   "FROM `_call` AS ass"+"\r\n"+
                   "LEFT OUTER JOIN `agent_login` AS ag ON ag.`id`=ass.`agent_id`"+"\r\n"+
                   "LEFT OUTER JOIN `_campaign_detail` AS cam_de ON ass.`camp_detail_id`=cam_de.`id`"+"\r\n"+
                   "LEFT OUTER JOIN `_campaign` AS cam ON cam.`id`=cam_de.`camp_id`"+"\r\n"+
                   "LEFT OUTER JOIN `_question` AS ques ON cam.`id`=ques.`camp_id`"+"\r\n";


                sql+="WHERE ag.`agent_id`='"+agent+"'"+"\r\n"+
                    "AND cam.id='"+ camp_id +"'" + "\r\n" +
                    "GROUP BY ques.id"+"\r\n";

              
                result = con.executeQuery(sql);

                while (result.next()) 
                {
                    String content=result.getString("content");
                    int type=Integer.parseInt(result.getString("question_type_id")) ;

                    ques.add(type, content);
                  

                }   
            }
            con.closeConnect();
        } catch (Exception ex) {
            Logger.getLogger(Question_Camp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ques = new nttnetworks.com.controls.question();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblCus_id = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbl_gender = new javax.swing.JLabel();
        lbl_birth = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbl_Addr = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtCamp_desc = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        cbxStatus = new javax.swing.JComboBox();
        btnfinish = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("QUESTION");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Question"));

        ques.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ques, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ques, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Infomation"));

        jLabel5.setText("Customer : ");

        lblCus_id.setText("Cus_Name");

        jLabel3.setText("Gender     : ");

        lbl_gender.setText("Gender");

        lbl_birth.setText("1990");

        jLabel7.setText("Birthday : ");

        jLabel6.setText("Address    : ");

        lbl_Addr.setText("Hồ Chí Minh");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabel7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_gender)
                                    .addComponent(lblCus_id)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(lbl_Addr)))
                        .addGap(0, 95, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(lbl_birth)
                .addGap(52, 52, 52))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblCus_id))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lbl_gender)
                    .addComponent(jLabel7)
                    .addComponent(lbl_birth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lbl_Addr)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Campaign_name"));

        txtCamp_desc.setEditable(false);
        txtCamp_desc.setColumns(20);
        txtCamp_desc.setLineWrap(true);
        txtCamp_desc.setRows(3);
        txtCamp_desc.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtCamp_desc);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        txtNote.setColumns(20);
        txtNote.setRows(5);
        jScrollPane1.setViewportView(txtNote);

        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Calling", "Complete" }));
        cbxStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxStatusItemStateChanged(evt);
            }
        });
        cbxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxStatusActionPerformed(evt);
            }
        });

        btnfinish.setText("FINISH");
        btnfinish.setEnabled(false);
        btnfinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfinishActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnfinish)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancel)
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 14, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                            .addComponent(btnfinish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(cbxStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    public static String injectSql(String str) {
       return str.replace("'", "\\'")
            .replace("\"", "\\\\\"");
    }
    
    private void btnfinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfinishActionPerformed
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
             if(con.isConnect())
             {
                ResultSet result=null;
                String sql1="SELECT * FROM _question_result WHERE call_id='"+ title_call+"' AND ";
                result=con.executeQuery(sql1);
                
                if (result.next()) {
                    JOptionPane.showMessageDialog(null, "data is exist");
                    result.close();
                }
                else
                {
                   JOptionPane.showMessageDialog(null, "select index : "+cbxStatus.getSelectedIndex()+"\n"+
                                                        "item count : "+cbxStatus.getItemCount()+"\n");
                     if(cbxStatus.getSelectedIndex()==(cbxStatus.getItemCount()-1))
                    {
                        JOptionPane.showMessageDialog(null, (String)cbxStatus.getSelectedItem());
                        for (int i=0; i < ques.length(); i++) {
                            String[] rs = ques.get(i).getAnswerResult();
                            JOptionPane.showMessageDialog(null, "1");
                            if (rs.length > 0) {
                                System.out.println("cau hoi so : "+i+" - có : "+rs.length);JOptionPane.showMessageDialog(null, "2");
                                String answerArray = "";

                                for(String t:rs){
                                    System.out.println("t "+t);JOptionPane.showMessageDialog(null, "3");

                                    answerArray += ",\""+ injectSql(t) +"\"";
                                }   
                                JOptionPane.showMessageDialog(null, "4");
                                String sql = "INSERT INTO _question_result VALUES("+null+", '"+i+"','"+title_call+"','"
                                             + "[" + answerArray.substring(1) + "]')";
                                JOptionPane.showMessageDialog(null, sql);
                                JOptionPane.showMessageDialog(null, "5");
                                con.executeUpdate(sql);
                                JOptionPane.showMessageDialog(null, "6");

                             }
                            
                        }
                        
                        JOptionPane.showMessageDialog(null,"Insert successful");
                          String query_update =
                            "UPDATE  `_call` SET  `note` =  '"+injectSql(txtNote.getText()) + "', `status_id`='"+(cbxStatus.getSelectedIndex()+1)+"'"
                          + "WHERE  `id` ='"+title_call+"'";
                    con.executeUpdate(query_update);
                    JOptionPane.showMessageDialog(null,"Update1 Note successful");
                    }
                     else{
                          String query_update =
                            "UPDATE  `_call` SET  `note` =  '"+injectSql(txtNote.getText()) + "', `status_id`='"+(cbxStatus.getSelectedIndex()+1)+"'"
                          + "WHERE  `id` ='"+title_call+"'";
                    con.executeUpdate(query_update);
                    JOptionPane.showMessageDialog(null,"Update2 Note successful");

                     }
                    
                   
                   
                    
                   
                     
                }
            }
            con.closeConnect();
        } catch (Exception ex) {}
        
        own.showCustomer();
        own.setEnabled(true);
        own.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnfinishActionPerformed

    private void cbxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxStatusActionPerformed
        // TODO add your handling code here:
        

    }//GEN-LAST:event_cbxStatusActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
        own.setEnabled(true);
        own.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cbxStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxStatusItemStateChanged
       int index = cbxStatus.getItemCount() - cbxStatus.getItemCount();
       System.out.println("count : "+cbxStatus.getItemCount());
       System.out.println("index : "+index);
       System.out.println("get index : "+cbxStatus.getSelectedIndex());
        if(cbxStatus.getSelectedIndex() == index){
            btnfinish.setEnabled(false);
            
            
        }else
        {
            btnfinish.setEnabled(true);
            
            
        }
    }//GEN-LAST:event_cbxStatusItemStateChanged
 
   
   public JLabel getlblGender()
    {
       return lbl_gender;
    }
 
    public JTextArea getTextCamp_Desc()
    {
        return txtCamp_desc;
    }
    public JLabel getlblCus_id()
    {
      return lblCus_id;
    }
  
    public JLabel getlblAddr()
    {
      return lbl_Addr;
    }
    public JLabel getlblBirth()
    {
      return lbl_birth;
    }
    
     public void showComboBox()
    {
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
             if(con.isConnect()){
                 //////////
                 String query1="SELECT * FROM _call_status";
                 ResultSet rs1 = con.executeQuery(query1);

                 ArrayList<String> cb_assin = new ArrayList<>();
                 while(rs1.next())
                 {
                     cb_assin.add(rs1.getString("desc"));

                 }

                 cbxStatus.setModel(new DefaultComboBoxModel(cb_assin.toArray()));
                 ///////
                
             }
             con.closeConnect();
        } catch (Exception ex) {
            Logger.getLogger(FeedbackForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 //<editor-fold defaultstate="collapsed" desc="Variables declaration - do not modify">      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnfinish;
    public javax.swing.JComboBox cbxStatus;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCus_id;
    private javax.swing.JLabel lbl_Addr;
    private javax.swing.JLabel lbl_birth;
    private javax.swing.JLabel lbl_gender;
    private nttnetworks.com.controls.question ques;
    private javax.swing.JTextArea txtCamp_desc;
    public javax.swing.JTextArea txtNote;
    // End of variables declaration//GEN-END:variables
 //</editor-fold>
}

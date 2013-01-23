/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.ConnectDatabase;

/**
 *
 * @author PHUONGTRANG
 */
public class Question_Camp extends javax.swing.JDialog {
    
    static AgentObject agentObject;
   private MainForm own;
                       		
    private static String Mysql_server = "172.168.10.5";      
    private static String Mysql_dbname = "ast_callcenter";
    private static String Mysql_user = "callcenter";
    private static String Mysql_pwd  = "callcenter";   
     private ConnectDatabase con;
   
    ResultSet result;
    DefaultTableModel dt;
    Vector rowdata;
    String sql;
    
    /**
     * Creates new form gggg
     */
    public Question_Camp(MainForm own, String agent)  
    {
        super(own);
        this.own = own;
        this.own.setEnabled(false);
        
        initComponents1();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation(new Point((screenSize.width -this.getWidth()) / 2,
                              (screenSize.height-this.getHeight()) / 2));

        ShowContentComponent(agent); 
    }//

    
   
    private void ShowContentComponent(String agent)
    {
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
            if(con.isConnect()){
                sql="SELECT ques.*"+"\r\n"+
                   "FROM `_call` AS ass"+"\r\n"+
                   "LEFT OUTER JOIN `agent_login` AS ag ON ag.`id`=ass.`agent_id`"+"\r\n"+
                   "LEFT OUTER JOIN `_campaign_detail` AS cam_de ON ass.`camp_detail_id`=cam_de.`id`"+"\r\n"+
                   "LEFT OUTER JOIN `_campaign` AS cam ON cam.`id`=cam_de.`camp_id`"+"\r\n"+
                   "LEFT OUTER JOIN `_question` AS ques ON cam.`id`=ques.`camp_id`"+"\r\n";


                sql+="WHERE ag.`agent_id`='"+agent+"'"+"\r\n"+
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

        ques = new nttnetworks.com.controls.question();
        btnfinish = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblCam_id = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblAgent_id = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblCus_id = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        cbxStatus = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblCall = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblDetail = new javax.swing.JLabel();

        setTitle("QUESTION");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                onClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                onClosing(evt);
            }
        });

        btnfinish.setText("FINISH");
        btnfinish.setEnabled(false);
        btnfinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfinishActionPerformed(evt);
            }
        });

        jLabel1.setText("CAMPAIGN : ");

        lblCam_id.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCam_id.setText("Camp_id");

        jLabel3.setText("AGENT : ");

        lblAgent_id.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAgent_id.setText("agent_id");

        jLabel5.setText("CUSTOMER : ");

        lblCus_id.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCus_id.setText("Cus_id");

        txtNote.setColumns(20);
        txtNote.setRows(5);
        jScrollPane1.setViewportView(txtNote);

        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Calling", "Complete" }));
        cbxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxStatusActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel2.setText("CALL : ");

        lblCall.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCall.setText("1");

        jLabel4.setText("CAMP DETAIL : ");

        lblDetail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDetail.setText("DETAIL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ques, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnfinish)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(0, 30, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCam_id))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDetail)))
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAgent_id)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCall)
                                .addGap(54, 54, 54))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblCus_id)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(lblCall))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lblAgent_id))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lblCam_id)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(lblDetail))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lblCus_id)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(ques, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel)
                        .addComponent(btnfinish)))
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void initComponents1() {

        ques = new nttnetworks.com.controls.question();
        btnfinish = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblCam_id = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblAgent_id = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblCus_id = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNote = new javax.swing.JTextArea();
        cbxStatus = new javax.swing.JComboBox();
        btnCancel = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblCall = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblDetail = new javax.swing.JLabel();

       // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QUESTION");

        btnfinish.setText("FINISH");
        btnfinish.setEnabled(false);
        btnfinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfinishActionPerformed(evt);
            }
        });

        jLabel1.setText("CAMPAIGN : ");

        lblCam_id.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCam_id.setText("Camp_id");

        jLabel3.setText("AGENT : ");

        lblAgent_id.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblAgent_id.setText("agent_id");

        jLabel5.setText("CUSTOMER : ");

        lblCus_id.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCus_id.setText("Cus_id");

        txtNote.setColumns(20);
        txtNote.setRows(5);
        jScrollPane1.setViewportView(txtNote);

        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Calling", "Complete" }));
        cbxStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxStatusActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jLabel2.setText("CALL : ");

        lblCall.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCall.setText("1");

        jLabel4.setText("CAMP DETAIL : ");

        lblDetail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDetail.setText("DETAIL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(ques, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnfinish)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(0, 30, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCam_id))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDetail)))
                        .addGap(74, 74, 74)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAgent_id)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblCall)
                                .addGap(54, 54, 54))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblCus_id)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(lblCall))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lblAgent_id))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lblCam_id)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(lblDetail))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lblCus_id)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(ques, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCancel)
                        .addComponent(btnfinish)))
                .addGap(13, 13, 13))
        );

        pack();
    }
    private static String injectSql(String str) {
       return str.replace("'", "\\'")
            .replace("\"", "\\\\\"");
    }
    
    private void btnfinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfinishActionPerformed
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);
             if(con.isConnect()){
           
            ResultSet result=null;
            String sql1="SELECT * FROM _question_result where call_id='"+lblCall.getText()+"'";
             result=con.executeQuery(sql1);
            if (result.next()) {
                   JOptionPane.showMessageDialog(null, "data is exist");
                   result.close();
            }
            else
            {
                for (int i=0; i < ques.length(); i++) {
                    String[] rs = ques.get(i).getAnswerResult();

                    if (rs.length > 0) {
                        System.out.println("cau hoi so : "+i+" - có : "+rs.length);
                        String answerArray = "";

                        for(String t:rs){
                            System.out.println("t "+t);

                            answerArray += ",\""+ injectSql(t) +"\"";
                        }   

                        String sql = "INSERT INTO _question_result VALUES( '"+i+"','"+lblCall.getText()+"','"
                                     + "[" + answerArray.substring(1) + "]')";

                        con.executeUpdate(sql);
                        

                        }
                    }
                  String query_update =
                        "UPDATE  `_call` SET  `note` =  '"+injectSql(txtNote.getText()) + "', `status_id`='3'"
                      + "WHERE  `id` ='"+lblCall.getText()+"'";
                con.executeUpdate(query_update);
                JOptionPane.showMessageDialog(null,"Update Note successful");
                
                JOptionPane.showMessageDialog(null,"Insert successful");
            }

           
             }
             con.closeConnect();
        } catch (Exception ex) {}
        
        this.own.showCustomer();
        own.setEnabled(true);
        own.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnfinishActionPerformed

    private void cbxStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxStatusActionPerformed
        // TODO add your handling code here:
        int i=cbxStatus.getSelectedIndex();
        if(i==1)
        {

            btnfinish.setEnabled(true);
        }
        else
        {

            btnfinish.setEnabled(false);
        }

    }//GEN-LAST:event_cbxStatusActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        dispose();
        own.setEnabled(true);
        own.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void onClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onClosing

    }//GEN-LAST:event_onClosing
 
    
   public JLabel getlblAgent_id()
  {
      return lblAgent_id;
  }
   public JLabel getlblCall()
  {
      return lblCall;
  }
    public JLabel getlblCam_id()
  {
      return lblCam_id;
  }
      public JLabel getlblCus_id()
  {
      return lblCus_id;
  }
      public JLabel getlblDetail()
  {
      return lblDetail;
  }
   
      public JTextArea gettxtNote() {
          return txtNote;
      }
      
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnfinish;
    private javax.swing.JComboBox cbxStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAgent_id;
    private javax.swing.JLabel lblCall;
    private javax.swing.JLabel lblCam_id;
    private javax.swing.JLabel lblCus_id;
    private javax.swing.JLabel lblDetail;
    private nttnetworks.com.controls.question ques;
    private javax.swing.JTextArea txtNote;
    // End of variables declaration//GEN-END:variables
}

 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import org.asterisk.utility.Utility;

/**
 *
 * @author leehoa
 */
public class ConfigForm extends javax.swing.JFrame {
    private static Utility uti = new Utility();
    static String filename = "infor.properties";
    static String host = "";
    static String qport = "";
    static String aport = "";
    LoginForm loginform;
                
    /**
     * Creates new form ConfigForm
     */
//    LoginForm m ;
    public ConfigForm() {
        initComponents();
        try{
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_config.png");
            host = uti.readInfor(filename, "host");
            aport = uti.readInfor(filename, "aport");
            qport = uti.readInfor(filename, "qport");
            tx_server.setText(host);
//            tx_aport.setText(aport);
//            tx_qport.setText(qport);
            this.setIconImage(image); 
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
            initComponents();
        }catch(Exception e){
        }        
    }
    
    public ConfigForm(LoginForm m) {
        initComponents();
        try{
            Image image = Toolkit.getDefaultToolkit().getImage("images/icon_config.png");
            host = uti.readInfor(filename, "host");
            aport = uti.readInfor(filename, "aport");
            qport = uti.readInfor(filename, "qport");
            tx_server.setText(host);
//            tx_aport.setText(aport);
//            tx_qport.setText(qport);
            loginform = m;
            this.setIconImage(image); 
            this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
            Color LightSkyBlue2 = new Color(164, 211, 238);
            Color white = new Color(255,255,255);    
            Color yellow2 = new Color(238, 238, 0);        
            Color khaki1 = new Color( 255, 246, 143);  
            Color LightGoldenrod1 = new Color(255, 236, 139);
            Color PaleGoldenrod = new Color(238, 232, 170);
            Color blue1 = new Color(0, 0, 255);
            Color dodgerBlue3 = new Color(24, 116, 205); 
            this.getContentPane().setBackground(white);
//            btn_finish.setBackground(dodgerBlue3);
//            btn_finish.setForeground(white);
            
            
            
        }catch(Exception e){
        }        
    }    
    
//    this.addWindowStateListener(new WindowAdapter() {
//        public void windowClosing(WindowEvent e) {
//            ExitAction.getInstance().actionPerformed(null);
//        }
//
//    });    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btn_finish = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        tx_server = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setOpaque(false);
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        btn_finish.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btn_finish.setText("Finish");
        btn_finish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_finishActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Asterisk Server ");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        tx_server.setEnabled(false);
        tx_server.setOpaque(false);
        tx_server.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tx_serverMouseClicked(evt);
            }
        });
        tx_server.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tx_serverKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(126, 126, 126))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tx_server, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 13, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tx_server, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_finish, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, tx_server});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_finishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_finishActionPerformed
        // TODO add your handling code here:
        try{
            host = tx_server.getText();
//            aport = tx_aport.getText();
//            qport = tx_qport.getText();
            uti.writeInfor(filename, "host", host);
            uti.writeInfor(filename, "aport", aport);
            uti.writeInfor(filename, "qport", qport);
            uti.writeInfor(filename, "MySql_Server", host);
            this.dispose();
            loginform.host = uti.readInfor(filename, "host");
            loginform.aport = Integer.parseInt(uti.readInfor(filename, "aport"));
            loginform.qport = Integer.parseInt(uti.readInfor(filename, "qport"));             
            loginform.getListQueue();
            loginform.setEnabled(true);
            loginform.setVisible(true);
        }catch(Exception e ){
            
        }

    }//GEN-LAST:event_btn_finishActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
//        System.out.println("rrrrrrrrrrrrr");
    }//GEN-LAST:event_jLabel3MouseClicked

    private void tx_serverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tx_serverMouseClicked
        // TODO add your handling code here:
        tx_server.setEnabled(true);
    }//GEN-LAST:event_tx_serverMouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:
        if(tx_server.isEnabled())
            tx_server.setEnabled(false);
//        if(tx_aport.isEnabled())
//            tx_aport.setEnabled(false);
//        if(tx_qport.isEnabled())
//            tx_qport.setEnabled(false);
    }//GEN-LAST:event_jPanel1MouseClicked

    private void tx_serverKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tx_serverKeyPressed
        // TODO add your handling code here:
         if(evt.getKeyCode() == 10){
             btn_finishActionPerformed(null);
         }        
    }//GEN-LAST:event_tx_serverKeyPressed

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
            java.util.logging.Logger.getLogger(ConfigForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConfigForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConfigForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConfigForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                new ConfigForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_finish;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JTextField tx_server;
    // End of variables declaration//GEN-END:variables
}
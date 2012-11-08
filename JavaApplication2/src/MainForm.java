
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
public class MainForm extends javax.swing.JFrame {

    TrayIcon trayIcon;
    SystemTray stray;
    DefaultTableModel d = new DefaultTableModel();
    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        String[]columnNamess = {"First Name", "Last Name", "Date of Birth", "Account Balance", "Gender"};
        d.addColumn("columnNamess");
        d.addColumn("columnNamess");
//        d.addColumn(d, os);
        Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
        if (SystemTray.isSupported()) {
            /////////////////////
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
            System.out.println("system tray supported");
            stray = SystemTray.getSystemTray();                        
//            ActionListener exitListener = new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    System.out.println("Exiting....");
//                    System.exit(0);
//                }
//            };
//            PopupMenu popup = new PopupMenu();
//            MenuItem defaultItem = new MenuItem("Exit");
//            defaultItem.addActionListener(exitListener);
//            popup.add(defaultItem);
//            defaultItem = new MenuItem("Configuration");
//            defaultItem.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    stray.remove(trayIcon);
//                    setVisible(true);
//                    System.out.println("Tray icon removed");                        
//                }
//            });
//            popup.add(defaultItem);
//            trayIcon = new TrayIcon(image, "config", popup);
            trayIcon = new TrayIcon(image, "config");
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(mouseListener);
            //////////////////////////
            
        } else {
            System.out.println("system tray not supported");
        }                        
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String time = dateFormat2.format(cal.getTime());
        tx_datetime.setText(time);                
        ////////////////////////////////////////////////////////////////////////
        
                String items[] = {"Java", "JSP", "PHP", "C", "C++"};
                String[] columnNames = {"First Name",
                        "Last Name",
                        "Sport",
                        "# of Years",
                        "Vegetarian"};
                
                Object[][] data = {
                            {"Kathy", "Smith","Snowboarding", new Integer(5), new Boolean(false)},
                            {"John", "Doe","Rowing", new Integer(3), new Boolean(true)},
                            {"Sue", "Black","Knitting", new Integer(2), new Boolean(false)},
                            {"Jane", "White","Speed reading", new Integer(20), new Boolean(true)},
                            {"Joe", "Brown","Pool", new Integer(10), new Boolean(false)}
                            };
                jTable1.setModel(new CustomTableModel(columnNames,data));                
                jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tx_agent = new javax.swing.JLabel();
        tx_datetime = new javax.swing.JLabel();
        tx_test = new javax.swing.JLabel();
        bt_view = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login form for agent");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setViewportView(jTable1);

        tx_agent.setText("AgentId: a");

        tx_datetime.setText("datetimenow");

        tx_test.setText("Start Date");

        bt_view.setText("View");
        bt_view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_viewActionPerformed(evt);
            }
        });

        jLabel5.setText("End Date");

        jLabel2.setText("jLabel1");

        jMenu1.setText("File");

        jMenuItem1.setText("Configuration");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Exit");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem2MouseClicked(evt);
            }
        });
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 853, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tx_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(tx_datetime)
                        .addGap(29, 29, 29)
                        .addComponent(tx_test, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bt_view)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {tx_agent, tx_datetime});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel5, tx_test});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tx_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tx_datetime)
                        .addComponent(tx_test)
                        .addComponent(bt_view)
                        .addComponent(jLabel5)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel5, tx_agent, tx_datetime, tx_test});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {            
            addtraysys();
            System.out.println("formWindowClosing exit system");
        } catch (AWTException ex) {            
        }
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
//        System.out.println("formWindowClosed exit system");
    }//GEN-LAST:event_formWindowClosed

    private void bt_viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_viewActionPerformed
        // TODO add your handling code here:        
        ///////////////////////////
        try{
            ConnectDatabase con = new  ConnectDatabase();
            String sql = "SELECT * FROM login_action where agent_id ='a'";
            String sql2 = "SELECT agent_id,SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) FROM login_action  group by agent_id";
            String sql3 = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) FROM login_action  where datetime_login LIKE  '%2012-11-01%' AND agent_id ='a'";
            ResultSet rs = con.executeQuery(sql);
            String items[] = {"Java", "JSP", "PHP", "C", "C++","Visual","C#","J#"};
            String colname[] = {"Agent", "Work Time", "Pause Time", "Hold Time", "Talk Time","Call Receive","C#","J#"};
            ResultSetMetaData rsMeta = rs.getMetaData();
            int count = rsMeta.getColumnCount();            
            Vector col = new Vector(count);
            for (int i = 0; i <colname.length; i++) {
                col.addElement(colname[i].toString());
                System.out.println("\t"+colname[i].toString());
            }
                
//            col.addElement("Agent Name");
//            col.addElement("Work Time");
//            col.addElement("Hold Time");
//            col.addElement("Pause Time");
//            col.addElement("InComing Call");
//            col.addElement("OutGoing Call");
//            col.addElement("An");
//            col.addElement("8");
         
//            for (int i = 1; i <= count; i++) 
//                col.addElement(rsMeta.getColumnName(i));
        
//            col.addElement("");
            Vector row = new  Vector();
            while (rs.next()) {
                Vector dataRow = new Vector(count);
                for (int i = 1; i <= count; i++) {
                    dataRow.addElement(rs.getObject(i));
                }
                row.addElement(dataRow);
            }
            jTable1.setModel(new tableModel(col, row));
//            jTable1.setModel(new rsTableModel(rs));
            
            
        }catch(Exception e){
            System.out.println(e);
        }
        
        
    }//GEN-LAST:event_bt_viewActionPerformed
    public void addtraysys() throws AWTException{
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
        stray.add(trayIcon); 
        setVisible(false);
    }
    
    public void exit_system(){
//        System.exit(0);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
        int kq=JOptionPane.showConfirmDialog(null, "Do you want to exit Admin control ?","Congfig",JOptionPane.YES_NO_OPTION);
        if(kq==0){
            System.exit(0);
            System.out.println("exit system");
        }else{
            System.out.println("don't exit system");
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
    private javax.swing.JButton bt_view;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTable1;
    private javax.swing.JLabel tx_agent;
    private javax.swing.JLabel tx_datetime;
    private javax.swing.JLabel tx_test;
    // End of variables declaration//GEN-END:variables
}

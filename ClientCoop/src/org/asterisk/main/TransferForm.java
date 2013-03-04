/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JRootPane;
import javax.swing.ListSelectionModel;
import org.asterisk.model.AgentObject;
import org.asterisk.utility.Agent;
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
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.QueueMemberPausedEvent;

/**
 *
 * @author leehoa
 */
public class TransferForm extends javax.swing.JFrame implements AsteriskQueueListener, AsteriskServerListener, ManagerEventListener{

    private Agent agentClient = null;
    private AgentObject agentObject = null;
    private AsteriskServer asteriskServer;
    private ManagerConnection manaConnect = null;
    private Utility uti = new Utility();
    private String filename = "infor.properties";
    private String Mysql_server = "172.168.10.208";      
    private String Mysql_dbname = "cti_database";
    private String Mysql_user = "cti";
    private String Mysql_pwd  = "123456";   
    private String astServer = "172.168.10.202";
    private String astUserSystem = "manager";
    private String astPwdSystem = "123456";   
    private MainForm mainForm ;
    /**
     * Creates new form TransferForm
     */
    public TransferForm() {
        initComponents();
        try{
           list_agent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
           connectAsterisk();
           queueListener();            
        }catch(Exception ex){
        }        
    }
    public TransferForm(Agent agent, AgentObject agentOb, final MainForm main) {
       final Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                while(main.isVisible()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) { break; }
                }
                
                TransferForm.this.dispose();
            }
        });
        thr.start();
        
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent we) {
                thr.interrupt();
                super.windowClosed(we);
            }
        });
        
        initComponents();
        
        try{
            agentClient = agent;
            agentObject = agentOb;
            mainForm = main;
            list_agent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list_agent.setCellRenderer(new SelectedListCellRenderer());
            setLocationRelativeTo(null);
//            Mysql_dbname = uti.readInfor(filename, "MySql_database");
//            Mysql_server = uti.readInfor(filename, "MySql_server");
//            Mysql_user = uti.readInfor(filename, "MySql_user");
//            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
//            astServer = uti.readInfor(filename, "astServer");
//            astPwdSystem = uti.readInfor(filename, "astPwdSystem");
//            astUserSystem = uti.readInfor(filename, "astUserSystem");
            connectAsterisk();
            queueListener();
            agentAvailable();
        }catch(Exception ex){
        }        
    }    

    private void connectAsterisk(){
        try{
            asteriskServer = new DefaultAsteriskServer("172.168.10.202", "manager", "123456");
            asteriskServer.addAsteriskServerListener(this);  
            manaConnect = asteriskServer.getManagerConnection();
            manaConnect.addEventListener(this);
            manaConnect.sendAction(new StatusAction());            
        }catch(Exception ex){}
    }
    private void disconnectAsterisk(){ 
        asteriskServer.removeAsteriskServerListener(this);   
        manaConnect.removeEventListener(this);
        asteriskServer.shutdown();             
    }
    private void queueListener(){
        for (AsteriskQueue asteriskQueue : asteriskServer.getQueues()){
            asteriskQueue.addAsteriskQueueListener(this);
        }        
    }    
    
    private void agentAvailable(){
        DefaultListModel listModel = new DefaultListModel();
        String iface = "SIP/"+agentObject.getInterface();
        if(list_agent != null)
            list_agent.removeAll();
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
        list_agent.setModel(listModel);      
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_transfer = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_agent = new javax.swing.JList();
        btn_close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        btn_transfer.setText("Transfer Call");
        btn_transfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_transferActionPerformed(evt);
            }
        });

        list_agent.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        list_agent.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list_agent);

        btn_close.setText("CLose");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_close, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_transfer)
                    .addComponent(btn_close))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_transferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_transferActionPerformed
        try{
            String temp = (String)list_agent.getSelectedValue();
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

    }//GEN-LAST:event_btn_transferActionPerformed

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        // TODO add your handling code here:
        mainForm.btn_transfer.setEnabled(true);
        disconnectAsterisk();
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed

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
            java.util.logging.Logger.getLogger(TransferForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransferForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransferForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransferForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransferForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    private javax.swing.JButton btn_transfer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list_agent;
    // End of variables declaration//GEN-END:variables

    
/*AsteriskQueueListener_begin*/    
    @Override
    public void onNewEntry(AsteriskQueueEntry aqe) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onEntryLeave(AsteriskQueueEntry aqe) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onEntryServiceLevelExceeded(AsteriskQueueEntry aqe) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void onMemberStateChange(AsteriskQueueMember aqm) {
        agentAvailable();
    }    

    @Override
    public void onMemberAdded(AsteriskQueueMember aqm) {
        agentAvailable();
    }

    @Override
    public void onMemberRemoved(AsteriskQueueMember aqm) {
        agentAvailable();
    }
/*AsteriskQueueListener_end*/    

    
/*AsteriskServerListener_begin*/    
    @Override
    public void onNewAsteriskChannel(AsteriskChannel ac) {
        
    }

    @Override
    public void onNewMeetMeUser(MeetMeUser mmu) {
        
    }

    @Override
    public void onNewAgent(AsteriskAgentImpl aai) {
        
    }

    @Override
    public void onNewQueueEntry(AsteriskQueueEntry aqe) {
        
    }
/*AsteriskServerListener_end*/        

    @Override
    public void onManagerEvent(ManagerEvent me) {
        if(me instanceof QueueMemberPausedEvent){
            agentAvailable();
        }
    }
}
class SelectedListCellRenderer extends DefaultListCellRenderer {
     @Override
     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         Color lightSeaGreen = new Color(32, 178, 170);
         Color orangeRed1 = new Color(255, 69, 0);         
         String temp_value = (String)value;
         temp_value = temp_value.substring(temp_value.indexOf("-")+1, temp_value.length());
         switch(temp_value){
             case "READY":
                 c.setBackground(Color.GREEN);
                 break;
             case "TALKING":
                 c.setBackground(Color.RED);
                 break;
             case "UNAVAILABLE":
                 c.setBackground(Color.GRAY);
                 break;
             case "BUSY":
                 c.setBackground(Color.ORANGE);
                 break;
             case "UNKNOWN":
                 c.setBackground(Color.GRAY);
                 break;
             case "INVALID":
                 c.setBackground(Color.GRAY);
                 break;   
             case "PAUSE":
                 c.setBackground(Color.GRAY);
                 break;                 
             case "RINGING":
                 c.setBackground(Color.ORANGE);
                 break;                 
             default :
                 break;
         }
         return c;
     }
}

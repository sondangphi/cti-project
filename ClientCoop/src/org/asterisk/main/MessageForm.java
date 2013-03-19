/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import nttnetworks.com.controls.IPanelTabEvent;
import nttnetworks.com.controls.TabCloseIcon;
import nttnetworks.com.controls.panelTab;
import nttnetworks.com.controls.panelTab1;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;

/**
 *
 * @author PHUONGTRANG
 */
public class MessageForm extends javax.swing.JFrame {

    private String Agent_loged = "unknown";
    public HashMap <String,panelTab1> mapAgent=new HashMap<>();
    private  String filename = "infor.properties";
    private  String Mysql_server = "172.168.10.202";      
    private  String Mysql_dbname = "ast_callcenter";
    private  String Mysql_user = "callcenter";
    private  String Mysql_pwd  = "callcenter"; 
    private static Utility uti;
    private ConnectDatabase con;
    private Agent agentClient;
   
   public static MainForm mainform = null ;
   
   public void receive(String from, String message) {
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

               
                jTabbedPane1.addTab(agent,new TabCloseIcon(), tab);
                int display= jTabbedPane1.getTabCount()-1;
                jTabbedPane1.setSelectedIndex(display);

                mapAgent.put(agent, tab);
                String s="";
                for(int i=0;i< jTabbedPane1.getTabCount();i++)
                {
                    s+=(jTabbedPane1.getTitleAt(i).toString()+",");
                }
                this.setTitle(s.substring(0,s.length()-1));
                this.setSize(jTabbedPane1.getX()+jTabbedPane1.getWidth()+10,this.getHeight());
                tabs = tab;
       }
       
       tabs.showMessage(from, message);
   }
   
    public MessageForm() {
        try {
            initComponents();
            uti = new Utility();
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            //jTabbedPane1.setTabPlacement(3);
            showAgent();
        } catch (Exception ex) {
            Logger.getLogger(MessageForm.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        
    }
    public MessageForm(String Agent_loged, Agent agentClient) {
        try {
            
            initComponents();
           this.setLayout(null);
            uti = new Utility();
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            //jTabbedPane1.setTabPlacement(3);
            showAgent();
            this.Agent_loged = Agent_loged;
            this.agentClient = agentClient;
            this.setSize(jTabbedPane1.getX(),this.getHeight());
        } catch (Exception ex) {
            Logger.getLogger(MessageForm.class.getName()).log(Level.SEVERE, null, ex);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
        jTable1.setShowHorizontalLines(false);
        jTable1.setShowVerticalLines(false);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Show online");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jCheckBox1)))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int i=jTable1.getSelectedRow();
        if(i>=0)
        {
            createPopupMenu();
            if(evt.getClickCount()==2)
            {
                show_chat(); 
            }
          
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
         
        if(jCheckBox1.isSelected())
        {
             showAgentOnline();
        }
        else
        {
            showAgent();
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
//        String s="";
//        for(int i=0;i<=jTabbedPane1.getSelectedIndex();i++)
//        {
//            s=jTabbedPane1.getTitleAt(i);
//        }
//        //System.out.println(s);
//        System.err.println("index : "+jTabbedPane1.getSelectedIndex()+ " : "+ s);
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        if(evt.getKeyCode() == 10){
          show_chat(); 
        }
    }//GEN-LAST:event_jTable1KeyReleased
/////////////popup
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
 
        //Create the popup menu.
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
        //menuItem.addActionListener(this);
        popup.add(menuItem);
 
        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new PopupListener(popup);
        jTable1.addMouseListener(popupListener);
    }
   
    ////////////////////////////
    private void show_chat()
    {
          try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

            if(con.isConnect())
            {
                setSize(jTabbedPane1.getX()+jTabbedPane1.getWidth()+10,this.getHeight());
                
                int row=jTable1.getSelectedRow();
                final String col1=""+jTable1.getValueAt(row,1); 
                for(int i=0;i<jTabbedPane1.getTabCount();i++)
                {
                    if(jTabbedPane1.getTitleAt(i).equals(col1))
                    {
                        jTabbedPane1.setSelectedIndex(i);
                        return;
                    }

                }

              
                final panelTab1 tab=new panelTab1();
                tab.events = new IPanelTabEvent() {
                    @Override
                    public void send() {
                        try {
                            if(!"".equals(tab.getText())){
                                //send
                                agentClient.sendtoServer("120@"+Agent_loged+"@"+col1+"@"+tab.getText());
                                tab.showMessage(Agent_loged, tab.getText());
                                tab.send();
                            }
                        } catch (Exception ex) {}
                    }
                };

//                jTabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                jTabbedPane1.addTab(col1,new TabCloseIcon(), tab);
                int display=jTabbedPane1.getTabCount()-1;
                jTabbedPane1.setSelectedIndex(display);

                mapAgent.put(col1, tab);
               
                String s="";
                for(int i=0;i< jTabbedPane1.getTabCount();i++)
                {
                    s+=(jTabbedPane1.getTitleAt(i).toString()+",");
                }
                this.setTitle(s.substring(0,s.length()-1));
            }
            con.closeConnect();
        }
        catch(Exception e)
        {

        }
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
                jTable1.getTableHeader().setReorderingAllowed(false);
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
                    Pattern pattern = Pattern.compile("\\d*");//// \\d+ hay \\d* đều được
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
                for (int j=0; j<st_online.size(); j++) {
                    dt.addRow(st_online.get(j));
                }
                for (int j=0; j<st_offline.size(); j++) {
                    dt.addRow(st_offline.get(j));
                }
                jTable1.setModel(dt);
                
                jTable1.setDefaultRenderer(Object.class, new TableCellRenderer() {
                    final int currentRow = -1;
                        @Override
                        public Component getTableCellRendererComponent(JTable table, 
                                                                        Object value, 
                                                                        boolean isSelected, 
                                                                        boolean hasFocus, 
                                                                        int row, int column) {
                         
                            
                            JLabel out=new JLabel();
                            
                            if (column == 0) {
                                out.setText(Integer.toString(row+1));
                            } else {
                                out.setText((String)value);
                            }
//                             System.out.println("value : "+Integer.parseInt((String)dt.getValueAt(row, 8)));
                            if (Integer.parseInt((String)dt.getValueAt(row, 3)) == 1) {
                                out.setForeground(Color.red);
                                
                                
                            } 
                            else {
                                
                                out.setForeground(new Color(0x88, 0x88, 0x88, 0xff));
                            }
                            if (isSelected) {
                                    out.setBackground(new Color(0x88, 0x88, 0x88, 0x88));
                                    out.setOpaque(true);
                                }
                           
                            return out;
                        }
                    });
                    
                   
                    
                 
                
                TableColumn column = null;
                for (int k = 0;k < jTable1.getColumnCount(); k++) {
                    column = jTable1.getColumnModel().getColumn(k);

                    if (k == 0) {
                        column.setWidth(0);
                        column.setMinWidth(0);
                        column.setMaxWidth(0);
                    }
                     if (k == 3) {
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
   
    private void showAgentOnline()
    {
        try{
             con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

           
             if(con.isConnect())
            {
                ResultSet result=null;
           
            String sql="SELECT * FROM `agent_status` WHERE  `interface`<> '0' AND `queue`<>'0'";
           
//                String sql="SELECT * FROM `agent_status` AS s LEFT JOIN `agent_login` AS l "
//                        + "ON s.`agent_id`=l.`agent_id` "
//                        + "WHERE  `interface`<> '0' AND `queue`<>'0' AND role='2'";
//                
               result = con.executeQuery(sql);
               jTable1.getTableHeader().setReorderingAllowed(false);

                String strHeader[]={"","",""};
                final DefaultTableModel  dt=new DefaultTableModel(strHeader,0)
                {

                    @Override
                    public boolean isCellEditable(int i, int i1) {
                        return false;
                    }

                };
                int i=0;
                while (result.next()) { 
                    i++;
                    String agent=result.getString("agent_id");
                  
                    String showOnline=" now online";
                  
                    Vector rowdata = new Vector();
                    rowdata.add(Integer.toString(i));
                    rowdata.add(agent);
                    rowdata.add(showOnline);    
                    dt.addRow(rowdata);
                    
                }
                jTable1.setModel(dt);
                 jTable1.setDefaultRenderer(Object.class, new TableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, 
                                                                        Object value, 
                                                                        boolean isSelected, 
                                                                        boolean hasFocus, 
                                                                        int row, int column) {
                         
                            
                            JLabel out=new JLabel();
                            
                            if (column == 0) {
                                out.setText(Integer.toString(row+1));
                            } else {
                                out.setText((String)value);
                            }
//                         
                           
                                out.setForeground(Color.red);
                                if (isSelected) {
                                    out.setBackground(new Color(0xff, 0xff, 0x88, 0xff));
                                    out.setOpaque(true);
                                }
                                
                          
                            
                            return out;
                        }
                    });
                     
               TableColumn column = null;
                for (int k = 0;k < jTable1.getColumnCount(); k++) {
                    column = jTable1.getColumnModel().getColumn(k);

                    if (k == 0) {
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
                
                
//                 if(this==null || !this.isVisible())
//                {
                
//                    int row=jTable1.getSelectedRow();
//                    final String col1=""+jTable1.getValueAt(row,1); 
//                    for(int i=0;i<jTabbedPane1.getTabCount();i++)
//                    {
//                        if(jTabbedPane1.getTitleAt(i).equals(col1))
//                        {
//                            jTabbedPane1.setSelectedIndex(i);
//                            return;
//                        }
//
//                    }
////                }
               

                final String agent=Agent;

 
//                JOptionPane.showMessageDialog(null, "6");
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

               
                jTabbedPane1.addTab(agent,new TabCloseIcon(), tab);
                int display= jTabbedPane1.getTabCount()-1;
                jTabbedPane1.setSelectedIndex(display);

                mapAgent.put(agent, tab);
                String s="";
                for(int i=0;i< jTabbedPane1.getTabCount();i++)
                {
                    s+=(jTabbedPane1.getTitleAt(i).toString()+",");
                }
                this.setTitle(s.substring(0,s.length()-1));
                this.setVisible(true);
                this.setSize(jTabbedPane1.getX()+jTabbedPane1.getWidth()+10,this.getHeight());
                tab.showMessage(agent, message);
            }
            con.closeConnect();
        }
        catch(Exception e)
        {

        }
   }
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MessageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MessageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MessageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MessageForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MessageForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

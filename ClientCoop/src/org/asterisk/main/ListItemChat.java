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
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import nttnetworks.com.controls.IPanelTabEvent;

import nttnetworks.com.controls.TabCloseIcon;
import nttnetworks.com.controls.panelTab;
import org.asterisk.utility.Agent;
import org.asterisk.utility.ConnectDatabase;
import org.asterisk.utility.Utility;

/**
 *
 * @author PHUONGTRANG
 */
public class ListItemChat extends javax.swing.JFrame {
    
    private String Agent_loged = "unknown";
    private HashMap <String,panelTab> mapAgent=new HashMap<>();
    private  String filename = "infor.properties";
    private  String Mysql_server = "172.168.10.202";      
    private  String Mysql_dbname = "ast_callcenter";
    private  String Mysql_user = "callcenter";
    private  String Mysql_pwd  = "callcenter"; 
    private static Utility uti;
    private ConnectDatabase con;
    private Agent agentClient;
    private JTabbedPane jTabbedPane1;
    public static MainForm mainform = null ;
//    public static ShowChat showchat;
    public JFrame showchat;
   
    public void receive(String from, String message) {
       panelTab tabs = mapAgent.get(from);
       
       System.out.println(tabs);
       
       if (tabs != null) {
            tabs.showMessage(from, message);
       }
   }
   
   public void popup(String Agent, String message)
   {
        try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

            if(con.isConnect())
            {
                
                
                 if(showchat==null || !showchat.isVisible())
                {
                    jTabbedPane1=new JTabbedPane();
                    showchat=new JFrame();
                    jTabbedPane1.setTabPlacement(3);
                    showchat.setVisible(true);
                    showchat.setSize(300,300);
                    showchat.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    showchat.setContentPane(jTabbedPane1);
                }
               

                final String col1=Agent;

 
//                JOptionPane.showMessageDialog(null, "6");
                final panelTab tab=new panelTab();
                tab.events = new IPanelTabEvent() {
                    @Override
                    public void send() {
                        try {
                            if(!"".equals(tab.getText())){
                                //send
                                agentClient.sendtoServer("120@"+Agent_loged+"@"+col1+"@"+tab.getText().replace("@", "&#64;"));
                                tab.showMessage(Agent_loged, tab.getText());
                                tab.send();
                            }
                        } catch (Exception ex) {}
                    }
                };

               
                jTabbedPane1.addTab(col1,new TabCloseIcon(), tab);
                int display= jTabbedPane1.getTabCount()-1;
                jTabbedPane1.setSelectedIndex(display);

                mapAgent.put(col1, tab);
                String s="";
                for(int i=0;i< jTabbedPane1.getTabCount();i++)
                {
                    s+=(jTabbedPane1.getTitleAt(i).toString()+",");
                }
                showchat.setTitle(s.substring(0,s.length()-1));
                tab.showMessage(col1, message);
            }
            con.closeConnect();
        }
        catch(Exception e)
        {

        }
   }
    /**
     * Creates new form ListItemChat
     */
    public ListItemChat() {
      try {
            initComponents();
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
           
            uti = new Utility();
            Mysql_dbname = uti.readInfor(filename, "MySql_database");
            Mysql_server = uti.readInfor(filename, "MySql_server");
            Mysql_user = uti.readInfor(filename, "MySql_user");
            Mysql_pwd = uti.readInfor(filename, "MySql_pwd");
            
            showAgent();
        } catch (Exception ex) {
            Logger.getLogger(MessageForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ListItemChat(String Agent_loged, Agent agentClient) {
          try {
              initComponents();
               setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
              uti = new Utility();
              Mysql_dbname = uti.readInfor(filename, "MySql_database");
              Mysql_server = uti.readInfor(filename, "MySql_server");
              Mysql_user = uti.readInfor(filename, "MySql_user");
              Mysql_pwd = uti.readInfor(filename, "MySql_pwd");

              showAgent();
              this.Agent_loged = Agent_loged;
              this.agentClient = agentClient;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblListItemChat = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblListItemChat.setModel(new javax.swing.table.DefaultTableModel(
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
        tblListItemChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListItemChatMouseClicked(evt);
            }
        });
        tblListItemChat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblListItemChatKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblListItemChat);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblListItemChatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListItemChatMouseClicked
       int i=tblListItemChat.getSelectedRow();
        if(i>=0)
        {
            createPopupMenu();
            if(evt.getClickCount()==2)
            {
                show_chat(); 
            }
            
        }
    }//GEN-LAST:event_tblListItemChatMouseClicked

    private void tblListItemChatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblListItemChatKeyReleased
        if(evt.getKeyCode() == 10){
              show_chat(); 
            }   
    }//GEN-LAST:event_tblListItemChatKeyReleased
 
   private void show_chat()
    {
          try {
            con = new ConnectDatabase(Mysql_dbname, Mysql_user, Mysql_pwd, Mysql_server);

            if(con.isConnect())
            {
                
                
                 if(showchat==null || !showchat.isVisible())
                {
                    jTabbedPane1=new JTabbedPane();
                    showchat=new JFrame();
                    jTabbedPane1.setTabPlacement(3);
                    showchat.setVisible(true);
                    showchat.setSize(300,300);
                    showchat.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    showchat.setContentPane(jTabbedPane1);
                }
               
                int row=tblListItemChat.getSelectedRow();
                final String col1=""+tblListItemChat.getValueAt(row,1);
                for(int i=0;i< jTabbedPane1.getTabCount();i++)
                {
                    if(jTabbedPane1.getTitleAt(i).equals(col1))
                    {
                        jTabbedPane1.setSelectedIndex(i);
                        return;
                    }

                }
 
//                JOptionPane.showMessageDialog(null, "6");
                final panelTab tab=new panelTab();
                tab.events = new IPanelTabEvent() {
                    @Override
                    public void send() {
                        try {
                            if(!"".equals(tab.getText())){
                                //send
                                agentClient.sendtoServer("120@"+Agent_loged+"@"+col1+"@"+tab.getText().replace("@", "&#64;"));
                                tab.showMessage(Agent_loged, tab.getText());
                                tab.send();
                            }
                        } catch (Exception ex) {}
                    }
                };

               
                jTabbedPane1.addTab(col1,new TabCloseIcon(), tab);
                int display= jTabbedPane1.getTabCount()-1;
                jTabbedPane1.setSelectedIndex(display);

                mapAgent.put(col1, tab);
                String s="";
                for(int i=0;i< jTabbedPane1.getTabCount();i++)
                {
                    s+=(jTabbedPane1.getTitleAt(i).toString()+",");
                }
                showchat.setTitle(s.substring(0,s.length()-1));
            }
            con.closeConnect();
        }
        catch(Exception e)
        {

        }
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
        menuItem = new JMenuItem("Item ++ ");
        //menuItem.addActionListener(this);
        popup.add(menuItem);
 
        //Add listener to the text area so the popup menu can come up.
        MouseListener popupListener = new ListItemChat.PopupListener(popup);
        tblListItemChat.addMouseListener(popupListener);
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
                tblListItemChat.getTableHeader().setReorderingAllowed(false);
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
                tblListItemChat.setModel(dt);
                
                tblListItemChat.setDefaultRenderer(Object.class, new TableCellRenderer() {
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
                for (int k = 0;k < tblListItemChat.getColumnCount(); k++) {
                    column = tblListItemChat.getColumnModel().getColumn(k);

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ListItemChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListItemChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListItemChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListItemChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListItemChat().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblListItemChat;
    // End of variables declaration//GEN-END:variables
}

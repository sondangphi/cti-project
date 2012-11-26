
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
public class MainForm extends javax.swing.JFrame  implements ListSelectionListener{

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
                String[] columnNames = {"First Name","Last Name"};
                
//                Object[][] data = {
//                            {"Kathy", "Smith","Snowboarding", new Integer(5), new Boolean(false)},
//                            {"John", "Doe","Rowing", new Integer(3), new Boolean(true)},
//                            {"Sue", "Black","Knitting", new Integer(2), new Boolean(false)},
//                            {"Jane", "White","Speed reading", new Integer(20), new Boolean(true)},
//                            {"Joe", "Brown","Pool", new Integer(10), new Boolean(false)}
//                            };
                ArrayList<String[]> data = new ArrayList<String[]>();      
                String []temp1 = {"123","abc"}; 
//                jTable1.setModel(new CustomTableModel(columnNames,data));                
//                jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                
                
            int count = columnNames.length;          
            Vector col = new Vector(count);
            for (int i = 0; i <count; i++) 
                col.addElement(columnNames[i].toString());             
            Vector row = new  Vector();
            Vector dataRow = new Vector(count);
            dataRow.addElement("123");
            dataRow.addElement("abc");
            row.addElement(dataRow);
            jTable1.setModel(new tbModel(col, row)); 

                
            date1.setSize(20, 40);
            JScrollPane scrollPane = new JScrollPane(jTable1);
            jTable1.setFillsViewportHeight(true); 
            jTable2.setCellSelectionEnabled(true);
//            jTable2.set

//        ListSelectionModel cellSelectionModel = jTable2.getSelectionModel();
//        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);            
//        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                int rowcell = jTable2.getSelectedRow();
//                int colcell = jTable2.getSelectedColumn();
//                System.out.println("value: " + jTable2.getValueAt(rowcell, colcell));
//            }
//        });    
        
        jTable2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int rowcell = jTable2.getSelectedRow();
                int colcell = jTable2.getSelectedColumn();
//                System.out.println("value is: " + jTable2.getValueAt(rowcell, colcell)+" ["+rowcell+"]["+colcell+"]");
                String name = jTable2.getColumnName(colcell);              
                if(name.equalsIgnoreCase("First Name"))
                    System.out.println("col name is: "+name);
                if(colcell==0){
                    System.out.println("value is: " + jTable2.getValueAt(rowcell, colcell)+" ["+rowcell+"]["+colcell+"]");
                }
            }
        });

                        
                        
                
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
        date1 = new com.toedter.calendar.JDateChooser();
        date2 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        lb_1 = new javax.swing.JTextField();
        lb_2 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
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

        date1.setDateFormatString("yyyy-M-dd");
        date1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                date1MouseClicked(evt);
            }
        });
        date1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                date1KeyPressed(evt);
            }
        });

        date2.setDateFormatString("d MMM, yyyy");
        date2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                date2MouseClicked(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lb_1.setText("2012-11-08");

        lb_2.setText("2012-11-02");

        jScrollPane2.setViewportView(jTable2);

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("export");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tx_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(tx_datetime)
                                .addGap(29, 29, 29)
                                .addComponent(tx_test, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel5)
                                .addGap(33, 33, 33)
                                .addComponent(date2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_2)
                            .addComponent(lb_1, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(bt_view)
                                .addContainerGap())
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton5)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel5, tx_test});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tx_agent, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tx_datetime)
                            .addComponent(tx_test)
                            .addComponent(bt_view)
                            .addComponent(lb_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(date2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {date1, date2, jLabel5, tx_agent, tx_datetime, tx_test});

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
        try{
            ConnectDatabase con = new  ConnectDatabase();
            String sql = "SELECT * FROM login_action where agent_id ='a'";
            String sql2 = "SELECT agent_id,SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) FROM login_action  group by agent_id";
            String sql3 = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) FROM login_action  where datetime_login LIKE  '%2012-11-01%' AND agent_id ='a'";
            ResultSet rs = con.executeQuery(sql);
            String items[] = {"Java", "JSP", "PHP", "C", "C++","Visual","C#","J#"};
            String colname[] = {"Work Time", "Pause Time", "Hold Time", "Talk Time","Call Receive","RingNoanswer","J#","Agent"};
            ResultSetMetaData rsMeta = rs.getMetaData();
            int count = rsMeta.getColumnCount();            
            Vector col = new Vector(count);
            for (int i = 0; i <colname.length; i++) {
                col.addElement(colname[i].toString());
                System.out.println("\t"+colname[i].toString());
            }
            Vector row = new  Vector();
            while (rs.next()) {
                Vector dataRow = new Vector(count);
                for (int i = 1; i <= count; i++) {
                    dataRow.addElement(rs.getObject(i));
                }
                row.addElement(dataRow);
            }
            jTable1.setModel(new tbModel(col, row));
//            jTable1.setModel(new rsTableModel(rs));
            
            
        }catch(Exception e){
            System.out.println(e);
        }
        
        
    }//GEN-LAST:event_bt_viewActionPerformed

    private void date1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_date1KeyPressed
        // TODO add your handling code here:
//        System.out.println("start date");
    }//GEN-LAST:event_date1KeyPressed

    private void date2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_date2MouseClicked
        // TODO add your handling code here:
        System.out.println("end date");
        
    }//GEN-LAST:event_date2MouseClicked

    private void date1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_date1MouseClicked
        // TODO add your handling code here:
        System.out.println("start date");
    }//GEN-LAST:event_date1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try{
            ResultSet rs = null;
            caculatortime ca = new caculatortime();
            ConnectDatabase con = new  ConnectDatabase();            
            String start = lb_1.getText();
            String end   = lb_2.getText();
            String worktime = "";
            String logintime = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) "
                    + "FROM login_action  where datetime_login LIKE  '%"+start+"%' AND agent_id ='a'";
            String logintime2 = "SELECT agent_id,SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) "
                    + "FROM login_action  where datetime_login between '"+start+"' and '"+end+"' AND agent_id = 'a'";
            String talktime = "SELECT note1 FROM dial_event WHERE agent_id = 'a' AND event='5' OR event ='6' "
                    + "AND datetime LIKE  '%"+start+"%'";
            String pausetime = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_unpause,datetime_pause)))) "
                    + "FROM pause_action  where datetime_pause LIKE  '%"+start+"%' and agent_id='a'";
            String pausetime2 = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_unpause,datetime_pause)))) "
                    + "FROM pause_action  where datetime_pause between '"+start+"' and '"+end+"' and agent_id='a'";
            String callRec = "SELECT COUNT(*) FROM dial_event WHERE agent_id = 'a' AND event='4' AND datetime LIKE  '%"+start+"%'";
            String colname[] = {"Login Time", "Pause Time","Work Time","Talk Time","Call Receive"};
            int count = colname.length;
            Vector col = new Vector(count);
            Vector row = new  Vector();
            Vector dataRow = new Vector(count);
            rs = con.executeQuery(logintime);
            if(rs.next())
                logintime = String.valueOf(rs.getObject(1));
            dataRow.addElement(logintime);
            rs = con.executeQuery(pausetime);
            if(rs.next())
                pausetime = String.valueOf(rs.getObject(1));
            if(pausetime==null)
                pausetime = "00:00:00";
            dataRow.addElement(pausetime);
            worktime = ca.getTime(ca.divtime(logintime, pausetime));     
            dataRow.addElement(worktime);
            rs = con.executeQuery(talktime);
            long secs = 0;
//            System.out.println("size\t"+rs.());
            while(rs.next()){
                String temp = String.valueOf(rs.getObject(1));
                secs += Long.parseLong(temp);                
            }                
            dataRow.addElement(ca.getTime(secs));
            rs = con.executeQuery(callRec);
            if(rs.next())
                dataRow.addElement(String.valueOf(rs.getObject(1)));
            for (int i = 0; i <colname.length; i++) {
                col.addElement(colname[i].toString());
                System.out.println("\t"+colname[i].toString());
            }                        
            row.addElement(dataRow);
            jTable1.setModel(new tbModel(col, row) );   
//            jTable1.
        }catch(Exception e){
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try{
            String colname[] = {"Date","Login Time", "Pause Time","Work Time","Talk Time","Call Receive"};
            ResultSet rs = null;
            caculatortime ca = new caculatortime();
            ConnectDatabase con = new  ConnectDatabase();
            int count = colname.length;
            Vector col = new Vector(count);
            Vector row = new  Vector();
            String sql = "SELECT CAST( datetime_login AS DATE ),SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_logout,datetime_login)))) "
                    + "FROM login_action  where  agent_id ='a' and datetime_login <=  '2012-11-08 23:00:00' "
                    + "AND datetime_login >=  '2012-11-01 00:00:00' group by CAST(datetime_login AS DATE);";
            ArrayList<String[]> data = new ArrayList<String[]>();
            rs = con.executeQuery(sql);
            while(rs.next()){
                String [] temp  = new String[colname.length];
                int j= 0;
                temp[j++] = String.valueOf(rs.getObject(1));
                temp[j++] = String.valueOf(rs.getObject(2));
                data.add(temp);
            }
            for(int i=0;i<data.size();i++){
                //work time
                sql  = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(timediff(datetime_unpause,datetime_pause)))) "
                        + "FROM pause_action  where  agent_id ='a' and datetime_pause LIKE '%"+data.get(i)[0]+"%'"
                        + "group by CAST(datetime_pause AS DATE);";
                rs = con.executeQuery(sql);
                if(rs.next())
                    data.get(i)[2] = String.valueOf(rs.getObject(1));
                else
                    data.get(i)[2] = "00:00:00";  
                sql = "select SEC_TO_TIME("+ca.divtime(data.get(i)[1], data.get(i)[2])+")";
                rs = con.executeQuery(sql);
//                data.get(i)[3] = ca.getTime(ca.divtime(data.get(i)[1], data.get(i)[2]));   
                if(rs.next())
                    data.get(i)[3] = String.valueOf(rs.getString(1));
                //call receive
                sql = "SELECT COUNT(*) FROM dial_event WHERE agent_id = 'a' AND event='4' AND datetime LIKE  '%"+data.get(i)[0]+"%'";  
                rs = con.executeQuery(sql);
                if(rs.next())
                    data.get(i)[5] = String.valueOf(rs.getObject(1));
                else
                    data.get(i)[5] = "";      
                //talk time
                sql = "SELECT SEC_TO_TIME(SUM(note1))FROM dial_event WHERE agent_id = 'a' AND datetime LIKE '%"+data.get(i)[0]+"%' "
                        + "AND (event ='6' OR event ='5')";
                rs = con.executeQuery(sql);
                if(rs.next()){
                    String time = String.valueOf(rs.getObject(1));
                    if(time!="null")
                        data.get(i)[4] = time;
                    else 
                        data.get(i)[4] = "00:00:00"; 
                }else
                    data.get(i)[4] = "00:00:00"; 
            }      
            //add data into table
            for(int i = 0;i<data.size();i++){                
                Vector dataRow = new Vector(count);
                String []temp = new String[colname.length];
                for(int j=0;j<data.get(i).length;j++){
                    temp = data.get(i);
                    if(temp[j]!=null)
                        dataRow.addElement(temp[j]);
                    else
                        dataRow.addElement("");
                }
                row.addElement(dataRow);
            }
            for (int i = 0; i <colname.length; i++) 
                col.addElement(colname[i].toString());
            jTable2.setModel(new tbModel(col, row));                 
        }catch(Exception e){
            System.out.println(e);
        }   
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //////////////////////////////////////////////////////////////////////////
        String columnNames[] = {"First Name","Last Name"};    
        int count = columnNames.length;          
        Vector col = new Vector(count);
        Vector row = new  Vector();
        for (int i = 0; i <count; i++) 
            col.addElement(columnNames[i].toString());             
        for(int j= 0 ;j<5;j++){
            Vector dataRow = new Vector(count);
            dataRow.addElement("123");
            dataRow.addElement("abc");
            row.addElement(dataRow);
        }
        jTable2.setModel(new tbModel(col, row));        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int row = jTable2.getSelectedRow();
        int col = jTable2.getSelectedColumn();
        System.out.println(row+"\t"+col);
        jTable2.getModel().getValueAt(row, col);
        String t = String.valueOf("value is: "+jTable2.getValueAt(row, col));
//        String t2 = String.valueOf(tbModel.getValueAt(row, col));
        System.out.println(t);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        try{
//            CsvReader csv = new CsvReader("report.csv");
//            csv.readHeaders();
            String outputFile = "report.csv";		
            
//            boolean alreadyExists = new File(outputFile).exists();
            CsvWriter csvOutput = new CsvWriter(new FileWriter(outputFile, true), ',');
//            if (!alreadyExists){
                for(int i=0;i<jTable2.getColumnCount();i++)                    
                    csvOutput.write(jTable2.getColumnName(i));                    
                csvOutput.endRecord();
                for(int i=0;i<jTable2.getRowCount();i++) {
                    for(int j=0;j<jTable2.getColumnCount();j++)
                        csvOutput.write(String.valueOf(jTable2.getValueAt(i,j)));
                    csvOutput.endRecord();
                }       
                csvOutput.close();
//            }
        }catch(Exception e){
        
        }
        
        
    }//GEN-LAST:event_jButton5ActionPerformed
    
    
    public void addtraysys() throws AWTException{
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
        stray.add(trayIcon); 
        setVisible(false);
    }
    
    public void exit_system(){
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
    private com.toedter.calendar.JDateChooser date1;
    private com.toedter.calendar.JDateChooser date2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private static javax.swing.JTable jTable1;
    private static javax.swing.JTable jTable2;
    private javax.swing.JTextField lb_1;
    private javax.swing.JTextField lb_2;
    private javax.swing.JLabel tx_agent;
    private javax.swing.JLabel tx_datetime;
    private javax.swing.JLabel tx_test;
    // End of variables declaration//GEN-END:variables

    @Override
    public void valueChanged(ListSelectionEvent e) {
//        if(e.getValueIsAdjusting()) // mouse button not released yet
//            return;
        int row = jTable2.getSelectedRow();
//        if(row < 0)              // true when clearSelection
//        return;
        int col = jTable2.getSelectedColumn();   
//        if(col < 0)              // true when clearSelection
//        return;
        String t = String.valueOf(jTable2.getValueAt(row, col));
        System.out.println(t);
        jTable2.clearSelection();
//        jTable2.rowAtPoint(null);
    }
}
//class SelectionListener implements ListSelectionListener {
//    @Override
//    public void valueChanged(ListSelectionEvent e) {
//        try{
//            if(e.getValueIsAdjusting()) // mouse button not released yet
//                return;
//            int row = table.getSelectedRow();
//            if(row < 0)              // true when clearSelection
//            return;
//            int col = table.getSelectedColumn();   
//            if(col < 0)              // true when clearSelection
//            return;
//
//            table.clearSelection();            
//        }catch(Exception e){
//        }
//        
//    }
//}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nttnetworks.com.controls;

import java.awt.FontMetrics;
import javax.swing.JScrollPane;

/**
 *
 * @author PHUONGTRANG
 */
public class panelTab1 extends javax.swing.JPanel {
    private final Object syn_fix = new Object();
   String HTML="<html><body>%s</body></html>";
   String content="";
   String FName;
    private final String[] colors = new String[] {
        "#ff0000",
        "#0000ff"
    };
    public IPanelTabEvent events;
    public String getText() {
        return jTextField1.getText();
    }
    public void send() {
        jTextField1.setText("");
    }

    private void showMessage()
    {
//       jTextArea1.setText(String.format(HTML, content));
//       jTextArea2.setText(String.format(HTML, content));
       
            jEditorPane1.setContentType("text/html");

            jEditorPane1.setText(String.format(HTML, content));
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     while (panelTab1.this.isVisible()) {
                         synchronized (syn_fix) {
                             panelTab1.this.setSize(panelTab1.this.getWidth() - 1, panelTab1.this.getHeight());
                             try {
                                 Thread.sleep(200);
                             } catch (InterruptedException ex) { }
                             panelTab1.this.setSize(panelTab1.this.getWidth() + 1, panelTab1.this.getHeight());
                         }
                     }
                 }
             }).start();


             fixHeight();
      
    }
    private void fixHeight() {
        int lines = content.length() - content.replace("<br />", "lass='br />").length();
        int height = lines * jEditorPane1.getFontMetrics(jEditorPane1.getFont()).getHeight();
        
        jEditorPane1.setSize(jEditorPane1.getWidth(), height);
    }
    public synchronized void showMessage(String name, String message) {

        String color;
        if (FName == null) {
            FName = name;
            color = colors[0];
        } else {
            color = colors[1];
            if (FName.equals(name)) {
                color = colors[0];
            }
        }
        if(!"".equals(message)){
             content += "<span class='row'><span style='color: "+ color +"; font-weight: bold; float: left'>"+ 
                    name +":</span><span style='float: left'> "+ wrap(name, message) +"</span></span><br />";
            showMessage();
              System.out.println(Integer.toString(message.length()));
         }
        
        jEditorPane1.setSelectionStart(jEditorPane1.getText().length());
        jEditorPane1.setSelectionEnd(jEditorPane1.getText().length());
    }
    
    /**
     * Creates new form penelTab
     */
    public panelTab1() {
        initComponents();
        jScrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        

    }
  private String wrap(String agent, String message) {
        String mess = agent + ": " + message;
        FontMetrics met = jEditorPane1.getFontMetrics(jEditorPane1.getFont());
        
        StringBuilder out = new StringBuilder();
        int first_line = agent.length() + 2;
        int preI = agent.length() + 2;
        int w = 0;
        int pre_w = 0;
        
        for (int i=agent.length() + 2; i<mess.length(); i++) {
            char c = mess.charAt(i);
            
            if (c == ' ') {
                preI = i;
            } else {
                w = met.stringWidth(mess.substring(0, i)) - pre_w;
                if (w >= jEditorPane1.getWidth()-10) {
                    if (preI > first_line) {
                        int end_pos = preI - agent.length() - 2;
                        int start_pos = first_line - agent.length() - 2;
                        out.append(message.substring(start_pos, end_pos - start_pos))
                                .append("<br />");
                        first_line = preI = preI + 1;
                        pre_w += w;
                    } else if (preI == first_line) {
                        preI = i;
                        int end_pos = preI - agent.length() - 2;
                        int start_pos = first_line - agent.length() - 2;
                        out.append(message.substring(start_pos, end_pos))
                                .append("<br />");
                        first_line = preI = preI + 1;
                        pre_w += w;
                    }
                }
            }
        }
        
        int end_pos = mess.length() - agent.length() - 2;
        int start_pos = first_line - agent.length() - 2;
        out.append(message.substring(start_pos, end_pos));
        return out.toString();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        jTextField1.setText("jTextField1");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jEditorPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        events.send();
//         System.out.println(Integer.toString(jEditorPane1.getWidth()));
//          System.out.println(Integer.toString(jEditorPane1.getHeight()));
         
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
       if(evt.getKeyCode()==10)
       {
             events.send();
       }
    }//GEN-LAST:event_jTextField1KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

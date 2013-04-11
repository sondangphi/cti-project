
package nttnetworks.com.controls.questions;

import com.sun.java.swing.plaf.windows.WindowsBorders;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class answer extends javax.swing.JPanel {
    private String[] answ;
    private int type;
    private int num;
    private ButtonGroup group = new ButtonGroup();
    private ArrayList<JComponent> items = new ArrayList<>();
    
    public String[] getAnswerResult() {
        if (answ == null) {
            return null;
        }
        
        if (type == 1) {    //selection
            if (num == 1) {     //radio | combo
                if (answ.length > 4) {                          //combo
                    JComboBox com = (JComboBox)items.get(0);
                    return new String[] { (String)com.getItemAt(com.getSelectedIndex()) };
                } 
                
                //radio
                for (int i=0; i<items.size(); i++) {
                    JRadioButton com = (JRadioButton)items.get(i);
                    if (com.isSelected()) {
                        return new String[] { com.getText() };
                    }
                }
                
                return new String[] {};
            } 
            
            //checkbox
            ArrayList<String> output = new ArrayList<>();
            for (int i=0; i<items.size(); i++) {
                JCheckBox com = (JCheckBox)items.get(i);
                if (com.isSelected()) {
                    output.add(com.getText());
                    if (output.size() >= num) {
                        break;
                    }
                }
            }
            
            String[] rs = new String[output.size()];
            for(int i=0; i<rs.length; i++) {
                rs[i] = output.get(i);
            }
            return rs;
        } 
        
        //textbox
        JTextArea com = (JTextArea)items.get(0);
        return new String[] { com.getText() };
    }
    
    public int getTypeAnswer() {
        return type;
    }
    
    public String[] getAnswers() {
        if (type==1) {
            return answ;
        } 
        return null;
    }
    
    public int getNumberOfVaildAnswer() {
        if (type==1) {
            return num;
        } 
        return 1;
    }
    
    public void fixHeight() {
        if (answ == null) {
            return;
        }

        if (type == 1) {    //selection
            if (num == 1) {     //radio | combo
                if (answ.length > 4) {                          //combo
                    items.get(0).setSize(answer.this.getWidth()-10, 23);
                    answer.this.setSize(answer.this.getWidth(), 33);
                } else {                                        //radio
                    answer.this.setSize(answer.this.getWidth(), 10 + 23*answ.length);
                    for (int i=0; i<items.size(); i++) {
                        items.get(i).setSize(answer.this.getWidth()-10, 23);
                    }
                }
            } else {                                            //checkbox
                answer.this.setSize(answer.this.getWidth(), 10 + 23*answ.length);
                for (int i=0; i<items.size(); i++) {
                    items.get(i).setSize(answer.this.getWidth()-10, 23);
                }
            }
        } else {        //textbox
            items.get(0).setSize(answer.this.getWidth()-10, 50);
            answer.this.setSize(answer.this.getWidth(), 60);
        }
    }
    
    public answer() {
        initComponents();
    }
    
    public void clear() {
        for (int i=0; i<items.size(); i++) {
            this.remove(items.get(i));
        }
        
        items.clear();
    }
    
    public void showAnswer(String[] answ, int type, int num) {
        this.answ = answ;
        this.type = type;
        this.num = num;
        
        clear();
        if (type == 1) {    //selection
            if (num == 1) {     //radio | combo
                if (answ.length > 4) {                          //combo
                    JComboBox com = new JComboBox();
                    com.setModel(new javax.swing.DefaultComboBoxModel(answ));
                    com.setBackground(Color.white);
                    com.setLocation(5, 5);
                    com.setSize(this.getWidth() - 10, 23);
                    com.setVisible(true);
                    this.add(com);
                    items.add(com);
                } else {                                        //radio
                    for (int i=0; i<answ.length; i++) {
                        JRadioButton com = new JRadioButton();
                        com.setBackground(Color.white);
                        com.setText(answ[i]);
                        com.setLocation(5, 5 + 23*i);
                        com.setSize(this.getWidth()-10, 23);
                        com.setVisible(true);
                        
                        group.add(com);
                        
                        this.add(com);
                        items.add(com);
                    }
                }
            } else {                                            //checkbox
                for (int i=0; i<answ.length; i++) {
                    final JCheckBox com = new JCheckBox();
                    
                    com.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent ce) {
                            int count = 0;
                            if (com.isSelected()) {
                                for (int i=0; i<items.size(); i++) {
                                    JCheckBox chk = (JCheckBox)items.get(i);
                                    if (chk.isSelected() && chk!=com) {
                                        count+=1;
                                        if (count >= answer.this.num) {
                                            break;
                                        }
                                    }
                                }
                                
                                if (count >= answer.this.num) {
                                    com.setSelected(false);
                                }
                            }
                        }
                    });
                    com.setBackground(Color.white);
                    com.setText(answ[i]);
                    com.setLocation(5, 5 + 23*i);
                    com.setSize(this.getWidth()-10, 23);
                    com.setVisible(true);
                    this.add(com);
                    items.add(com);
                }
            }
        } else {        //textbox
            JTextArea com = new JTextArea();
            com.setBackground(Color.white);
            com.setBorder(new LineBorder(new Color(0xe0, 0xe0, 0xe0)));
            com.setLocation(5, 5);
            com.setSize(this.getWidth() - 10, 50);
            com.setVisible(true);
            this.add(com);
            items.add(com);
        }
        
        fixHeight();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nttnetworks.com.controls.questions;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author minh
 */
public class questionAnsw extends javax.swing.JPanel {
    private autoHLabel text = new autoHLabel();
    private answer ans = new answer();
    
    public String getQuestion() {
        return text.getText();
    }
    
    public String[] getAnswerResult() {
        return ans.getAnswerResult();
    }
    
    public int getQuestionType() {
        return ans.getTypeAnswer();
    }
    
    public int getNumberOfValidAnswers() {
        return ans.getNumberOfVaildAnswer();
    }
    
    public questionAnsw() {
        initComponents();
        
        text.setLocation(5, 5);
        this.add(text);
        this.add(ans);
        this.addComponentListener(new ComponentAdapter() {
            @Override                                                                               
            public void componentResized(ComponentEvent ce) {                 
                fixHeight();
            }   
        });
    }
    
    public void fixHeight() {
        text.setSize(questionAnsw.this.getWidth()-10, questionAnsw.this.getHeight()-10);
        text.fixHeight();
        
        ans.setLocation(0, text.getHeight() + 10);
        ans.setSize(this.getWidth(), 0);
        ans.fixHeight();
        questionAnsw.this.setSize(questionAnsw.this.getWidth(), text.getHeight() + ans.getHeight() + 15);
    }
    
    public void showQuestion(String question, int type, String[] answers, int num) {
        text.setText(question);
        ans.showAnswer(answers, type, num);
        fixHeight();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
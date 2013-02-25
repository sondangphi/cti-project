package nttnetworks.com.controls.questions;


import java.awt.*;
import java.awt.event.*;                                                                      
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.*;

public class autoHLabel extends JPanel {                                          
    JTextArea textArea;                                                                         

    public autoHLabel() {  
        this.setLayout(null);
        this.setBorder(null);
        
        textArea = new JTextArea("");                                                       
        textArea.setEditable(false);                                                              
        textArea.setLineWrap(true);    
        textArea.setBorder(BorderFactory.createLineBorder(Color.white));
        textArea.setWrapStyleWord(true);                                                          

        textArea.setBackground(new Color(0xf8,0xf8,0xf8));
        textArea.setLocation(0, 0);
        textArea.setSize(this.getWidth(), this.getHeight());
        textArea.setFont(new Font(textArea.getFont().getName(), Font.BOLD, textArea.getFont().getSize() + 5));
        this.add(textArea);       
        fixHeight();
    }
    
    public void fixHeight() {
        int height = countLines(textArea) * textArea.getFontMetrics(textArea.getFont()).getHeight() + 5;
        textArea.setSize(autoHLabel.this.getWidth(), height);
        autoHLabel.this.setSize(autoHLabel.this.getWidth(), height);
    }
    
    public void setText(String text) {
        textArea.setText("Question: "+ text);
        fixHeight();
    }
    
    public String getText() {
        return textArea.getText();
    }

    private static int countLines(JTextArea textArea) {
        try {
            if (textArea.getText().length() == 0) {
                return 1;
            }

            AttributedString text = new AttributedString(textArea.getText());
            FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();

            AttributedCharacterIterator charIt = text.getIterator();
            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
            float formatWidth = (float) textArea.getSize().width;
            lineMeasurer.setPosition(charIt.getBeginIndex());

            int noLines = 0;
            while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
                lineMeasurer.nextLayout(formatWidth);
                noLines++;
            }

            return noLines;
        } catch(Exception e) { return 1; }
    }                                                                                           
}                                  
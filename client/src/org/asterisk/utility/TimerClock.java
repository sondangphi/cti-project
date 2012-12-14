/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;
import org.asterisk.main.MainForm2;

/**
 *
 * @author leehoa
 */
public class TimerClock implements ActionListener{
    private Timer clock;
    private int secs = 0;
    private int mins = 0;
    private int hrs = 0;
    private int tempsec = 0;
    private int tempmin = 0;
    private int temphr = 0;    
    private String time = "";
    private MainForm2 mainform = null;
    private boolean flag = true;
    private DecimalFormat dFormat = new DecimalFormat("00");

    public TimerClock() {
    }
    public TimerClock(MainForm2 m) {
        clock = new Timer(1000, this);
        mainform = m;
    }
    public TimerClock(MainForm2 m, boolean f) {
        clock = new Timer(1000, this);
        mainform = m;
        flag = f;
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
//        throw new UnsupportedOperationException("Not supported yet.");
        if (e.getSource() == clock)
            secs++;     
        if (secs == 60){
            mins++;
            secs = 0;
        } 
        if (mins == 60){
            hrs++;
            mins = 0;
            secs = 0;
        } 
        if (hrs == 24){
            hrs = 0;
            mins = 0;
            secs = 0;
        }
        time = dFormat.format(hrs) + ":" + dFormat.format(mins) + ":" + dFormat.format(secs);
        if(flag){
            mainform.lb_callduration.setText(time);
        }else
            mainform.lb_workTime.setText(time);
        
//        System.out.println(time);        
    }
    
    public void start(){
        clock.start();
    }
    public void stop(){
        clock.stop();
    }    
    
     public void pause(){
        temphr = hrs;
        tempmin = mins;
        tempsec = secs;
        clock.stop();
    }       
     public void resume(){
         if(hrs != 0 || mins != 0 || secs != 0){
            hrs = temphr;
            mins = tempmin;
            secs = tempsec;
            clock.start();
         }         
    }
}

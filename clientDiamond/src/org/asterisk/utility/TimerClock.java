/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;
import org.asterisk.main.MainForm;

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
    private MainForm mainform = null;
    private boolean flag = true;
    private DecimalFormat dFormat = new DecimalFormat("00");

    public TimerClock() {
    }
    public TimerClock(MainForm m) {
        clock = new Timer(1000, this);
        mainform = m;
    }
    public TimerClock(MainForm m, boolean f) {
        clock = new Timer(1000, this);
        mainform = m;
        flag = f;
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
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
    }
    
    public void start(){
        System.out.println("start clock");
        clock.start();
    }
    public void stop(){
        System.out.println("stop clock");
        clock.stop();
        secs = 0;
        mins = 0;
        hrs = 0;        
    }    
    
     public void pause(){
         System.out.println("pause clock");
        temphr = hrs;
        tempmin = mins;
        tempsec = secs;
        clock.stop();
    }       
     public void resume(){
         System.out.println("resume clock");
         if(hrs != 0 || mins != 0 || secs != 0){
            hrs = temphr;
            mins = tempmin;
            secs = tempsec;
            clock.start();
         }         
    }
}

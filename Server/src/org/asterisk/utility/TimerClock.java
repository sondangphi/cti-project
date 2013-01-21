/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.asterisk.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;
//import org.asterisk.main.MainForm2;

/**
 *
 * @author leehoa
 */
public class TimerClock implements ActionListener {

    private Timer clock;
    public int secs = 0;
    private int tempsec = 0;

    public TimerClock() {
        clock = new Timer(1000, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clock) {
            secs++;
        }
    }

    public void start() {
        secs = 0;
        clock.start();
    }

    public void stop() {
        clock.stop();
    }

    public void pause() {
        tempsec = secs;
        clock.stop();
    }

    public void resume() {
        if (secs != 0) {
            secs = tempsec;
            clock.start();
        }
    }
}

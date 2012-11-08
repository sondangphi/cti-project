/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leehoa
 */
//public class BasicTray {
    
//}
import javax.swing.*;
import java.awt.*;

public class BasicTray {
  public static void main(String args[]) {
    Runnable runner = new Runnable() {
      public void run() {
        if (SystemTray.isSupported()) {
          SystemTray tray = SystemTray.getSystemTray();
          Image image = Toolkit.getDefaultToolkit().getImage("gifIcon.gif");
          PopupMenu popup = new PopupMenu();
          MenuItem item = new MenuItem("A MenuItem");
          popup.add(item);
          TrayIcon trayIcon = new TrayIcon(image, "The Tip Text", popup);
          try {
            tray.add(trayIcon);
          } catch (AWTException e) {
            System.err.println("Can't add to tray");
          }
        } else {
          System.err.println("Tray unavailable");
        }
      }
    };
    EventQueue.invokeLater(runner);
  }
}
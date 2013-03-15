package nttnetworks.com.controls;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Herkules
 */
public class TabCloseIcon implements Icon
{
    private final Icon mIcon;
    private JTabbedPane mTabbedPane = null;
    private transient Rectangle mPosition = null;

    /**
     * Creates a new instance of TabCloseIcon.
     */
    public TabCloseIcon( Icon icon )
    {
        mIcon = icon;
    }


    /**
     * Creates a new instance of TabCloseIcon.
     */
    public TabCloseIcon()
    {
       
        this( new ImageIcon( TabCloseIcon.class.getResource("/org/asterisk/image/Close_tab_icon.png")) );
    }


    /**
     * when painting, remember last position painted.
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        if( null==mTabbedPane )
        {
            mTabbedPane = (JTabbedPane)c;
            mTabbedPane.addMouseListener( new MouseAdapter()
            {
                @Override
                public void mouseReleased( MouseEvent e )
                {
                // asking for isConsumed is *very* important, otherwise more than one tab might get closed!
                    if ( !e.isConsumed()  &&   mPosition.contains( e.getX(), e.getY() ) )
                    {
                        final int index = mTabbedPane.getSelectedIndex();
                        try{
                            mTabbedPane.remove( index );
                            e.consume();
//                            if(mTabbedPane.getTabCount()==0)
//                            {
//                                
//                                Component com = mTabbedPane;
//                                do{
//                                    com = com.getParent();
//                                }
//                                while(!(com instanceof JFrame));
//                                
//                                ((JFrame)com).dispose();
//                            }
                        }catch(Exception ex){}
                    }
                 }
             });
        }

        mPosition = new Rectangle( x,y, getIconWidth(), getIconHeight() );
        mIcon.paintIcon(c, g, x, y );
    }


    /**
     * just delegate
     */
    public int getIconWidth()
    {
        return mIcon.getIconWidth();
    }

    /**
     * just delegate
     */
    public int getIconHeight()
    {
        return mIcon.getIconHeight();
    }

}
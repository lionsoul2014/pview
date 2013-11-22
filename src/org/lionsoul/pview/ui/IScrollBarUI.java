package org.lionsoul.pview.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;        
import java.awt.GradientPaint;
import java.awt.Graphics;             
import java.awt.Graphics2D;
import java.awt.Rectangle;        
     
import javax.swing.JButton;     
import javax.swing.JComponent;      
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicArrowButton;     
import javax.swing.plaf.basic.BasicScrollBarUI;     

import org.lionsoul.pview.inc.PViewCfg;

    
/**
 * scroll bar UI
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IScrollBarUI extends BasicScrollBarUI {  
	
	public static Color S_COLOR = Color.DARK_GRAY;
    
    public Dimension getPreferredSize(JComponent c) {     
        return new Dimension(15, 15);     
    }     
    
    //repaint the slider of the scroll bar.
    public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {     
        super.paintThumb(g, c, thumbBounds); 
        Graphics2D g2 = ( Graphics2D ) g;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8F));
        //must call this
        g2.translate(thumbBounds.x, thumbBounds.y);     
        int tw = thumbBounds.width;
		int th = thumbBounds.height;

		GradientPaint gp_l = null;
		GradientPaint gp_r = null;
		if ( this.scrollbar.getOrientation() == JScrollBar.VERTICAL ) {
			gp_l = new GradientPaint(0, 0, S_COLOR, 
					tw / 2, 0, PViewCfg.MAIN_BG_COLOR);
			gp_r = new GradientPaint(tw / 2, 0, PViewCfg.MAIN_BG_COLOR,
					tw, 0, S_COLOR);
			g2.setPaint(gp_l);
			g2.fillRect(0, 0, tw / 2, th);
			g2.setPaint(gp_r);
			g2.fillRect(tw / 2, 0, tw, th);
		}
		if ( this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL ) {
			gp_l = new GradientPaint(0, 0, S_COLOR,
					0, th / 2, PViewCfg.MAIN_BG_COLOR);
			gp_r = new GradientPaint(0, th / 2, PViewCfg.MAIN_BG_COLOR,
					0, th, S_COLOR);
			g2.setPaint(gp_l);
			g2.fillRect(0, 0, tw, th / 2);
			g2.setPaint(gp_r);
			g2.fillRect(0, th / 2, tw, th);
		}
		
    }     
    
    /**slider background*/
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {     
    	g.setColor(PViewCfg.MAIN_BG_COLOR);
    	g.fillRect(0, 0, trackBounds.width + 20, trackBounds.height + 30);
    }       
    
    /**increase button*/
    protected JButton createIncreaseButton(int orientation) {     
        return new BasicArrowButton(orientation) {     
			private static final long serialVersionUID = 1L;
   
            public void paintTriangle(Graphics g, int x, int y, 
            		int size, int direction, boolean isEnabled) {
            	g.setColor(PViewCfg.MAIN_BG_COLOR);
            	g.fill3DRect(0, 0, 20, 20, true);
            }     
        };     
    }     
    
    /**decreatse button*/
    protected JButton createDecreaseButton(int orientation) {     
        return new BasicArrowButton(orientation) {     
			private static final long serialVersionUID = 1L;
			public void paintTriangle(Graphics g, int x, int y,
					int size, int direction, boolean isEnabled) {         
				g.setColor(PViewCfg.MAIN_BG_COLOR);
            	g.fill3DRect(0, 0, 20, 20, true);
            }     
        };     
    }     
}  
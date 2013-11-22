package org.lionsoul.pview;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

//import com.sun.jna.platform.WindowUtils;

/**
 * loading tip layer pane
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class JLoadDialog extends JDialog {

	private static final long serialVersionUID = 2914192200129777056L;
	
	public static final Dimension W_SIZE = new Dimension(150, 40);
	public static final Font W_FONT = new Font("Arial", Font.BOLD, 18);
	
	private static JLoadDialog __instance = null;
	
	private String str = "Loading...";
	
	public static JLoadDialog getInstance() {
		if ( __instance == null )
			__instance = new JLoadDialog();
		return __instance;
	}
	
	private JLoadDialog() {
		super(PView.getInstance());
		setUndecorated(true);
		setPreferredSize(W_SIZE);
		setSize(W_SIZE);
	    setLocationRelativeTo(null);
		setVisible(false);
		
		com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.6f); 
	    com.sun.awt.AWTUtilities.setWindowShape(this
	    		 , new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
	}
	
	@Override
	public void paint( Graphics g ) {
		Graphics2D g2 = ( Graphics2D ) g;
		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6F));
		g2.setColor(new Color(255, 0, 0));
		g2.fill3DRect(0, 0, this.getWidth(), this.getHeight(), true);
		g2.setColor(Color.WHITE);
		
		if ( str != null ) {
			g2.setFont(W_FONT);
			FontMetrics fm = this.getFontMetrics(W_FONT);
			g2.drawString(str,
					( this.getWidth() - fm.stringWidth(str) ) / 2,
					this.getHeight() / 2 + 5);
		} 
	}
	
	public void hiddenDialog() {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				setVisible(false);
			}
		});
	}
	
	public void showDialog() {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				setVisible(true);
			}
		});
	}

}

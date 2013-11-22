package org.lionsoul.pview.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicMenuItemUI;

import org.lionsoul.pview.inc.PViewCfg;

import sun.swing.SwingUtilities2;


/**
 * menu bar ui
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IJMenuItemUI extends BasicMenuItemUI {
	
	private boolean mouseOver = false;
	public static int acce_key_space = 10;
	private static Dimension size = new Dimension( 200, 26 );
	
	public IJMenuItemUI() {}

	/**
	 * install components.
	 * 		you could change the icon 
	 */
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setPreferredSize(size);
		c.setFont(PViewCfg.MENU_TOOL_FONT);
	}

	@Override
	protected MouseInputListener createMouseInputListener(JComponent c) {
		return new MouseInputHandler(){
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				mouseOver = true;
			}
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				mouseOver = false;
			}
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				mouseOver = true;
			}
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				mouseOver = true;
			}
		};
	}

	/**
	 * paint component 
	 */
	@Override
	public void paint(Graphics g, JComponent comp) {
		
		JMenuItem item = ( JMenuItem ) comp;
		FontMetrics fm = SwingUtilities2.getFontMetrics(item, g);
		String text = item.getText();
		KeyStroke k = item.getAccelerator();
		if ( k != null ) {
			String acce = k.toString().replace("pressed", "+");
			int s = item.getWidth() - fm.stringWidth(text)
					- fm.stringWidth(acce) - 2 * acce_key_space;
			int step = fm.stringWidth(" ");
			//append space
			for ( int j = s ;j >= 0; j -= step) {
				text += " ";
			}
			text += acce;
		}
		
		g.setColor(PViewCfg.MAIN_BG_COLOR);
		g.fillRect(0, 0, comp.getWidth(), comp.getHeight());
		
		if ( mouseOver ) {
			g.setColor(Color.DARK_GRAY);
			g.fill3DRect(0, 0, comp.getWidth(), comp.getHeight(), true);
		}
		
		if ( text != null && ! text.equals("") ) {
			g.setColor(PViewCfg.MAIN_FR_COLOR);
			g.drawString(text, acce_key_space, item.getHeight() / 2 + 4);
		} 
	}
}

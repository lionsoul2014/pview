package org.lionsoul.pview.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicMenuUI;

import org.lionsoul.pview.inc.PViewCfg;

import sun.swing.SwingUtilities2;


/**
 * MenuItemUI rewrite.
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IMenuUI extends BasicMenuUI {
	
	private boolean mouseOver = false;
	
	public IMenuUI() {}

	/**
	 * install components.
	 * 		you could change the icon 
	 */
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		menuItem.setPreferredSize(new Dimension(50, 30));
		menuItem.setFont(PViewCfg.MENU_TOOL_FONT);
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
		AbstractButton b = ( AbstractButton ) comp;
		FontMetrics fm = SwingUtilities2.getFontMetrics(b, g);
		String text = b.getText();
		
		
		g.setColor(PViewCfg.MAIN_BG_COLOR);
		g.fillRect(0, 0, comp.getWidth(), comp.getHeight());
		
		//repaint when the pressed
		if ( mouseOver ) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, comp.getWidth(), comp.getHeight());
		}
		
		if ( text != null && ! text.equals("") ) {
			g.setColor(PViewCfg.MAIN_FR_COLOR);
			g.drawString(text, 
					( b.getWidth() - fm.stringWidth(text) ) / 2,
					b.getHeight() / 2 + 4);
		}
	}

}

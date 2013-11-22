package org.lionsoul.pview.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/**
 * ScrollPane UI
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IScrollPaneUI extends BasicScrollPaneUI {
	
	@Override
	public void installUI ( JComponent c ) {
		super.installUI(c);
		c.setBorder(null);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
}

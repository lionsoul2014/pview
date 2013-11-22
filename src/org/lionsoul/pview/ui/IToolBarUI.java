package org.lionsoul.pview.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicToolBarUI;

import org.lionsoul.pview.inc.PViewCfg;


/**
 * tool bar UI for PView
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IToolBarUI extends BasicToolBarUI {
	
	public static final int TOOL_BAR_HEIGHT = 40;
	
	public IToolBarUI() {}
	
	@Override
	public void installUI( JComponent c ) {
		super.installUI(c);
		c.setPreferredSize(new Dimension( c.getWidth(), TOOL_BAR_HEIGHT ));
		c.setBorder(new BevelBorder( 2, PViewCfg.MAIN_BG_COLOR, PViewCfg.MAIN_BG_COLOR ));
		c.setLayout(new FlowLayout( 15, 5, SwingConstants.LEFT ));
	}
	
	@Override
	public void paint( Graphics g, JComponent comp ) {
		Graphics2D g2 = ( Graphics2D ) g;
		g2.setColor(PViewCfg.MAIN_BG_COLOR);
		g2.fill3DRect(0, 0, comp.getWidth(), comp.getHeight(), true);
	}
}

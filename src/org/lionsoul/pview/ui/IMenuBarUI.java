package org.lionsoul.pview.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicMenuBarUI;

import org.lionsoul.pview.inc.PViewCfg;


/**
 * MenuItemUI rewrite.
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IMenuBarUI extends BasicMenuBarUI {
	
	public static final int JCOMPONENT_HEIGHT = 28;
	
	public IMenuBarUI() {}
	
	@Override
	public void installUI( JComponent c ) {
		super.installUI(c);
		c.setPreferredSize(new Dimension(c.getWidth(), JCOMPONENT_HEIGHT));
		c.setFont(PViewCfg.MENU_TOOL_FONT);
		c.setBorder(new BevelBorder( 2, Color.BLACK, PViewCfg.MAIN_BG_COLOR ));
	}

	/**
	 * paint component 
	 */
	@Override
	public void paint(Graphics g, JComponent comp) {
		g.setColor(PViewCfg.MAIN_BG_COLOR);
		g.fill3DRect(0, 0, comp.getWidth(), comp.getHeight(), true);
	}

}

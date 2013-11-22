package org.lionsoul.pview.ui;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

import org.lionsoul.pview.inc.PViewCfg;


/**
 * popup menu ui.
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IPopupMenuUI extends BasicPopupMenuUI {
	
	public static ComponentUI createUI( JComponent c ) {
		return new IPopupMenuUI();
	}
	
	@Override
	public void installUI( JComponent c ) {
		super.installUI(c);
		c.setBorder(null);
	}
	
	@Override
	public void paint( Graphics g, JComponent comp ) {
		super.paint(g, comp);
		g.setColor(PViewCfg.MAIN_BG_COLOR);
		g.fill3DRect(0, 0, comp.getWidth(), comp.getHeight(), true);
	}
	
}

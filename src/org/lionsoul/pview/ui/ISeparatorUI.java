package org.lionsoul.pview.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicSeparatorUI;

import org.lionsoul.pview.inc.PViewCfg;


/**
 * seperator ui class.
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class ISeparatorUI extends BasicSeparatorUI {
	
	public static Color color1 = new Color(145, 100, 55);
	
	public ISeparatorUI() {}
	
	
	@Override
	public void installUI( JComponent c ) {
		super.installUI(c);
		c.setPreferredSize(new Dimension( c.getWidth(), 1 ));
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D)g;
		
		//left half part.
		GradientPaint gp1 = new GradientPaint(0, 0, PViewCfg.MAIN_BG_COLOR,
				c.getWidth()/2, 0, color1);
		g2.setPaint(gp1);
		g2.fillRect(0, 0, c.getWidth()/2, c.getHeight());
		
		
		//right half part
		GradientPaint gp2 = new GradientPaint(c.getWidth()/2, 0, color1,
				c.getWidth(), 0, PViewCfg.MAIN_BG_COLOR);
		g2.setPaint(gp2);
		g2.fillRect(c.getWidth()/2, 0, c.getWidth(), c.getHeight());
	}
}

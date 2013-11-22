package org.lionsoul.pview;

import java.awt.Dimension;

import javax.swing.JMenuBar;

import org.lionsoul.pview.ui.IMenuUI;


/**
 * JMenuBar for PView
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class PJMenuBar extends JMenuBar {

	private static final long serialVersionUID = -8826024466282676879L;
	
	public static Dimension size = new Dimension( 200, 100 );
	
	public PJMenuBar() {
		this.setPreferredSize(size);
		this.setBorder(null);
		this.setUI(new IMenuUI());
	}

}

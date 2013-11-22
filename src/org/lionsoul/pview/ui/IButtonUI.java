package org.lionsoul.pview.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import org.lionsoul.pview.inc.CreateIcon;
import org.lionsoul.pview.inc.PViewCfg;


import sun.swing.SwingUtilities2;

/**
 * IButton UI for PView
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class IButtonUI extends BasicButtonUI {
	
	public static final int M_BUTTON = 0;
	public static final int B_BUTTON = 1;
	
	public static enum B_ICON {
		NEXT, PREVIOUS,
		ROTATE_LEFT, ROTATE_RIGHT,
		FIT_BEST, ZOOM_IN, ZOOM_OUT
	};
	
	private int size_t;
	private B_ICON icon_t;
	private ImageIcon icon;
	private boolean mouseOver = false;
	
	public IButtonUI ( int size_t, B_ICON icon_t ) {
		this.size_t = size_t;
		this.icon_t = icon_t;
		switch ( icon_t ) {
			case NEXT:
				icon = CreateIcon.createIcon("go-next.png");
				break;
			case PREVIOUS:
				icon = CreateIcon.createIcon("go-previous.png");
				break;
			case ROTATE_LEFT:
				icon = CreateIcon.createIcon("object-rotate-left.png");
				break;
			case ROTATE_RIGHT:
				icon = CreateIcon.createIcon("object-rotate-right.png");
				break;
			case FIT_BEST:
				icon = CreateIcon.createIcon("zoom-fit-best.png");
				break;
			case ZOOM_IN:
				icon = CreateIcon.createIcon("zoom-in.png");
				break;
			case ZOOM_OUT:
				icon = CreateIcon.createIcon("zoom-out.png");
				break;
		}
	}
	
	@Override
	public void installUI( JComponent c ) {
		super.installUI(c);
		JButton b = ( JButton ) c;
		if ( size_t == M_BUTTON )
			b.setPreferredSize(PViewCfg.M_BUTTON_SIZE);
		else 
			b.setPreferredSize(PViewCfg.B_BUTTON_SIZE);
		b.setRolloverEnabled(true);
		b.setBorder( null );
		b.setFont(PViewCfg.MENU_TOOL_FONT);
	}
	
	@Override
	public void installListeners(final  AbstractButton b ) {
		super.installListeners(b);
		final JButton button = ( JButton ) b;
		b.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseOver = true;
				button.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseOver = false;
				button.repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseOver = true;
				button.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseOver = false;
				button.repaint();
			}
		});
	}
	
	@Override
	public void paint( Graphics g, JComponent c ) {
		super.paint(g, c);
		JButton button = ( JButton ) c;
		String text = button.getText();
		FontMetrics fm = SwingUtilities2.getFontMetrics(button, g);
		
		ButtonModel model = button.getModel();
		
		g.setColor(PViewCfg.MAIN_BG_COLOR);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		
		
		
/*		if ( b.isFocusPainted() && b.hasFocus() ) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}*/
		
		if ( mouseOver == true ) {
			g.setColor(new Color(80, 80, 80));
			g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
		}
		
		//perform UI specific press action, e.g. Windows L&F shifts text
        if ( model.isArmed() && model.isPressed() ) {
        	g.setColor(Color.DARK_GRAY);
            g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
        }
        

		if ( text != null && ! text.equals("") ) {
			g.setColor(PViewCfg.MAIN_FR_COLOR);
			int x = (c.getWidth() - fm.stringWidth(text)) / 2;
			if ( size_t == B_BUTTON )
				x += 15;
			g.drawString(text, 
					x, c.getHeight() / 2 + 4);
		} 
		
		//paint the icon
		if ( icon != null ) {
			int y = ( c.getHeight() - icon.getIconHeight() ) / 2;
			int x = ( c.getWidth() - icon.getIconWidth() ) / 2;
			if ( icon_t == B_ICON.NEXT || icon_t == B_ICON.PREVIOUS ) 
				x = 5;
			g.drawImage(icon.getImage(), x, y, null);
		} 
    }
	
}

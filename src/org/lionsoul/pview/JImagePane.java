package org.lionsoul.pview;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;

import org.lionsoul.pview.inc.PViewCfg;


/**
 * pview JPanel
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class JImagePane extends JPanel {

	private static final long serialVersionUID = 7088737542307810259L;
	
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
	public static final int ORIN_SIZE = 1;
	public static final int WIND_SIZE = 2;
	
	public static final float MAX_PERCENT = 1.5F;
	public static final float MIN_PERCENT = 0.02F;
	public static final float WHEEL_ZOOM_OFFSET = 0.02F;
	public static final float CLICK_ZOOM_OFFSET = 0.1F;
	
	private ImageIcon icon = null;
	private ImageIcon i_bak = null;
	private int theta = 0;
	private float percent = 1.0F;
	private Dimension orin_size = null;
	
	private int i_width, i_height;
	private int w_width, w_height;
	private Point point;
	private int x_val = -1, y_val = -1;
	private boolean wheel_lock = false;
	
	public JImagePane() {
		//this.setPreferredSize(new Dimension(2000, 3000));
		DragEventListener listener = new DragEventListener();
		this.addMouseListener( listener );
		this.addMouseMotionListener(listener);
		this.addMouseWheelListener(listener);
	}
	
	/**
	 * picture drag handler inner class 
	 */
	private class DragEventListener extends MouseAdapter 
			implements MouseMotionListener,MouseWheelListener {
		@Override
		public void mousePressed(MouseEvent e) {
			if ( icon != null ) {
				point = e.getLocationOnScreen();
				y_val = PView.getInstance().v_bar.getValue();
				x_val = PView.getInstance().h_bar.getValue();
				setCursor( new Cursor( Cursor.MOVE_CURSOR ) );
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if ( icon != null ) {
				setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
			}
		}

		@Override
		public synchronized void mouseDragged(MouseEvent e) {
			if ( icon != null && point != null ) {
				Point p = e.getLocationOnScreen();
				int x_offset = p.x - point.x;
				int y_offset = p.y - point.y;
				
				JScrollBar v_bar = PView.getInstance().v_bar;
				JScrollBar h_bar = PView.getInstance().h_bar;
				
				//check the vertical direction.
				if ( y_offset > 0 ) {				//head down
					if ( v_bar.getValue() > v_bar.getMinimum() ) {
						int v = y_val - y_offset;
						if ( v < v_bar.getMinimum() )
							v = v_bar.getMinimum();
						v_bar.setValue(v);
					}
				} else {							//head up
					if ( v_bar.getValue() < v_bar.getMaximum() ) {
						int v = y_val - y_offset;
						if ( v > v_bar.getMaximum() )
							v = v_bar.getMaximum();
						v_bar.setValue(v);
					} 
				}
				
				//check the horizontal direction
				if ( x_offset > 0 ) {				//head right
					if ( h_bar.getValue() > h_bar.getMinimum() ) {
						int v = x_val - x_offset;
						if ( v < h_bar.getMinimum() )
							v = h_bar.getMinimum();
						h_bar.setValue(v);
					}
				} else {							//head left
					if ( h_bar.getValue() < h_bar.getMaximum() ) {
						int v = x_val - x_offset;
						if ( v > h_bar.getMaximum() )
							v = h_bar.getMaximum();
						h_bar.setValue(v);
					}
				}
			} 
		}
		
		@Override
		public synchronized void mouseWheelMoved( MouseWheelEvent e) {
			if ( wheel_lock == false && icon != null && 
					e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL
					/*&& ( e.isShiftDown() || e.isControlDown() )*/) {
				wheel_lock = true;
				if ( e.getWheelRotation() < 0 ) {
					PView.ThreadPoll.execute(new Runnable(){						
						@Override
						public void run() {
							larger( WHEEL_ZOOM_OFFSET );
							wheel_lock = false;
						}
					});
				} else {
					PView.ThreadPoll.execute(new Runnable(){						
						@Override
						public void run() {
							smaller( WHEEL_ZOOM_OFFSET );
							wheel_lock = false;
						}
					});
				} 
					
			}
		}
		
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		Graphics2D g2 = ( Graphics2D ) g;
		g2.setColor(PViewCfg.PIC_DEFAULT_BG);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if ( i_bak != null ) {
			g2.translate(this.getWidth() / 2, this.getHeight() / 2);
			//draw the image
			g2.rotate(Math.toRadians(theta));
			g.drawImage(i_bak.getImage(), - i_width / 2, - i_height / 2, i_width, i_height, null);
			JLoadDialog.getInstance().hiddenDialog();
		}
	}
	
	public void drawPicture( ImageIcon __src ) {
		if ( orin_size != null ) resetWindSizeFlag( ORIN_SIZE );
		else resetWindSizeFlag( WIND_SIZE );
		icon = __src;
		theta = 0;percent = 1.0F;
		re_count();
		selfRepaint();
	}
	
	public void rotateLeft() {
		if ( icon != null ) {
			JLoadDialog.getInstance().showDialog();
			synchronized ( PView.LOCK ) {
				resetWindSizeFlag( ORIN_SIZE );
				percent = 1.0F;
				theta += -90;
				if ( theta == -360 ) theta = 0;
				re_count();
				selfRepaint();
			}
		}
	}
	
	public void rotateRight() {
		if ( icon != null ) {
			JLoadDialog.getInstance().showDialog();
			synchronized ( PView.LOCK ) {
				resetWindSizeFlag( ORIN_SIZE );
				percent = 1.0F;
				theta += 90;
				if ( theta == 360 ) theta = 0;
				re_count();
				selfRepaint();
			}
		}
	}
	
	public void larger( float offset ) {
		if ( icon != null && percent < MAX_PERCENT ) {
			JLoadDialog.getInstance().showDialog();
			synchronized ( PView.LOCK ) {
				percent += offset;
				if ( percent > MAX_PERCENT ) 
					percent = MAX_PERCENT;
				re_count();
				selfRepaint();
				PView.getInstance().centerScrollPane();
			}
		}
	}
	
	public void smaller( float offset ) {
		if ( icon != null && percent > MIN_PERCENT ) {
			JLoadDialog.getInstance().showDialog();
			synchronized ( PView.LOCK ) {
				percent -= offset;
				if ( percent < MIN_PERCENT ) 
					percent = MIN_PERCENT;
				re_count();
				selfRepaint();
				PView.getInstance().centerScrollPane();
			}
		}
	}
	
	public void self() {
		if ( icon != null ) {
			JLoadDialog.getInstance().showDialog();
			synchronized ( PView.LOCK ) {
				resetWindSizeFlag( ORIN_SIZE );
				theta = 0;
				percent = 1.0F;
				re_count();
				selfRepaint();
			}
		}
	}
	
	public void setPaneSize( Dimension size ) {
		if ( size != null ) { 
			setPreferredSize( new Dimension( size.width - 25, size.height - 30 ) );
			setSize(size);
		}
	}
	
	public void selfRepaint() {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				repaint();
			}
		});
	}
	
	public void re_count() {
		if ( icon == null ) return; 
		i_width = icon.getIconWidth();
		i_height = icon.getIconHeight();
		float wbit = ( float ) w_width / icon.getIconWidth();
		float hbit = ( float ) w_height / icon.getIconHeight();
		
		if ( theta < 0 ) theta += 360; 
		if ( theta == 90 ||  theta == 270 ) {
			wbit = ( float ) w_width / icon.getIconHeight();
			hbit = ( float ) w_height / icon.getIconWidth();
			//width = icon.getIconHeight();
			//height = icon.getIconWidth();
		}
		
		if ( percent == 1.0F && ( wbit < 1 || hbit < 1 ) ) {
			percent = Math.min(wbit, hbit);
			orin_size = new Dimension(w_width, w_height);
		}
		PView.getInstance().setPercent(percent);
		i_width = ( int ) ( i_width *  percent );
		i_height = ( int ) ( i_height * percent );
		
		//get the scaled image
		i_bak = new ImageIcon( icon.getImage()
				.getScaledInstance(i_width, i_height, Image.SCALE_FAST) );
		
		//update the size of image pane
		if ( i_width > this.getWidth() || i_height > this.getHeight() ) {
			this.setPaneSize(new Dimension(
					Math.max(w_width, i_width),
					Math.max(w_height, i_height)));
		} else {	
			this.setPaneSize(orin_size);
		}
		
		
	}
	
	public void resetOrinSize( int w, int h ) {
		orin_size = new Dimension( w, h );
	}
	
	public void resetWindSizeFlag( int flag ) {
		if ( flag == ORIN_SIZE ) {
			w_width = orin_size.width;
			w_height = orin_size.height;
		} else if ( flag == WIND_SIZE ) {
			w_width = this.getWidth();
			w_height = this.getHeight();
		}
	}
	
}

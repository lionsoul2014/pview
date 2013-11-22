package org.lionsoul.pview;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lionsoul.pview.inc.PViewCfg;
import org.lionsoul.pview.ui.IButtonUI;
import org.lionsoul.pview.ui.IJMenuItemUI;
import org.lionsoul.pview.ui.IMenuBarUI;
import org.lionsoul.pview.ui.IMenuUI;
import org.lionsoul.pview.ui.IPopupMenuUI;
import org.lionsoul.pview.ui.IScrollBarUI;
import org.lionsoul.pview.ui.IScrollPaneUI;
import org.lionsoul.pview.ui.ISeparatorUI;
import org.lionsoul.pview.ui.IToolBarUI;


/**
 * picture viwer main class.
 * 
 * @author chenxin <chenxin619315@gmail.com>
 */
public class PView extends JFrame {

	private static final long serialVersionUID = 5417989883734105714L;
	public static Object LOCK = new Object();
	public static Executor ThreadPoll = Executors.newCachedThreadPool();
	public static Dimension S_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private static PView __instance = null; 
	
	private JButton prev = null;
	private JButton next = null;
	
	private JLabel label = null;
	private JLabel plabel = null;
	private JImagePane pane = null;
	public JScrollPane scrollPane = null;
	JScrollBar v_bar = null;
	JScrollBar h_bar = null;
	
	private Container c = null;
	private int idx = 90;
	
	private ArrayList<String> list = new ArrayList<String>();
	private String old_parent = null;
	
	public static PView getInstance() {
		if ( __instance == null )
			__instance = new PView();
		return __instance;
	}
	
	private PView() {							//the main method.
		setTitle(PViewCfg.W_TITLE);
		this.setPreferredSize(PViewCfg.W_SIZE);
		this.setSize(PViewCfg.W_SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		init();
		pane = new JImagePane();
		scrollPane = new JScrollPane(pane);
		
		scrollPane.setUI(new IScrollPaneUI());
		v_bar = scrollPane.getVerticalScrollBar();
		h_bar = scrollPane.getHorizontalScrollBar();
		v_bar.setUI(new IScrollBarUI());
		h_bar.setUI(new IScrollBarUI());
		setScrollPaneSize( getWidth(), getHeight() );
		c.add(scrollPane, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setScrollPaneSize( getWidth(), getHeight() );
				Dimension size = scrollPane.getSize();
				pane.resetOrinSize( size.width, size.height );
				//pane.re_count();
				//pane.repaint();
			}
		});
	}
	
	/**
	 * initialize the GUI of the pview 
	 */
	private void init() {
		
		JMenuBar menu = new JMenuBar();			//菜单栏
		menu.setUI(new IMenuBarUI());
		JMenu file = new JMenu("文件");
		file.getPopupMenu().setUI(new IPopupMenuUI());
		file.setUI(new IMenuUI());
		JMenu about = new JMenu("关于");
		about.getPopupMenu().setUI(new IPopupMenuUI());
		about.setUI(new IMenuUI());
		
		JMenuItem open = new JMenuItem("打开");		//菜单栏下的项目--打开
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
		open.setUI(new IJMenuItemUI());
		open.addActionListener(new ActionListener() {
			@Override								//告诉编译器，下面的代码是要重写的
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						JFileChooser chooser = new JFileChooser();		//文件选择器
						chooser.setFileHidingEnabled(true);
						chooser.setAcceptAllFileFilterUsed(false);
						//chooser.setFileFilter(new FileNameExtensionFilter("*.bmp", "png"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.gif", "gif"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.jpeg", "jpeg"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
						//chooser.setSelectedFile(new File("新建文本文档.txt"));
						int result = chooser.showOpenDialog(null);
						if ( result == JFileChooser.APPROVE_OPTION ) {
							//获取选择到的文件
							File pic = chooser.getSelectedFile();
							boolean load = true;
							if ( old_parent != null ) {
								String n = pic.getParentFile().getAbsolutePath();
								if ( n.equals(old_parent) ) load = false;
							}
							if ( load == true ) {
								if ( old_parent != null ) list.clear(); 
								File parent = pic.getParentFile();
								old_parent = parent.getAbsolutePath();
								//File[] pics = parent.listFiles(new IFIleFIlter());
								String[] names = parent.list(new IFIleFIlter());
								for ( int j = 0; j < names.length; j++ ) {
									//System.out.println(names[j]);
									if ( names[j].equals(pic.getName()) ) idx = j;
									list.add( names[j] );
								}
							}
							//pic = null;
							idx--;
							next();
						}
					}
				});
			}
		});
		JMenuItem saveAs = new JMenuItem("另存为");					//另存为
		saveAs.setUI(new IJMenuItemUI());
		saveAs.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if ( list.size() == 0 ) return; 
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						JFileChooser chooser = new JFileChooser();
						chooser.setFileHidingEnabled(true);
						chooser.setAcceptAllFileFilterUsed(false);
						//chooser.setFileFilter(new FileNameExtensionFilter("*.bmp", "png"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.gif", "gif"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.jpeg", "jpeg"));
						chooser.setFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));
						int result = chooser.showSaveDialog(null);
						if ( result == JFileChooser.APPROVE_OPTION ) {
							ImageIcon icon = new ImageIcon(old_parent+"/"+list.get(idx));
							String name = list.get(idx);
							try {
								//创建字节缓冲输出流
								FileOutputStream os = new FileOutputStream(chooser.getSelectedFile());
								BufferedOutputStream bos = new BufferedOutputStream(os);
								
								//创建缓冲图片对象
								BufferedImage img = new BufferedImage(icon.getIconWidth(),
										icon.getIconHeight(),
										BufferedImage.TYPE_INT_RGB);
								//将ImageIcon转换为BufferdImage
								img.createGraphics().drawImage(icon.getImage(), 0, 0, null);
								//
								String prefix = name.substring(
										name.lastIndexOf('.') + 1, name.length());
								//保存图片到指定的缓冲流
								ImageIO.write(img, prefix, bos);
								os.close();
								bos.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				});
			}
		});
		JMenuItem exit = new JMenuItem("退出");
		exit.setUI(new IJMenuItemUI());
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "确定退出PView吗？", "PView:",
						JOptionPane.OK_CANCEL_OPTION);
				if ( result == 0 ) 
					System.exit(0);
			}
		});
		file.add(open);
		JSeparator sep_1 = new JSeparator();
		sep_1.setUI(new ISeparatorUI());
		file.add(sep_1);
		file.add(saveAs);
		JSeparator sep_2 = new JSeparator();
		sep_2.setUI(new ISeparatorUI());
		file.add(sep_2);
		file.add(exit);
		menu.add(file);
		
		
		JMenuItem author = new JMenuItem("关于作者");
		author.setUI(new IJMenuItemUI());
		author.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.SHIFT_DOWN_MASK));
		author.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, PViewCfg.PIC_AUTHOR_INFO, "Author："
						, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JMenuItem company = new JMenuItem("本软件");
		company.setUI(new IJMenuItemUI());
		company.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, PViewCfg.PIC_COMPANY_INFO,
						"PView：", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		about.add(author);
		JSeparator sep_3 = new JSeparator();
		sep_3.setUI(new ISeparatorUI());
		about.add(sep_3);
		about.add(company);
		menu.add(about);
		this.setJMenuBar(menu);
		
		//tool bar
		JToolBar tool = new JToolBar();
		tool.setLayout(new FlowLayout(5, 5, FlowLayout.LEFT));
		tool.setFloatable(false);
		tool.setUI(new IToolBarUI());
		prev = new JButton("上一张");
		prev.setUI(new IButtonUI( IButtonUI.B_BUTTON, IButtonUI.B_ICON.PREVIOUS ));
		prev.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						prev();
					}
				});
			}
		});
		next = new JButton("下一张");
		next.setUI(new IButtonUI( IButtonUI.B_BUTTON, IButtonUI.B_ICON.NEXT ));
		next.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						next();
					}
				});
			}
		});
		tool.add(prev);
		tool.add(next);
		tool.addSeparator();
		
		//zoom in out
		JButton zoom_in = new JButton();
		zoom_in.setUI(new IButtonUI( IButtonUI.M_BUTTON, IButtonUI.B_ICON.ZOOM_IN ));
		zoom_in.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						pane.larger( JImagePane.CLICK_ZOOM_OFFSET );
					}
				});
			}
		});
		JButton zoom_out = new JButton();
		zoom_out.setUI(new IButtonUI( IButtonUI.M_BUTTON, IButtonUI.B_ICON.ZOOM_OUT ));
		zoom_out.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						pane.smaller( JImagePane.CLICK_ZOOM_OFFSET );
					}
				});
			}
		});
		JButton rsize = new JButton();
		rsize.setUI(new IButtonUI( IButtonUI.M_BUTTON, IButtonUI.B_ICON.FIT_BEST ));
		rsize.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						pane.self();
					}
				});
			}
		});
		tool.add(zoom_in);
		tool.add(zoom_out);
		tool.add(rsize);
		tool.addSeparator();
		
		//turn
		JButton lturn = new JButton();
		lturn.setUI(new IButtonUI( IButtonUI.M_BUTTON , IButtonUI.B_ICON.ROTATE_LEFT));
		lturn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						pane.rotateLeft();
					}
				});
			}
		});
		JButton rturn = new JButton();
		rturn.setUI(new IButtonUI( IButtonUI.M_BUTTON, IButtonUI.B_ICON.ROTATE_RIGHT ));
		rturn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ThreadPoll.execute(new Runnable(){
					@Override
					public void run() {
						pane.rotateRight();
					}
				});
			}
		});
		tool.add(lturn);
		tool.add(rturn);
		
		c.add(tool, BorderLayout.NORTH);
		
		//底部信息状态
		label = new JLabel(PViewCfg.LABLE_TEXT);
		label.setOpaque(false);
		label.setForeground(PViewCfg.MAIN_FR_COLOR);
		JPanel bpane = new JPanel();
		bpane.setBackground(PViewCfg.MAIN_BG_COLOR);
		bpane.setPreferredSize(new Dimension(c.getWidth(), PViewCfg.TOOL_BAR_HEIGHT));
		bpane.setLayout(new FlowLayout(5, 10, FlowLayout.LEFT));
		bpane.add(label);
		bpane.add(new JSeparator());
		plabel = new JLabel();
		plabel.setOpaque(false);
		plabel.setForeground(PViewCfg.MAIN_FR_COLOR);
		bpane.add(plabel);
		c.add(bpane, BorderLayout.SOUTH);
	}
	
	/**
	 * set the size of the ScrollPane 
	 */
	public void setScrollPaneSize( int width, int height ) {
		Dimension size = new Dimension( width - 8,
				height - PViewCfg.MENU_BAR_HEIGHT - IToolBarUI.TOOL_BAR_HEIGHT - 
				IMenuBarUI.JCOMPONENT_HEIGHT - PViewCfg.WIND_BOR_HEIGHT);
		scrollPane.setPreferredSize(size);
		scrollPane.setSize(size);
	}
	
	/**
	 * append text to the statu bar 
	 */
	public void setPercent( float per ) {
		String str = (per*100)+"";
		plabel.setText(str.substring(0, str.indexOf('.'))+"%");
	}
	
	public void setLabelText( String str ) {
		label.setText(str);
	}
	
	/**
	 * 查看下一张图片 
	 */
	public void next() {
		if ( list.size() == 0 ) return;
		JLoadDialog.getInstance().showDialog();
		synchronized ( LOCK ) {
			idx++;
			if ( idx > list.size() - 1 )
				idx =  0;
			ImageIcon icon = new ImageIcon(old_parent+"/"+list.get(idx));
				setTitle(list.get(idx)+" - "+PViewCfg.W_TITLE);
				setLabelText(" "+icon.getIconWidth()+" × "+icon.getIconHeight()+" 像素   "+
						getFileSize(idx));
			pane.drawPicture(icon);
		}
	}
	
	/**
	 * 查看上一张 
	 */
	public void prev() {
		if ( list.size() == 0 ) return;
		JLoadDialog.getInstance().showDialog();
		synchronized ( LOCK ) {
			idx--;
			if ( idx < 0 )
				idx =  list.size() - 1;
			ImageIcon icon = new ImageIcon( old_parent+"/"+list.get(idx) );
			setTitle(list.get(idx)+" - "+PViewCfg.W_TITLE);
			setLabelText(" "+icon.getIconWidth()+" × "+icon.getIconHeight()+" 像素   "+
					getFileSize(idx));
			pane.drawPicture(icon);
		}
	}
	
	private String getFileSize( int idx ) {
		File pic = new File(old_parent+"/"+list.get(idx));
		if ( pic.length() >= 1048576 ) {
			String str = ( ( float ) pic.length() / 1048576 ) +"";
			return str.substring(0, str.indexOf('.') + 3)+"MB";
		} else if ( pic.length() > 1024 ) {
			String str = ( ( float ) pic.length() / 1024 ) +"";
			return str.substring(0, str.indexOf('.') + 3)+"KB";
		} 
		return pic.length()+"B";
	}
	
	public void centerScrollPane() {
		v_bar.setValue(v_bar.getMaximum() / 4);
		h_bar.setValue(h_bar.getMaximum() / 4);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch ( Exception e ) {}
		getInstance().setVisible(true);
	}

}

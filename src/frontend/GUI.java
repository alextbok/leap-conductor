package frontend;

/**
 * GUI
 * a graphical user interface for the leap conductor
 * @author Alex Bok
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import backend.FileProcessor;
import backend.audio.SongApp;


public class GUI extends JFrame {

  /*Our JPanels*/
  private VisualizerPanel _visualizerPanel;
  private SongPanel _songPanel;
  
  /*Public access to our non-resizable frame so children panels can set relative sizes*/
  public static final int WIDTH = 1300;
  public static final int HEIGHT = 700;
  
  /**
   * Constructor 
   */
  public GUI(SongApp songApp) {
	    // set up frame
	    super("Leap Conductor");
	    this.setSize(new Dimension(WIDTH, HEIGHT));

	    // set up panels
	    _visualizerPanel = new VisualizerPanel(songApp, 5000, 3);
	    _songPanel = new SongPanel();
	    
	    // add components
	    this.add(_visualizerPanel, BorderLayout.CENTER);
	    this.add(_songPanel, BorderLayout.NORTH);
	    
	    //add menu bar
	    this.setJMenuBar(new LeapConductorMenuBar());
	    
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	    
	    //gets the default mp3 files (those is /src/sounds)
	    File[] songs = FileChooser.getAllMp3Files();
	    for (File song: songs) {
	    	SongList.addSong(song);
	    }
  }

  	/**
  	 * A menu bar with one menu with two options (to view readme and controls)
  	 * @author abok
  	 *
  	 */
  	@SuppressWarnings("serial")
	private class LeapConductorMenuBar extends JMenuBar {
	  
	  LeapConductorMenuBar() {
		  
		  this.setBackground(SongPanel.BACKGROUND_COLOR);
		  
		  //add the menu
		  JMenu menu = new JMenu("Help");
		  menu.setBackground(SongPanel.BACKGROUND_COLOR);
		  menu.setMnemonic(KeyEvent.VK_A);
		  this.add(menu);
		  
		  //add README menu item
		  JMenuItem menuItemReadMe = new JMenuItem("README", KeyEvent.VK_T);
		  menuItemReadMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		  addMenuItemActionListener(menuItemReadMe);
		  menu.add(menuItemReadMe);
		  
		  JMenuItem menuItemControls = new JMenuItem("Controls");
		  menuItemControls.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		  addMenuItemActionListener(menuItemControls);
		  menu.add(menuItemControls);
	  }
	  
	  /**
	   * add action listener to input menu item
	   */
	  private void addMenuItemActionListener(JMenuItem item) {
		  item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				//if the user clicks "README", show the appropriate popup
				if (action.equals("README")) {
					new LeapConductorPopup(FileProcessor.README_FILENAME);
				}
				//if the user clicks "Controls", show the appropriate popup
				else if (action.equals("Controls")) {
					new LeapConductorPopup(FileProcessor.CONTROLS_FILENAME);
				}
			}	  
		  });
	  }
  }
  	
}

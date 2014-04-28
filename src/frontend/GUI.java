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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import frontend.soundpanel.FileChooser;
import frontend.soundpanel.LeapConductorPopup;
import frontend.soundpanel.ProgressBarPanel;
import frontend.soundpanel.SongList;
import frontend.soundpanel.SongPanel;
import backend.FileProcessor;


@SuppressWarnings("serial")
public class GUI extends JFrame {

  /*Our JPanels*/
  private VisualizerPanel _visualizerPanel;
  private SongPanel _songPanel;
  private ProgressBarPanel _progressPanel;
	
  
  /*Public access to our non-resizable frame so children panels can set relative sizes*/
  public static final int WIDTH = 1300;
  public static final int HEIGHT = 720;
  
  /**
   * Constructor 
   */
  public GUI() {
		// set up frame
		super("Leap Conductor");
		this.setSize(new Dimension(WIDTH, HEIGHT));
	
		// set up panels
		_visualizerPanel = new VisualizerPanel(5000, 2);
		_songPanel = new SongPanel();
		_progressPanel = new ProgressBarPanel();
		    
		// add components
		this.add(_visualizerPanel, BorderLayout.CENTER);
		this.add(_songPanel, BorderLayout.NORTH);
		this.add(_progressPanel, BorderLayout.SOUTH);
		    
		this.addWindowListener(new LeapConductorWindowAdapter());
		
		//add menu bar
		this.setJMenuBar(new LeapConductorMenuBar());
		    
		this.setMinimumSize(new Dimension(775,500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		    
		//gets the default music files (those is /src/sounds and from previous session)
		File[] defaultSongs = FileChooser.getAllMusicFiles();
		for (File song: defaultSongs) {
			if (song.exists())
				SongList.addSong(song);
		}
		File[] previousSongs = FileProcessor.getSongsFromPreviousSession();
		for (File song: previousSongs) {
			if (song.exists())
				SongList.addSong(song);
		}
  }
  
  	/**
  	 * A menu bar with one menu with two options (to view readme and controls)
  	 * @author abok
  	 *
  	 */
	private class LeapConductorMenuBar extends JMenuBar {
	  
		
		public LeapConductorMenuBar() {
		  
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
						new LeapConductorPopup(FileProcessor.README_FILENAME, "README");
					}
					//if the user clicks "Controls", show the appropriate popup
					else if (action.equals("Controls")) {
						new LeapConductorPopup(FileProcessor.CONTROLS_FILENAME, "Controls");
					}
				}	  
			});
		}
	} //end private class JMenuBar
	
	/**
	 * Saves song file paths when the user closes the window
	 * @author abok
	 *
	 */
	private class LeapConductorWindowAdapter extends WindowAdapter {
	    @Override
	    public void windowClosing(WindowEvent e) {
	    	String[] filepaths = SongList.getAllSongs();
			try {
				String dir = System.getProperty("user.dir");
	    		PrintWriter writer = new PrintWriter(dir + "/song_files.txt", "UTF-8");
	    		for (String path: filepaths) {
	    			writer.println(path);
	    		}
	    		writer.close();
			} catch (FileNotFoundException e1) {
				/*ignore and do nothing*/
			} catch (UnsupportedEncodingException e1) {
				/*Ignore and do nothing*/
			}
	    }
	}
  	
}

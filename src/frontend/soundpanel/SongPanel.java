package frontend.soundpanel;

import hub.SoundController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import backend.FileProcessor;
import frontend.GUI;
import frontend.audiovisualizer.VisualizerPanel;

/**
 * JPanel with sound controls and controls to choose song
 * @author abok
 */
@SuppressWarnings("serial")
public class SongPanel extends JPanel implements ResponseReceiver {

	/*Store this in a variable so our Knobs can access same color*/
	public static final Color BACKGROUND_COLOR = new Color(214,212,210);

	/*Volume control knob panel*/
	public static final KnobPanel knobVolume = new KnobPanel("Volume");

	/*Speed control knob panel*/
	public static final KnobPanel knobSpeed = new KnobPanel("Speed");

	/*Frequency control knob panels*/
	public static final KnobPanel knobLow = new KnobPanel("Low");
	public static final KnobPanel knobMid = new KnobPanel("Mid");
	public static final KnobPanel knobHigh = new KnobPanel("High");

	/*Our icons to paint*/
	private static final Image playIcon = getIcon("play");
	private static final Image pauseIcon = getIcon("pause");
	private static final Image addIcon = getIcon("add");
	private static final Image removeIcon = getIcon("remove");


	/*Constants that help keep track of where to paint things*/
	public static final int KNOB_X_OFFSET = 7;
	public static final int KNOB_Y_OFFSET = 10;
	private static final int BTN_SIZE = 40;

	//coordinates of icons (set relative in repaint method)
	private static int PLAY_X = 990;
	private static int PLAY_Y = 10;
	private static int PAUSE_X = 990;
	private static int PAUSE_Y = 55;
	private static int ADD_X = 270;
	private static int ADD_Y = 10;
	private static int REMOVE_X = 270;
	private static int REMOVE_Y = 55;

	/*The song currently being played*/
	private File _currentSong;

	private TutorialStage _tutorialStage = TutorialStage.FINISHED;
	private KeyListener _tutorialListener;

	private LeapListener _leap;

	/**
	 * Constructor - provides initial setup
	 */
	public SongPanel() {

		this.add(SongList.getScrollableList());
		this.addKnobs();

		//add our button mouse listener
		this.addMouseListener(new SoundPanelMouseListener());

		//get our JPanel to show the way we want
		this.setPreferredSize(new Dimension(GUI.WIDTH, 100));
		this.setBackground(BACKGROUND_COLOR);
		this.setVisible(true);
	}

	/**
	 * Paints button images and rotates as necessary
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D brush = (Graphics2D) g;

		//set positions relative to volume knob so they are painted
		//correctly when the frame is resized
		PLAY_X = knobVolume.getX() + 475;
		PLAY_Y = knobVolume.getY();
		PAUSE_X = knobVolume.getX() + 475;
		PAUSE_Y = knobVolume.getY() + 45;
		ADD_X = knobVolume.getX() - 250;
		ADD_Y = knobVolume.getY();
		REMOVE_X = knobVolume.getX() - 250;
		REMOVE_Y = knobVolume.getY() + 45;

		//draw buttons
		brush.drawImage(playIcon, PLAY_X, PLAY_Y, null);
		brush.drawImage(pauseIcon, PAUSE_X, PAUSE_Y, null);
		brush.drawImage(addIcon, ADD_X, ADD_Y, null);
		brush.drawImage(removeIcon, REMOVE_X, REMOVE_Y, null);

	}


	/**
	 * Add our knobs and their respective mouse listeners (to factor out code)
	 */
	private void addKnobs() { 	
		this.add(knobVolume);
		this.add(knobSpeed);
		this.add(knobLow);
		this.add(knobMid);
		this.add(knobHigh);
	}



	/**
	 * Generates and returns the icon with the input string file name
	 * Returns null and prints an error if there is an IOException
	 * @return Image knob icon
	 */
	public static Image getIcon(String name) {
		try {
			String dir = System.getProperty("user.dir") + "/src/data/icons/";
			return ImageIO.read(new File(dir + name + ".png"));
		} catch (IOException e) {
			System.out.println("ERROR: IOException when trying to load image in SongPanel.getIcon()");
			return null;
		}
	}

	/**
	 * Provides public access to knob panels for hub.SoundController
	 * @param type
	 * @return
	 */
	public static KnobPanel getKnobPanel(KnobType type) {
		switch(type) {
		case VOLUME:
			return knobVolume;
		case SPEED:
			return knobSpeed;
		case LOW:
			return knobLow;
		case MID:
			return knobMid;
		case HIGH:
			return knobHigh;
		default:
			return null;
		}
	}

	/**
	 * Enters tutorial mode
	 */
	public void startTutorial() {
		this.setFocusable(true);
		this.requestFocusInWindow();
		_tutorialStage = TutorialStage.INTRO_1;
		VisualizerPanel.overlayText = "Welcome to the tutorial! Press any key to begin.";
		_tutorialListener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				switch(_tutorialStage) {
				case INTRO_1:
					_tutorialStage = TutorialStage.INTRO_2;
					VisualizerPanel.overlayText = "Nice job. This guide will walk you through the basics of controlling music with your Leap.";
					break;
				case INTRO_2:
					_tutorialStage = TutorialStage.MOTION_INTRO;
					VisualizerPanel.overlayText = "Hover your hand over the leap and spread out your fingers.";
					VisualizerPanel.overlayText2 = "The circles on the screen represent your fingers and palm.";
					SongPanel.this.removeKeyListener(_tutorialListener);
					_leap.listenForFiveFingers(SongPanel.this);
					break;
				default:
					break;
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		};

		this.addKeyListener(_tutorialListener);
	}

	@Override
	public void receiveResponse() {
		switch(_tutorialStage) {	
		case MOTION_INTRO:
			_tutorialStage = TutorialStage.PLAY;
			VisualizerPanel.overlayText = "Cool, right? If you want to move your hand without accidentally registering";
			VisualizerPanel.overlayText2 = "a gesture, just make a fist. Try it out.";
			_leap.listenForFist(this);
			break;
		case PLAY:
			_tutorialStage = TutorialStage.VOLUME;
			VisualizerPanel.overlayText = "Now we get to the fun part. Let's play some music!";
			VisualizerPanel.overlayText2 = "Hold both hands flat and then gently bring them together to start the jams.";
			_leap.listenForPlay(this);
			break;
		case VOLUME:
			_tutorialStage = TutorialStage.BAND_CONTROL;
			VisualizerPanel.overlayText = "You can even control the volume with your hands.";
			VisualizerPanel.overlayText2 = "Make a V with two fingers, and then move them up or down";
			_leap.listenForVolume(this);
			break;
		case BAND_CONTROL:
			_tutorialStage = TutorialStage.SPEED;
			VisualizerPanel.overlayText = "To change up the low, mid, or high sounds of a song, raise or lower one hand to the left,";
			VisualizerPanel.overlayText2 = "in the middle, or on the right. Remember to make a fist when you don't want movements recognized!";
			_leap.listenForHigh(this);
			break;
		case SPEED:
			_tutorialStage = TutorialStage.STOP;
			VisualizerPanel.overlayText = "Move your finger in a circle clockwise to speed up the music,";
			VisualizerPanel.overlayText2 = "or counterclockwise to slow it down. Try one now.";
			_leap.listenForSpeed(this);
			break;
		case STOP:
			_tutorialStage = TutorialStage.FINISHED;
			VisualizerPanel.overlayText = "Finally, hold your hands flat and together and gently spread them apart";
			VisualizerPanel.overlayText2 = "to stop the song.";
			_leap.listenForStop(this);
			break;
		case FINISHED:
			FileProcessor.completeTutorial();
			VisualizerPanel.overlayText = "Nice job! You can also load in your own music by clicking the plus button. Enjoy!";
			VisualizerPanel.overlayText2 = "(Press any key to exit tutorial)";
			_tutorialListener = new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					cleanUpTutorial();
				}
				@Override
				public void keyPressed(KeyEvent e) {}
				@Override
				public void keyReleased(KeyEvent e) {}
			};
			this.addKeyListener(_tutorialListener);
			break;
		default:
			break;
		}
	}

	public void cleanUpTutorial() {
		_tutorialStage = TutorialStage.FINISHED;
		if(_tutorialListener != null) {
			this.removeKeyListener(_tutorialListener);
		}
		VisualizerPanel.overlayText = "";
		VisualizerPanel.overlayText2 = "";
	}

	public void setLeap(LeapListener leap) {
		this._leap = leap;
	}

	/**
	 * Private class that listens for Mouse click events on the play/pause button
	 * @author abok
	 *
	 */
	private class SoundPanelMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			//if the click is on the play button
			if (isWithinRadius(x,y,(PLAY_X + BTN_SIZE/2), (PLAY_Y + BTN_SIZE/2), BTN_SIZE/2 )) {
				File selectedSong = SongList.getCurrentlySelectedSong();
				//if a song is currently being played and it is not the currently selected song, stop and play new song

				if (selectedSong != null){

					if (_currentSong != null && _currentSong != selectedSong) {
						SoundController.stopSong();
						SoundController.setSong(selectedSong);
						_currentSong = selectedSong;
					}
					else {
						_currentSong = selectedSong;
					}
					SoundController.playSong();
				}
				
			}
			//if the click is on the pause button, pause the song currently playing
			else if (isWithinRadius(x,y,(PAUSE_X + BTN_SIZE/2), (PAUSE_Y + BTN_SIZE/2), BTN_SIZE/2 )){
				SoundController.stopSong();
			}
			//if the click is on the add button, bring up file chooser and add chosen songs
			else if (isWithinRadius(x,y,(ADD_X + BTN_SIZE/2), (ADD_Y + BTN_SIZE/2), BTN_SIZE/2 )){

				//do this in another thread so we don't lag up the visualizer
				new Thread() {

					@Override
					public void run() {
						File[] files = FileChooser.getSongsFromUser();
						for (File file: files)
							SongList.addSong(file);	
					}

				}.start();			

			}
			//if the click is on the remove button, remove the currently selected song
			else if (isWithinRadius(x,y,(REMOVE_X + BTN_SIZE/2), (REMOVE_Y + BTN_SIZE/2), BTN_SIZE/2 )){
				SongList.removeSelectedSongs();
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		/**
		 * Returns true if (x,y) is within or on radius of (c_x,c_y)
		 * @param x, int, x-coordinate of point
		 * @param y, int, y-coordinate of point
		 * @param c_x,int, x-coordinate of center
		 * @param c_y,int y-coordinate of center
		 * @param radius
		 * @return
		 */
		private boolean isWithinRadius(int x, int y, int c_x, int c_y, int radius) {
			if (Point2D.distance(x,y,c_x,c_y) <= radius)
				return true;
			return false;
		}
	}//end SoundPanelMouseListener

}

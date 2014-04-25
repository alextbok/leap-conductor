package frontend.soundpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import frontend.GUI;
import backend.audio.SongApp;

/**
 * JPanel with sound controls and controls to choose song
 * @author abok
 */
@SuppressWarnings("serial")
public class SongPanel extends JPanel {

	/*Store this in a variable so our Knobs can access same color*/
	public static final Color BACKGROUND_COLOR = new Color(214,212,210);
	
	/*Volume control knob*/
	public static final Knob knobVolume = new Knob("Volume");
	
	/*Speed control knob*/
	public static final Knob knobSpeed = new Knob("Speed");
	
	/*Frequency control knobs*/
	public static final Knob knobLow = new Knob("Low");
	public static final Knob knobMid = new Knob("Mid");
	public static final Knob knobHigh = new Knob("High");
	
	
	/*Keeps track of mouse-down y-coordinates for the MouseDraggedEvent*/
	private HashMap<String, Integer> _mouseDownCoordinates;
	
	/*Constants that help keep track of where to paint things*/
	private static final int KNOB_X_OFFSET = 7;
	private static final int KNOB_Y_OFFSET = 10;
	private static final int BTN_SIZE = 40;
	private static final int PLAY_X = 990;
	private static final int PLAY_Y = 10;
	private static final int PAUSE_X = 990;
	private static final int PAUSE_Y = 55;
	private static final int ADD_X = 270;
	private static final int ADD_Y = 10;
	private static final int REMOVE_X = 270;
	private static final int REMOVE_Y = 55;
	
	/*The song currently being played*/
	private File _currentSong;
	
	/**
	 * Constructor - provides initial setup
	 */
	public SongPanel() {

		//add our knobs and make their map
		_mouseDownCoordinates = new HashMap<String, Integer>();
		
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
        
        //draw buttons
        brush.drawImage(getIcon("play"), PLAY_X, PLAY_Y, null);
        brush.drawImage(getIcon("pause"), PAUSE_X, PAUSE_Y, null);
        brush.drawImage(getIcon("add"), ADD_X, ADD_Y, null);
        brush.drawImage(getIcon("remove"), REMOVE_X, REMOVE_Y, null);
        
        //rotate brush around volume knob by its current angle before painting it
        double angleVol = Math.toRadians(knobVolume.getAngle());
        brush.rotate(angleVol, Knob.WIDTH/2 + knobVolume.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobVolume.getY() + KNOB_Y_OFFSET);
        brush.drawImage(Knob.getImage(), knobVolume.getX() + KNOB_X_OFFSET, knobVolume.getY() + KNOB_Y_OFFSET, null);
        //rotate brush back
        brush.rotate(-angleVol, Knob.WIDTH/2 + knobVolume.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobVolume.getY() + KNOB_Y_OFFSET);
    
        //rotate brush around speed knob by its current angle before painting it        
        double angleSpeed = Math.toRadians(knobSpeed.getAngle());
        brush.rotate(angleSpeed, Knob.WIDTH/2 + knobSpeed.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobSpeed.getY() + KNOB_Y_OFFSET);
        brush.drawImage(Knob.getImage(), knobSpeed.getX() + KNOB_X_OFFSET, knobSpeed.getY() + KNOB_Y_OFFSET, null);
        //rotate brush back
        brush.rotate(-angleSpeed, Knob.WIDTH/2 + knobSpeed.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobSpeed.getY() + KNOB_Y_OFFSET);
        
        //rotate brush around low knob by its current angle before painting it        
        double angleLow = Math.toRadians(knobLow.getAngle());
        brush.rotate(angleLow, Knob.WIDTH/2 + knobLow.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobLow.getY() + KNOB_Y_OFFSET);
        brush.drawImage(Knob.getImage(), knobLow.getX() + KNOB_X_OFFSET, knobLow.getY() + KNOB_Y_OFFSET, null);
        //rotate brush back
        brush.rotate(-angleLow, Knob.WIDTH/2 + knobLow.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobLow.getY() + KNOB_Y_OFFSET);
    
        //rotate brush around mid knob by its current angle before painting it        
        double angleMid = Math.toRadians(knobMid.getAngle());
        brush.rotate(angleMid, Knob.WIDTH/2 + knobMid.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobMid.getY() + KNOB_Y_OFFSET);
        brush.drawImage(Knob.getImage(), knobMid.getX() + KNOB_X_OFFSET, knobMid.getY() + KNOB_Y_OFFSET, null);
        //rotate brush back
        brush.rotate(-angleMid, Knob.WIDTH/2 + knobMid.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobMid.getY() + KNOB_Y_OFFSET);
    
        //rotate brush around high knob by its current angle before painting it        
        double angleHigh = Math.toRadians(knobHigh.getAngle());
        brush.rotate(angleHigh, Knob.WIDTH/2 + knobHigh.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobHigh.getY() + KNOB_Y_OFFSET);
        brush.drawImage(Knob.getImage(), knobHigh.getX() + KNOB_X_OFFSET, knobHigh.getY() + KNOB_Y_OFFSET, null);
        //rotate brush back
        brush.rotate(-angleHigh, Knob.WIDTH/2 + knobHigh.getX() + KNOB_X_OFFSET, Knob.HEIGHT/2 + knobHigh.getY() + KNOB_Y_OFFSET);
        
    }
    

    /**
     * Add our knobs and their respective mouse listeners
     * called every repaint()
     */
    private void addKnobs() {
    	
		this.add(knobVolume);
		_mouseDownCoordinates.put(knobVolume.getText(), 0);
		knobVolume.addMouseListener(new KnobMouseListener(knobVolume));
		knobVolume.addMouseMotionListener(new KnobMouseMotionListener(knobVolume));
		
		this.add(knobSpeed);
		_mouseDownCoordinates.put(knobSpeed.getText(), 0);
		knobSpeed.addMouseListener(new KnobMouseListener(knobSpeed));
		knobSpeed.addMouseMotionListener(new KnobMouseMotionListener(knobSpeed));
		
		this.add(knobLow);
		_mouseDownCoordinates.put(knobLow.getText(), 0);
		knobLow.addMouseListener(new KnobMouseListener(knobLow));
		knobLow.addMouseMotionListener(new KnobMouseMotionListener(knobLow));
		
		this.add(knobMid);
		_mouseDownCoordinates.put(knobMid.getText(), 0);
		knobMid.addMouseListener(new KnobMouseListener(knobMid));
		knobMid.addMouseMotionListener(new KnobMouseMotionListener(knobMid));
		
		this.add(knobHigh);
		_mouseDownCoordinates.put(knobHigh.getText(), 0);
		knobHigh.addMouseListener(new KnobMouseListener(knobHigh));
		knobHigh.addMouseMotionListener(new KnobMouseMotionListener(knobHigh));
    }
    
    
    
	/**
	 * Generates and returns the icon with the input string file name
	 * Returns null and prints an error if there is an IOException
	 * @return Image knob icon
	 */
	public static Image getIcon(String name) {
		try {
			String dir = System.getProperty("user.dir") + "/src/icons/";
			return ImageIO.read(new File(dir + name + ".png"));
		} catch (IOException e) {
			System.out.println("ERROR: IOException when trying to load image in SingPanel.getPlayIcon()");
			return null;
		}
	}
    
    
    /**
     * 
     * Private class KnobMouseListener that listens for mouse down events
     * We store the y-coordinate on this event, which will be used on the
     * MouseDragged event in our mouse motion listener
     *
     */
	private class KnobMouseListener implements MouseListener {

		private Knob _knob;

		public KnobMouseListener(Knob knob) {
			_knob = knob;
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			_mouseDownCoordinates.put(_knob.getText(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	
	/**
	 * Private class that listens for MouseDragged events, which turn the knobs
	 * @author abok
	 *
	 */
	private class KnobMouseMotionListener implements MouseMotionListener {

		private Knob _knob;

		public KnobMouseMotionListener(Knob knob) {
			_knob = knob;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			double rotation = 0.5 * (_mouseDownCoordinates.get(_knob.getText()) - e.getY());
			_knob.rotateImage(rotation);

			if (_knob.getText().equals("Volume"))
				SongApp.changeVolume(rotation / 50);
			else if (_knob.getText().equals("Speed"))
				SongApp.changeSpeed(rotation * 0.001);

			_mouseDownCoordinates.put(_knob.getText(), e.getY());
			SongPanel.this.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {}
	}
	
	/**
	 * Private class that listens for Mouse click events on the play/pause button
	 * @author abok
	 *
	 */
	private class SoundPanelMouseListener implements MouseListener {

		public SoundPanelMouseListener() {

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			//if the click is on the play button
			if (isWithinRadius(x,y,(PLAY_X + BTN_SIZE/2), (PLAY_Y + BTN_SIZE/2), BTN_SIZE/2 )) {
				File selectedSong = SongList.getCurrentlySelectedSong();
				//if a song is currently being played and it is not the currently selected song, stop and play new song
				if (_currentSong != null && _currentSong != selectedSong) {
					SongApp.stopSong();
					SongApp.setSong(selectedSong);
					_currentSong = selectedSong;
				}
				else {
					_currentSong = selectedSong;
				}
				SongApp.playSong();
			}
			//if the click is on the pause button, pause the song currently playing
			else if (isWithinRadius(x,y,(PAUSE_X + BTN_SIZE/2), (PAUSE_Y + BTN_SIZE/2), BTN_SIZE/2 )){
				SongApp.stopSong();
			}
			//if the click is on the add button, bring up file chooser and add chosen songs
			else if (isWithinRadius(x,y,(ADD_X + BTN_SIZE/2), (ADD_Y + BTN_SIZE/2), BTN_SIZE/2 )){
				
				//do this in another thread so we don't lag up the visualizer
				new Thread() {
					
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

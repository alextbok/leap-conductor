package frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import javax.swing.JPanel;

import backend.audio.SongApp;

/**
 * JPanel that 
 * @author abok
 */
@SuppressWarnings("serial")
public class SongPanel extends JPanel {

	/*Store this in a variable so our Knobs can access same color*/
	public static final Color BACKGROUND_COLOR = new Color(0,128,135);
	
	/*The song*/
	private SongApp _songApp;
	
	/*Volume control knob*/
	public static final Knob knobVolume = new Knob("Volume",0,0);
	
	/*Speed control knob*/
	public static final Knob knobSpeed = new Knob("Speed",0,0);
	
	/*Keeps track of mouse-down y-coordinates for the MouseDraggedEvent*/
	private HashMap<String, Integer> _mouseDownCoordinates;
	
	/**
	 * Constructor - provides initial setup
	 */
	public SongPanel(SongApp songApp) {

		_songApp = songApp;
		SongApp.playSong();
		
		//add our knobs and make their map
		_mouseDownCoordinates = new HashMap<String, Integer>();
		this.addKnobs();
		
		//get our JPanel to show the way we want
		this.setPreferredSize(new Dimension(GUI.WIDTH,100));
		this.setBackground(BACKGROUND_COLOR);
		this.setVisible(true);
		
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D brush = (Graphics2D) g;
        

        double angleSpeed = Math.toRadians(knobVolume.getAngle());
        brush.rotate(angleSpeed, Knob.WIDTH/2 + 560, Knob.HEIGHT/2 + 5);
        brush.drawImage(Knob.getImage(), 560, 5, null);
        brush.rotate(-angleSpeed, Knob.WIDTH/2 + 560, Knob.HEIGHT/2 + 5);
        
        double angleVol = Math.toRadians(knobSpeed.getAngle());
        brush.rotate(angleVol, Knob.WIDTH/2 + 640, Knob.HEIGHT/2 + 5);
        brush.drawImage(Knob.getImage(), 640, 5, null);
        brush.rotate(-angleVol, Knob.WIDTH/2 + 640, Knob.HEIGHT/2 + 5);
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
	
	private class KnobMouseMotionListener implements MouseMotionListener {
		
		private Knob _knob;
		
		public KnobMouseMotionListener(Knob knob) {
			_knob = knob;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			double rotation = .5*( _mouseDownCoordinates.get(_knob.getText()) - e.getY() );
			_knob.rotateImage(rotation);
			if (_knob.getText().equals("Volume"))
				SongApp.changeVolume(rotation/2);
			else if (_knob.getText().equals("Speed"))
				SongApp.changeSpeed(rotation*.001);
			_mouseDownCoordinates.put(_knob.getText(), e.getY());
			SongPanel.this.repaint();
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {}
	}
    
}
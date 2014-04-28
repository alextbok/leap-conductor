package frontend.soundpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import backend.audio.SongApp;

/**
 * KnobPanel class
 * JPanel that contains a JLabel with text relevant to the instance's function (e.g. "Volume")
 * The knob icon will be painted inside the panel
 * @author abok
 *
 */
@SuppressWarnings("serial")
public class KnobPanel extends JPanel {

	/*Dimension of picture - not equal to dimension of panel (hard-coded because we cropped the picture file to 75x75 pixels)*/
	public static final int WIDTH = 75;
	public static final int HEIGHT = 75;
	
	/*Current angle of image in degrees*/
	private double _angle;
	
	/*Title text (indicative of instance's function) associated with this knob*/
	private String _text;
	
	/*Our knob icon, accessed via SongPanel*/
	private static final Image icon = SongPanel.getIcon("knob");
	
	/*Mouse click y coordinate used on MouseDragged events*/
	private int _previousY = 0;
	
	/**
	 * Constructor
	 */
	public KnobPanel(String s) {
		_angle = 112.0;
		_text = s;
	   	
		//create border and its title font
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(192,193,194), 2, true), _text);
		border.setTitleFont(new Font("Courier", Font.BOLD, 12));
		border.setTitleColor(new Color(135,136,138));
	   	this.setBorder(border);

	   	this.addMouseListener(new KnobMouseListener());
	   	this.addMouseMotionListener(new KnobMouseMotionListener());
	   	
	   	//tweak size and look of panel
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(WIDTH + 15,HEIGHT + 15));
		this.setMaximumSize(new Dimension(WIDTH + 15,HEIGHT + 15));
		this.setBackground(SongPanel.BACKGROUND_COLOR);
		this.setVisible(true);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
        super.paintComponent(g);
        Graphics2D brush = (Graphics2D) g;
        
        //rotate brush around volume knob by its current angle before painting it
        double angleVol = Math.toRadians(_angle);
        brush.rotate(angleVol, KnobPanel.WIDTH/2 + SongPanel.KNOB_X_OFFSET, KnobPanel.HEIGHT/2 + SongPanel.KNOB_Y_OFFSET);
        brush.drawImage(icon, SongPanel.KNOB_X_OFFSET, SongPanel.KNOB_Y_OFFSET, null);
        //rotate brush back
        brush.rotate(-angleVol, KnobPanel.WIDTH/2 + SongPanel.KNOB_X_OFFSET, KnobPanel.HEIGHT/2 + SongPanel.KNOB_Y_OFFSET);
	}
	
	/**
	 * Changes border color to green to show which knob is being manipulated
	 */
	public void select() {
		//create border and its title font
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN, 2, true), _text);
		border.setTitleFont(new Font("Courier", Font.BOLD, 12));
		border.setTitleColor(new Color(135,136,138));
	   	this.setBorder(border);
	}
	
	/**
	 * Changes border color to gray to show which knob is being manipulated
	 */
	public void deselect() {
		//create border and its title font
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(192,193,194), 2, true), _text);
		border.setTitleFont(new Font("Courier", Font.BOLD, 12));
		border.setTitleColor(new Color(135,136,138));
	   	this.setBorder(border);
	}
	
	/*ACCESSORS*/

	/**
	 * Increases the angle by 5.0 degrees
	 * The double will be converted to radians before repainting
	 */
	private void rotateImage(double d) {
		_angle += d;
	     if (_angle >= 360.0)
	       _angle = 0.0;
	}
	
    /**
     * 
     * Private class KnobMouseListener that listens for mouse down events
     * We store the y-coordinate on this event, which will be used on the
     * MouseDragged event in our mouse motion listener
     *
     */
	private class KnobMouseListener implements MouseListener {

		public KnobMouseListener() {

		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			_previousY = e.getY();
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

		public KnobMouseMotionListener() {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			double rotation = 0.5 * (_previousY - e.getY());
			KnobPanel.this.rotateImage(rotation);

			if (_text.equals("Volume"))
				SongApp.changeVolume(rotation / 50);
			else if (_text.equals("Speed"))
				SongApp.changeSpeed(rotation * 0.001);

			_previousY = e.getY();
			KnobPanel.this.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {}
	}
	
	
}

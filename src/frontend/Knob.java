package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Knob class
 * JPanel that contains a JLabel with text relevant to the instance's function (e.g. "Volume")
 * also contains a .png of a knob and methods to load and manipulate (e.g. rotate) it
 * @author abok
 *
 */
@SuppressWarnings("serial")
public class Knob extends JPanel {

	/*Dimension of picture (hard-coded because we cropped the picture file to 75x75 pixels)*/
	public static final int WIDTH = 75;
	public static final int HEIGHT = 75;
	
	/*The image of our knob that will be rotated*/
	private static final Image _image = Knob.makeIcon();
	
	/*Current angle of image in degrees*/
	private double _angle;
	
	/*Label text associated with this knob*/
	private String _text;
	
	/*Position of object - used for rotation*/
	private int _x, _y;
	
	/**
	 * Constructor
	 */
	public Knob(String s, int x, int y) {
		_angle = 0.0;
		_text = s;
		_x = x;
	   	_y = y;
		this.setLayout(new BorderLayout());
		this.add(new JLabel(_text), BorderLayout.SOUTH);
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(new Color(0f,0f,0f,0f)); //transparent color
		this.setVisible(true);
	}
	
	
	/**
	 * Generates and returns a 75x75 pixel knob icon
	 * Returns null and prints an error if there is an IOException
	 * @return Image knob icon
	 */
	public static Image makeIcon() {
		String dir = System.getProperty("user.dir") + "/src/icons/";
		try {
			return ImageIO.read(new File(dir + "knob.png"));
		} catch (IOException e) {
			System.out.println("ERROR: IOException when trying to load image in Knob.makeIcon()");
			return null;
		}
	}
	
	
	
	/*ACCESSORS*/
	
	/**
	 * Provides access to the knob image
	 * @return our image
	 */
	public static Image getImage() {
		if (_image != null)
			return _image;
		return Knob.makeIcon();
	}
	
	/**
	 * Increases the angle by 5.0 degrees
	 * The double will be converted to radians before repainting
	 */
	public void rotateImage(double d) {
		_angle += d;
	     if (_angle >= 360.0)
	       _angle = 0.0;
	}
	
	/**
	 * Provides access to our angle
	 * @return our angle
	 */
	public double getAngle() {
		return _angle;
	}
	
	/**
	 * Provides access to JLabel text
	 * Used for hashing in SongPanel
	 */
	public String getText() {
		return _text;
	}
	
	/**
	 * Provides access to x coordinate
	 */
	public int getPanelX() {
		return _x;
	}

	/**
	 * Provides access to y coordinate
	 */
	public int getPanelY() {
		return _y;
	}

	
}

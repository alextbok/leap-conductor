package frontend.soundpanel;

import hub.SoundController;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import frontend.GUI;


/**
 * Panel that contains our song progress bar (in an inner class)
 * @author abok
 *
 */
@SuppressWarnings("serial")
public class ProgressBarPanel extends JPanel {

	/*Progress bar*/
	private LeapConductorProgressBar _progressBar;
	
	/*Dimensions*/
	public final static int PROGRESS_BAR_HEIGHT = 10;
	public final static int PROGRESS_BAR_WIDTH = GUI.WIDTH - 130;
	public final static int PANEL_HEIGHT = 20;
	
	/*Label that shows our progress as a fraction*/
	private JLabel _progressLabel;
	
	/**
	 * Constructor
	 */
	public ProgressBarPanel() { 
		_progressBar = new LeapConductorProgressBar();
		new Thread(_progressBar).start();
		this.add(_progressBar);
		
		_progressLabel = new JLabel();
		_progressLabel.setFont(new Font("Courier", Font.BOLD, 12));
		this.add(_progressLabel);
		
		this.addMouseListener(new ProgressBarMouseListener());
		
		this.setBackground(SongPanel.BACKGROUND_COLOR);
		this.setPreferredSize(new Dimension(GUI.WIDTH, PANEL_HEIGHT));
	}
	
	/**
	 * Converts input millisecond to number of minutes and seconds
	 * @return String milliseconds in "mm:ss" time
	 */
	private String millisecondsToStr(int ms) {
		double minutes = ms/60000.0;
		double seconds = ( ( minutes - Math.floor(minutes) ) * 60);
		
		String strMin = Integer.toString( (int) Math.floor(minutes) );
		String strSec = Integer.toString((int) seconds);
		
		if (seconds < 10)
			strSec = "0" + strSec;
		
		return strMin + ":" + strSec;
	}
	
	/**
	 * Private custom JProgressBar that updates in its own thread
	 * It constantly queries the current song time and the song duration from the SongApp class
	 * @author abok
	 */
	private class LeapConductorProgressBar extends JProgressBar implements Runnable {
		
		public LeapConductorProgressBar() {
			this.setPreferredSize(new Dimension(PROGRESS_BAR_WIDTH,PROGRESS_BAR_HEIGHT));
		}
		
		/**
		 * Helps the progress bar resize with the frame
		 */
		@Override
		protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        this.setPreferredSize(new Dimension(ProgressBarPanel.this.getWidth() - 130,PROGRESS_BAR_HEIGHT));
		}
		
		/**
		 * Repaints the progress bar with the new progress (as well as the label)
		 */
		@Override
		public void run() {
			while (true) {
				int totalTime = SoundController.getTotalDuration();
				int currentTime = SoundController.getCurrentTime();
						
				this.setMinimum(0);
				this.setMaximum(totalTime);
				this.setValue(currentTime);
				_progressLabel.setText(millisecondsToStr(currentTime) + " / " + millisecondsToStr(totalTime));
			}		
		}

	}
	
	/**
	 * Private custom mouse listener that listens for clicks and provides a song buffer
	 * @author abok
	 *
	 */
	private class ProgressBarMouseListener implements MouseListener {

		/**
		 * Get the mouse x position relative to the progress bar width and seek to that position in the song
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			double percent_complete = (e.getX() - _progressBar.getX())/(_progressBar.getWidth() + 0.0)*100;
			double milliseconds = percent_complete*SoundController.getTotalDuration()/100;
			if (percent_complete < 100.0 && percent_complete > 0.0)
				SoundController.seekTo(milliseconds);
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
}

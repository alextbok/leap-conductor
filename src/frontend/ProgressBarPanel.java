package frontend;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import backend.audio.SongApp;


/**
 * Panel that contains our song progress bar
 * @author abok
 *
 */
@SuppressWarnings("serial")
public class ProgressBarPanel extends JPanel {

	/*Progress bar*/
	private static LeapConductorProgressBar _progressBar;
	
	/*Dimensions*/
	public final static int PROGRESS_BAR_HEIGHT = 10;
	public final static int PROGRESS_BAR_WIDTH = GUI.WIDTH - 60;
	public final static int PANEL_HEIGHT = 20;
	
	/**
	 * Constructor
	 */
	public ProgressBarPanel() { 
		_progressBar = new LeapConductorProgressBar();
		new Thread(_progressBar).start();
		this.add(_progressBar);
		this.setBackground(SongPanel.BACKGROUND_COLOR);
		this.setPreferredSize(new Dimension(GUI.WIDTH, PANEL_HEIGHT));
	}

	public class LeapConductorProgressBar extends JProgressBar implements Runnable {
		
		public LeapConductorProgressBar() {
			this.setPreferredSize(new Dimension(PROGRESS_BAR_WIDTH,PROGRESS_BAR_HEIGHT));
		}
		
		/**
		 * Repaints the progress bar with the new progress
		 */
		@Override
		public void run() {
			while (true) {
				this.setMinimum(0);
				this.setMaximum(SongApp.getTotalDuration());
				this.setValue(SongApp.getCurrentTime());
			}		
		}

	}
	
}

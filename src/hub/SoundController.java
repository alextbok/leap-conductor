package hub;

/**
 * SoundController
 * a static wrapper class for SongApp
 */

import java.io.File;

import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import backend.audio.SongApp;
import frontend.soundpanel.KnobPanel;
import frontend.soundpanel.KnobType;
import frontend.soundpanel.SongPanel;

/**
 * This class is used to control the current song playing statically. It functions largely as a wrapper for SongApp,
 * but with some added functionality so that it also controls the knobs
 * @author Sawyer
 *
 */
public class SoundController {
	
	private static SongApp app;
	private static KnobPanel volumeKnob = SongPanel.getKnobPanel(KnobType.VOLUME);
	private static KnobPanel speedKnob = SongPanel.getKnobPanel(KnobType.SPEED);
	private static KnobPanel lowKnob = SongPanel.getKnobPanel(KnobType.LOW);
	private static KnobPanel midKnob = SongPanel.getKnobPanel(KnobType.MID);
	private static KnobPanel highKnob = SongPanel.getKnobPanel(KnobType.HIGH);
	private static KnobPanel selected = null;
	private final static double maxSpeed = 2;
	private final static double minSpeed = 0;
	private final static double maxVolume = 1;
	private final static double minVolume = 0;
	private final static double minSeg = -24;
	private final static double maxSeg = 12;
	
	//private final static double minPart;
	
	public SoundController(SongApp app) {
		SoundController.app = app;
		resetValues();
	}
	
	public static void resetValues() {
		if(app != null) {
			app.resetValues();
			updateKnob(volumeKnob, app.getVolume(), minVolume, maxVolume);
			updateKnob(lowKnob, 0.0, minSeg, maxSeg);
			updateKnob(midKnob, 0.0, minSeg, maxSeg);
			updateKnob(highKnob, 0.0, minSeg, maxSeg);
			updateKnob(speedKnob, 1.0, minSpeed, maxSpeed);
		}
	}
	
	public static void playSong() {
		if(app != null) {
			app.playSong();
		}
	}
	
	public static void stopSong() {
		if(app != null) {
			app.stopSong();
		}
	}
	
	/**
	 * The methods below increase, decrease and set the speed, volume, bass, mid, and high values of the current song playing
	 */
	
	public static void speedUpSong() {
		if(app != null && app.getRate() < maxSpeed) {
			app.speedUpSong();			
			updateKnob(speedKnob, app.getRate(), minSpeed, maxSpeed);			
		}
		updateSelection(speedKnob);
	}
	
	public static void slowDownSong() {
		if(app != null && app.getRate() > minSpeed) {
			app.slowDownSong();
			updateKnob(speedKnob, app.getRate(), minSpeed, maxSpeed);
		}
		updateSelection(speedKnob);
	}
	
	public static void volumeUp() {
		if(app != null && app.getVolume() < maxVolume) {
			app.volumeUp();
			updateKnob(volumeKnob, app.getVolume(), minVolume, maxVolume);
		}
		updateSelection(volumeKnob);
	}
	
	public static void volumeDown() {
		if(app != null && app.getVolume() > minVolume) {
			app.volumeDown();
			updateKnob(volumeKnob, app.getVolume(), minVolume, maxVolume);			
		}
		updateSelection(volumeKnob);
	}
	
	public static void changeVolume(double d) {
		if(app != null && app.getVolume() + d < maxVolume && app.getVolume() + d > minVolume) {
			app.changeVolume(d);
			updateKnob(volumeKnob, app.getVolume(), minVolume, maxVolume);
		}
		updateSelection(volumeKnob);
	}
	
	public static void changeSpeed(double d) {
		if(app != null && app.getRate() + d < maxSpeed && app.getRate() + d > minSpeed) {
			app.changeSpeed(d);			
			updateKnob(speedKnob, app.getRate(), minSpeed, maxSpeed);
		}
		updateSelection(speedKnob);
	}
	
	public static void changeLow(double d) {
		if(app != null && app.getLows() + d < maxSeg && app.getLows() + d > minSeg) {
			app.changeBass(d);	
			updateKnob(lowKnob, app.getLows(), minSeg, maxSeg);
		}
		updateSelection(lowKnob);
	}
	
	public static void changeMid(double d) {
		if(app != null && app.getMids() + d < maxSeg && app.getMids() + d > minSeg) {
			app.changeMid(d);			
			updateKnob(midKnob, app.getMids(), minSeg, maxSeg);
		}
		updateSelection(midKnob);
	}
	
	public static void changeHigh(double d) {
		if(app != null && app.getHighs() + d < maxSeg && app.getHighs() + d > minSeg) {
			app.changeHigh(d);			
			updateKnob(highKnob, app.getHighs(), minSeg, maxSeg);
		}
		updateSelection(highKnob);
	}

	public static void raiseBass() {
		if(app != null && app.getLows() < maxSeg) {
			app.raiseBass();
			updateKnob(lowKnob, app.getLows(), minSeg, maxSeg);
		}
		updateSelection(lowKnob);
	}
	
	public static void lowerBass() {
		if(app != null && app.getLows() > minSeg) {
			app.lowerBass();
			updateKnob(lowKnob, app.getLows(), minSeg, maxSeg);
		}
		updateSelection(lowKnob);
	}
	
	public static void raiseMid() {
		if(app != null && app.getMids() < maxSeg) {
			app.raiseMid();
			updateKnob(midKnob, app.getMids(), minSeg, maxSeg);
		}
		updateSelection(midKnob);
	}
	
	public static void lowerMid() {
		if(app != null && app.getMids() > minSeg) {
			app.lowerMid();
			updateKnob(midKnob, app.getMids(), minSeg, maxSeg);
		}
		updateSelection(midKnob);
	}
	
	public static void raiseHigh() {
		if(app != null && app.getHighs() < maxSeg) {
			app.raiseHigh();
			updateKnob(highKnob, app.getHighs(), minSeg, maxSeg);
		}
		updateSelection(highKnob);
	}
	
	public static void lowerHigh() {
		if(app != null && app.getHighs() > minSeg) {
			app.lowerHigh();
			updateKnob(highKnob, app.getHighs(), minSeg, maxSeg);
		}
		updateSelection(highKnob);
	}
	
	private static void updateKnob(KnobPanel knob, double val, double min, double max) {
		double rot = 224 * (min - val) / (min - max);
		knob.setRotation(rot);
		knob.updateUI();
	}

    public static MediaPlayer getMediaPlayer() {
        return app.getMediaPlayer();
    }

    public static double getVolume() {
        if(app != null) return app.getVolume();
        return 0;
    }

	public static int getTotalDuration() {
		if(app != null) return app.getTotalDuration();
		return 0;
	}
	
	public static int getCurrentTime() {
		if(app != null) return app.getCurrentTime();
		return 0;
	}
	
	public static void seekTo(double ms) {
		if(app != null) app.seekTo(ms);
	}
	
	public static void setSong(File file) {
		if(app != null) app.setSong(file);
	}
	
	public static void setAudioSpectrumListener(AudioSpectrumListener asl) {
		if(app != null) app.setAudioSpectrumListener(asl);
	}
	
	public static double getRate() {
		if(app != null) return app.getRate();
		return 0.0;
	}

    public static double getLows() {
        if(app != null) return app.getLows();
        return 0.0;
    }

    public static double getMids() {
        if(app != null) return app.getMids();
        return 0.0;
    }

    public static double getHighs() {
        if(app != null) return app.getHighs();
        return 0.0;
    }

	public static void updateSelection(KnobPanel curr) {
		if(selected != null) selected.deselect();
		curr.select();
		selected = curr;
	}

}

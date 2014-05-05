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
import frontend.soundpanel.SongList;
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
	private final static double minBand = -24;
	private final static double maxBand = 12;
	
	/**
	 * Instantiates the SoundController with the app the is actually playing music
	 * @param app
	 */
	public SoundController(SongApp app) {
		SoundController.app = app;
		resetValues();
	}
	
	/**
	 * Resets all values of the song to default, and sets the knobs to match
	 */
	public static void resetValues() {
		if(app != null) {
			app.resetValues();
			updateKnob(volumeKnob, app.getVolume(), minVolume, maxVolume);
			updateKnob(lowKnob, 0.0, minBand, maxBand);
			updateKnob(midKnob, 0.0, minBand, maxBand);
			updateKnob(highKnob, 0.0, minBand, maxBand);
			updateKnob(speedKnob, 1.0, minSpeed, maxSpeed);
		}
	}
	
	/**
	 * Starts the song. Will do nothing if the song is already playing
	 */
	public static void playSong() {
		if(app != null) {
			app.playSong();
		}
	}
	
	/**
	 * Pauses the song. Will do nothing if the song is already playing
	 */
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

    public static void mute() {
        if(app != null) app.getMediaPlayer().setMute(true);
    }

    public static void unmute() {
        if (app != null) app.getMediaPlayer().setMute(false);
    }

    public static boolean isMute() {
        if (app != null) return app.getMediaPlayer().isMute();
        else return false;
    }

	public static void changeSpeed(double d) {
		if(app != null && app.getRate() + d < maxSpeed && app.getRate() + d > minSpeed) {
			app.changeSpeed(d);			
			updateKnob(speedKnob, app.getRate(), minSpeed, maxSpeed);
		}
		updateSelection(speedKnob);
	}
	
	public static void changeLow(double d) {
		if(app != null && app.getLows() + d < maxBand && app.getLows() + d > minBand) {
			app.changeBass(d);	
			updateKnob(lowKnob, app.getLows(), minBand, maxBand);
		}
		updateSelection(lowKnob);
	}
	
	public static void changeMid(double d) {
		if(app != null && app.getMids() + d < maxBand && app.getMids() + d > minBand) {
			app.changeMid(d);			
			updateKnob(midKnob, app.getMids(), minBand, maxBand);
		}
		updateSelection(midKnob);
	}
	
	public static void changeHigh(double d) {
		if(app != null && app.getHighs() + d < maxBand && app.getHighs() + d > minBand) {
			app.changeHigh(d);			
			updateKnob(highKnob, app.getHighs(), minBand, maxBand);
		}
		updateSelection(highKnob);
	}

	public static void raiseBass() {
		if(app != null && app.getLows() < maxBand) {
			app.raiseBass();
			updateKnob(lowKnob, app.getLows(), minBand, maxBand);
		}
		updateSelection(lowKnob);
	}
	
	public static void lowerBass() {
		if(app != null && app.getLows() > minBand) {
			app.lowerBass();
			updateKnob(lowKnob, app.getLows(), minBand, maxBand);
		}
		updateSelection(lowKnob);
	}
	
	public static void raiseMid() {
		if(app != null && app.getMids() < maxBand) {
			app.raiseMid();
			updateKnob(midKnob, app.getMids(), minBand, maxBand);
		}
		updateSelection(midKnob);
	}
	
	public static void lowerMid() {
		if(app != null && app.getMids() > minBand) {
			app.lowerMid();
			updateKnob(midKnob, app.getMids(), minBand, maxBand);
		}
		updateSelection(midKnob);
	}
	
	public static void raiseHigh() {
		if(app != null && app.getHighs() < maxBand) {
			app.raiseHigh();
			updateKnob(highKnob, app.getHighs(), minBand, maxBand);
		}
		updateSelection(highKnob);
	}
	
	public static void lowerHigh() {
		if(app != null && app.getHighs() > minBand) {
			app.lowerHigh();
			updateKnob(highKnob, app.getHighs(), minBand, maxBand);
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

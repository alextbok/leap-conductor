package hub;

import java.io.File;

import javafx.scene.media.AudioSpectrumListener;
import backend.audio.SongApp;
import frontend.soundpanel.KnobPanel;
import frontend.soundpanel.KnobType;
import frontend.soundpanel.SongPanel;

public class SoundController {
	
	private static SongApp app;
	private static KnobPanel volumeKnob = SongPanel.getKnobPanel(KnobType.VOLUME);
	private static KnobPanel speedKnob = SongPanel.getKnobPanel(KnobType.SPEED);
	private static KnobPanel lowKnob = SongPanel.getKnobPanel(KnobType.LOW);
	private static KnobPanel midKnob = SongPanel.getKnobPanel(KnobType.MID);
	private static KnobPanel highKnob = SongPanel.getKnobPanel(KnobType.HIGH);
	private static KnobPanel selected = null;
	private final static double scale = 10d;
	
	public SoundController(SongApp app) {
		SoundController.app = app;
	}
	
	public static void resetValues() {
		if(app != null) {
			app.resetValues();
		}
		//reset knobs
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
	
	public static void speedUpSong() {
		if(app != null) {
			Double oldRate = app.getRate();
			app.speedUpSong();
			Double newRate = app.getRate();
			
			Double rateChange = newRate - oldRate;
			
			speedKnob.rotateImage(rateChange * scale);
		}
		updateSelection(speedKnob);
	}
	
	public static void slowDownSong() {
		if(app != null) {
			Double oldRate = app.getRate();
			app.slowDownSong();
			Double newRate = app.getRate();
			
			Double rateChange = newRate - oldRate;
			
			speedKnob.rotateImage(rateChange * scale);
		}
		updateSelection(speedKnob);
	}
	
	public static void volumeUp() {
		if(app != null) {
			Double oldVolume = app.getVolume();
			app.volumeUp();
			Double newVolume = app.getVolume();
			
			Double volumeChange = newVolume - oldVolume;
			
			volumeKnob.rotateImage(volumeChange * scale);
		}
		updateSelection(volumeKnob);
	}
	
	public static void volumeDown() {
		if(app != null) {
			Double oldVolume = app.getVolume();
			app.volumeDown();
			Double newVolume = app.getVolume();
			
			Double volumeChange = newVolume - oldVolume;
			
			volumeKnob.rotateImage(volumeChange * scale);
		}
		updateSelection(volumeKnob);
	}
	
	public static void changeVolume(double d) {
		if(app != null) {
			Double oldVolume = app.getVolume();
			app.changeVolume(d);
			Double newVolume = app.getVolume();
			
			Double volumeChange = newVolume - oldVolume;
			
			volumeKnob.rotateImage(volumeChange * scale);
		}
		updateSelection(volumeKnob);
	}
	
	public static void changeSpeed(double d) {
		if(app != null) {
			Double oldRate = app.getRate();
			app.changeSpeed(d);
			Double newRate = app.getRate();
			
			Double rateChange = newRate - oldRate;
			
			speedKnob.rotateImage(rateChange * scale);
		}
		updateSelection(speedKnob);
	}
	
	public static void raiseBass() {
		if(app != null) {
			Double oldVal = app.getLows();
			app.raiseBass();
			Double newVal = app.getLows();
			
			Double change = newVal - oldVal;
			
			lowKnob.rotateImage(change * scale);
		}
		updateSelection(lowKnob);
	}
	
	public static void lowerBass() {
		if(app != null) {
			Double oldVal = app.getLows();
			app.lowerBass();
			Double newVal = app.getLows();
			
			Double change = newVal - oldVal;
			
			lowKnob.rotateImage(change * scale);
		}
		updateSelection(lowKnob);
	}
	
	public static void raiseMid() {
		if(app != null) {
			Double oldVal = app.getMids();
			app.raiseMid();;
			Double newVal = app.getMids();
			
			Double change = newVal - oldVal;
			
			midKnob.rotateImage(change * scale);
		}
		updateSelection(midKnob);
	}
	
	public static void lowerMid() {
		if(app != null) {
			Double oldVal = app.getMids();
			app.lowerMid();;
			Double newVal = app.getMids();
			
			Double change = newVal - oldVal;
			
			midKnob.rotateImage(change * scale);
		}
		updateSelection(midKnob);
	}
	
	public static void raiseHigh() {
		if(app != null) {
			Double oldVal = app.getHighs();
			app.raiseHigh();;
			Double newVal = app.getHighs();
			
			Double change = newVal - oldVal;
			
			highKnob.rotateImage(change * scale);
		}
		updateSelection(highKnob);
	}
	
	public static void lowerHigh() {
		if(app != null) {
			Double oldVal = app.getHighs();
			app.lowerHigh();;
			Double newVal = app.getHighs();
			
			Double change = newVal - oldVal;
			
			highKnob.rotateImage(change * scale);
		}
		updateSelection(highKnob);
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
		
	private static void updateSelection(KnobPanel curr) {
		if(selected != null) selected.deselect();
		curr.select();
		selected = curr;
	}

}

package backend.audio;

/**
 * SongApp
 */

import java.io.File;

import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import org.jaudiotagger.audio.AudioFile;

import backend.FileProcessor;
import backend.speech.SongsBySpeech;

public class SongApp {
	private  Media _media;
	private  MediaPlayer _mediaPlayer;
	private static String _url;
	
	public SongApp(String url) {
		new JFXPanel();
		_url = url;
    	try {
    		_media = new Media(new File(_url).toURI().toString());
    		_mediaPlayer = new MediaPlayer(_media);
    	} catch (MediaException e) {
    		System.out.println("ERROR: No such file or directory " + url);
    	}

        (new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (_mediaPlayer.getCurrentRate() > 0)
                        continue;

                    try {
                    	String pathToItunes = FileProcessor.getFolderWithMostMusicFiles().getAbsolutePath();
                        SongsBySpeech speech = new SongsBySpeech(pathToItunes);
                        AudioFile newSong = speech.speechCommand();
                        if (newSong != null) {
                            setSong(newSong.getFile());
                            playSong();
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }).start();
	}
	
	
	/**
	 * resets the speed/volume/band gains 
	 */
	public void resetValues() {
		if (_mediaPlayer != null){
			_mediaPlayer.setRate(1.0);
			_mediaPlayer.setVolume(1.0); // this is full volumes, the default of MediaPlayer
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 0; i < 10; i ++){
				bands.get(i).setGain(0.0);
			}
			
		}
	}
	
	
	/**
	 * plays the song and creates a new progress bar that runs in its own thread
	 */
	public void playSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.play();
	}
	
	/**
	 * stops the song
	 */
	public void stopSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.pause();
	}
	
	/**
	 * speeds up the song
	 */
	public void speedUpSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.setRate(_mediaPlayer.getRate() + 0.01);
	}
	
	/**
	 * slows down the song
	 */
	public void slowDownSong() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setRate(_mediaPlayer.getRate() - 0.01);
		}
	}

	/**
	 * increases the volume
	 */
	public void volumeUp() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() + 0.01);
		}
	}
	
	/**
	 * decreases the volume
	 */
	public void volumeDown() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() - 0.01);
		}
	}
	
	/**
	 * changes volume by d
	 */
	public void changeVolume(double d) {
		if (_mediaPlayer != null)
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() + d);
	}
	
	/**
	 * changes speed by d
	 */
	public void changeSpeed(double d) {
		if (_mediaPlayer != null)
			_mediaPlayer.setRate(_mediaPlayer.getRate() + d);
	}
	
	/**
	 * raises gain on bass frequencies
	 */
	public void raiseBass() {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 0; i < 3; i ++){
				bands.get(i).setGain(bands.get(i).getGain() + .5);
			}
		}
	}
	
	/**
	 * lowers gain on bass frequencies
	 */
	public void lowerBass() {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 0; i < 3; i ++){
				bands.get(i).setGain(bands.get(i).getGain() - 0.5);
			}
		}
	}
	
	public void changeBass(double d) {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 0; i < 3; i ++){
				bands.get(i).setGain(bands.get(i).getGain() + d);
			}
		}
	}
	
	/**
	 * raises gain on mid frequencies
	 */
	public void raiseMid() {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 3; i < 7; i ++){
				bands.get(i).setGain(bands.get(i).getGain() + 0.5);
			}
		}
	}
	
	
	/**
	 * lowers gain on mid frequencies
	 */
	public void lowerMid() {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 3; i < 7; i ++){
				bands.get(i).setGain(bands.get(i).getGain() - 0.5);
			}
		}
	}
	
	public void changeMid(double d) {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 3; i < 7; i ++){
				bands.get(i).setGain(bands.get(i).getGain() + d);
			}
		}
	}
	
	/**
	 * raises gain on high frequencies
	 */
	public void raiseHigh() {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 7; i < 10; i ++){
				bands.get(i).setGain(bands.get(i).getGain() + 0.5);
			}
		}
	}
	
	/**
	 * lowers gain on high frequencies
	 */
	public void lowerHigh() {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 7; i < 10; i ++){
				bands.get(i).setGain(bands.get(i).getGain() - 0.5);
			}
		}
	}
	
	public void changeHigh(double d) {
		if (_mediaPlayer != null){
			AudioEqualizer eq = _mediaPlayer.getAudioEqualizer();
			ObservableList<EqualizerBand> bands = eq.getBands();
			for(int i = 7; i < 10; i ++){
				bands.get(i).setGain(bands.get(i).getGain() + d);
			}
		}
	}

    /**
     * getMediaPlayer
     * @return _mediaPlayer
     */
    public MediaPlayer getMediaPlayer() {
        return _mediaPlayer;
    }

	/**
	 * Returns the total time of the current song as the number of milliseconds
	 * @return
	 */
	public int getTotalDuration() {
		return (int) _mediaPlayer.getTotalDuration().toMillis();
	}

	/**
	 * Returns the current time of the current song in milliseconds
	 * @return
	 */
	public int getCurrentTime() {
		return (int) _mediaPlayer.getCurrentTime().toMillis();
	}
	
	/**
	 * Wrapper for seek method. Seeks to input milliseconds (double)
	 * @param ms
	 */
	public void seekTo(double ms){
		Duration duration = new Duration(ms);
		_mediaPlayer.seek(duration);
	}
	
    /**
     * Wrapper for mediaplayer
     * Provides public access to the rate of the song
     * @return media
     */
    public double getRate() {
        return _mediaPlayer.getRate();
    }
    
    /**
     * Provides public access to the volume of the song
     * @return
     */
    public double getVolume() {
    	return _mediaPlayer.getVolume();
    }
    
    public double getLows() {
    	return _mediaPlayer.getAudioEqualizer().getBands().get(0).getGain();
    }
    
    public double getMids() {
    	return _mediaPlayer.getAudioEqualizer().getBands().get(5).getGain();
    }
    
    public double getHighs() {
    	return _mediaPlayer.getAudioEqualizer().getBands().get(9).getGain();
    }
    
    /**
     * Wrapper for mediaplayer
     * Sets the audio spectrum listener
     * @param asl
     */
    public void setAudioSpectrumListener(AudioSpectrumListener asl) {
    	_mediaPlayer.setAudioSpectrumListener(asl);
    }
    
    /**
     * Sets the song to be played
     */
    public void setSong(File file) {
    	try {
    		_media = new Media(file.toURI().toString());
    		_mediaPlayer = new MediaPlayer(_media);
    	} catch (MediaException e) {
    		System.out.println("ERROR: No such file or directory " + file.getAbsolutePath());
    	}
    }
}

package backend.audio;

/**
 * SongApp
 */

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SongApp {
	private static Media _media;
	private static MediaPlayer _mediaPlayer;
	private static String _url;
	
	public SongApp(String url) {
		new JFXPanel();
		_url = url;
		_media = new Media(new File(_url).toURI().toString());
		_mediaPlayer = new MediaPlayer(_media);
	}
	
	/**
	 * plays the song
	 */
	public static void playSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.play();
	}
	
	/**
	 * stops the song
	 */
	public static void stopSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.pause();
	}
	
	/**
	 * speeds up the song
	 */
	public static void speedUpSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.setRate(_mediaPlayer.getRate() + 0.003);
	}
	
	/**
	 * slows down the song
	 */
	public static void slowDownSong() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setRate(_mediaPlayer.getRate() - 0.003);
		}
	}

	/**
	 * increases the volume
	 */
	public static void volumeUp() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() + 0.01);
			if(_mediaPlayer.getVolume() > 1.0) _mediaPlayer.setVolume(1.0);
		}
	}
	
	/**
	 * decreases the volume
	 */
	public static void volumeDown() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() - 0.01);
			if(_mediaPlayer.getVolume() < 0.0) _mediaPlayer.setVolume(0.0);
		}
	}
	
	/**
	 * changes volume by d
	 */
	public static void changeVolume(double d) {
		if (_mediaPlayer != null)
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() + d);
	}
	
	/**
	 * changes speed by d
	 */
	public static void changeSpeed(double d) {
		if (_mediaPlayer != null)
			_mediaPlayer.setRate(_mediaPlayer.getRate() + d);
	}

    /**
     * getMedia
     * @return media
     */
    public MediaPlayer getMediaPlayer() {
        return _mediaPlayer;
    }
    
    /**
     * Sets the song to be played
     */
    public static void setSong(File file) {
		_media = new Media(file.toURI().toString());
		_mediaPlayer = new MediaPlayer(_media);
    }
}

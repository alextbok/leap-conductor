package backend.audio;

import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFrame;

import frontend.SongPanel;
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
	
	/*SONG EFFECTS*/
	
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
			_mediaPlayer.setRate(_mediaPlayer.getRate() + .5);
	}
	
	/**
	 * slows down the song
	 */
	public static void slowDownSong() {
		if (_mediaPlayer != null)
			_mediaPlayer.setRate(_mediaPlayer.getRate() - .5);
	}

	/**
	 * increases the volume
	 */
	public static void volumeUp() {
		if (_mediaPlayer != null)
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() + 0.1);
	}
	
	/**
	 * decreases the volume
	 */
	public static void volumeDown() {
		if (_mediaPlayer != null)
			_mediaPlayer.setVolume(_mediaPlayer.getVolume() - 0.1);
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
	
	
	
	public static void main(String[] args) throws MalformedURLException{
		new SongApp(args[0]);
		SongApp.playSong();
	}
	
	
}

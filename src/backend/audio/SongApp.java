package backend.audio;

/**
 * SongApp
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.id3.ID3v1_1;
import org.farng.mp3.id3.ID3v2_2;
import org.farng.mp3.id3.ID3v2_3;
import org.farng.mp3.id3.ID3v2_4;

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
		
////////////////
		getMediaData(url);
///////////////
	}
	
/////////

/*
	private void getMediaData(String url) {
		
		File song =  new File(url);
		try {
			MP3File mp3file = new MP3File(song);
		    //ID3v1_1 tag = new ID3v1_1(new RandomAccessFile(url,"r"));
		    //ID3v1 tag = new ID3v1(new RandomAccessFile(url,"r"));
		    //ID3v2_4 tag = new ID3v2_4(new RandomAccessFile(url,"r"));
		    //ID3v2_3 tag = new ID3v2_3(new RandomAccessFile(url,"r"));
		    //ID3v2_2 tag = new ID3v2_2(new RandomAccessFile(url,"r"));
		    System.out.println(mp3file.hasID3v1Tag());
		    System.out.println(mp3file.hasID3v2Tag());
		    System.out.println("|" + mp3file.getID3v2Tag() + "|");
		    System.out.println("|" + mp3file.getLengthInSeconds() + "|");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			e.printStackTrace();
		}

		
	}
	*/

	private void getMediaData(String url) {
		try {
			File song =  new File(url);
			FileInputStream input = new FileInputStream(song);
		    byte[] bytesSong = new byte[1024];
		    //input.skip((int)song.length() - 128);
		    input.read(bytesSong);
		    String id3 = new String(bytesSong);
		    System.out.println(id3);
		    /*
		    String tag = id3.substring(0, 3);
		    System.out.println("|" + id3 + "|");
		    if (tag.equals("TAG")) {
		    	System.out.println("Title: " + id3.substring(3, 32));
		        System.out.println("Artist: " + id3.substring(33, 62));
		        System.out.println("Album: " + id3.substring(63, 91));
		        System.out.println("Year: " + id3.substring(93, 97));
		    }
		    */
		    input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

////////
	
	
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
			_mediaPlayer.setRate(_mediaPlayer.getRate() + 0.01);
	}
	
	/**
	 * slows down the song
	 */
	public static void slowDownSong() {
		if (_mediaPlayer != null) {
			_mediaPlayer.setRate(_mediaPlayer.getRate() - 0.01);
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
}

package backend.audio.recording;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Class used to record audio and save it as a WAV file
 * @author Sawyer
 *
 */
public class MyRecorder {
	
	private AudioFormat audioFormat;
	private TargetDataLine recording;
	
	/**
	 * Begins the recording. Creates a new thread to listen to audio input and write it to a file
	 */
	public void startRecording() {
		try {
			audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			recording = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			
			new RecordThread().start();
			
		} catch (LineUnavailableException e) {
			System.err.println("ERROR: There was a problem accessing the microphone");
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the recording, leaving a wav file containing the recording's audio
	 */
	public void stopRecording() {
		recording.stop();
		recording.close();
	}
	
	/**
	 * Thread used to listen to audio input and write it
	 * @author Sawyer
	 *
	 */
	private class RecordThread extends Thread {
		
		@Override
		public void run() {
			try {
				recording.open(audioFormat);
				recording.start();
				
				AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
				File file = new File("src/data/vocals.wav"); //we'll have to decide where to save the audio
				
				AudioSystem.write(new AudioInputStream(recording), fileType, file);
			}
			catch(LineUnavailableException | IOException e) {
				System.err.println("ERROR: There was a problem accessing the microphone");
				e.printStackTrace();
			}
		}
	}
}

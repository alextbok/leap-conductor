package recording.copy;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class MyRecorder {
	
	private TargetDataLine recording;
	
	public void startRecording() {
		try {
			AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true, false);
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			recording = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			
			recording.open(audioFormat);
			recording.start();
			
			AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
			File file = new File("vocals.wav");
			
			AudioSystem.write(new AudioInputStream(recording), fileType, file);
			
		} catch (LineUnavailableException | IOException e) {
			System.err.println("ERROR: There was a problem accessing the microphone");
			e.printStackTrace();
		}
	}
	
	public void stopRecording() {
		recording.stop();
		recording.close();
	}

}

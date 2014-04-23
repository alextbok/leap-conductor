package backend.audio.recording;

public class RecordingTest {

	public static void main(String[] args) {
		MyRecorder rec = new MyRecorder();
		rec.startRecording();
		long time = System.currentTimeMillis();
		System.out.println("Starting 3-second recording");
		while(System.currentTimeMillis() < time + 3000) {
		}
		System.out.println("Recording complete");
		rec.stopRecording();
	}

}

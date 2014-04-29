package backend.speech;

/**
 * SongsBySpeech
 * @author Arun Varma
 * gets songs in user's library according to speech commands
 */

import javax.sound.sampled.*;
import com.darkprograms.speech.microphone.*;
import com.darkprograms.speech.recognizer.*;
import org.jaudiotagger.audio.*;
import java.util.regex.*;

public class SongsBySpeech {
  private SongDirectory songDirectory;
  private MicrophoneAnalyzer mic;
  private Pattern playSong;
  private String file;
  private final int THRESHOLD = 40;

  /**
   * SongsBySpeech
   * @param path
   */
  public SongsBySpeech(String path) {
    songDirectory = new SongDirectory(path);

    // get audio from mic, save to wave file
    mic = new MicrophoneAnalyzer(AudioFileFormat.Type.WAVE);
    file = "src/backend/speech/voice.wav";

    // regex pattern matching
    playSong = Pattern.compile("play (.*)");
  }

  /**
   * findSong
   * @return the audio file corresponding to the user's voice command
   */
  public AudioFile speechCommand() throws Exception {
    // open connection with microphone
    mic.open();

    // wait until user speaks; then collect their speech
    int volume;
    while ((volume = mic.getAudioVolume()) < THRESHOLD) {}
    mic.captureAudioToFile(file);
    while (mic.getAudioVolume() > volume - 2) {}
    mic.close();

    // recognize spoken audio
    Recognizer recognizer = new Recognizer(Recognizer.Languages.ENGLISH_US);
    GoogleResponse response = recognizer.getRecognizedDataForWave(file);
    System.out.println(response.getResponse());

    // match output to regex
    String responseStr = response.getResponse().toString();
    Matcher m = playSong.matcher(responseStr);
    if (m.find()) {
      return songDirectory.getSong(m.group(1));
    }

    return songDirectory.getSong(responseStr);
  }
}

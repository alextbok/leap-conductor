package backend.speech;

/**
 * SongsBySpeech
 * @author Arun Varma
 * gets songs in user's library according to speech commands
 */

import frontend.audiovisualizer.*;
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
    mic.captureAudioToFile(file);
    Thread.sleep(3000);
    mic.close();

    // recognize spoken audio
    Recognizer recognizer = new Recognizer(Recognizer.Languages.ENGLISH_US);
    try {
      GoogleResponse response = recognizer.getRecognizedDataForWave(file);

      // match output to regex
      String responseStr = response.getResponse().toString();
      Matcher m = playSong.matcher(responseStr);
      if (m.find()) {
          final String toSet = m.group(1);
          (new Thread() {
            public void run() {
              VisualizerPanel.overlayText = toSet;
              try {
                Thread.sleep(3000);
              } catch (Exception e) {}

              VisualizerPanel.overlayText = "";
            }
          }).start();

        return songDirectory.getSong(toSet);
      }

      return null;
    } catch (Exception e) {
      return null;
    }
  }
}

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
import org.jaudiotagger.tag.FieldKey;

import java.util.regex.*;
import java.util.*;

public class SongsBySpeech {
  private SongDirectory songDirectory;
  private MicrophoneAnalyzer mic;
  private Pattern playSong;
  private String file;
  private final int THRESHOLD = 40;

  /**
   * SongsBySpeech
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
   *
   */
  public List<AudioFile> speechCommand() throws Exception {
    ArrayList<AudioFile> toReturn = new ArrayList<>();
    mic.open();

    int volume;
    while ((volume = mic.getAudioVolume()) < THRESHOLD) {}

    mic.captureAudioToFile(file);
    while (mic.getAudioVolume() > volume - 2) {}
    mic.close();

    // recognize spoken audio
    Recognizer recognizer = new Recognizer(Recognizer.Languages.ENGLISH_US);
    GoogleResponse response = recognizer.getRecognizedDataForWave(file);

    // match output to regex
    String responseStr = response.getResponse().toString();
    Matcher m = playSong.matcher(responseStr);
    if (m.find()) {
      toReturn.addAll(songDirectory.getAllSongs(m.group(1)));
      System.out.print(toReturn.get(0).getTag().getFirst(FieldKey.TITLE) + ", " + toReturn.get(0).getTag().getFirst(FieldKey.ARTIST));
      return toReturn;
    }

    toReturn.addAll(songDirectory.getAllSongs(responseStr));
    System.out.print(toReturn.get(0).getTag().getFirst(FieldKey.TITLE) + ", " + toReturn.get(0).getTag().getFirst(FieldKey.ARTIST));
    return toReturn;
  }
}

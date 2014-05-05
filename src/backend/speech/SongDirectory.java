package backend.speech;

/**
 * SongDirectory
 * @author Arun Varma
 * gets songs in user's library according to artist and/or title
 */

import java.io.*;

import frontend.audiovisualizer.VisualizerPanel;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.audio.exceptions.*;
import org.jaudiotagger.tag.*;

public class SongDirectory {
  private String path;

  /**
   * SongDirectory
   * @param path
   */
  public SongDirectory(String path) {
    this.path = path;
  }

  /**
   * getSong
   * @param songName
   * @return the song with the given name; null otherwise
   */
  public AudioFile getSong(String songName) {
    AudioFile toReturn = songHelper(path, songName);

    return toReturn;
  }

  /**
   * songHelper
   * @param path
   * @param songName
   * @return the song with the given name; null otherwise
   */
  private AudioFile songHelper(String path, String songName) {
    File dir = new File(path);
    File[] listFiles = dir.listFiles();

    for (File file : listFiles) {
      if (file.isDirectory()) {
        AudioFile audio = songHelper(file.getAbsolutePath(), songName);
        if (audio != null)
          return audio;
      }
      else if (file.isFile()) {
        try {
          AudioFile audio = AudioFileIO.read(file);

          if (audio.getTag().getFirst(FieldKey.TITLE).toLowerCase().replaceAll("\\([^\\(]*\\)", "").replaceAll("[^A-Za-z0-9 ]", "").trim().equals(songName.toLowerCase())) {
            final String toSet = audio.getTag().getFirst(FieldKey.TITLE);
            (new Thread() {
              public void run() {
                VisualizerPanel.overlayText = toSet;
                try {
                  Thread.sleep(3000);
                } catch (Exception e) {}

                VisualizerPanel.overlayText = "";
              }
            }).start();

            return audio;
          }
          else if (audio.getTag().getFirst(FieldKey.TITLE).toLowerCase().replaceAll("\\([^\\(]*\\)", "").replaceAll("[^A-Za-z0-9 ]", "").replaceAll("&", "and").trim().equals(songName.toLowerCase())) {
            final String toSet = audio.getTag().getFirst(FieldKey.TITLE);
            (new Thread() {
              public void run() {
                VisualizerPanel.overlayText = toSet;
                try {
                  Thread.sleep(3000);
                } catch (Exception e) {}

                VisualizerPanel.overlayText = "";
              }
            }).start();

            return audio;
          }
        } catch (CannotReadException e) {
          continue;
        } catch (TagException e) {
          continue;
        } catch (IOException e) {
          continue;
        } catch (ReadOnlyFileException e) {
          continue;
        } catch (InvalidAudioFrameException e) {
          continue;
        }
      }
    }

    return null;
  }
}

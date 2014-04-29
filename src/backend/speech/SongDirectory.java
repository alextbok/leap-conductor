package backend.speech;

/**
 * SongDirectory
 * @author Arun Varma
 * gets songs in user's library according to artist and/or title
 */

import java.io.*;
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
    return songHelper(path, songName);
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

          if (audio.getTag().getFirst(FieldKey.TITLE).toLowerCase().equals(songName.toLowerCase()))
            return audio;
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

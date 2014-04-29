package backend.speech;

/**
 * SongDirectory
 * @author Arun Varma
 * gets songs in user's library according to artist and/or title
 */

import java.io.*;
import java.util.*;
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
   *
   * @return list of all songs with given song name
   */
  public List<AudioFile> getAllSongs(String songName) {
    return getSongsFromPath(path, songName);
  }

  /**
   * getSongs
   * @param path
   * @return a list of songs with the given song name in the current directory
   */
  private List<AudioFile> getSongsFromPath(String path, String songName) {
      System.out.println(songName);
    File dir = new File(path);
    File[] listFiles = dir.listFiles();
    ArrayList<AudioFile> toReturn = new ArrayList<>();

    for (File file : listFiles) {
      if (file.isDirectory())
        toReturn.addAll(getSongsFromPath(file.getAbsolutePath(), songName));
      else if (file.isFile()) {
        try {
          AudioFile audio = AudioFileIO.read(file);

          if (audio.getTag().getFirst(FieldKey.TITLE).equals(songName))
            toReturn.add(audio);
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

    return toReturn;
  }
}

package backend.speech;

/**
 * SongsDirectory
 * @author Arun Varma
 *
 */

import java.io.*;
import java.util.*;
import org.farng.mp3.*;
import org.farng.mp3.id3.AbstractID3v2;

public class SongsDirectory {
  private File dir;
  private File[] listFiles;

  /**
   * SongsDirectory
   */
  public SongsDirectory(String path) {
    dir = new File(path);
    listFiles = dir.listFiles();
  }

  /**
   * getSongs
   * @param artist
   * @return
   */
  public List<MP3File> getSongs(String artist) {
    ArrayList<MP3File> toReturn = new ArrayList<>();

    for (File file : listFiles) {
      if (file.isFile()) {
        try {
          MP3File mp3 = new MP3File(file);

          if (mp3.getID3v2Tag().getLeadArtist().equals(artist))
            toReturn.add(mp3);
        } catch (IOException e) {}
          catch (TagException e) {}
      }
    }

    return toReturn;
  }

  /**
   * getSongs
   * @param artist
   * @param name
   * @return
   */
  public List<MP3File> getSongs(String artist, String name) {
    ArrayList<MP3File> toReturn = new ArrayList<>();

    for (File file : listFiles) {
      if (file.isFile()) {
        try {
          MP3File mp3 = new MP3File(file);
          AbstractID3v2 tag = mp3.getID3v2Tag();

          if (tag.getLeadArtist().equals(artist) && tag.getSongTitle().equals(name))
            toReturn.add(mp3);
        } catch (IOException e) {}
        catch (TagException e) {}
      }
    }

    return toReturn;
  }
}

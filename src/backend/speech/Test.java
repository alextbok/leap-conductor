package backend.speech;

public class Test {
  public static void main(String[] args) {
    SongsBySpeech s = new SongsBySpeech("/Users/Arun/Music/iTunes/iTunes Media/Music");
    while (true) {
    try {
      s.speechCommand();
    } catch (Exception e) {continue;}}
  }
}

package frontend;

import backend.audio.SongApp;

/**
 * Main
 * main class for running leap conductor
 * @author Arun Varma
 */

public class Main {
  public static void main(String[] args) {
    new GUI(new SongApp(args[0]));
  }
}

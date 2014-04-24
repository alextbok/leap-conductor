package frontend;

import backend.FileProcessor;
import backend.audio.SongApp;

/**
 * Main
 * main class for running leap conductor
 * @author Arun Varma
 */

public class Main {
  public static void main(String[] args) {
	//create a FileProcessor and start looking for folder with most mp3 files
	FileProcessor fp = new FileProcessor();
	fp.start();
	//instantiate our SongApp
	new SongApp(args[0]);
	//instantiate our GUI
    new GUI();
  }
}

package frontend;

/**
 * Main
 * main class for running leap conductor
 * @author Arun Varma
 */

import hub.SoundController;
import backend.FileProcessor;
import backend.audio.SongApp;

public class Main {
  public static void main(String[] args) {
    // set computer to use opengl
	System.setProperty("sun.java2d.opengl", "true");

	// create FileProcessor, start looking for folder with most mp3 files
	FileProcessor fp = new FileProcessor();
	fp.start();

	// instantiate our SongApp
	new SoundController(new SongApp("src/data/sounds/Atlas_Hands.mp3"));

	// instantiate our GUI
    new GUI();
  }
}

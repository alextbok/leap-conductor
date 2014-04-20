package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import backend.audio.SongApp;

public class GUI extends JFrame{
	
  private VisualizerPanel _visualizerPanel;
  private SongPanel _songPanel;
  
  public static final int WIDTH = 1300;
  public static final int HEIGHT = 700;
  
  public GUI(SongApp songApp) {
	  
	    // set up frame
	    super("Leap Conductor");
	    this.setSize(new Dimension(WIDTH, HEIGHT));

	    // set up panels
	    _visualizerPanel = new VisualizerPanel(5000, 3, 2);
	    _songPanel = new SongPanel(songApp);
	    
	    // add components
	    this.add(_visualizerPanel, BorderLayout.CENTER);
	    this.add(_songPanel, BorderLayout.NORTH);
	    
	    this.setResizable(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
  }
  
}

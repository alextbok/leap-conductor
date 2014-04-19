package frontend;

/**
 * GUI
 * @author Arun Varma
 * a graphical user interface for the leap conductor
 */

import java.awt.*;
import javax.swing.*;

public class GUI {
  private JFrame gui;
  private VisualizerPanel visualizerPanel;

  /*
   * run
   * runs the graphical user interface
   */
  public void run() {
    // set up frame
    gui = new JFrame("Leap Conductor");
    gui.setSize(new Dimension(1200, 700));

    // set up panels
    visualizerPanel = new VisualizerPanel(5000, 3, 2, gui.getWidth(), gui.getHeight());

    // add components
    gui.add(visualizerPanel, BorderLayout.CENTER);

    gui.setResizable(false);
    gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gui.setVisible(true);
  }
}

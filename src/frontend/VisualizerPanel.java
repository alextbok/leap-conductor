package frontend;

/**
 * VisualizerPanel
 * panel for visualizing hand motion
 * @auther Arun Varma
 */

import frontend.audiovisualizer.*;
import com.leapmotion.leap.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.*;
import java.util.*;

public class VisualizerPanel extends JPanel {
  private LeapListener leapListener;
  private Controller leapController;
  private List<Color> colors;
  private ParticleField particleField;
  private int trailSize, preset;
  private boolean sizeChange;

  /**
   * VisualizerPanel
   */
  public VisualizerPanel(int particles, int trailSize, int preset, double width, double height) {
    setBackground(Color.DARK_GRAY);

    // set up controller and listener
    leapController = new Controller();
    leapListener = new LeapListener();
    leapController.addListener(leapListener);

    this.trailSize = trailSize;
    this.preset = preset;

    // add particle field to panel
    colors = Collections.synchronizedList(new ArrayList<Color>());
    colors.add(Color.RED);
    colors.add(Color.WHITE);
    particleField = new ParticleField(colors, preset, particles, trailSize, (int) width, (int) height);

    sizeChange = false;
  }

  /**
   * paintComponent
   * @param g
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // update particle positions
    particleField.move();

    // circle size
    /*if (sizeChange) {
      particleField.getCircle().randomRadius();
      sizeChange = false;
    }
    else {
      if (particleField.getCircle().getRadius() < )
    }*/

    // paint hands
    List<Point2D> hands = leapListener.getHandLocs();
    if (hands != null) {
      for (Point2D hand : hands) {
        g2.setColor(new Color(127, 255, 212));
        g2.fillOval((int) hand.getX(), (int) hand.getY(), 70, 70);
      }
    }

    // paint fingers
    List<Point2D> fingers = leapListener.getFingerLocs();
    if (fingers != null) {
      for (Point2D finger : fingers) {
        g2.setColor(new Color(0, 104, 139));
        g2.fillOval((int) finger.getX(), (int) finger.getY(), 30, 30);
      }
    }

    // paint particles for audio visualizer
    List<Particle> particles = particleField.getParticles();
    int num = 0;
    for (int i = 0; i < particles.size(); i++) {
      Particle particle = particles.get(i);
      g2.setColor(particle.getColor());

      List<Point2D> points = particle.getTrail();
      for (int j = 0; j < points.size() - 1; j++) {
        Point2D p1 = points.get(j);
        Point2D p2 = points.get(j + 1);

        if (p1.getX() < 0 || p1.getX() > getWidth() || p1.getY() < 0 || p1.getY() > getHeight()) {
          particles.remove(i);
          num++;
          break;
        }

        g2.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
      }
    }

    particleField.generateParticles(num, trailSize);
    repaint();
  }

  /**
   * ConnectListener
   */
  /*public class ConnectListener extends Listener {
    @Override
    public void onConnect(Controller controller) {
      g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      g.setFill(Color.BROWN);
      g.fillText("Device connected", 30, 30);

      // added by ben temporarily
      controller.enableGesture(Gesture.Type.TYPE_SWIPE);
    }

    @Override
    public void onDisconnect(Controller controller) {
      g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      g.setFill(Color.BROWN);
      g.fillText("Please connect a device", 30, 30);
    }
  }*/
}

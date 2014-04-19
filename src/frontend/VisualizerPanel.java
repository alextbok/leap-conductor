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
  private int trailSize, newRadius;
  private boolean sizeChange, smaller;

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

    // add particle field to panel
    colors = Collections.synchronizedList(new ArrayList<Color>());
    colors.add(Color.RED);
    colors.add(Color.WHITE);
    particleField = new ParticleField(colors, preset, particles, trailSize, (int) width, (int) height);

    sizeChange = true;
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
    ParticleCircle circle = particleField.getCircle();
    if (sizeChange) {
      newRadius = circle.randomRadius();

      if (newRadius > circle.getRadius())
        smaller = true;
      else
        smaller = false;

      sizeChange = false;
    }
    else {
      if (circle.getRadius() < newRadius && smaller)
        circle.setRadius(circle.getRadius() + 20);
      else if (circle.getRadius() > newRadius && (!smaller))
        circle.setRadius(circle.getRadius() - 20);
      else
        sizeChange = true;
    }

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
      for (Point2D point : points) {
        if (point.getX() < 0 || point.getX() > getWidth() || point.getY() < 0 || point.getY() > getHeight()) {
          particles.remove(i);
          num++;
          break;
        }

        if (particleField.isInCircle(point))
          g2.fillOval((int) point.getX(), (int) point.getY(), 1, 1);
        else
          g2.fillOval((int) point.getX(), (int) point.getY(), 2, 2);
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

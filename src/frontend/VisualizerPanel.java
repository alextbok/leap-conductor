package frontend;

/**
 * VisualizerPanel
 * panel for visualizing hand motion
 * @auther Arun Varma
 */

import backend.audio.*;
import frontend.audiovisualizer.*;
import com.leapmotion.leap.*;
import javax.swing.*;
import javafx.scene.media.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.*;
import java.util.*;

public class VisualizerPanel extends JPanel {
  private AudioSpectrumListener songListener;
  private LeapListener leapListener;
  private Controller leapController;
  private List<Color> colors;
  private ParticleField particleField;
  private int trailSize, newRadius;
  private boolean sizeChange, smaller;

  /**
   * VisualizerPanel
   */
  public VisualizerPanel(SongApp songApp, int particles, int trailSize, int preset) {
    setBackground(Color.DARK_GRAY);

    songListener = songApp.getMediaPlayer().getAudioSpectrumListener();

    // set up controller and listener
    leapController = new Controller();
    leapListener = new LeapListener();
    leapController.addListener(leapListener);

    this.trailSize = trailSize;

    // add particle field to panel
    colors = Collections.synchronizedList(new ArrayList<Color>());
    colors.add(Color.RED);
    colors.add(Color.WHITE);
    particleField = new ParticleField(colors, preset, particles, trailSize, GUI.WIDTH, GUI.HEIGHT - 100);

    sizeChange = true;

    AudioSpectrumListener audioSpectrumListener = new AudioSpectrumListener() {
      @Override
      public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (sizeChange)
          newRadius = 2 * (40 - (int) magnitudes[0]);
      }
    };
    songApp.getMediaPlayer().setAudioSpectrumListener(audioSpectrumListener);
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
      if (newRadius > circle.getRadius())
        smaller = true;
      else
        smaller = false;

      sizeChange = false;
    }
    else {
      if (circle.getRadius() < newRadius && smaller)
        circle.setRadius(circle.getRadius() + 10);
      else if (circle.getRadius() > newRadius && (!smaller))
        circle.setRadius(circle.getRadius() - 10);
      else
        sizeChange = true;
    }

    // paint hands
    ParticleCircle leftCircle = particleField.getLeftCircle();
    ParticleCircle rightCircle = particleField.getRightCircle();
    List<Point2D> hands = leapListener.getHandLocs();
    if (hands != null) {
      if (hands.size() == 0) {
        leftCircle.setPos(getWidth() / 2, getHeight() / 2);
        rightCircle.setPos(getWidth() / 2, getHeight() / 2);
      }
      else if (hands.size() == 1) {
        Point2D pt = hands.get(0);
        leftCircle.setPos(pt.getX(), pt.getY());
      }
      else {
        Point2D pt1 = hands.get(0);
        Point2D pt2 = hands.get(1);
        leftCircle.setPos(pt1.getX(), pt1.getY());
        rightCircle.setPos(pt2.getX(), pt2.getY());
      }
    }

    // paint fingers
    List<Point2D> fingers = leapListener.getFingerLocs();
    if (fingers != null) {
      for (Point2D finger : fingers) {
        int x = (int) finger.getX();
        int y = (int) finger.getY();

        if (x > 0 && y > 0) {
          g2.setColor(new Color(135, 206, 235));
          g2.fillOval((int) finger.getX(), (int) finger.getY(), 30, 30);
        }
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

        if (circle.isInCircle(point) || leftCircle.isInCircle(point) || rightCircle.isInCircle(point))
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

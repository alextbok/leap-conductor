package frontend.audiovisualizer;

/**
 * VisualizerPanel
 * panel for visualizing hand motion
 * @auther Arun Varma
 */

import frontend.*;
import frontend.soundpanel.*;
import hub.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.media.*;
import javax.swing.*;
import com.leapmotion.leap.*;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel {
  private LeapListener leapListener;
  private AudioSpectrumListener audioSpectrumListener;
  private Controller leapController;
  private List<Color> colors;
  private ParticleField particleField;
  private int trailSize;
  private double newRadius;
  private boolean sizeChange, smaller;

  /**
   * VisualizerPanel
   */
  public VisualizerPanel(int particles, int trailSize) {
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
    particleField = new ParticleField(colors, particles, trailSize, GUI.WIDTH, GUI.HEIGHT - 120);

    sizeChange = true;

    audioSpectrumListener = new AudioSpectrumListener() {
      @Override
      public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (sizeChange)
          newRadius = 7 * (magnitudes[0] + 60);
      }
    };
  }

  /**
   * paintComponent
   * @param g
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    particleField.setWidth(getWidth());
    particleField.setHeight(getHeight());
    SoundController.setAudioSpectrumListener(audioSpectrumListener);

    // update particle positions
    particleField.setSpeed(SoundController.getRate());
    particleField.updateColor(SoundController.getLows(), SoundController.getMids(), SoundController.getHighs());
    particleField.move();

    // change circle size according to audio, draw circle
    ParticleCircle circle = particleField.getCircle();
    double centerRadius = circle.getRadius();
    if (SoundController.getMediaPlayer().getCurrentRate() == 0)
      circle.setRadius(Math.max(centerRadius - 6, 0));
    else if (sizeChange) {
      if (newRadius > centerRadius)
        smaller = true;
      else
        smaller = false;

      sizeChange = false;
    }
    else {
      if (centerRadius < newRadius && smaller)
        circle.setRadius(centerRadius + 15);
      else if (centerRadius > newRadius && (!smaller))
        circle.setRadius(centerRadius - 15);
      else
        sizeChange = true;
    }
    g2.setColor(new Color(0.1f, 0.1f, 0.1f, 0.025f));
    Ellipse2D centerEllipse = new Ellipse2D.Double(particleField.getCircle().getX() - centerRadius, particleField.getCircle().getY() - centerRadius, centerRadius * 2, centerRadius * 2);
    g2.fill(centerEllipse);

    // paint hands
    ParticleCircle leftCircle = particleField.getLeftCircle();
    ParticleCircle rightCircle = particleField.getRightCircle();
    List<Point2D> hands = leapListener.getHandLocs();
    if (hands != null) {
      if (hands.size() == 1) {
        Point2D pt = hands.get(0);

        double x = pt.getX() * getWidth();
        double y = pt.getY() * getHeight();
        if (x > 0 && y > 0 && x < getWidth() && y < getHeight()) {
          leftCircle.setPos(x, y);
          g2.setColor(new Color(0.5f, 0.75f, 0.8f, 0.4f));
          double radius = leftCircle.getRadius();
          Ellipse2D ellipse = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
          g2.fill(ellipse);
        }
        particleField.getRightCircle().setPos(-100, -100);
      }
      else if (hands.size() == 2) {
        Point2D pt1 = hands.get(0);
        Point2D pt2 = hands.get(1);

        double x = pt1.getX() * getWidth();
        double y = pt1.getY() * getHeight();
        if (x > 0 && y > 0 && x < getWidth() && y < getHeight()) {
          leftCircle.setPos(x, y);
          g2.setColor(new Color(0.5f, 0.75f, 0.8f, 0.4f));
          double radius = leftCircle.getRadius();
          Ellipse2D ellipse = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
          g2.fill(ellipse);
        }

        x = pt2.getX() * getWidth();
        y = pt2.getY() * getHeight();
        if (x > 0 && y > 0 && x < getWidth() && y < getHeight()) {
          rightCircle.setPos(x, y);
          g2.setColor(new Color(0.5f, 0.75f, 0.8f, 0.4f));
          double radius = rightCircle.getRadius();
          Ellipse2D ellipse = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
          g2.fill(ellipse);
        }
      }
    }
    else {
      particleField.getLeftCircle().setPos(-100, -100);
      particleField.getRightCircle().setPos(-100, -100);
    }

    // paint fingers
    List<Point2D> fingers = leapListener.getFingerLocs();
    if (fingers != null) {
      for (Point2D finger : fingers) {
        double x = finger.getX() * getWidth();
        double y = finger.getY() * getHeight();

        if (x > 0 && y > 0 && x < getWidth() && y < getHeight()) {
          g2.setColor(new Color(76, 81, 109));
          Ellipse2D ellipse = new Ellipse2D.Double(x, y, 30, 30);
          g2.fill(ellipse);
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

        if (circle.isInCircle(point) || leftCircle.isInCircle(point) || rightCircle.isInCircle(point)) {
          Ellipse2D ellipse = new Ellipse2D.Double(point.getX(), point.getY(), 1, 1);
          g2.fill(ellipse);
        }
        else {
          Ellipse2D ellipse = new Ellipse2D.Double(point.getX(), point.getY(), 2, SoundController.getVolume() * 2);
          g2.fill(ellipse);
        }
      }
    }

    particleField.generateParticles(num, trailSize);
    repaint();
  }
}

package frontend.audiovisualizer;

/**
 * VisualizerPanel
 * panel for visualizing hand motion
 * @auther Arun Varma
 */

import frontend.GUI;
import hub.SoundController;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.media.AudioSpectrumListener;

import javax.swing.JPanel;

import com.leapmotion.leap.Controller;

import frontend.audiovisualizer.Particle;
import frontend.audiovisualizer.ParticleCircle;
import frontend.audiovisualizer.ParticleField;
import frontend.soundpanel.LeapListener;

@SuppressWarnings("serial")
public class VisualizerPanel extends JPanel {
  private LeapListener leapListener;
  private Controller leapController;
  private List<Color> colors;
  private ParticleField particleField;
  private int trailSize, particleSize, newRadius;
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
    particleSize = 2;

    // add particle field to panel
    colors = Collections.synchronizedList(new ArrayList<Color>());
    colors.add(Color.RED);
    colors.add(Color.WHITE);
    particleField = new ParticleField(colors, particles, trailSize, GUI.WIDTH, GUI.HEIGHT - 120);

    sizeChange = true;

    AudioSpectrumListener audioSpectrumListener = new AudioSpectrumListener() {
      @Override
      public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (sizeChange)
          newRadius = 3 * (40 - (int) magnitudes[0]);
      }
    };
    SoundController.setAudioSpectrumListener(audioSpectrumListener);
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
    particleField.setSpeed(SoundController.getRate());
    particleField.move();

    // change circle size according to audio, draw circle
    ParticleCircle circle = particleField.getCircle();
    int centerRadius = circle.getRadius();
    if (sizeChange) {
      if (newRadius > centerRadius + 50)
        smaller = true;
      else
        smaller = false;

      sizeChange = false;
    }
    else {
      if (centerRadius + 50 < newRadius && smaller)
        circle.setRadius(centerRadius + 15);
      else if (centerRadius + 50 > newRadius && (!smaller))
        circle.setRadius(centerRadius - 15);
      else
        sizeChange = true;
    }
    g2.setColor(new Color(0.1f, 0.1f, 0.1f, 0.025f));
    g2.fillOval((int) particleField.getCircle().getX() - centerRadius, (int) particleField.getCircle().getY() - centerRadius, centerRadius * 2, centerRadius * 2);

    // paint hands
    ParticleCircle leftCircle = particleField.getLeftCircle();
    ParticleCircle rightCircle = particleField.getRightCircle();
    List<Point2D> hands = leapListener.getHandLocs();
    if (hands != null) {
      if (hands.size() == 1) {
        Point2D pt = hands.get(0);

        if (pt.getX() > 0 && pt.getY() > 0) {
          leftCircle.setPos(pt.getX(), pt.getY());
          g2.setColor(new Color(0.5f, 0.75f, 0.8f, 0.4f));
          double radius = leftCircle.getRadius();
          Ellipse2D ellipse = new Ellipse2D.Double(pt.getX() - radius, pt.getY() - radius, radius * 2, radius * 2);
          g2.fill(ellipse);
        }
      }
      else if (hands.size() == 2) {
        Point2D pt1 = hands.get(0);
        Point2D pt2 = hands.get(1);

        if (pt1.getX() > 0 && pt1.getY() > 0) {
          leftCircle.setPos(pt1.getX(), pt1.getY());
          g2.setColor(new Color(0.5f, 0.75f, 0.8f, 0.4f));
          double radius = leftCircle.getRadius() + 10;
          Ellipse2D ellipse = new Ellipse2D.Double(pt1.getX() - radius, pt1.getY() - radius, radius * 2, radius * 2);
          g2.fill(ellipse);
        }
        if (pt2.getX() > 0 && pt2.getY() > 0) {
          rightCircle.setPos(pt2.getX(), pt2.getY());
          g2.setColor(new Color(0.5f, 0.75f, 0.8f, 0.4f));
          double radius = rightCircle.getRadius();
          Ellipse2D ellipse = new Ellipse2D.Double(pt2.getX() - radius, pt2.getY() - radius, radius * 2, radius * 2);
          g2.fill(ellipse);
        }
      }
    }

    // paint fingers
    List<Point2D> fingers = leapListener.getFingerLocs();
    if (fingers != null) {
      for (Point2D finger : fingers) {
        int x = (int) finger.getX();
        int y = (int) finger.getY();

        if (x > 0 && y > 0) {
          g2.setColor(new Color(76, 81, 109));
          Ellipse2D ellipse = new Ellipse2D.Double(finger.getX(), finger.getY(), 30, 30);
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

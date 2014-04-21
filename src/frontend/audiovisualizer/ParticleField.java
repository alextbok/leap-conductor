package frontend.audiovisualizer;

/**
 * ParticleField
 * defines a particle field for the audio visualizer
 * @author Arun Varma
 */

import java.awt.Color;
import java.awt.geom.*;
import java.util.*;

public class ParticleField {
  private List<Particle> particles;
  private List<Color> colors;
  private int width, height;
  private int preset;
  private ParticleCircle circle, leftCircle, rightCircle;

  /**
   * ParticleField
   * @param colors
   * @param initParticles
   * @param initTrailSize
   * @param width
   * @param height
   */
  public ParticleField(List<Color> colors, int preset, int initParticles, int initTrailSize, int width, int height) {
    particles = new ArrayList<>();
    this.colors = colors;
    this.width = width;
    this.height = height;
    this.preset = preset;
    circle = new ParticleCircle(width, height);
    leftCircle = new ParticleCircle(width, height);
    rightCircle = new ParticleCircle(width, height);

    generateParticles(initParticles, initTrailSize);
  }

  /**
   * generateParticles
   * @param numParticles
   * @param trailSize
   */
  public void generateParticles(int numParticles, int trailSize) {
    for (int i = 0; i < numParticles; i++)
      particles.add(new Particle(randomPoint(), randomColor(), trailSize));
  }

  /**
   * randomPoint
   * @return a randomly generated x,y point
   */
  public Point2D randomPoint() {
    int x = (int) (Math.random() * width);
    int y = (int) (Math.random() * height);

    return new Point2D.Double(x, y);
  }

  /**
   * randomColor
   * @return a randomly generated color
   */
  public Color randomColor() {
    int rand = (int) (Math.random() * colors.size());
    return colors.get(rand);
  }

  /**
   * velocityVector
   * @return the given point's new position, according to the velocity vector
   */
  public Point2D velocityVector(Point2D point) {
    int centerX = width / 2;
    int centerY = height / 2;
    int slope;

    // define different velocities for particles contained within and outside circle
    if (circle.isInCircle(point) || leftCircle.isInCircle(point) || rightCircle.isInCircle(point)) {
      if (preset == 1) {
        slope = (int) ((point.getY() - centerY) / (point.getX() - centerX));
        return new Point2D.Double(point.getX() + 1, point.getY() + slope);
      }
      else if (preset == 2) {
        int slopeX = (int) point.getX() - (width / 2);
        int slopeY = (height / 2) - (int) point.getY();
        slope = (slopeX * slopeY) / 50000;
        int quadrant = quadrant(point);

        if (quadrant == 1)
          return new Point2D.Double(point.getX() + 0.03, point.getY() - slope);
        else if (quadrant == 2)
          return new Point2D.Double(point.getX() - 0.03, point.getY() + slope);
        else if (quadrant == 3)
          return new Point2D.Double(point.getX() - 0.03, point.getY() + slope);
        else
          return new Point2D.Double(point.getX() + 0.03, point.getY() - slope);
      }
    }

    else {
      if (preset == 1) {
        slope = (int) ((point.getY() - centerY) / (point.getX() - centerX));
        return new Point2D.Double(point.getX() + 1, point.getY() + (slope * 2));
      }
      else if (preset == 2) {
        int slopeX = (int) point.getX() - (width / 2);
        int slopeY = (height / 2) - (int) point.getY();
        slope = (slopeX * slopeY) / 20000;
        int quadrant = quadrant(point);

        if (quadrant == 1)
          return new Point2D.Double(point.getX() + 0.5, point.getY() - slope);
        else if (quadrant == 2)
          return new Point2D.Double(point.getX() - 0.5, point.getY() + slope);
        else if (quadrant == 3)
          return new Point2D.Double(point.getX() - 0.5, point.getY() + slope);
        else
          return new Point2D.Double(point.getX() + 0.5, point.getY() - slope);
      }
    }

    return null;
  }

  /**
   * move
   * moves each particle in the field according to the velocity vector
   */
  public void move() {
    for (Particle particle : particles) {
      Point2D newPos = velocityVector(particle.getHead());
      particle.move(newPos);
    }
  }

  /**
   * quadrant
   * @return the quadrant number of the given point
   */
  public int quadrant(Point2D point) {
    if (point.getX() > width / 2 && point.getY() <= height / 2)
      return 1;
    else if (point.getX() <= width / 2 && point.getY() <= height / 2)
      return 2;
    else if (point.getX() <= width / 2 && point.getY() > height / 2)
      return 3;
    else
      return 4;
  }

  /**
   * setPreset
   * @param newPreset
   */
  public void setPreset(int newPreset) {
    preset = newPreset;
  }

  /**
   * getParticles
   * @return particles
   */
  public List<Particle> getParticles() {
    return particles;
  }

  /**
   * getLeftCircle
   * @return leftCircle
   */
  public ParticleCircle getLeftCircle() {
    return leftCircle;
  }

  /**
   * getRightCircle
   * @return rightCircle
   */
  public ParticleCircle getRightCircle() {
    return rightCircle;
  }

  /**
   * getRightCircle
   * @return rightCircle
   */
  public ParticleCircle getCircle() {
    return circle;
  }
}

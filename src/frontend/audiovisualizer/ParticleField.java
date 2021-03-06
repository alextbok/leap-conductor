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
  private int width, height;
  private double particleSpeed;
  private ParticleCircle circle, leftCircle, rightCircle;

  /**
   * ParticleField
   * @param initParticles
   * @param initTrailSize
   * @param width
   * @param height
   */
  public ParticleField(int initParticles, int initTrailSize, int width, int height) {
    particles = new ArrayList<>();
    this.width = width;
    this.height = height;
    circle = new ParticleCircle(width, height);
    leftCircle = new ParticleCircle(width, height);
    leftCircle.setPos(width * 2, height * 2);
    rightCircle = new ParticleCircle(width, height);
    rightCircle.setPos(width * 2, height * 2);
    particleSpeed = 0.05;

    generateParticles(initParticles, initTrailSize);
  }

  /**
   * generateParticles
   * @param numParticles
   * @param trailSize
   */
  public void generateParticles(int numParticles, int trailSize) {
    for (int i = 0; i < numParticles; i++)
      particles.add(new Particle(randomPoint(), trailSize));
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
   * velocityVector
   * @return the given point's new position, according to the velocity vector
   */
  public Point2D velocityVector(Point2D point) {
    double slope, slopeX, slopeY;

    // define different velocities for particles contained within and outside circle
    if (circle.isInCircle(point)) {
      slopeX = (int) point.getX() - (width / 2);
      slopeY = (height / 2) - (int) point.getY();
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

    else {
      // repel points from hands
      if (leftCircle.isInCircle(point)) {
        slopeX = point.getX() - leftCircle.getX();
        slopeY = leftCircle.getY() - point.getY();
        slope = (slopeX * slopeY) / 400;
        int quadrant = leftCircle.quadrant(point);
        if (quadrant == 1)
          return new Point2D.Double(point.getX() + 2, point.getY() - slope);
        else if (quadrant == 2)
          return new Point2D.Double(point.getX() - 2, point.getY() + slope);
        else if (quadrant == 3)
          return new Point2D.Double(point.getX() - 2, point.getY() + slope);
        else
          return new Point2D.Double(point.getX() + 2, point.getY() - slope);
      }
      else if (rightCircle.isInCircle(point)) {
        slopeX = point.getX() - rightCircle.getX();
        slopeY = rightCircle.getY() - point.getY();
        slope = (slopeX * slopeY) / 400;
        int quadrant = rightCircle.quadrant(point);
        if (quadrant == 1)
          return new Point2D.Double(point.getX() + 2, point.getY() - slope);
        else if (quadrant == 2)
          return new Point2D.Double(point.getX() - 2, point.getY() + slope);
        else if (quadrant == 3)
          return new Point2D.Double(point.getX() - 2, point.getY() + slope);
        else
          return new Point2D.Double(point.getX() + 2, point.getY() - slope);
      }

      // else, point's movement is proportional to function x*y
      slopeX = point.getX() - (width / 2);
      slopeY = (height / 2) - point.getY();
      if (slopeX == 0)
        slope = 1;
      else
        slope = (slopeX * slopeY) / 40000;
      int quadrant = quadrant(point);
      if (quadrant == 1)
        return new Point2D.Double(point.getX() + Math.pow(particleSpeed * 1.2, 4), point.getY() - slope);
      else if (quadrant == 2)
        return new Point2D.Double(point.getX() - Math.pow(particleSpeed * 1.2, 4), point.getY() + slope);
      else if (quadrant == 3)
        return new Point2D.Double(point.getX() - Math.pow(particleSpeed * 1.2, 4), point.getY() + slope);
      else
        return new Point2D.Double(point.getX() + Math.pow(particleSpeed * 1.2, 4), point.getY() - slope);

      /*if (Math.random() < 0.15)
        return randomPoint();
      else
        return point;*/
    }
  }

  /**
   * move
   * moves each particle in the field according to the velocity vector
   */
  public void move() {
    for (Particle particle : particles) {
      Point2D newPos = velocityVector(particle.head());
      particle.move(newPos);
    }
  }

  /**
   * updateColor
   * @param lows
   * @param mids
   * @param highs
   */
  public void updateColor(double lows, double mids, double highs) {
    for (Particle particle : particles) {
      if (particle.getType() == 0) {
        try {
          particle.setColor(new Color(0, 150 + (3 * (int) lows), 150));
        } catch (IllegalArgumentException e) {
          particle.setColor(new Color(0, 255, 150));
        }
      }
      else if (particle.getType() == 1) {
        try {
          particle.setColor(new Color(160 + (3 * (int) mids), 160 + (3 * (int) mids), 160 + (3 * (int) mids)));
        } catch (IllegalArgumentException e) {
          particle.setColor(new Color(255, 255, 255));
        }
      }
      else {
        try {
          particle.setColor(new Color(200 + (3 * (int) highs), 0, 0));
        } catch (IllegalArgumentException e) {
          particle.setColor(new Color(255, 0, 0));
        }
      }
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
   * setSpeed
   * @param newSpeed
   */
  public void setSpeed(double newSpeed) {
    particleSpeed = newSpeed;
  }

  /**
   * setWidth
   * @param newWidth
   */
  public void setWidth(int newWidth) {
    width = newWidth;
    circle.setPos(width / 2, height / 2);
  }

  /**
   * newHeight
   * @param newHeight
   */
  public void setHeight(int newHeight) {
    height = newHeight;
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

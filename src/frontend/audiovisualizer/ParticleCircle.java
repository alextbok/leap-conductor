package frontend.audiovisualizer;

import java.awt.geom.Point2D;

/**
 * ParticleCircle
 * defines a circle in the particle field for the audio visualizer
 * @author Arun Varma
 */

public class ParticleCircle {
  private double x, y;
  private int radius;
  private int lowerBound, upperBound;

  /**
   * ParticleCircle
   * @param panelWidth
   * @param panelHeight
   */
  public ParticleCircle(int panelWidth, int panelHeight) {
    radius = Math.min(panelWidth, panelHeight) / 4;
    lowerBound = Math.min(panelWidth, panelHeight) / 6;
    upperBound = Math.min(panelWidth, panelHeight) / 3;
    x = panelWidth / 2;
    y = panelHeight / 2;
  }

  /**
   * randomRadius
   * generates a random radius for the circle
   */
  public int randomRadius() {
    return (int) (Math.random() * upperBound) + 50;
  }

  /**
   * isInCircle
   * @return true if the point is within the circle, false otherwise
   */
  public boolean isInCircle(Point2D point) {
    if (Point2D.distance(x, y, point.getX(), point.getY()) < radius)
      return true;
    else
      return false;
  }

  /**
   * setPos
   */
  public void setPos(double newX, double newY) {
    x = newX;
    y = newY;
  }

  /**
   * setRadius
   * sets the radius to the given radius
   * @param newRadius
   */
  public void setRadius(int newRadius) {
    radius = newRadius;
  }

  /**
   * setX
   */
  public double getX() {
    return x;
  }

  /**
   * setX
   */
  public double getY() {
    return y;
  }

  /**
   * getRadius
   * @return radius
   */
  public int getRadius() {
    return radius;
  }
}

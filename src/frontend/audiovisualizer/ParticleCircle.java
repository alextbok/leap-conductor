package frontend.audiovisualizer;

/**
 * ParticleCircle
 * defines a circle in the particle field for the audio visualizer
 * @author Arun Varma
 */

import java.awt.geom.Point2D;

public class ParticleCircle {
  private double x, y;
  private double radius;

  /**
   * ParticleCircle
   * @param panelWidth
   * @param panelHeight
   */
  public ParticleCircle(int panelWidth, int panelHeight) {
    radius = Math.min(panelWidth, panelHeight) / 10;
    x = panelWidth / 2;
    y = panelHeight / 2;
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
   * quadrant
   * @param point
   */
  public int quadrant(Point2D point) {
    if (point.getX() > x && point.getY() <= y)
      return 1;
    else if (point.getX() <= x && point.getY() <= y)
      return 2;
    else if (point.getX() <= x && point.getY() > y)
      return 3;
    else
      return 4;
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
  public void setRadius(double newRadius) {
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
  public double getRadius() {
    return radius;
  }
}

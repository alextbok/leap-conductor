package frontend.audiovisualizer;

/**
 * Particle
 * defines a particle in the audio visualizer
 * @author Arun Varma
 */

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import java.util.List;

public class Particle {
  private List<Point2D> trail;
  private Color color;
  private int trailSize;
  private int type;

  /**
   * Particle
   * @param point
   * @param trailSize
   */
  public Particle(Point2D point, int trailSize) {
    trail = new ArrayList<>();
    trail.add(point);
    this.trailSize = trailSize;
    type = (int) (Math.random() * 3);
  }

  /**
   * move
   * moves the trail to the given point
   * @param newPoint
   */
  public void move(Point2D newPoint) {
    trail.add(0, newPoint);

    if (trail.size() > trailSize)
      trail.remove(trailSize);
  }

  /**
   * getHead
   * @return the head of the particle trail
   */
  public Point2D head() {
    return trail.get(0);
  }

  /**
   * getTrail
   * @return trail
   */
  public List<Point2D> getTrail() {
    return trail;
  }

  /**
   * setColor
   * @param newColor
   */
  public void setColor(Color newColor) {
    color = newColor;
  }

  /**
   * getColor
   * @return
   */
  public Color getColor() {
    return color;
  }

  /**
   * getType
   * @return type
   */
  public int getType() {
    return type;
  }
}

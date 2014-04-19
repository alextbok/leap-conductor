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

  /**
   * Particle
   * @param
   */
  public Particle(Point2D point, Color color, int trailSize) {
    trail = new ArrayList<>();
    trail.add(point);
    this.color = color;
    this.trailSize = trailSize;
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
  public Point2D getHead() {
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
   * getColor
   * @return
   */
  public Color getColor() {
    return color;
  }
}

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
  private ParticleCircle circle;

  /**
   * ParticleField
   * @param colors
   * @param initParticles
   * @param initTrailSize
   * @param width
   * @param height
   */
  public ParticleField(List<Color> colors, int initParticles, int initTrailSize, int width, int height) {
    particles = new ArrayList<>();
    this.colors = colors;
    this.width = width;
    this.height = height;
    circle = new ParticleCircle(width / 5);

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
    int rand = (int) (Math.random() * (colors.size() - 1));
    return colors.get(rand);
  }

  /**
   * velocityVector
   * @return the given point's new position, according to the velocity vector
   */
  public Point2D velocityVector(Point2D point) {
    return new Point2D.Double(point.getX() + 5, point.getY() - 10);
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
   * getParticles
   * @return particles
   */
  public List<Particle> getParticles() {
    return particles;
  }
}

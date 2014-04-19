package frontend.audiovisualizer;

/**
 * ParticleCircle
 * defines a circle in the particle field for the audio visualizer
 * @author Arun Varma
 */

public class ParticleCircle {
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
  }

  /**
   * randomRadius
   * generates a random radius for the circle
   */
  public int randomRadius() {
    return (int) (Math.random() * upperBound) + 50;
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
   * getRadius
   * @return radius
   */
  public int getRadius() {
    return radius;
  }
}

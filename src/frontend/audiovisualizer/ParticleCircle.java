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
   * @param initRadius
   */
  public ParticleCircle(int initRadius) {
    radius = initRadius;
    lowerBound = initRadius / 4;
    upperBound = initRadius * 2;
  }

  /**
   * randomRadius
   * generates a random radius for the circle
   */
  public void randomRadius() {
    int newRadius;
    do {
      newRadius = (int) (Math.random() * upperBound);
    } while (lowerBound <= newRadius && newRadius <= upperBound);

    radius = newRadius;
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

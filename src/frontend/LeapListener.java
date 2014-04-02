package frontend;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import backend.motion.HandsUpGesture;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.Vector;

public class LeapListener extends Listener {
  private ObjectProperty<Point2D[]> handLocs;
  private ObjectProperty<Point2D[]> fingerLocs;

  /**
   * LeapListener
   */
  public LeapListener() {
    handLocs = new SimpleObjectProperty<>();
    fingerLocs = new SimpleObjectProperty<>();
  }

  /**
   * onFrame
   * get location of hands and fingers (if available) and set their values correspondingly
   * @param controller
   */
  @Override
  public void onFrame(Controller controller) {
    Frame frame = controller.frame();
    
    if(HandsUpGesture.isDetected(controller)) {
    	System.out.println("Hands up!");
    }

    if (!frame.hands().isEmpty()) {
      Screen screen = controller.locatedScreens().get(0);

      if (screen != null && screen.isValid()) {
        HandList hands = frame.hands();
        Point2D[] handArray = new Point2D[2];

        for (int i = 0; i < hands.count(); i++) {
          if (hands.get(i).isValid() && i < handArray.length) {
            Vector intersect = screen.intersect(hands.get(i).palmPosition(), hands.get(i).direction(), true);
            handArray[i] = new Point2D(screen.widthPixels() * intersect.getX(), screen.heightPixels() * (1d - intersect.getY()));
          }
        }

        handLocs.setValue(handArray);
      }
    }

    if (!frame.fingers().isEmpty()) {
      Screen screen = controller.locatedScreens().get(0);

      if (screen != null && screen.isValid()) {
        FingerList fingers = frame.fingers();
        Point2D[] fingerArray = new Point2D[10];

        for (int i = 0; i < fingers.count(); i++) {
          if (fingers.get(i).isValid() && i < fingerArray.length) {
            Vector intersect = screen.intersect(fingers.get(i).stabilizedTipPosition(), fingers.get(i).direction(), true);
            fingerArray[i] = new Point2D(screen.widthPixels() * intersect.getX(), screen.heightPixels() * (1d - intersect.getY()));
          }
        }

        fingerLocs.setValue(fingerArray);
      }
    }
  }

  /**
   * getHandLocs
   * @return handLocs
   */
  public ObjectProperty<Point2D[]> getHandLocs() {
    return handLocs;
  }

  /**
   * getFingerLocs
   * @return fingerLocs
   */
  public ObjectProperty<Point2D[]> getFingerLocs() {
    return fingerLocs;
  }
}

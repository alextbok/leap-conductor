package frontend;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;
import backend.audio.SongApp;
import backend.motion.*;
import com.leapmotion.leap.*;

public class LeapListener extends Listener {
  private List<Point2D> handLocs;
  private List<Point2D> fingerLocs;

  @Override
  public void onConnect(Controller controller) {
    controller.enableGesture(Gesture.Type.TYPE_SWIPE);
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
      SongApp.volumeUp();
    }
    
    if(HandsDownGesture.isDetected(controller)) {
      SongApp.volumeDown();
    }

    if (!frame.hands().isEmpty()) {
      Screen screen = controller.locatedScreens().get(0);

      if (screen != null && screen.isValid()) {
        HandList hands = frame.hands();
        ArrayList<Point2D> handList = new ArrayList<>();

        for (int i = 0; i < hands.count() && i < 2; i++) {
          if (hands.get(i).isValid()) {
            Vector intersect = screen.intersect(hands.get(i).palmPosition(), hands.get(i).direction(), true);
            handList.add(new Point2D.Double(screen.widthPixels() * intersect.getX(), screen.heightPixels() * (1d - intersect.getY())));
          }
        }

        handLocs = handList;
      }
    }

    if (!frame.fingers().isEmpty()) {
      Screen screen = controller.locatedScreens().get(0);

      if (screen != null && screen.isValid()) {
        FingerList fingers = frame.fingers();
        ArrayList<Point2D> fingerList = new ArrayList<>();

        for (int i = 0; i < fingers.count() && i < 10; i++) {
          if (fingers.get(i).isValid()) {
            Vector intersect = screen.intersect(fingers.get(i).stabilizedTipPosition(), fingers.get(i).direction(), true);
            fingerList.add(new Point2D.Double(screen.widthPixels() * intersect.getX(), screen.heightPixels() * (1d - intersect.getY())));
          }
        }

        fingerLocs = fingerList;
      }
    }
  }

  /**
   * getHandLocs
   * @return handLocs
   */
  public List<Point2D> getHandLocs() {
    return handLocs;
  }

  /**
   * getFingerLocs
   * @return fingerLocs
   */
  public List<Point2D> getFingerLocs() {
    return fingerLocs;
  }
}

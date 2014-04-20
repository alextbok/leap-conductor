package frontend;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import backend.audio.SongApp;
import backend.motion.HandsUpGesture;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.Vector;

public class LeapListener extends Listener {
  private List<Point2D> handLocs;
  private List<Point2D> fingerLocs;

  @Override
  public void onConnect(Controller controller) {
	  System.out.println("connected");
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
    	SongApp.speedUpSong();
    }
    
    // ADDED BY BEN TEMPROARILY
//    GestureList gestures = controller.frame().gestures();
//	for (int i = 0; i < gestures.count(); i++) {
//	    Gesture gesture = gestures.get(i);
//	    if(gesture.type().equals(Gesture.Type.TYPE_SWIPE)) {
//
//	    	if (gesture.state().equals(Gesture.State.STATE_STOP)){
//	    		SwipeGesture s = new SwipeGesture(gesture);
//	    		Vector v = s.startPosition().minus(s.position());
//	    		if (Math.abs(v.getX()) > Math.abs(v.getY())){
//	    			if (s.startPosition().getX() > s.position().getX()){
//	    				SongApp.slowDownSong();
//	    				System.out.println("going left");
//	    			}else {
//	    				SongApp.speedUpSong();
//	    				System.out.println("going right");
//	    			}
//	    		}else{
//	    			if (s.startPosition().getY() > s.position().getY()){
//	    				SongApp.volumeDown();
//	    				System.out.println("down");
//	    			}else {
//	    				SongApp.volumeUp();
//	    				System.out.println("up");
//	    			}
//	    		}
//	    	}
//
//	    }
//	}
    // END OF BENS ADDITIONS


    if (!frame.hands().isEmpty()) {
      Screen screen = controller.locatedScreens().get(0);

      if (screen != null && screen.isValid()) {
        HandList hands = frame.hands();
        ArrayList<Point2D> handList = new ArrayList<>();

        for (int i = 0; i < hands.count(); i++) {
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

        for (int i = 0; i < fingers.count(); i++) {
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

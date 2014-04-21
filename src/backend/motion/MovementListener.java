package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;

/**
 * Class to detect and handle leap input. Currenly just prints out if there's a swipe, but will end up handling much more complicated movements
 * @author Sawyer
 *
 */
public class MovementListener extends Listener {
	
	@Override
	public void onConnect(Controller controller) {
		System.out.println("connected");
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}

	@Override
	public void onFrame(Controller controller) {
		GestureList gestures = controller.frame().gestures();
		for (int i = 0; i < gestures.count(); i++) {
		    Gesture gesture = gestures.get(i);
		    if(gesture.type().equals(Gesture.Type.TYPE_SWIPE)) {
		    	System.out.println("Swipe detected!");
		    }
		}
	}
}

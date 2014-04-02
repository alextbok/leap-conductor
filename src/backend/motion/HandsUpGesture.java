package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class HandsUpGesture {

	public static boolean isDetected(Controller controller) {
		//System.out.println(controller.frame());
		
		HandList hands = controller.frame().hands();
		if(!hands.isEmpty()) {
			for(Hand hand : hands) {
				int goodFingers = 0;
				for(Finger finger : hand.fingers()) {
					if(finger.tipPosition().getY() > hand.palmPosition().getY()) {
						goodFingers++;
					}
				}
				if(goodFingers < 1) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

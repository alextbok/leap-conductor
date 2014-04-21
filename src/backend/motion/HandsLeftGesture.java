package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class HandsLeftGesture {

	public static boolean isDetected(Controller controller) {
		controller.frame().gestures();
		
		HandList hands = controller.frame().hands();
		HandList prevHands = controller.frame(1).hands();
		
		if (!hands.isEmpty() && !prevHands.isEmpty()){
			Hand rightHand = hands.rightmost();
			Hand prevRight = prevHands.rightmost();
			
			float dif = rightHand.stabilizedPalmPosition().getX() - prevRight.stabilizedPalmPosition().getX();
			
			int numFingers = rightHand.fingers().count();

			if(dif <= -3 && numFingers >= 3) {
				System.out.println("Hands Left Activated");
				return true;
			}
		}
		return false;
	}
	
}

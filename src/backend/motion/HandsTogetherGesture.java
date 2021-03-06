package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class HandsTogetherGesture {

	public static boolean isDetected(Controller controller) {
		controller.frame().gestures();
		
		HandList hands = controller.frame().hands();
		HandList prevHands = controller.frame(1).hands();
		
		if (!hands.isEmpty() && !prevHands.isEmpty() && hands.count() == 2 && prevHands.count() == 2){
			Hand rightHand = hands.rightmost();
			Hand prevRight = prevHands.rightmost();
			Hand leftHand = hands.leftmost();
			Hand prevLeft = prevHands.leftmost();
			
			float rightDif = rightHand.stabilizedPalmPosition().getX() - prevRight.stabilizedPalmPosition().getX();
			float leftDif = leftHand.stabilizedPalmPosition().getX() - prevLeft.stabilizedPalmPosition().getX();
			int rightFingers = rightHand.fingers().count();
			int leftFingers = leftHand.fingers().count();

			if (leftFingers > 2 && rightFingers > 2 && rightDif <= -3 && leftDif >= 3){
				return true;
			}
		}
		return false;
	}
	
}

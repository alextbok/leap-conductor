package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class HandsUpGesture {

	public static boolean isDetected(Controller controller) {
		controller.frame().gestures();
		
		HandList hands = controller.frame().hands();
		HandList prevHands = controller.frame(1).hands();
		
		if (!hands.isEmpty() && !prevHands.isEmpty()){
			Hand rightHand = hands.rightmost();
			Hand prevRight = prevHands.rightmost();
			
			float dif = rightHand.stabilizedPalmPosition().getY() - prevRight.stabilizedPalmPosition().getY();
			
			System.out.println("Difference is : " + dif);
			
			int numFingers = rightHand.fingers().count();
			System.out.println(numFingers);
			if(dif >= 3 && numFingers >= 3) {
				return true;
			}
		}
		return false;
		
		/*
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
		*/
	}

}

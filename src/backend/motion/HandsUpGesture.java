package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class HandsUpGesture {

	public static boolean isDetected(Controller controller) {
		//System.out.println(controller.frame());
		HandList hands = controller.frame().hands();
		
		if (!hands.isEmpty()){
			Hand rightHand = hands.rightmost();
			
			//System.out.println("Y of palm is : " + rightHand.palmPosition().getY());
			
			
			
			float dif = rightHand.sphereCenter().getY() - rightHand.stabilizedPalmPosition().getY();
			
			//System.out.println("Difference is : " + dif);
			//System.out.println("Number of fingers detected is " + rightHand.fingers().count());
			
			int numFingers = rightHand.fingers().count();
			
			if (rightHand.sphereCenter().getY() > rightHand.stabilizedPalmPosition().getY() && numFingers > 2){
				//System.out.println("Hand facing up!");
				Frame toCompareTo = controller.frame(50);
				
				if (rightHand.stabilizedPalmPosition().getY() - toCompareTo.hands().rightmost().stabilizedPalmPosition().getY() > 115){
					if (toCompareTo.timestamp() - controller.frame().timestamp() < 1000){
					return true;
					}
				}
				
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

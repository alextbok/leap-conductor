package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class TwoHandsDownGesture {

	public static boolean isDetected(Controller controller) {
		controller.frame().gestures();
		
		HandList hands = controller.frame().hands();
		HandList prevHands = controller.frame(1).hands();
		
		if (hands.count() == 2 && prevHands.count() == 2){
			//System.out.println("1");
			Hand rightHand = hands.rightmost();
			Hand prevRight = prevHands.rightmost();
			Hand leftHand = hands.leftmost();
			Hand prevLeft = prevHands.leftmost();
			
			float rightDif = rightHand.stabilizedPalmPosition().getY() - prevRight.stabilizedPalmPosition().getY();
			float leftDif = leftHand.stabilizedPalmPosition().getY() - prevLeft.stabilizedPalmPosition().getY();
			int rightFingers = rightHand.fingers().count();
			int leftFingers = leftHand.fingers().count();
			
			//if(rightFingers >= 3 && leftFingers >= 3) System.out.println("2");
			
			//if(leftDif <= -3) System.out.println("3");
			//if(rightDif <= -3) System.out.println("4");

			if (rightFingers >= 3 && leftFingers >= 3 && rightDif <=-3 && leftDif <=-3){
				//System.out.println("5");
				return true;
			}
		}
		return false;
	}
	
}

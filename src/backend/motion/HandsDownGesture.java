package backend.motion;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;

public class HandsDownGesture implements Detectable {

	@Override
	public boolean isDetected(Controller controller) {
		
		Frame toCompareTo = controller.frame(5);
		if (toCompareTo.isValid()){
			HandList tctHands = toCompareTo.hands();
			if (!tctHands.isEmpty()){
				double pos = tctHands.rightmost().palmPosition().getY();
				HandList hands = controller.frame().hands();
				if (!hands.isEmpty()){
					Hand rightHand = hands.rightmost();
					
					
					int numFingers = rightHand.fingers().count();
					
					if (numFingers > 2){
						double currentPos = rightHand.palmPosition().getY();
						if (pos - currentPos > 30){
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
}

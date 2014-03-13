package backend.motion;

import com.leapmotion.leap.Controller;


public interface Detectable {
	
	/**
	 * Returns true if the gesture is detected
	 * @param controller
	 * @return
	 */
	public boolean isDetected(Controller controller);

}

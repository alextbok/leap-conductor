package frontend.soundpanel;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

import javax.swing.Timer;

import backend.audio.SongApp;
import backend.motion.*;

import com.leapmotion.leap.*;

public class LeapListener extends Listener {
	private List<Point2D> handLocs;
	private List<Point2D> fingerLocs;
	private boolean cooldownComplete = true;

	@Override
	public void onConnect(Controller controller) {
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

		/***********************
		 * RECOGNIZING GESTURES
		 **********************/
		
		// gesture to pause the song
		if (cooldownComplete && HandsSeperateGesture.isDetected(controller)){
			SongApp.stopSong();
			cooldownComplete = false;
			new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cooldownComplete = true;
				}
			}).start();
		}else {

			// gestures for raising high/mid/bass
			if(HandsUpRightGesture.isDetected(controller)) {
				SongApp.raiseHigh();
			}
			else if (HandsUpMiddleGesture.isDetected(controller)){
				SongApp.raiseMid();
			}
			else if(HandsUpLeftGesture.isDetected(controller)) {
				SongApp.raiseBass();
			}
			// gestures for lowering high/mid/bass
			else if (HandsDownRightGesture.isDetected(controller)){
				SongApp.lowerHigh();
			}
			else if (HandsDownMiddleGesture.isDetected(controller)){
				SongApp.lowerMid();
			}
			else if (HandsDownMiddleGesture.isDetected(controller)){
				SongApp.lowerBass();
			}
			// Gestures for speeding up and slowing down song
			else if (HandsRightGesture.isDetected(controller)){
				SongApp.speedUpSong();
			}
			else if (HandsLeftGesture.isDetected(controller)){
				SongApp.slowDownSong();
			}

		}


		/****************
		 * DRAWING HANDS
		 ***************/
		
		if (!frame.hands().isEmpty()) {
			Screen screen = controller.locatedScreens().get(0);

			if (screen != null && screen.isValid()) {
				HandList hands = frame.hands();
				ArrayList<Point2D> handList = new ArrayList<>();

				for (int i = 0; i < hands.count() && i < 2; i++) {
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

				for (int i = 0; i < fingers.count() && i < 10; i++) {
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

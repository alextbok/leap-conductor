package frontend.soundpanel;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import hub.SoundController;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import backend.motion.HandsDownLeftGesture;
import backend.motion.HandsDownMiddleGesture;
import backend.motion.HandsDownRightGesture;
import backend.motion.HandsLeftGesture;
import backend.motion.HandsRightGesture;
import backend.motion.HandsSeperateGesture;
import backend.motion.HandsTogetherGesture;
import backend.motion.HandsUpLeftGesture;
import backend.motion.HandsUpMiddleGesture;
import backend.motion.HandsUpRightGesture;
import backend.motion.TwoHandsDownGesture;
import backend.motion.TwoHandsUpGesture;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Screen;
import com.leapmotion.leap.Vector;

public class LeapListener extends Listener {
	private List<Point2D> handLocs;
	private List<Point2D> fingerLocs;
	private boolean cooldownComplete = true;

	@Override
	public void onConnect(Controller controller) {
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
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

		for(Gesture g : controller.frame().gestures()) {
			if(g.type() == Gesture.Type.TYPE_CIRCLE) {
				CircleGesture circle = new CircleGesture(g);
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
					//clockwise circle
					SoundController.speedUpSong();
				}
				else {
					//counterclockwise circle
					SoundController.slowDownSong();
				}
			}
		}

		// gestures to pause/play the song
		if (HandsSeperateGesture.isDetected(controller)){
			SoundController.stopSong();
			/*
			cooldownComplete = false;
			new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					cooldownComplete = true;
				}
			}).start();
			 */
		}
		else if (HandsTogetherGesture.isDetected(controller)){
			SoundController.playSong();
		}
		// gestures for volume control
		else if (TwoHandsUpGesture.isDetected(controller)){
			SoundController.volumeUp();
		}
		else if (TwoHandsDownGesture.isDetected(controller)){
			SoundController.volumeDown();
		}
		// gestures for raising high/mid/bass
		else if(HandsUpRightGesture.isDetected(controller)) {
			SoundController.raiseHigh();
		}
		else if (HandsUpMiddleGesture.isDetected(controller)){
			SoundController.raiseMid();
		}
		else if(HandsUpLeftGesture.isDetected(controller)) {
			SoundController.raiseBass();
		}
		// gestures for lowering high/mid/bass
		else if (HandsDownRightGesture.isDetected(controller)){
			SoundController.lowerHigh();
		}
		else if (HandsDownMiddleGesture.isDetected(controller)){
			SoundController.lowerMid();
		}
		else if (HandsDownLeftGesture.isDetected(controller)){
			SoundController.lowerBass();
		}
		// Gestures for speeding up and slowing down song
		else if (HandsRightGesture.isDetected(controller)){
			SoundController.speedUpSong();
		}
		else if (HandsLeftGesture.isDetected(controller)){
			SoundController.slowDownSong();
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

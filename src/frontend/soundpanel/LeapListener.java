package frontend.soundpanel;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import com.leapmotion.leap.*;
import hub.SoundController;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import backend.motion.HandsDownLeftGesture;
import backend.motion.HandsDownMiddleGesture;
import backend.motion.HandsDownRightGesture;
import backend.motion.HandsSeperateGesture;
import backend.motion.HandsTogetherGesture;
import backend.motion.HandsUpLeftGesture;
import backend.motion.HandsUpMiddleGesture;
import backend.motion.HandsUpRightGesture;
import backend.motion.TwoHandsDownGesture;
import backend.motion.TwoHandsUpGesture;

public class LeapListener extends Listener {
	private List<Point2D> handLocs;
	private List<Point2D> fingerLocs;
    private double width, height;

	@Override
	public void onConnect(Controller controller) {
        // enable gestures
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);

        // get leap's range of detection
        InteractionBox box = controller.frame().interactionBox();
        width = box.width();
        height = box.height();
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

		boolean realGestureRecognized = false;
		
		for(Gesture g : controller.frame().gestures()) {
			if(g.type() == Gesture.Type.TYPE_CIRCLE) {
				realGestureRecognized = true;
				CircleGesture circle = new CircleGesture(g);
				if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
					//clockwise circle
					SoundController.speedUpSong();
				}
				else {
					//counterclockwise circle
					SoundController.slowDownSong();
				}
				//TODO change song on swipe
			}
		}
		
		boolean normalGestureRecognized = true;

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
		else {
			normalGestureRecognized = false;
		}
		if(!normalGestureRecognized && !realGestureRecognized) {
			//if hand is flat, update the selection based on hand position
			HandList hands = controller.frame().hands();
			if (!hands.isEmpty()){
				Hand rightHand = hands.rightmost();
				int numFingers = rightHand.fingers().count();
				if(numFingers >= 3) {
					float pos = rightHand.stabilizedPalmPosition().getX();
					if(pos > 80) {
						SoundController.updateSelection(SongPanel.getKnobPanel(KnobType.HIGH));
					}
					else if(pos < -80) {
						SoundController.updateSelection(SongPanel.getKnobPanel(KnobType.LOW));
					}
					else {
						SoundController.updateSelection(SongPanel.getKnobPanel(KnobType.MID));
					}
				}
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
						handList.add(new Point2D.Double(intersect.getX(), 1d - intersect.getY()));
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
						fingerList.add(new Point2D.Double(intersect.getX(), 1d - intersect.getY()));
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

    /**
     * getBoxWidth
     * @return width
     */
    public double getBoxWidth() {
        return width;
    }

    /**
     * getBoxHeight
     * @return height
     */
    public double getBoxHeight() {
        return height;
    }

}

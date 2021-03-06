package frontend.soundpanel;

/**
 * LeapListener
 * listener for leap motion for visualization purposes
 * @author Arun Varma
 */

import backend.*;
import backend.speech.*;
import hub.SoundController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import backend.motion.*;
import com.leapmotion.leap.*;
import org.jaudiotagger.audio.AudioFile;

public class LeapListener extends Listener {
    private SongsBySpeech speech;
	private List<Point2D> handLocs;
	private List<Point2D> fingerLocs;
	private SyncBoolean cooldownComplete = new SyncBoolean(true);
	private double width, height;
	private ResponseReceiver fiveFingerRequest = null;
	private int fiveFingerHeld = 0;
	private ResponseReceiver fistRequest = null;
	private int fistHeld = 0;
	private ResponseReceiver playRequest = null;
	private ResponseReceiver volumeRequest = null;
	private int volumeHeld = 0;
	private ResponseReceiver highRequest = null;
	private int highHeld = 0;
	private ResponseReceiver speedRequest = null;
	private int speedHeld = 0;
	private ResponseReceiver stopRequest = null;
	private ResponseReceiver swipeRequest = null;

	@Override
	public void onConnect(Controller controller) {
        String pathToItunes = FileProcessor.getFolderWithMostMusicFiles().getAbsolutePath();
        speech = new SongsBySpeech(pathToItunes);

		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);

		// set up swipes

		if(controller.config().setFloat("Gesture.Swipe.MinLength", 350.0f) &&
				controller.config().setFloat("Gesture.Swipe.MinVelocity", 1500f))
			controller.config().save();

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

				HandList h = controller.frame().hands();
				// make sure there are hands
				if (!h.isEmpty()){
					// make sure there's only one hand
					if (h.count() == 1){
						// make sure there's only one finger
						if (h.rightmost().fingers().count() == 1){

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
						}
					}
				}
			}
			else if (g.type() == Gesture.Type.TYPE_SWIPE){
				if (g.state() == Gesture.State.STATE_STOP){
					SwipeGesture swipe = new SwipeGesture(g);
					if (cooldownComplete.getValue()){

						cooldownComplete.setValue(false);
						if (swipe.direction().getX() > 0.5){
							File nextSong = SongList.getNextSong();
							if (nextSong != null){
								SoundController.stopSong();
								SoundController.setSong(nextSong);
								SoundController.resetValues();
								SoundController.playSong();
							}
						}
						else if (swipe.direction().getX() < -0.5){
							if (SoundController.getCurrentTime() > 3000){
								SoundController.seekTo(0.0);
							}
							// Go to previous song
							else{
								File previousSong = SongList.getPreviousSong();
								if (previousSong != null){
								SoundController.stopSong();
								SoundController.setSong(previousSong);
								SoundController.resetValues();
								SoundController.playSong();
								}
							}

						}

						Timer t = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								cooldownComplete.setValue(true);
							}
						});
						t.setRepeats(false);
						t.start();


					}
				}
			}

            // screen tap for speech recognition
			else if (g.type() == Gesture.Type.TYPE_SCREEN_TAP){
				if (g.state() == Gesture.State.STATE_STOP){
                    (new Thread() {
                        public void run() {
                            // mute for duration
                            SoundController.mute();
                            try {
                                Thread.sleep(4000);
                            } catch (Exception e) {}

                            // unmute
                            SoundController.unmute();
		    		    }
			        }).start();

                    (new Thread() {
                        public void run() {
                            try {
                                // attempt speech recognition
                                AudioFile newSong = speech.speechCommand();
                                if (newSong != null) {
                                    SongList.addSong(newSong.getFile());
                                    SongList.setCurrentSong(newSong.getFile());
                                    SoundController.stopSong();
                                    SoundController.setSong(newSong.getFile());
                                    SoundController.playSong();
                                }
                            } catch (Exception e) {}
                        }
                    }).start();
                }
		    }
        }

		boolean normalGestureRecognized = true;

		// gestures to pause/play the song
		if (HandsSeperateGesture.isDetected(controller)){
			SoundController.stopSong();
		}
		else if (HandsTogetherGesture.isDetected(controller)){
			SoundController.playSong();
		}
		// gestures for volume control
		else if (VUpGesture.isDetected(controller)){
			SoundController.volumeUp();
		}
		else if (VDownGesture.isDetected(controller)){
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
		else
			handLocs = null;

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
		else
			fingerLocs = null;


		/****************************
		 * CHECKING RESPONSE REQUESTS
		 ****************************/

		HandList h = controller.frame().hands();

		//listen for 5-finger
		if(fiveFingerRequest != null) {
			if(!h.isEmpty()) {
				if(h.leftmost().fingers().count() == 5) {
					fiveFingerHeld++;
					if(fiveFingerHeld > 250) {
						fiveFingerRequest.receiveResponse();
						fiveFingerRequest = null;
						fiveFingerHeld = 0;
					}
				}
			}
		}

		//listen for fist
		if(fistRequest != null) {
			if(!h.isEmpty()) {
				if(h.leftmost().fingers().count() <= 1) {
					fistHeld++;
					if(fistHeld > 100) {
						fistRequest.receiveResponse();
						fistRequest = null;
						fistHeld = 0;
					}
				}
			}
		}

		//listen for play
		if(playRequest != null) {
			if(HandsTogetherGesture.isDetected(controller)) {
				SongList.setCurrentSong(SongList.getCurrentlySelectedSong());
				playRequest.receiveResponse();
				playRequest = null;
			}
		}

		//listen for volume
		if(volumeRequest != null) {
			if(VDownGesture.isDetected(controller) || VUpGesture.isDetected(controller)) {
				volumeHeld++;
				if(volumeHeld > 150) {
					volumeRequest.receiveResponse();
					volumeRequest = null;
					volumeHeld = 0;
				}
			}
		}

		//listen for high
		if(highRequest != null) {
			if(HandsUpRightGesture.isDetected(controller) || HandsDownRightGesture.isDetected(controller)) {
				highHeld++;
				if(highHeld > 150) {
					highRequest.receiveResponse();
					highRequest = null;
					highHeld = 0;
				}
			}
		}

		//listen for speed change
		if(speedRequest != null) {
			for(Gesture g : controller.frame().gestures()) {
				if(g.type().equals(Gesture.Type.TYPE_CIRCLE)) {
					speedHeld++;
					if(speedHeld > 150) {
						speedRequest.receiveResponse();
						speedRequest = null;
						speedHeld = 0;
						
					}
				}
			}
		}
/*
		//listen for swipe
		if(swipeRequest != null) {
			for(Gesture g : controller.frame().gestures()) {
				if(g.type().equals(Gesture.Type.TYPE_SWIPE)) {

					SwipeGesture swipe = new SwipeGesture(g);
					
					if (g.state() == Gesture.State.STATE_STOP){
						if (Math.abs( swipe.direction().getX() ) > 0.5 ){
							swipeRequest.receiveResponse();
							swipeRequest = null;
						}
					}
					
				}
			}
		}
*/
		//listen for stop
		if(stopRequest != null) {
			if(HandsSeperateGesture.isDetected(controller)) {
				stopRequest.receiveResponse();
				stopRequest = null;
			}
		}
	}

	/**
	 * Requests to listen for various gestures
	 */

	public void listenForFiveFingers(ResponseReceiver rr) {
		this.fiveFingerRequest = rr;
	}

	public void listenForFist(ResponseReceiver rr) {
		this.fistRequest = rr;
	}

	public void listenForPlay(ResponseReceiver rr) {
		this.playRequest = rr;
	}

	public void listenForVolume(ResponseReceiver rr) {
		this.volumeRequest = rr;
	}

	public void listenForHigh(ResponseReceiver rr) {
		this.highRequest = rr;
	}

	public void listenForSpeed(ResponseReceiver rr) {
		this.speedRequest = rr;
	}

	public void listenForStop(ResponseReceiver rr) {
		this.stopRequest = rr;
	}
	public void listenForSwipe(ResponseReceiver rr) {
		this.swipeRequest = rr;
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

package frontend;

/**
 * VisualizerPanel
 * panel for visualizing hand motion
 * @auther Arun Varma
 */

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;

public class VisualizerPanel extends Pane {
  private LeapListener leapListener;
  private ConnectListener connectListener;
  private Controller leapController;
  private AnchorPane root;
  private Canvas canvas;
  private GraphicsContext g;

  /**
   * VisualizerPanel
   */
  public VisualizerPanel(double width, double height) {
    // set up controller and listener
    leapController = new Controller();
    leapListener = new LeapListener();
    connectListener = new ConnectListener();
    leapController.addListener(leapListener);

    // set up canvas, add to panel
    root = new AnchorPane();
    canvas = new Canvas(width, height);
    g = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);
    getChildren().add(root);

    leapController.addListener(connectListener);

    leapListener.getHandLocs().addListener(new ChangeListener<Point2D[]>() {
      @Override
      public void changed(ObservableValue observableValue, Point2D[] points1, final Point2D[] points2) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            // reset canvas
            g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // draw fingers
            g.setFill(Color.AQUAMARINE);
            for (Point2D element : points2) {
              if (element == null)
                break;

              g.fillOval(element.getX(), element.getY(), 70, 70);
            }

            // draw hands
            Point2D[] fingerLocs = leapListener.getFingerLocs().getValue();
            g.setFill(Color.DEEPSKYBLUE);
            if (fingerLocs != null) {
              for (Point2D fingerLoc : fingerLocs) {
                if (fingerLoc == null)
                  break;

                g.fillOval(fingerLoc.getX(), fingerLoc.getY(), 30, 30);
              }
            }
          }
        });
      }
    });
  }

  /**
   * ConnectListener
   */
  public class ConnectListener extends Listener {
    @Override
    public void onConnect(Controller controller) {
      g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      g.setFill(Color.BROWN);
      g.fillText("Device connected", 30, 30);
      
      // added by ben temproarily
      controller.enableGesture(Gesture.Type.TYPE_SWIPE);
    }

    @Override
    public void onDisconnect(Controller controller) {
      g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      g.setFill(Color.BROWN);
      g.fillText("Please connect a device", 30, 30);
    }
  }
}

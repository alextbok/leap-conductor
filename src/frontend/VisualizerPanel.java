package frontend;

/**
 * VisualizerPanel
 * panel for visualizing hand motion
 * @auther Arun Varma
 */

import com.leapmotion.leap.*;
import javafx.application.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.beans.value.*;
import javafx.geometry.*;
import javafx.scene.paint.*;

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
            for (int i = 0; i < points2.length; i++) {
              if (points2[i] == null)
                break;

              g.fillOval(points2[i].getX(), points2[i].getY(), 70, 70);
            }

            // draw hands
            Point2D[] fingerLocs = leapListener.getFingerLocs().getValue();
            g.setFill(Color.DEEPSKYBLUE);
            if (fingerLocs != null) {
              for (int i = 0; i < fingerLocs.length; i++) {
                if (fingerLocs[i] == null)
                  break;

                g.fillOval(fingerLocs[i].getX(), fingerLocs[i].getY(), 30, 30);
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
    }

    @Override
    public void onDisconnect(Controller controller) {
      g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      g.setFill(Color.BROWN);
      g.fillText("Please connect a device", 30, 30);
    }
  }
}

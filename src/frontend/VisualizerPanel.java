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
import javafx.scene.paint.Color;

import java.awt.*;

public class VisualizerPanel extends Pane {
  private LeapListener leapListener;
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
    leapController.addListener(leapListener);

    // set up canvas, add to panel
    root = new AnchorPane();
    canvas = new Canvas(width, height);
    g = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);
    getChildren().add(root);

    leapListener.getFingerLocs().addListener(new ChangeListener<Point2D[]>() {
      @Override
      public void changed(ObservableValue observableValue, Point2D[] points1, final Point2D[] points2) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            // reset canvas
            g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // draw fingers
            g.setFill(Color.DEEPSKYBLUE);
            for (int i = 0; i < points2.length; i++) {
              if (points2[i] == null)
                break;

              g.fillOval(points2[i].getX(), points2[i].getY(), 30, 50);
            }

            // draw hands
            Point2D[] handLocs = leapListener.getHandLocs().getValue();
            g.setFill(Color.AQUAMARINE);
            if (handLocs != null) {
              for (int i = 0; i < handLocs.length; i++) {
                if (handLocs[i] == null)
                  break;

                g.fillOval(handLocs[i].getX(), handLocs[i].getY(), 70, 70);
              }
            }
          }
        });
      }
    });
  }
}

package frontend;

/**
 * MainFrame
 * the main GUI frame for visualizing and controlling the leap conductor
 * @author Arun Varma
 */

import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

public class MainFrame extends Application {
  BorderPane root;
  VisualizerPanel visualizer;
  ControlPanel controlPanel;
  HBox buttonBox;
  Button resetButton, startButton, closeButton;

  /**
   * start
   */
  @Override
  public void start(Stage primaryStage) {
    // set title
    primaryStage.setTitle("Leap Conductor");

    // setup buttons
    resetButton = new Button("     Reset     ");
    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
    resetButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ;
      }
    });

    startButton = new Button("                Start                ");
    //startButton.setPrefWidth(primaryStage.getWidth() / 4);
    startButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ;
      }
    });

    closeButton = new Button("     Close     ");
    //closeButton.setPrefWidth(primaryStage.getWidth() / 4);
    closeButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Node eventSource = (Node) event.getSource();
        Stage winStage  = (Stage) eventSource.getScene().getWindow();
        winStage.close();
      }
    });

    // setup inner panels
    visualizer = new VisualizerPanel(1200, 800);

    controlPanel = new ControlPanel();

    buttonBox = new HBox();
    buttonBox.setSpacing(100);
    buttonBox.setPadding(new Insets(0, 20, 10, 20));
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(resetButton, startButton, closeButton);

    // setup main frame components
    root = new BorderPane();
    root.setCenter(visualizer);
    root.setRight(controlPanel);
    root.setBottom(buttonBox);
    primaryStage.setScene(new Scene(root, 1200, 800));
    primaryStage.show();
  }

  /**
   * caller
   * launches the main frame
   * @param args
   */
  public void caller(String[] args) {
    launch(args);
  }
}

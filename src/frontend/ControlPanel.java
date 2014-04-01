package frontend;

/**
 * ControlPanel
 * a control panel for the leap conductor gui
 * @author Arun Varma
 */

import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;

public class ControlPanel extends VBox {
  private HBox volumeHBox, pitchHBox, resonanceHBox;
  private Slider volumeSlider, pitchSlider, resonanceSlider;
  private Label volumeLabel, volumeVal, pitchLabel, pitchVal, resonanceLabel, resonanceVal;
  private final int INIT_VOLUME = 75;
  private final int INIT_PITCH = 50;
  private final int INIT_RESONANCE = 0;

  /**
   * ControlPanel
   */
  public ControlPanel() {
    setPadding(new Insets(100, 40, 0, 0));
    setSpacing(60);

    // volume control slider
    volumeHBox = new HBox();
    volumeHBox.setAlignment(Pos.CENTER_RIGHT);
    volumeSlider = new Slider(0, 100, INIT_VOLUME);
    volumeSlider.setOrientation(Orientation.HORIZONTAL);
    volumeSlider.setPrefWidth(150);
    volumeLabel = new Label("Volume");
    volumeVal = new Label("" + INIT_VOLUME);
    volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        volumeVal.setText("" + newValue.intValue());
      }
    });
    volumeHBox.getChildren().add(volumeLabel);
    volumeHBox.getChildren().add(volumeSlider);
    volumeHBox.getChildren().add(volumeVal);

    // pitch control slider
    pitchHBox = new HBox();
    pitchHBox.setAlignment(Pos.CENTER_RIGHT);
    pitchSlider = new Slider(0, 100, INIT_PITCH);
    pitchSlider.setOrientation(Orientation.HORIZONTAL);
    pitchSlider.setPrefWidth(150);
    pitchLabel = new Label("Pitch");
    pitchVal = new Label("" + 0);
    pitchSlider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        pitchVal.setText("" + (newValue.intValue() - INIT_PITCH));
      }
    });
    pitchHBox.getChildren().add(pitchLabel);
    pitchHBox.getChildren().add(pitchSlider);
    pitchHBox.getChildren().add(pitchVal);

    // pitch control slider
    resonanceHBox = new HBox();
    resonanceHBox.setAlignment(Pos.CENTER_RIGHT);
    resonanceSlider = new Slider(0, 100, INIT_RESONANCE);
    resonanceSlider.setOrientation(Orientation.HORIZONTAL);
    resonanceSlider.setPrefWidth(150);
    resonanceLabel = new Label("Resonance");
    resonanceVal = new Label("" + INIT_RESONANCE);
    resonanceSlider.valueProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        resonanceVal.setText("" + newValue.intValue());
      }
    });
    resonanceHBox.getChildren().add(resonanceLabel);
    resonanceHBox.getChildren().add(resonanceSlider);
    resonanceHBox.getChildren().add(resonanceVal);

    // add components to panel
    getChildren().add(volumeHBox);
    getChildren().add(pitchHBox);
    getChildren().add(resonanceHBox);
  }
}

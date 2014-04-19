package backend.audio;

import java.io.File;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class TemporaryTest extends Application{

	static String url;
	private final StringProperty album = new SimpleStringProperty(this, "album");
	  private final StringProperty artist = new SimpleStringProperty(this,"artist");
	  private final StringProperty title = new SimpleStringProperty(this, "title");
	  private final StringProperty year = new SimpleStringProperty(this, "year");
	  
	public static void main(String[] args) throws MalformedURLException{
		url = args[0];
		Application.launch();
	}
	
	public void setAlbum(String a){ album.setValue(a);}
	public void setArtist(String a) { artist.setValue(a);}
	public void setTitle(String a) { title.setValue(a);}
	public void setYear(String a) { year.setValue(a);}



	@Override
	public void start(Stage primaryStage) throws Exception {

		
	    Media m = new Media(new File(url).toURI().toString());
	    m.getMetadata().addListener(new MapChangeListener<String, Object>() {
	        @Override
	        public void onChanged(Change<? extends String, ? extends Object> ch) {
	          if (ch.wasAdded()) {
	            handleMetadata(ch.getKey(), ch.getValueAdded());
	          }
	        }
	      });
	    

	    System.out.println(album);
	    System.out.println(artist);
	   // System.out.println("Artist is :" + x.get("artist"));
	    
	    final MediaPlayer mp = new MediaPlayer(m);
	    
		Button startButton = new Button("Start");
	    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
	    startButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        mp.play();
	      }
	    });
	    
	    Button speedButton = new Button("Speed Up");
	    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
	    speedButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        mp.setRate(mp.getRate() + .5);
	        System.out.println("Rate is : " + mp.getRate());
	      }
	    });
	    
	    Button slowButton = new Button("Slow Down");
	    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
	    slowButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        mp.setRate(mp.getRate() - .5);
	        System.out.println("Rate is : " + mp.getRate());
	      }
	    });
	    
	    Button upButton = new Button("Volume Up");
	    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
	    upButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        mp.setVolume(mp.getVolume() + 0.1);
	        System.out.println("Vol is : " + mp.getVolume());
	      }
	    });
	    
	    Button downButton = new Button("Volume Down");
	    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
	    downButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        mp.setVolume(mp.getVolume() - 0.1);
	        System.out.println("Vol is : " + mp.getVolume());

	      }
	    });
	    
	    Button stopButton = new Button("Stop");
	    //resetButton.setPrefWidth(primaryStage.getWidth() / 2);
	    stopButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override
	      public void handle(ActionEvent event) {
	        mp.pause();
	      }
	    });
	    
	    
		BorderPane root = new BorderPane();
		BorderPane mid = new BorderPane();
		mid.setRight(speedButton);
		mid.setLeft(slowButton);
		
		root.setRight(startButton);
		root.setLeft(stopButton);
		root.setTop(upButton);
		root.setBottom(downButton);
		root.setCenter(mid);
		
		primaryStage.setScene(new Scene(root, 1200, 800));
	    primaryStage.show();
	    
	    mp.play();
	    /*
	    Scanner s = new Scanner(System.in);
	    while(s.hasNext()){
	    	String line = s.nextLine();
	    	if (line.equals("exit")){
	    		System.out.println("exit");
	    		break;
	    	}
	    	if (line.equals("stop")){
	    		System.out.println("stop");
	    		mp.pause();
	    	}
	    	if (line.equals("play")){
	    		System.out.println("HERE");
	    		mp.play();
	    	}
	    }
	    s.close();
	    */
	    
	}
	
	
	 private void handleMetadata(String key, Object value) {
		 System.out.println("HERE!");
		    if (key.equals("album")) {
		      setAlbum(value.toString());
		    } else if (key.equals("artist")) {
		      setArtist(value.toString());
		    } if (key.equals("title")) {
		      setTitle(value.toString());
		    } if (key.equals("year")) {
		      setYear(value.toString());
		    } 
		  }
		}


package scraps;


public class AudioMain {

	public static void main(String[] args) {
		FileLoader fl = new FileLoader();
		FileLoader.loadFile("leap-conductor/src/sounds/EMBRZ.wav");
		fl.start();
		fl.playSong();
		for (int i = 0; i < 1000000; i++) {
			System.out.println("playing");
		}
		fl.pauseSong();
		for (int j = 0; j < 1000000; j++) {
			System.out.println("paused");
		}
		fl.playSong();
		for (int k = 0; k < 1000000; k++) {
			System.out.println("playing");
		}
		fl.stopSong();
		for (int l = 0; l < 1000000; l++) {
			System.out.println("stopped");
		}
		fl.playSong();
		for (int m = 0; m < 1000000; m++) {
			System.out.println("playing");
		}
		fl.quit();
	}

}

package backend.audio;

import java.io.File;
import java.nio.ByteBuffer;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

/**
 * This class loads a sound file allows the file to be
 * played and paused
 * 
 * @author abok
 *
 */
public class FileLoader extends Thread {
	
    public static final AL al = ALFactory.getAL();

    // Buffers hold sound data.
    private static int[] buffer = new int[1];;

    // Sources are points emitting sound.
    private static int[] source = new int[1];
    
    // Position of the source sound.
    private static float[] sourcePos = { 0.0f, 0.0f, 0.0f };

    // Velocity of the source sound.
    private static float[] sourceVel = { 0.0f, 0.0f, 0.0f };

    // Position of the listener.
    private static float[] listenerPos = { 0.0f, 0.0f, 0.0f };

    // Velocity of the listener.
    private static float[] listenerVel = { 0.0f, 0.0f, 0.0f };

    // Orientation of the listener. (first 3 elements are "at", second 3 are "up")
    private static float[] listenerOri = { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f };
	
    // Action performed (1: play, 2: pause, 3: stop, 4: quit)
    private static int action = 0;
    
    // True when action on song is called
    private static boolean actionPerformed = false;
    
	/**
	 * Constructor. Will prompt the user to look for sound files on their computer and load them into songs
	 */
	public FileLoader() {
		//TODO: prompt user with file options to load
		//call loadALData() on choice
	}
	
	/**
	 * 
	 * @param file path to .wav file
	 * loads sounds data form file described by input and then create buffer objects
	 * @return int AL.AL_TRUE if no errors, AL.AL_FALSE otherwise
	 */
    public static int loadALData(String file) {
    	
    	if (!file.endsWith(".wav")) {
    		throw new IllegalArgumentException("ERROR: Sound file must be .wav format.");
    	}
   
        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];

        // Load wav data into a buffer.
        al.alGenBuffers(1, buffer, 0);
        if (al.alGetError() != AL.AL_NO_ERROR)
            return AL.AL_FALSE;

        ALut.alutLoadWAVFile(file, format, data, size, freq, loop);
        al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
        
        // Bind buffer with a source.
        al.alGenSources(1, source, 0);

        if (al.alGetError() != AL.AL_NO_ERROR)
            return AL.AL_FALSE;

        al.alSourcei(source[0], AL.AL_BUFFER, buffer[0]);
        al.alSourcef(source[0], AL.AL_PITCH, 1.0f);
        al.alSourcef(source[0], AL.AL_GAIN, 1.0f);
        al.alSourcefv(source[0], AL.AL_POSITION, sourcePos, 0);
        al.alSourcefv(source[0], AL.AL_VELOCITY, sourceVel, 0);
        al.alSourcei(source[0], AL.AL_LOOPING, loop[0]);

        
        // Do another error check and return.
        if(al.alGetError() == AL.AL_NO_ERROR)
            return AL.AL_TRUE;

        return AL.AL_FALSE;
    } //end loadALData()
	
    /**
     * updates listener properties
     */
    private static void setListenerValues() {
        al.alListenerfv(AL.AL_POSITION,	listenerPos, 0);
        al.alListenerfv(AL.AL_VELOCITY,    listenerVel, 0);
        al.alListenerfv(AL.AL_ORIENTATION, listenerOri, 0);
    }
    
    /**
     * shutdown procedure
     * release memory and audio devices in use
     */
    private static void killALData() {
        al.alDeleteBuffers(1, buffer, 0);
        al.alDeleteSources(1, source, 0);
        ALut.alutExit();
    }
    
    /**
     * plays loaded file
     */
    public synchronized void playSong() {
    	action = 1;
    	actionPerformed = true;
    }
    
    /**
     * pausing song currently playing
     */
    public synchronized void pauseSong() {
    	action = 2;
    	actionPerformed = true;
    }
    
    /**
     * stops the song currently playing
     */
    public synchronized void stopSong() {
    	action = 3;
    	actionPerformed = true;
    }
    
    /**
     * stops execution of the thread
     *
     */
    public synchronized void quit() {
    	action = 4;
    }
    
    /**
     * 
     * @param file
     * makes a call to load sound data of file 
     * described by input and sets up exit procedure
     */
    public static void loadFile(String file) {
    	ALut.alutInit();
        al.alGetError();
        
        // Load the wav data and exit if an error occurs
        if (loadALData(file) == AL.AL_FALSE)
            System.exit(-1);

        setListenerValues();

        // Setup an exit procedure
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(
            new Thread(
                new Runnable() {
                    public void run() {
                        killALData();
                    }
                }
            )
        );
        
    } //end loadFile()
    
    /**
     * the thread runs a loop that only exits when exit() is called
     * until exit() is called, the user can play, pause, or stop the song
     */
    public void run() {
    	while (action != 4) {
    		if (actionPerformed) {
    			switch (action) {
    				case 1:
    					al.alSourcePlay(source[0]);
    					break;
    				case 2:
    					al.alSourcePause(source[0]);
    					break;
    				case 3:
    					al.alSourceStop(source[0]);
    					break;   	
    			} //end switch
    			actionPerformed = false;
    		} //end if
    	} //end while
    }
    
}

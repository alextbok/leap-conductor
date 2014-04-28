package frontend.soundpanel;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import backend.FileProcessor;

/**
 * An JFileChooser wrapper with leap-conductor specific functionality
 * @author abok
 */

public class FileChooser {

	/*Will have one file chooser associated with the program*/
	private static final JFileChooser fc = new JFileChooser();
	
	/*The directory to start at. Will be set after the first song is added*/
	private static File _currentDirectory;
	
	/**
	 * Empty constructor
	 */
	public FileChooser() {
		/*do nothing*/
	}
	
	/**
	 * Shows the JFileChooser as a pop up and solicits a song choice(s) from the user
	 * Returns empty array of Files if none are chosen
	 */
	public static File[] getSongsFromUser() {
		
		//if we haven't added a song yet, set the current directory to the one with the most music files
	    fc.setCurrentDirectory(FileProcessor.getFolderWithMostMusicFiles());
		if (_currentDirectory != null)
			fc.setCurrentDirectory(_currentDirectory);
	    
	    //only accept files with .mp3, .mp4, .m4a, .wav extensions
	    fc.setAcceptAllFileFilterUsed(false);
	    fc.setFileFilter(new FileNameExtensionFilter("Music Files", "mp3", "m4a", "mp4", "wav"));

	    
	    //allow multiple files to be selected
	    fc.setMultiSelectionEnabled(true);
	    
	    //gets the user's choice when dialog is closed (0 == Save == APPROVE_OPTION, 1 == cancel)
		int choice = fc.showOpenDialog(fc.getParent());

		//when the user adds a file, save the folder for next song addition
		if (choice == JFileChooser.APPROVE_OPTION) {
			_currentDirectory = fc.getCurrentDirectory();
			return fc.getSelectedFiles();
		}
		return new File[0];
	}
	
	/**
	 * 
	 * @return Array of all .music files in the /sounds/directory
	 */
	public static File[] getAllMusicFiles() {
		
		String dir = System.getProperty("user.dir") + "/src/data/sounds/";
		File folder = new File(dir);
		//get all of the files in the src/sounds/ folder
		File[] files = folder.listFiles();
		
		ArrayList<File> returnList = new ArrayList<File>();

		for (File file : files) {
			String name = file.getName();
			//if the file name has one of the below extensions, add it to the return array
			if (name.endsWith(".mp3") ||
				name.endsWith(".mp4") ||
				name.endsWith(".m4a") ||
				name.endsWith(".wav"))
			{
				returnList.add(file);
			}
		}
		return returnList.toArray(new File[returnList.size()]);
	}
	
}

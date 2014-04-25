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
	    
	    //only accept files with .mp3 extension
	    fc.setFileFilter(new FileNameExtensionFilter("*.mp3", "mp3"));
	    fc.setAcceptAllFileFilterUsed(false);
	    
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
	 * @return Array of all .mp3 files in the /sounds/directory
	 */
	public static File[] getAllMp3Files() {
		
		String dir = System.getProperty("user.dir") + "/src/sounds/";
		File folder = new File(dir);
		
		//get all of the files in the src/sounds/ folder
		File[] files = folder.listFiles();
		
		ArrayList<File> returnList = new ArrayList<File>();

		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			//if the file name has a .mp3 extension, add it to the return array
			if (name.length() >= 4 && name.substring(name.length() - 4, name.length()).equals(".mp3"))
				returnList.add(files[i]);
		}
		return returnList.toArray(new File[returnList.size()]);
	}
	
	
	
	
	
}

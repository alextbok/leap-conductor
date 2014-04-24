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
		
		//set the current directory to the one with the most mp3 files
	    fc.setCurrentDirectory(FileProcessor.getFolderWithMostMp3Files());
	    
	    //only accept files with .mp3 extension
	    fc.setFileFilter(new FileNameExtensionFilter("*.mp3", "mp3"));
	    fc.setAcceptAllFileFilterUsed(false);
	    
	    //allow multiple files to be selected
	    fc.setMultiSelectionEnabled(true);
	    
	    //gets the user's choice when dialog is closed (0 == Save == APPROVE_OPTION, 1 == cancel)
		int choice = fc.showOpenDialog(fc.getParent());

		if (choice == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFiles();
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

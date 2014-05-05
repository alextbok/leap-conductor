package frontend.soundpanel;

/**
 * Maintains a list of songs that can be played (by double clicking the song name)
 * Since we only have one song list to show the user for the life of the program,
 * the class contains a final list and static methods so we do not have to pass objects
 * around panels (i.e. do not have to break encapsulation)
 * @author abok
 *
 */

import hub.*;

import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class SongList {

	/*Have one song list so we can use this class statically (and keep encapsulation)*/
	private final static JList<String> list = new JList<String>();
	private final static DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	/*Keeps track of our files. Map : String (file name) -> File (associated file)*/
	private final static HashMap<String, File> musicFiles = new HashMap<String, File>();
	
	/*Keeps track of the current song playing*/
	private static File _currentSong;
	
	/**
	 * Constructor - does nothing. This class is intended to be used statically
	 */
	public SongList() {
		/*do nothing*/
	}
	
	/**
	 * Appends songs onto the list - duplicates are ignored
	 * @param song, array of song titles
	 */
	public static void addSong(File song) {
        String audioFileName;
        try {
            audioFileName = AudioFileIO.read(song).getTag().getFirst(FieldKey.TITLE);
            if (audioFileName.length() == 0)
                audioFileName = song.getName();
        } catch (Exception e) {
            audioFileName = song.getName();
        }

		if (!listModel.contains(audioFileName)) {
            musicFiles.put(audioFileName, song);
            listModel.addElement(audioFileName);
            list.setModel(listModel);
		}
	}
	
	/**
	 * Removes the selected songs from the list
	 */
	public static void removeSelectedSongs() {
		
		//if there is a song(s) selected
		if (list.getSelectedValue() != null) {
			
			//get all selected songs
			List<String> selectedSongs = list.getSelectedValuesList();
			
			//remove all selected songs
			for (int i = 0; i < selectedSongs.size(); i++) {
				listModel.removeElement(selectedSongs.get(i));
			}
			
			//reset the list model and set the selected index to 0
			list.setModel(listModel);
			list.setSelectedIndex(0);
		}
	}
	
	/**
	 * Returns our text field so we can add it to the panel
	 * @return 
	 */
	public static JList<String> getListContainer() {
		return list;
	}
	
	public static JScrollPane getScrollableList() {
		
	   	addListMouseListener();
	   	
	   	//customize our list
		list.setFont(new Font("Courier", Font.BOLD, 12));
		list.setBackground(SongPanel.BACKGROUND_COLOR);
		list.setVisible(true);

		//make our scroll pane and its border
		JScrollPane scrollPane = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(200,90));
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(192,193,194), 2, true), "Songs");
		border.setTitleFont(new Font("Courier", Font.BOLD, 12));
		border.setTitleColor(new Color(135,136,138));
	   	scrollPane.setBorder(border);
		scrollPane.setBackground(SongPanel.BACKGROUND_COLOR);

		return scrollPane;
	}

	
	/**
	 * Returns the currently selected song, returns first song in list if no song is selected
	 * Returns null if no files are in the list
	 * Used in SoundPanel class (for play button click)
	 */
	public static File getCurrentlySelectedSong() {
		if (list.getSelectedValue() == null) {
			list.setSelectedIndex(0);
			return musicFiles.get( listModel.get(0) );
		}
		return musicFiles.get(list.getSelectedValue());
	}
	
	/**
	 * Returns all songs in the list so we can save file paths when the GUI closes
	 */
	public static String[] getAllSongs() {
		ArrayList<String> files = new ArrayList<String>();
		HashSet<String> songNames = new HashSet<String>(Collections.list(listModel.elements()));
		//if a song in our musicFiles (all added/deleted files) is currently in the song list, add it
		for (String name: musicFiles.keySet()) {
			if (songNames.contains(name)) {
				files.add(musicFiles.get(name).getAbsolutePath());
			}
		}
		return files.toArray(new String[files.size()]);
	}
	
	/**
	 * Sets the current song - called from Song Panel on play button push
	 * @param song
	 */
	public static void setCurrentSong(File song) {
		_currentSong = song;
	}
	
	/**
	 * Returns the next song in the song list - wraps
	 * If no song is being played, return the first song in the list
	 * @return
	 */
	public static File getNextSong() {
		
		if (_currentSong == null) {
			_currentSong = musicFiles.get(listModel.elementAt(0));
			list.setSelectedIndex(0);
			return _currentSong;
		}

        String toGet;
        try {
            toGet = AudioFileIO.read(_currentSong).getTag().getFirst(FieldKey.TITLE);
            if (toGet.length() == 0)
                toGet = _currentSong.getName();
        } catch (Exception e) {
            toGet = _currentSong.getName();
        }

		int currentIndex = listModel.indexOf(toGet);
		int nextIndex = (currentIndex + 1) % listModel.size();
		list.setSelectedIndex(nextIndex);
		
		String songName = listModel.elementAt(nextIndex);
		_currentSong = musicFiles.get(songName);
		
		return _currentSong;
	}
	
	/**
	 * Returns the previous song in the song list - wraps
	 * If no song is being played, return the last song in the list
	 * @return
	 */
	public static File getPreviousSong() {

		if (_currentSong == null){
			_currentSong = musicFiles.get(listModel.elementAt(listModel.size() - 1) );
			list.setSelectedIndex(listModel.size() - 1);
			return _currentSong;
		}

        String toGet;
        try {
            toGet = AudioFileIO.read(_currentSong).getTag().getFirst(FieldKey.TITLE);
            if (toGet.length() == 0)
                toGet = _currentSong.getName();
        } catch (Exception e) {
            toGet = _currentSong.getName();
        }

		int currentIndex = listModel.indexOf(toGet);
		int prevIndex = currentIndex - 1;
		
		if (prevIndex < 0) {
			prevIndex = listModel.size() - 1;
		}
		
		list.setSelectedIndex(prevIndex);
        
		String songName = listModel.elementAt(prevIndex);
		_currentSong = musicFiles.get(songName);

		return _currentSong;
	}
	
	/**
	 * Adds mouse listener to list that listens for double clicks
	 */
	private static void addListMouseListener() {
		list.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//if the user double clicks, set and play the song
				if (e.getClickCount() == 2) {
					SoundController.stopSong(); //stop what is currently playing
					File song = musicFiles.get(list.getSelectedValue());
					_currentSong = song;
					SoundController.setSong(song);
					SoundController.resetValues();
					SoundController.playSong();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {	}
			
		});
	}


	
}

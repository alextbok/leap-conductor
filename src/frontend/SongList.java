package frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import backend.audio.SongApp;

/**
 * Maintains a list of songs that can be played (by double clicking the song name)
 * Since we only have one song list to show the user for the life of the program,
 * the class contains a final list and static methods so we do not have to pass objects
 * around panels (i.e. do not have to break encapsulation)
 * @author abok
 *
 */
public class SongList {

	/*Have one song list so we can use this class statically (and keep encapsulation)*/
	private final static JList<String> list = new JList<String>();
	private final static DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	/*Keeps track of our files. Map : String (file name) -> File (associated file)*/
	private final static HashMap<String, File> mp3Files = new HashMap<String, File>();
	
	/**
	 * Constructor - does nothing. This class is intended to be used statically
	 */
	public SongList() {
		/*do nothing*/
	}
	
	/**
	 * Appends songs onto the list - duplicates are ignored
	 * @param songs, array of song titles
	 */
	public static void addSong(File song) {
		if (!listModel.contains(song.getName())) {
			mp3Files.put(song.getName(), song);
			listModel.addElement(song.getName());
			list.setModel(listModel);
		}
	}
	
	/**
	 * Removes the selected song from the list
	 */
	public static void removeSelectedSong() {
		if (list.getSelectedValue() != null) {
			listModel.removeElement(list.getSelectedValue());
			list.setModel(listModel);
			list.setSelectedIndex(0);
		}
	}
	
	/**
	 * Returns our text field so we can add it to the panel
	 * @return 
	 */
	public static JList<String> getListContainer() {
		customizeList();
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
		scrollPane.setPreferredSize(new Dimension(200,80));
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(192,193,194), 2, true), "Songs");
		border.setTitleFont(new Font("Courier", Font.BOLD, 12));
		border.setTitleColor(new Color(135,136,138));
	   	scrollPane.setBorder(border);
		scrollPane.setBackground(SongPanel.BACKGROUND_COLOR);

		return scrollPane;
		//return new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	/**
	 * Factors out code that customizes our JTextField
	 */
	private static void customizeList() {

	}
	
	/**
	 * Returns the currently selected song, returns first song in list if no song is selected
	 * Returns null if no files are in the list
	 * Used in SoundPanel class (for play button click)
	 */
	public static File getCurrentlySelectedSong() {
		if (list.getSelectedValue() == null) {
			list.setSelectedIndex(0);
			return mp3Files.get( listModel.get(0) );
		}
		return mp3Files.get(list.getSelectedValue());
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
					SongApp.stopSong(); //stop what is currently playing
					SongApp.setSong(mp3Files.get(list.getSelectedValue()));
					SongApp.playSong();
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
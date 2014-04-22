package frontend;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import backend.FileProcessor;

@SuppressWarnings("serial")
public class LeapConductorPopup extends JDialog {

	public LeapConductorPopup(String fileName) {
		showLeapConductorPopup(fileName);
	}
	
	/**
	 * Displays our customized popup (JDialog) to the user
	 * Contents shown taken from file with input file name
	 * 
	 */
	public void showLeapConductorPopup(String fileName) {
		
		PopupTextArea textArea = new PopupTextArea(10, 50);
		
		//get the text from the file and display it
		String textToShow = FileProcessor.getTextFromFile(fileName);
		textArea.append(textToShow);
		
		//show top of file (instead of starting at bottom)
		textArea.setCaretPosition(0);
		
		//make the text area scrollable
		JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		//show the JDialog instance
		this.setModal(true);
		this.add(scrollPane);
		this.setSize(800,500);
		this.setVisible(true);
	}
	
	/**
	 * Private class that implements a non-editable text area
	 * @author abok
	 *
	 */
	private class PopupTextArea extends JTextArea {
		
		/**
		 * Constructor - creates our customized JTextArea
		 */
		public PopupTextArea(int r, int c) {
			super(r,c);
			this.setBackground(SongPanel.BACKGROUND_COLOR);
			this.setEditable(false);
		}

	}
}

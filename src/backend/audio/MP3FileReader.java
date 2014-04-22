package backend.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MP3FileReader extends RandomAccessFile {

	public MP3FileReader(String fileName) throws FileNotFoundException {
		super(fileName, "r");
		this.getTag();
	}
	
	public void getTag() {
		try {
			byte[] b = new byte[(int) this.length()];
			this.readFully(b);
			
			for (int i = 0; i < b.length/100000; i++) {
				String s = new String(b, i, i + 3);
				System.out.println(s);
				if (s.equals("TCO")) {
					String s1 = new String(b, i, i + 20);
					System.out.println(s1);
				}

			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

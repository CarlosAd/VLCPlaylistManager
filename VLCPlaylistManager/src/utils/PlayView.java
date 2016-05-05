package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import model.SongList;

public class PlayView implements Runnable {
	private SongList songList;
	
	public PlayView (SongList songList) {
		this.songList = songList;
	}

	@Override
	public void run() {
		try {
			File f = new File("/home/carlos/index.html");
			PrintWriter printWriter = new PrintWriter(f);
			if (this.songList != null) {
				printWriter.write("<head>\n"
						+ "<title>VLCPlayList.html</title>\n"
						+ "</head>\n" + this.songList.toString());
			}
			else {
				printWriter.write("<head>\n"
						+ "<title>VLCPlayList.html</title>\n"
						+ "</head>\n");
			}
			printWriter.close();
			Runtime.getRuntime().exec("/home/carlos/refresh.sh");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SongList getSongList() {
		return songList;
	}

	public void setSongList(SongList songList) {
		this.songList = songList;
	}
}

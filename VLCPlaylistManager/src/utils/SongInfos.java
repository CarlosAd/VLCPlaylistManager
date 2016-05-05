package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import model.Song;

public class SongInfos {
	private TreeMap<String, String> infos;
	private String fileName;
	
	public SongInfos() throws IOException {
		this.fileName = "/media/carlos/2D84E58319553395/home/Music/Playlists/song_infos.txt";
		String key = null;
		String value = null;
		infos = new TreeMap<String, String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			while ((key = reader.readLine()) != null) {
				value = reader.readLine();
				
				infos.put(key, value);
			}
			
			reader.close();
		}
		catch (FileNotFoundException e) {
			File f = new File(fileName);
			f.createNewFile();
		}
	}
	
	public TreeMap<String, String> getInfos() {
		return this.infos;
	}
	
	public void put(String songPath, String songDate) {
		this.infos.put(songPath, songDate);
	}
	
	public void save() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(this.fileName);
		
		for (Map.Entry<String, String> entry : this.infos.entrySet()) {
			writer.println(entry.getKey());
			writer.println(entry.getValue());
		}
		
		writer.close();
	}

	public boolean isSaved(Song currentSong) {
		return this.infos.containsKey(currentSong.getPath());
	}
}
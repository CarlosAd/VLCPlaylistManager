package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TreeSet;

import utils.EntryComparator;
import utils.SongInfos;

public class PlayList extends Entry {
	private TreeSet<Song> songs;
	
	public PlayList(String directoryName, String fileName, SongInfos songInfos) throws ParseException {
		FileReader fileReader;
		Song song = null;
		String line = null;
		this.lastPlayedDate = Calendar.getInstance();
		this.lastPlayedDate.set(2000, 0, 1, 0, 0, 0);
		
		try {
			songs = new TreeSet<Song>(new EntryComparator());
			this.path = directoryName + fileName;
			
			fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			
			while ((line = reader.readLine()) != null) {
				if ((line.charAt(0) == '#') && (line.charAt(1) == 'E')) {
					continue;
				}
				else {
					song = new Song(directoryName, line);
					song.setLastPlayedDate(songInfos.getInfos().get(song.getPath()));
					if (this.lastPlayedDate.before(song.getLastPlayedDate())) {
						this.lastPlayedDate.setTime(song.getLastPlayedDate().getTime());
					}
					
					songs.add(song);
				}
			}
			
//			this.lastPlayedDate.setTime(this.songs.last().getLastPlayedDate().getTime());
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TreeSet<Song> getSongs() {
		return songs;
	}

	public void setSongs(TreeSet<Song> songs) {
		this.songs = songs;
		this.lastPlayedDate.setTime(this.songs.first().getLastPlayedDate().getTime());
	}

	@Override
	public TreeSet<Song> open() {
		return this.songs;
	}

	@Override
	public Song nextSong() throws ParseException {
		long totalSecondsFromLastPlay = 0;
		for (Song song : this.songs) {
			totalSecondsFromLastPlay += song.getSecondsFromLastPlay();
		}
		
		long selector = (long)((new Random()).nextDouble() * totalSecondsFromLastPlay) + 1;
		long sum = 0;
		
		for (Song song : this.songs) {
			sum += song.getSecondsFromLastPlay();
			
			if (selector <= sum) {
				song.setLastPlayedDate((new Date()).toString());
				return song;
			}
		}
		
		return null;
	}
}

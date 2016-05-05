package model;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import model.Song;
import utils.EntryComparator;
import utils.M3uFilter;
import utils.SongInfos;

public class SongList {
	private List<Entry> entries;
	private SongInfos songInfos;
	private EntryComparator entryComparator;
	
	public SongList(SongInfos songInfos) throws ParseException, IOException {
		entries = new ArrayList<Entry>();
		this.songInfos = songInfos;
		entryComparator = new EntryComparator();
		
		String path = "/media/carlos/2D84E58319553395/home/Music/Playlists/";
		File dir = new File(path);
		String[] fileNames = dir.list(new M3uFilter());
		
		for (String fileName : fileNames) {
			entries.add(new PlayList(path, fileName, songInfos));
		}
		
		entries.sort(this.entryComparator);
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
	public String toString() {
		String result = "";
		
		for (Entry entry : this.entries) {
			result += (entry.toString() + "</br>");
		}
		
		return result;
	}
	
	public SongInfos getSongInfos() {
		return this.songInfos;
	}

	public Song nextSong() throws ParseException {
		long totalSecondsFromLastPlay = 0;
		for (Entry entry : this.entries) {
			totalSecondsFromLastPlay += entry.getSecondsFromLastPlay();
		}
		
		long selector = (long)((new Random()).nextDouble() * totalSecondsFromLastPlay) + 1;
		long sum = 0;
		
		System.out.println(selector);
		
		for (Iterator<Entry> iterator = this.entries.iterator(); iterator.hasNext(); ) {
			Entry entry = iterator.next();
			sum += entry.getSecondsFromLastPlay();
			
			if (selector <= sum) {
				iterator.remove();
				Song next = entry.nextSong();
				this.entries.addAll(entry.open());
//				if (toEnter.size() > 1) {
					this.entries.sort(this.entryComparator);
//				}
				
//				Song next = entry.nextSong();
				return next;
			}
		}
		
//		for (Entry entry : this.entries) {
//			sum += entry.getSecondsFromLastPlay();
//			System.out.println("Sum: " + sum + "Comparison: " + (selector <= sum));
//			
//			if (selector <= sum) {
//				System.out.println(this.entries.remove(entry));
//				entries.addAll(entry.open());
//				Song next = entry.nextSong();
//				System.out.println("Selected: " + next);
//				return next;
//			}
//		}
		return null;
	}
}
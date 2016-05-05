package model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

import model.Entry;
import utils.EntryComparator;

public class Song extends Entry {
	private boolean isSaved;

	public Song(String directoryName, String fileName) {
		this.path = directoryName + fileName;
		this.lastPlayedDate = Calendar.getInstance();
		this.lastPlayedDate.set(2000, 0, 1, 0, 0, 0);
	}

	@Override
	public TreeSet<Song> open() throws ParseException {
//		TreeSet<Song> result = new TreeSet<Song>(new EntryComparator());
//		result.add(this);
//		return result;
//		this.setLastPlayedDate((new Date()).toString());
		TreeSet<Song> result = new TreeSet<Song>(new EntryComparator());
		result.add(this);
		return result;
	}

	@Override
	public Song nextSong() {
		this.lastPlayedDate.setTime(new Date());
		return this;
	}

	public void setPath(String string) {
		this.path = string;
	}
	
	@Override
	public String toString() {
		return ("\t" + super.toString());
	}

	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
}

package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TreeSet;

public abstract class Entry {
	protected String path;
	protected Calendar lastPlayedDate;

	public void setLastPlayedDate(String line) throws ParseException {
		if (line != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
			this.lastPlayedDate.setTime(sdf.parse(line));// all done
		}
	}

	public Calendar getLastPlayedDate() {
		return this.lastPlayedDate;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String toString() {
		return (this.path + "\t" + this.lastPlayedDate.getTime().toString());
	}
	
	public long getSecondsFromLastPlay() {
		return (Calendar.getInstance().getTimeInMillis() - this.lastPlayedDate.getTimeInMillis());
	}

	public abstract TreeSet<Song> open() throws ParseException;

	public abstract Song nextSong() throws ParseException;
}

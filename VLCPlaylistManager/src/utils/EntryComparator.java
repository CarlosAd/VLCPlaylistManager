package utils;

import java.util.Comparator;

import model.Entry;

public class EntryComparator implements Comparator<Entry> {
	@Override
	public int compare(Entry o1, Entry o2) {
		if (o1.getPath().equals(o2.getPath())) {
			return 0;
		}
		else if (o1.getSecondsFromLastPlay() > o2.getSecondsFromLastPlay()) {
			return 1;
		}
		else {
			return -1;
		}
	}
}

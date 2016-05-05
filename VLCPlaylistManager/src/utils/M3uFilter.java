package utils;

import java.io.File;
import java.io.FilenameFilter;

public class M3uFilter implements FilenameFilter {
	@Override
	public boolean accept(File dir, String name) {
		if (name.substring(name.length() - 3, name.length()).equals("m3u")) {
			return true;
		}
		else {
			return false;	
		}
	}
}

import model.Song;

import model.SongList;
import utils.EntryComparator;
import utils.PlayView;
import utils.SongInfos;
import utils.VLCUtils;

public class Application implements Runnable {
	static SongList songList;
//	static Date date;
	
	public static void process() throws Exception {
		SongInfos songInfos = new SongInfos();
		songList = new SongList(songInfos);
//		Date date = new Date();
		Runtime.getRuntime().exec("firefox /home/carlos/index.html");
		(new Thread(new PlayView(songList))).start();
		boolean currentSongIsSaved = false;
		
		Song lastSong = songList.nextSong();
		(new Thread(new PlayView(songList))).start();
		Song currentSong = lastSong;
//		date = new Date();
//		songInfos.put(lastSong.getPath(), (date.toString()));
//		lastSong.setLastPlayedDate(date.toString());
		VLCUtils.addSongToVLC(lastSong);
		EntryComparator entryComparator = new EntryComparator();
		while (true) {
			if (VLCUtils.isVLCConnected()) {
				currentSong = VLCUtils.getCurrentSong();
				try {
					if (entryComparator.compare(lastSong, currentSong) == 0) {
//						System.out.println(VLCUtils.getRemainingTime());
						if (VLCUtils.isPlaying()) {
							if (!currentSongIsSaved) {
								songInfos.put(lastSong.getPath(), lastSong.getLastPlayedDate().getTime().toString());
								currentSongIsSaved = true;
							}
							
							lastSong = songList.nextSong();
							(new Thread(new PlayView(songList))).start();
//							date = new Date();
//							songInfos.put(lastSong.getPath(), lastSong.getPath());
//							lastSong.setLastPlayedDate(date.toString());
							VLCUtils.addSongToVLC(lastSong);
						}
					}
					else {
						currentSongIsSaved = true;
					}
				}
				catch (IllegalArgumentException e) {
					e.printStackTrace();	
					System.out.println("currentSong is null => Illegal Argument\tt" + (currentSong == null));
				}
				catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println("currentSong is null => NullPointer" + (currentSong == null));
				}
			}
			else {
				songInfos.save();
				break;
			}
			
//			if (VLCUtils.receiveStopSignal()) {
//				songInfos.save();
//				break;
//			}
		}
		songInfos.save();
	}

	public static void main(String[] args) throws Exception {
		process();
	}

	@Override
	public void run() {
		try {
			process();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SongList getSongList() {
		return Application.songList;
	}
}

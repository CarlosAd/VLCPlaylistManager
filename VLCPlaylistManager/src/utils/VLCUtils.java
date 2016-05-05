package utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Song;

public class VLCUtils {

	public static boolean isVLCConnected() {
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication ("", "vlc".toCharArray());
		    }
		});

		try {
			URL url = new URL("http", "localhost", 8080, "/requests/playlist.xml");
			URLConnection uc = url.openConnection();
			return (((HttpURLConnection)uc).getResponseCode() == 200);
		}
		catch (Exception e) {	
			return false;
		}
	}
	public static void addSongToVLC(Song song) throws InterruptedException, IOException {
		Runtime runtime = Runtime.getRuntime();
		String[] cmd = {"vlc", "--one-instance", "--playlist-enqueue", ""};
		cmd[3] = song.getPath();
		runtime.exec(cmd);
		Thread.sleep(5000);
	}

	public static Song getCurrentSong() {
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication ("", "vlc".toCharArray());
		    }
		});

		try {	
			URL url = new URL("http", "localhost", 8080, "/requests/playlist.xml");
			URLConnection uc = url.openConnection();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
			Document doc = builder.parse(uc.getInputStream());
			
			NodeList nodeList = doc.getElementsByTagName("leaf");
			Element element = null;
			
			if(nodeList != null && nodeList.getLength() > 0){
			     for (int j = 0; j < nodeList.getLength(); j++) {
			         Element el = (org.w3c.dom.Element) nodeList.item(j);
			         if (el.hasAttribute("current")) {
			        	 element = el;
			         }
			     }
			}
			return new Song(decoder(element.getAttribute("uri").substring(7)), "");
		}
		catch (Exception e) {
			return null;
		}
	}

	public static boolean isPlaying() {
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication ("", "vlc".toCharArray());
		    }
		});
		
		URL url;
		try {
			url = new URL("http", "localhost", 8080, "/requests/status.xml");
			
			URLConnection uc = url.openConnection();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(uc.getInputStream());
			
			NodeList nodeList = doc.getElementsByTagName("state");
			if (nodeList.item(0).getFirstChild().getNodeValue().equals("playing")) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	private static String decoder(String substring) {
		String s = new String();
		for (int i = 0; i < substring.length(); i++){
			if (substring.charAt(i) == '%') {
				int[] codeAndIncrement = UTF8CodeToCharacter(substring, i);
				s += (char)codeAndIncrement[0];
				i += codeAndIncrement[1];
			}
			else {
				s += substring.charAt(i);
			}
		}
		return s;
	}

	private static int[] UTF8CodeToCharacter(String substring, int i) {
		int nextNumber = Integer.parseInt(substring.substring(i+1, i+2), 16);
		String charCode, binaryCodeString, UTF16CodeString;
		int code;
		int[] result = new int[2];
		
		if (nextNumber < 12) {
			charCode = substring.substring(i+1, i+3);
			result[0] = Integer.parseInt(charCode, 16);
			result[1] = 2;
		}
		else if (nextNumber < 14) {
			charCode = substring.substring(i+1, i + 3) + substring.substring(i + 4, i + 6);
			code = Integer.parseInt(charCode, 16);
			binaryCodeString = Integer.toBinaryString(code);
			UTF16CodeString = binaryCodeString.substring(3, 8) + binaryCodeString.substring(10, 16);
			code = Integer.parseInt(UTF16CodeString, 2);
			result[0] = code;
			result[1] = 5;
		}
		else /*if (nextNumber < 15) */{
			charCode = substring.substring(i + 1, i + 3) + substring.substring(i + 4, i + 6) + substring.substring(i + 7, i + 9);
			code = Integer.parseInt(charCode, 16);
			binaryCodeString = Integer.toBinaryString(code);
			UTF16CodeString = binaryCodeString.substring(4, 8) + binaryCodeString.substring(10, 16) + binaryCodeString.substring(18, 24);
			code = Integer.parseInt(UTF16CodeString, 2);
			result[0] = code;
			result[1] = 8;
		}
		return result;
	}

	public static int getRemainingTime() throws SAXException, IOException, ParserConfigurationException {	
		Authenticator.setDefault (new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication ("", "vlc".toCharArray());
		    }
		});
	
		URL url = new URL("http", "localhost", 8080, "/requests/status.xml");
		URLConnection uc = url.openConnection();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(uc.getInputStream());
		
		int totalTime = Integer.parseInt((doc.getElementsByTagName("length")).item(0).getFirstChild().getNodeValue());
		int elapsedTime = Integer.parseInt((doc.getElementsByTagName("time")).item(0).getFirstChild().getNodeValue());
		return (totalTime - elapsedTime);
	}
}

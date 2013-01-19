package lib.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import lib.utils.Utility;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;

public class SongManagerTest {

	
	
	/**
	 * Calculate fingerprint of mp3 file
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args)  {
		SongManager songManager = new SongManager();
		
		// add song to database
		songManager.addSong("Song #1", "Artist #1", "Album #1", 2013L, 22000, "samples/4.mp3");
		
		songManager.addSong("Song #2", "Artist #2", "Album #2", 2013L, 22000, "samples/2.mp3");

		// remove song from database
		songManager.removeSong(1L);
		
		try {
			FingerprintSystem fs = new FingerprintSystem(22000);
			byte[] audio = Utility.fileToByteArray("samples/4.mp3");
			Fingerprint fingerprint = fs.fingerprint(audio);
		
			// search song in database
			System.out.println("Search non-existent song: " + !songManager.searchSongByFingerprint(fingerprint).isEmpty());
			
			audio = Utility.fileToByteArray("samples/2.mp3");
			fingerprint = fs.fingerprint(audio);
			// search song in database
			System.out.println("Search existent song: " + songManager.searchSongByFingerprint(fingerprint).get(0).getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/* 
		FingerprintSystem fs = new FingerprintSystem(22000);
		try {
			byte[] audio = FileToByteArray("samples/4.mp3");
			Fingerprint fingerprint1 = fs.fingerprint(audio);
			// Output fingerprint #1
			System.out.println("Fingerprint:\n" + fingerprint1);
			
			audio = FileToByteArray("samples/3.mp3");
			Fingerprint fingerprint2 = fs.fingerprint(audio);
			
			// Compare 2 different fingerprints ( returns -1.0 as there is no match )
			System.out.println("Comparing 2 different Fingerprints: " + fingerprint1.match(fingerprint2));
			
			// Compare 2 same fingerprints (returns 0.0 as match is form the beginning )
			System.out.println("Comparing 2 same Fingerprints: " + fingerprint1.match(fingerprint1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

}
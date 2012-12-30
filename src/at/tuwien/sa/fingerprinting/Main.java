package at.tuwien.sa.fingerprinting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;

public class Main {

	/**
	 * Read file in byte array
	 * @param name - filename
	 * @return - byte array representation of file
	 * @throws IOException
	 */
	public static byte[] FileToByteArray(String name) throws IOException {
		FileInputStream fis = new FileInputStream(new File(name));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        return bos.toByteArray();
	}
	
	/**
	 * Calculate fingerprint of mp3 file
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args)  {
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
	}

}
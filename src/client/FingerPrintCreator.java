package client;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ac.at.tuwien.infosys.swa.audio.Fingerprint;
import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;


public class FingerPrintCreator {	
	

	public static byte[] FileToByteArray(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        return bos.toByteArray();
	}
	

	public static Fingerprint createFingerPrint(File input){
		
		FingerprintSystem fs = new FingerprintSystem(22000);
		Fingerprint fingerprint1 = null;
		try {
			byte[] audio = FileToByteArray(input);
			fingerprint1 = fs.fingerprint(audio);
			// Output fingerprint #1
			//System.out.println("Fingerprint:\n" + fingerprint1);			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fingerprint1;
		 
	}
}

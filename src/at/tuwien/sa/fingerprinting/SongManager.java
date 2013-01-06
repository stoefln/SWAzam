package at.tuwien.sa.fingerprinting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ac.at.tuwien.infosys.swa.audio.FingerprintSystem;
import at.tuwien.sa.model.dao.FingerprintDAO;
import at.tuwien.sa.model.dao.SongDAO;
import at.tuwien.sa.model.dao.SubFingerprintDAO;
import at.tuwien.sa.model.entities.Fingerprint;
import at.tuwien.sa.model.entities.Song;
import at.tuwien.sa.model.entities.SubFingerprint;

public class SongManager {
	private SongDAO songDAO = new SongDAO();
	private FingerprintDAO fingerprintDAO = new FingerprintDAO();
	private SubFingerprintDAO subFingerprintDAO = new SubFingerprintDAO();
	
	private byte[] FileToByteArray(String name) throws IOException {
		FileInputStream fis = new FileInputStream(new File(name));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
        }
        return bos.toByteArray();
	}
	
	public boolean addSong(String title, String artist, String album, Long year, float sampleRate, String songPath) {
		FingerprintSystem fs = new FingerprintSystem(sampleRate);
		try {
			byte[] audio = FileToByteArray(songPath);
			ac.at.tuwien.infosys.swa.audio.Fingerprint fingerprint = fs.fingerprint(audio);

			Fingerprint dbFingerprint = new Fingerprint();
			dbFingerprint.setStartTime(fingerprint.getStartTime());
			dbFingerprint.setShiftDuration(fingerprint.getShiftDuration());
			List<SubFingerprint> subfingerprints = new ArrayList<SubFingerprint>();
			for (int i = 0; i < fingerprint.getSubFingerprints().length; i++) {
				SubFingerprint subFingerprint = new SubFingerprint();
				subFingerprint.setValue(fingerprint.getSubFingerprints()[i].getValue());
				subFingerprintDAO.persist(subFingerprint);
				subfingerprints.add(subFingerprint);
			}
			dbFingerprint.setSubfingerprints(subfingerprints);
			
			fingerprintDAO.persist(dbFingerprint);
			
			Song song = new Song();
			song.setTitle(title);
			song.setArtist(artist);
			song.setAlbum(album);
			song.setYear(year);
			song.setFingerprint(dbFingerprint);

			songDAO.persist(song);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Song> searchSongByFingerprint(ac.at.tuwien.infosys.swa.audio.Fingerprint fingerprint) {
		List<Song> songs = new ArrayList<Song>();
		HashMap<Long,ac.at.tuwien.infosys.swa.audio.Fingerprint> candidateFingerprints = new HashMap<Long,ac.at.tuwien.infosys.swa.audio.Fingerprint>();
		for (int i = 0; i < fingerprint.getSubFingerprints().length; i++) {
			List<Fingerprint> dbFingerprints = fingerprintDAO.findBySubFingerprintValue(fingerprint.getSubFingerprints()[i].getValue());
			for (int j = 0; j < dbFingerprints.size(); j++) {
				Fingerprint dbFingerprint = dbFingerprints.get(j);
				List<ac.at.tuwien.infosys.swa.audio.SubFingerprint> subfingerprints = new ArrayList<ac.at.tuwien.infosys.swa.audio.SubFingerprint>();
				for (int k = 0; k < dbFingerprint.getSubfingerprints().size(); k++) {
					subfingerprints.add(new ac.at.tuwien.infosys.swa.audio.SubFingerprint(dbFingerprint.getSubfingerprints().get(k).getValue()));	
				}
				ac.at.tuwien.infosys.swa.audio.Fingerprint candidateFingerprint = new ac.at.tuwien.infosys.swa.audio.Fingerprint(dbFingerprint.getStartTime(), dbFingerprint.getShiftDuration(), subfingerprints);
				if ( !candidateFingerprints.containsKey(dbFingerprint.getId()) ) {
					candidateFingerprints.put(dbFingerprint.getId(),candidateFingerprint);
				}
			}
		}
		for (Long id : candidateFingerprints.keySet()) {
			if (fingerprint.match(candidateFingerprints.get(id)) != -1.0 ) {
				songs.add(songDAO.findByID(Song.class, id));
			}
		}
		return songs;
	}
	
	public void removeSong(Long id) {
		Song song = songDAO.findByID(Song.class, id);
		songDAO.remove(song);
	}
}

package at.tuwien.sa.model.interfaces;

import java.util.List;

import at.tuwien.sa.model.entities.Fingerprint;
import at.tuwien.sa.model.entities.Song;

public interface ISongDAO extends IGenericDAO<Song, Long> {
	public List<Song> findByFingerprint(Fingerprint fingerprint);
}

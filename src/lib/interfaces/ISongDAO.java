package lib.interfaces;

import java.util.List;

import lib.entities.Fingerprint;
import lib.entities.Song;


public interface ISongDAO extends IGenericDAO<Song, Long> {
	public List<Song> findByFingerprint(Fingerprint fingerprint);
}

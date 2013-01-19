package lib.dao;

import java.util.List;

import javax.persistence.Query;

import lib.entities.Fingerprint;
import lib.entities.Song;
import lib.interfaces.ISongDAO;
import lib.utils.HibernateUtil;


public class SongDAO extends GenericDaoImpl<Song, Long> implements ISongDAO {
	private static final long serialVersionUID = 3811278862963134245L;

	@Override
	@SuppressWarnings("unchecked")
	public List<Song> findByFingerprint(Fingerprint fingerprint) {
		Query q = HibernateUtil.getEntityManager().createQuery("SELECT s FROM Song as s WHERE s.fingerprint_id = :fingerprint_id").setParameter("fingerprint_id", fingerprint.getId());
		return q.getResultList();
	}

}

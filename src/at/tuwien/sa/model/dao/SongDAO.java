package at.tuwien.sa.model.dao;

import java.util.List;

import javax.persistence.Query;

import at.tuwien.sa.model.entities.Fingerprint;
import at.tuwien.sa.model.entities.Song;
import at.tuwien.sa.model.interfaces.ISongDAO;
import at.tuwien.sa.model.utils.HibernateUtil;

public class SongDAO extends GenericDaoImpl<Song, Long> implements ISongDAO {
	private static final long serialVersionUID = 3811278862963134245L;

	@Override
	@SuppressWarnings("unchecked")
	public List<Song> findByFingerprint(Fingerprint fingerprint) {
		Query q = HibernateUtil.getEntityManager().createQuery("SELECT s FROM Song as s WHERE s.fingerprint_id = :fingerprint_id").setParameter("fingerprint_id", fingerprint.getId());
		return q.getResultList();
	}

}

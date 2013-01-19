package lib.dao;

import java.util.List;

import javax.persistence.Query;

import lib.entities.Fingerprint;
import lib.interfaces.IFingerprintDAO;
import lib.utils.HibernateUtil;


public class FingerprintDAO extends GenericDaoImpl<Fingerprint, Long> implements IFingerprintDAO {
	private static final long serialVersionUID = 3811274742963134245L;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Fingerprint> findBySubFingerprintValue(Integer value) {
		Query q = HibernateUtil.getEntityManager().createQuery("SELECT f FROM Fingerprint AS f LEFT JOIN f.subfingerprints s WHERE s.value = :subfingerprint_id").setParameter("subfingerprint_id", value);
		return q.getResultList();
	}

}

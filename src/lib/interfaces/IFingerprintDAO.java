package lib.interfaces;

import java.util.List;

import lib.entities.Fingerprint;


public interface IFingerprintDAO extends IGenericDAO<Fingerprint, Long> {
	public List<Fingerprint> findBySubFingerprintValue(Integer value);
}

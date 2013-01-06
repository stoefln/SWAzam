package at.tuwien.sa.model.interfaces;

import java.util.List;

import at.tuwien.sa.model.entities.Fingerprint;

public interface IFingerprintDAO extends IGenericDAO<Fingerprint, Long> {
	public List<Fingerprint> findBySubFingerprintValue(Integer value);
}

package lib.dao;

import java.io.Serializable;

import lib.interfaces.IGenericDAO;
import lib.utils.HibernateUtil;


public class GenericDaoImpl<T,ID extends Serializable> implements IGenericDAO<T,ID> {
	private static final long serialVersionUID = 4136931177718662078L;
	protected String persistenceUnit = "swazam_peer";

	public void setPersistenceUnit(String key){
		this.persistenceUnit  = key;
	}
	@Override
	public void persist(T element) {
		ensureActiveTransaction();
		HibernateUtil.getEntityManager(persistenceUnit).persist(element);
		HibernateUtil.getEntityManager(persistenceUnit).getTransaction().commit();
	}

	@Override
	public T merge(T element) {
		ensureActiveTransaction();
		return HibernateUtil.getEntityManager(persistenceUnit).merge(element);
	}

	@Override
	public void remove(T element) {
		ensureActiveTransaction();
		HibernateUtil.getEntityManager(persistenceUnit).remove(element);
		HibernateUtil.getEntityManager(persistenceUnit).getTransaction().commit();
	}

	@Override
	public void refresh(T element) {
		ensureActiveTransaction();
		HibernateUtil.getEntityManager(persistenceUnit).refresh(element);
		HibernateUtil.getEntityManager(persistenceUnit).getTransaction().commit();
	}

	@Override
	public T findByID(Class<T> type, Long id) {
        return HibernateUtil.getEntityManager(persistenceUnit).find(type, id);
	}
	
	private void ensureActiveTransaction() {
		if (!HibernateUtil.getEntityManager(persistenceUnit).getTransaction().isActive()) {
			HibernateUtil.getEntityManager(persistenceUnit).getTransaction().begin();
		}
	}
	
}

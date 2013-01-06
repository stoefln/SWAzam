package at.tuwien.sa.model.dao;

import java.io.Serializable;

import at.tuwien.sa.model.interfaces.IGenericDAO;
import at.tuwien.sa.model.utils.HibernateUtil;

public class GenericDaoImpl<T,ID extends Serializable> implements IGenericDAO<T,ID> {
	private static final long serialVersionUID = 4136931177718662078L;

	@Override
	public void persist(T element) {
		HibernateUtil.getEntityManager().getTransaction().begin();
		HibernateUtil.getEntityManager().persist(element);
		HibernateUtil.getEntityManager().getTransaction().commit();
	}

	@Override
	public T merge(T element) {
		HibernateUtil.getEntityManager().getTransaction().begin();
		return HibernateUtil.getEntityManager().merge(element);
	}

	@Override
	public void remove(T element) {
		HibernateUtil.getEntityManager().getTransaction().begin();
		HibernateUtil.getEntityManager().remove(element);
		HibernateUtil.getEntityManager().getTransaction().commit();
	}

	@Override
	public void refresh(T element) {
		HibernateUtil.getEntityManager().getTransaction().begin();
		HibernateUtil.getEntityManager().refresh(element);
		HibernateUtil.getEntityManager().getTransaction().commit();
	}

	@Override
	public T findByID(Class<T> type, Long id) {
        return HibernateUtil.getEntityManager().find(type, id);
	}
}

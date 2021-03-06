package lib.dao;

import java.util.List;
import javax.persistence.Query;
import lib.utils.HibernateUtil;
import lib.entities.User;
import lib.interfaces.IUserDAO;

public class UserDAO extends GenericDaoImpl<User, Integer> implements IUserDAO {
	private static final long serialVersionUID = 3233237838684697121L;

	
	@Override
	@SuppressWarnings("unchecked")	
	public List<User> findByToken(String AccessToken) {
		Query q = HibernateUtil.getEntityManager(persistenceUnit).createQuery("SELECT u FROM User as u WHERE u.token = :token").setParameter("token", AccessToken);
		return q.getResultList();
	}
}

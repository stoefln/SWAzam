package lib.utils;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	private static HashMap<String,EntityManager> entityManagers = new HashMap<String, EntityManager>();
	
	public static EntityManager getEntityManager(String persistenceUnitName) {
		if(!entityManagers.containsKey(persistenceUnitName)){
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			entityManagers.put(persistenceUnitName, entityManager);
		}

		return entityManagers.get(persistenceUnitName);
	}
}

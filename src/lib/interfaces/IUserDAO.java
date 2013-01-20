package lib.interfaces;

import java.util.List;

import lib.entities.User;


public interface IUserDAO extends IGenericDAO<User, Integer> {
	public List<User> findByToken(String token);
}

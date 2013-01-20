package lib.interfaces;

import java.util.List;

import lib.entities.User;

public interface IUserDAO extends IGenericDAO<User, Long> {
	public List<User> findByToken(String token);
}

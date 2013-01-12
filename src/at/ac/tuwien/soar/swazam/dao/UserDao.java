package at.ac.tuwien.soar.swazam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import at.ac.tuwien.soar.swazam.entity.User;
 
@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	List<User> findByToken(String token); 
	
}

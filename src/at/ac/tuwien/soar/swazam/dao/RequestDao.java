package at.ac.tuwien.soar.swazam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import at.ac.tuwien.soar.swazam.entity.Request;
import at.ac.tuwien.soar.swazam.entity.User;
import at.tuwien.sa.model.entities.Fingerprint;
 
@Repository
public interface RequestDao extends JpaRepository<Request, Integer> {

 
}

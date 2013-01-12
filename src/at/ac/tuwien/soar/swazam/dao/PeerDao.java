package at.ac.tuwien.soar.swazam.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import at.ac.tuwien.soar.swazam.entity.Peer;
 
@Repository
public interface PeerDao extends JpaRepository<Peer, Integer> {
 
 
}

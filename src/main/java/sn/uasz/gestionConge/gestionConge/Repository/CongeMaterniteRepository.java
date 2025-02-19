package sn.uasz.gestionConge.gestionConge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;

import java.util.List;

@Repository
public interface CongeMaterniteRepository extends JpaRepository<CongeMaternite, Long> {

    @Query("SELECT c FROM CongeMaternite c ORDER BY c.id DESC")
    List<CongeMaternite> findAllOrderByCreationDateDesc();

    List<CongeMaternite> findByPersonnelId(Long personnelId);

}


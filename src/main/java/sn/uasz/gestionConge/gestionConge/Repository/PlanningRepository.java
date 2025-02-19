package sn.uasz.gestionConge.gestionConge.Repository;


import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;

import java.util.List;
import java.util.Optional;

public interface PlanningRepository extends JpaRepository<Planning, Long> {

    Optional<Planning> findByPersonnelIdAndAnnee(Long personnelId, int annee);

    @Query("SELECT c FROM Planning c WHERE c.personnelId = :personnelId")
    List<Planning> findByPersonnelId(@Param("personnelId") Long personnelId);

    List<Planning> findAllByPersonnelIdOrderByAnneeDesc(Long personnelId);

    @Query("SELECT c FROM Planning c ORDER BY c.id DESC")
    List<Planning> findAllOrderByCreationDateDesc();

}

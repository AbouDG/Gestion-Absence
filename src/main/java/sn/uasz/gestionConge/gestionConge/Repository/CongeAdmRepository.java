package sn.uasz.gestionConge.gestionConge.Repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;

import java.util.List;

@Repository
public interface CongeAdmRepository extends JpaRepository<CongeAdm, Long> {

    @Query("SELECT c FROM CongeAdm c WHERE YEAR(c.dateDebut) = :annee")
    List<CongeAdm> findByAnnee(@Param("annee") int annee);

    @Query("SELECT c FROM CongeAdm c ORDER BY c.id DESC")
    List<CongeAdm> findAllOrderByCreationDateDesc();

    @Query("SELECT c FROM CongeAdm c WHERE c.planning.personnelId = :personnelId")
    List<CongeAdm> findByPersonnelId(@Param("personnelId") Long personnelId);
}

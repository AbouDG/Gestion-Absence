package sn.uasz.gestionConge.gestionConge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sn.uasz.gestionConge.gestionConge.Entity.Absence;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    @Query("SELECT c FROM Absence c ORDER BY c.id DESC")
    List<Absence> findAllOrderByCreationDateDesc();

}


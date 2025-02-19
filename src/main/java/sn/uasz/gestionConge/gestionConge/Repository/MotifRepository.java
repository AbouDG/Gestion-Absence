package sn.uasz.gestionConge.gestionConge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.uasz.gestionConge.gestionConge.Entity.Motif;

public interface MotifRepository extends JpaRepository<Motif, Long> {
    // Méthodes personnalisées si nécessaire
}


package sn.uasz.gestionConge.gestionConge.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.uasz.gestionConge.gestionConge.Entity.GenericFile;

import java.util.Optional;

@Repository
public interface GenericFileRepository extends JpaRepository<GenericFile, Long> {
    Optional<GenericFile> findByEntityTypeAndEntityIdAndFileType(String entityType, Long entityId, String fileType);
}


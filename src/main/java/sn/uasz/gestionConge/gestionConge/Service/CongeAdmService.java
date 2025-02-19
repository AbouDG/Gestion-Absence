package sn.uasz.gestionConge.gestionConge.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;

import java.util.List;

public interface CongeAdmService {

    CongeAdm createCongeAdm(CongeAdm congeAdm, Long personnelId);

    CongeAdm validerCongeAdm(Long congeId);

    List<CongeAdm> getAllCongeAdms();

    CongeAdm getCongeAdmById(Long id);

    List<CongeAdm> getCongesByPersonnelId(Long personnelId);

    List<CongeAdm> getCongesByAnnee(int annee);

    void deleteCongeAdm(Long id);

    CongeAdm updateCongeAdm(Long id, CongeAdm updatedCongeAdm);


}

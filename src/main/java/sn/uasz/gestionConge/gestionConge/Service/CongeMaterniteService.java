package sn.uasz.gestionConge.gestionConge.Service;

import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;

import java.util.List;

public interface CongeMaterniteService {

    //ResponseEntity<Individu> getPersonnelByCni(String matricule);

    CongeMaternite creerCongeMaternite(CongeMaternite congeMaternite) ;

    CongeMaternite obtenirCongeMaternite(Long id);

    List<CongeMaternite> getPersonnel(Long personnelId);

    List<CongeMaternite> obtenirTousLesCongesMaternite();

//    CongeMaternite modifierCongeMaternite(Long id, CongeMaternite detailsCongeMaternite);
//

    void supprimerCongeMaternite(Long id);

    CongeMaternite modifierCongeMaternite(Long id, CongeMaternite detailsCongeMaternite);


}

package sn.uasz.gestionConge.gestionConge.Service;

import sn.uasz.gestionConge.gestionConge.Entity.Prolongement;

import java.util.List;

public interface ProlongementService {

    Prolongement creerProlongement(Prolongement prolongement) ;

    List<Prolongement> obtenirTousLesProlongements();

    Prolongement obtenirProlongementParId(Long id);

    Prolongement modifierProlongement(Long id, Prolongement detailsProlongement) ;

    void supprimerProlongement(Long id);

}

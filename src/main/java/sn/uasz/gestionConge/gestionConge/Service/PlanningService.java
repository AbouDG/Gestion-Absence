package sn.uasz.gestionConge.gestionConge.Service;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.exceptions.ResourceNotFoundException;
import sn.uasz.gestionConge.gestionConge.exceptions.ServiceUnavailableException;

import java.util.List;
import java.util.Optional;

public interface PlanningService {


    List<Planning> getAllPlanningWithPersonnel();

    Planning savePlanning(Planning planning) ;

    // Méthode pour récupérer tous les plannings
    List<Planning> getAllPlannings() ;

    // Méthode pour récupérer un planning par son ID
    Optional<Planning> getPlanningById(Long id) ;

    // Méthode pour mettre à jour un planning
    Planning updatePlanning(Long id, Planning planningDetails);

    // Méthode pour supprimer un planning par son ID
    void deletePlanning(Long id);


}

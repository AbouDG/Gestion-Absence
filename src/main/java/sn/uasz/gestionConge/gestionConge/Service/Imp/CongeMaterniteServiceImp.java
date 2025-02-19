package sn.uasz.gestionConge.gestionConge.Service.Imp;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import sn.uasz.gestionConge.gestionConge.Client.IndividuClient;
import sn.uasz.gestionConge.gestionConge.Client.PersonnelClient;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Repository.CongeMaterniteRepository;
import sn.uasz.gestionConge.gestionConge.Service.CongeMaterniteService;
import sn.uasz.gestionConge.gestionConge.exceptions.ResourceNotFoundException;
import sn.uasz.gestionConge.gestionConge.exceptions.ServiceUnavailableException;

import java.util.List;

@Service
@AllArgsConstructor
public class CongeMaterniteServiceImp  implements CongeMaterniteService {

    @Autowired
    private CongeMaterniteRepository congeMaterniteRepository;
    @Autowired
    private PersonnelClient personnelClient;
    @Autowired
    private IndividuClient individuClient;


    public List<CongeMaternite> getAllCongesWithPersonnel() {
        List<CongeMaternite> conges = congeMaterniteRepository.findAllOrderByCreationDateDesc();
        conges.forEach(conge -> {
            Personnel personnel = personnelClient.getPersonnel(conge.getPersonnelId()).getBody();
            conge.setPersonnel(personnel);
        });
        return conges;
    }

    @Transactional
    public CongeMaternite obtenirCongeMaternite(Long id) {
        CongeMaternite conge = congeMaterniteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Congé introuvable"));

        Personnel personnel = personnelClient.getPersonnel(conge.getPersonnelId()).getBody();
        conge.setPersonnel(personnel);  // Affectation directement au modèle


        return conge;  // Retourne directement le modèle avec le personnel
    }

    public CongeMaternite creerCongeMaternite(CongeMaternite congeMaternite) {
        try {
            // Appel au client Feign pour récupérer le personnel
            Personnel personnel = personnelClient.getPersonnel(congeMaternite.getPersonnelId())
                    .getBody();

            if (personnel == null) {
                throw new ResourceNotFoundException("Conge Maternite  avec l'ID "
                        + congeMaternite.getPersonnelId() + " n'existe pas.");
            }

            // Associe le personnel au planning
            congeMaternite.setPersonnel(personnel);

            // Sauvegarde le planning dans la base de données
            return congeMaterniteRepository.save(congeMaternite);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Personnel avec le matricule "
                    + congeMaternite.getPersonnelId() + " n'existe pas.");
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Erreur lors de la communication avec le service Personnel !", e);
        }
    }


    public List<CongeMaternite> obtenirTousLesCongesMaternite() {


        return congeMaterniteRepository.findAllOrderByCreationDateDesc();
    }

    public CongeMaternite modifierCongeMaternite(Long id, CongeMaternite detailsCongeMaternite) {
        // Recherche du congé maternité avec gestion des erreurs si non trouvé
        CongeMaternite congeMaternite = congeMaterniteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CongeMaternite avec l'ID " + id + " n'existe pas."));

        // Mise à jour des champs
        updateFields(congeMaternite, detailsCongeMaternite);

        // Sauvegarde des changements
        return congeMaterniteRepository.save(congeMaternite);
    }

    // Méthode pour centraliser la mise à jour des champs
    private void updateFields(CongeMaternite congeMaternite, CongeMaternite detailsCongeMaternite) {
        congeMaternite.setDateDebut(detailsCongeMaternite.getDateDebut());
        congeMaternite.setDateFin(detailsCongeMaternite.getDateFin());
        congeMaternite.setDateRepriseEffective(detailsCongeMaternite.getDateRepriseEffective());
        congeMaternite.setAttestationCessationService(detailsCongeMaternite.getAttestationCessationService());
        congeMaternite.setAttestationRepriseService(detailsCongeMaternite.getAttestationRepriseService());
        congeMaternite.setAttestationCessationPayement(detailsCongeMaternite.getAttestationCessationPayement());
        congeMaternite.setAreteCongeMaternite(detailsCongeMaternite.getAreteCongeMaternite());
        congeMaternite.setPersonnelId(detailsCongeMaternite.getPersonnelId());

    }


    public void supprimerCongeMaternite(Long id) {
        congeMaterniteRepository.deleteById(id);
    }


    public List<CongeMaternite> getPersonnel(Long personnelId) {
        List<CongeMaternite> conges = congeMaterniteRepository.findByPersonnelId(personnelId);
        // Appel Feign pour récupérer les infos du personnel
        ResponseEntity<Personnel> personnel = personnelClient.getPersonnel(personnelId);
        // Ajouter les infos du personnel à chaque congé maternité
        conges.forEach(conge -> conge.setPersonnel(personnel.getBody()));
            return conges;
    }


}


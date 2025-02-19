package sn.uasz.gestionConge.gestionConge.Service.Imp;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;
import sn.uasz.gestionConge.gestionConge.Client.IndividuClient;
import sn.uasz.gestionConge.gestionConge.Client.PersonnelClient;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Repository.PlanningRepository;
import sn.uasz.gestionConge.gestionConge.Service.PlanningService;
import sn.uasz.gestionConge.gestionConge.exceptions.ResourceNotFoundException;
import sn.uasz.gestionConge.gestionConge.exceptions.ServiceUnavailableException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlanningServiceImp implements PlanningService {
    @Autowired
    private PlanningRepository planningRepository;
    @Autowired
    private PersonnelClient personnelClient;

    public List<Planning> getAllPlanningWithPersonnel() {
        List<Planning> planning = planningRepository.findAllOrderByCreationDateDesc();
        planning.forEach(plan -> {
            Personnel personnel = personnelClient.getPersonnel(plan.getPersonnelId()).getBody();
            plan.setPersonnel(personnel);
        });
        return planning;
    }

    //CREER UN PLANNING
    public Planning savePlanning(Planning planning) {
        try {
// Vérifier s'il y a déjà un planning pour ce personnel et cette année
            Optional<Planning> existingPlannings = planningRepository
                    .findByPersonnelIdAndAnnee(planning.getPersonnelId(), planning.getAnnee());
            if (!existingPlannings.isEmpty()) {
                throw new IllegalArgumentException("Le personnel avec l'ID " + planning.getPersonnelId() + " a déjà un planning pour l'année " + planning.getAnnee() + ".");
            }
// Appel au client Feign pour récupérer le personnel
            Personnel personnel = personnelClient.getPersonnel(planning.getPersonnelId())
                    .getBody();
            if (personnel == null) {
                throw new ResourceNotFoundException("Personnel avec l'Id "
                        + planning.getPersonnelId() + " n'existe pas.");
            }
// Récupérer l'année actuelle
            int anneeActuelle = LocalDate.now().getYear();

            // Initialiser le planning
            planning.setPersonnelId(planning.getPersonnelId());
//            planning.setDateDebut(Date.valueOf(LocalDate.of(anneeActuelle, 1, 1)));
//            planning.setDateFin(Date.valueOf(LocalDate.of(anneeActuelle, 12, 30)));
//            planning.setNbrJour(planning.getNbrJour()); // Nombre de jours fixe
            planning.setAnnee(planning.getAnnee());

            // Rechercher le planning de l'année précédente
            Planning planningAnneePrecedente = planningRepository
                    .findByPersonnelIdAndAnnee(planning.getPersonnelId(), planning.getAnnee() - 1)
                    .orElse(null);
            if (planningAnneePrecedente != null) {
                // Copier les valeurs des reliquas depuis le planning de l'année précédente
                planning.setReliqua(planningAnneePrecedente.getNbrJour());
                planning.setReliqua2(planningAnneePrecedente.getReliqua1());
                planning.setReliqua1(planningAnneePrecedente.getReliqua());

            } else {
                // Si aucun planning de l'année précédente, initialiser à 0
                planning.setReliqua(planning.getNbrJour());
                planning.setReliqua1(0);
                planning.setReliqua2(0);
            }
// Associe le personnel au planning
            planning.setPersonnel(personnel);
// Sauvegarde le planning dans la base de données
            return planningRepository.save(planning);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Personnel avec le matricule "
                    + planning.getPersonnelId() + " n'existe pas.");
        } catch (FeignException e) {
            throw new ServiceUnavailableException("Erreur lors de la communication avec le service Personnel.", e);
        }
    }

    // Generer un planning pour tout le monde
    public List<String> generatePlanningsForAllPersonnel(Planning planningEntre) {
        List<Personnel> personnels = personnelClient.getAllPersonnel();
        List<String> messages = new ArrayList<>();
        if (personnels == null || personnels.isEmpty()) {
            throw new ResourceNotFoundException("Aucun personnel trouvé.");
        }
        for (Personnel personnel : personnels) {
            // Vérifiez s'il y a déjà un planning pour ce personnel et cette année
            Optional<Planning> existingPlannings = planningRepository
                    .findByPersonnelIdAndAnnee(personnel.getId(), planningEntre.getAnnee());
            if (!existingPlannings.isEmpty()) {
                // Si un planning existe déjà pour cette année, ne pas en créer un nouveau
                messages.add("Le personnel avec l'ID " + personnel.getId() + " a déjà un planning pour l'année " + planningEntre.getAnnee() + ".");
                continue;
            }

            Planning planning = new Planning();
            planning.setPersonnelId(personnel.getId());
            planning.setDateDebut(planningEntre.getDateDebut());
            planning.setDateFin(planningEntre.getDateFin());
            planning.setNbrJour(planningEntre.getNbrJour()); // Vous pouvez ajuster ce nombre selon vos besoins
            planning.setAnnee(planningEntre.getAnnee());

            savePlanning(planning); // Utilise la méthode existante pour sauvegarder le planning
        }
        return messages;
    }

    // Méthode pour récupérer tous les plannings
    public List<Planning> getAllPlannings() {
        return planningRepository.findAll();
    }

    // Méthode pour récupérer un planning par son ID
    public Optional<Planning> getPlanningById(Long id) {

        Planning planning = planningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Congé introuvable"));

        ResponseEntity<Personnel> response = personnelClient.getPersonnel(planning.getPersonnelId());
        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Impossible de récupérer les informations du personnel depuis le service distant.");
        }
        Personnel personnel = response.getBody();

        planning.setPersonnel(personnel);

        return Optional.of(planning);
    }

    public List<Planning> getPlanningByPersonnelId(Long personnelId) {
        List<Planning> plan = planningRepository.findByPersonnelId(personnelId);

        if (plan.isEmpty()) {
            throw new EntityNotFoundException("Aucun Planning trouvé pour l'ID du personnel : " + personnelId);
        }
        return plan;
    }

    // Méthode pour mettre à jour un planning
    public Planning updatePlanning(Long id, Planning planningDetails) {
        Optional<Planning> existingPlanning = planningRepository.findById(id);
        if (existingPlanning.isPresent()) {
            Planning planning = existingPlanning.get();

            // Mise à jour des informations du planning
            planning.setDateDebut(planningDetails.getDateDebut());
            planning.setDateFin(planningDetails.getDateFin());
            planning.setNbrJour(planningDetails.getNbrJour());
            planning.setReliqua(planningDetails.getReliqua());
            planning.setReliqua1(planningDetails.getReliqua1());
            planning.setReliqua2(planningDetails.getReliqua2());
            planning.setAnnee(planningDetails.getAnnee());
            planning.setPersonnel(planningDetails.getPersonnel()); // Assurez-vous que la relation Personnel est
            // correctement gérée

            // Sauvegarder le planning mis à jour
            return planningRepository.save(planning);
        } else {
            // Si le planning n'existe pas, vous pouvez lever une exception ou retourner
            // null
            return null;
        }
    }
    // Méthode pour supprimer un planning par son ID
    public void deletePlanning(Long id) {
        planningRepository.deleteById(id);
    }
}

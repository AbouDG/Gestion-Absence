package sn.uasz.gestionConge.gestionConge.Service.Imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.uasz.gestionConge.gestionConge.Client.PersonnelClient;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;
import sn.uasz.gestionConge.gestionConge.Model.EstAffecte;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Repository.CongeAdmRepository;
import sn.uasz.gestionConge.gestionConge.Repository.PlanningRepository;
import sn.uasz.gestionConge.gestionConge.Service.CongeAdmService;
import sn.uasz.gestionConge.gestionConge.Service.PlanningService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class CongeAdmServiceImp implements CongeAdmService {

    @Autowired
    private CongeAdmRepository congeAdmRepository;
    @Autowired
    private PlanningRepository planningRepository;
    @Autowired
    private PersonnelClient personnelClient;
    @Autowired
    private PlanningService planningService;

//    public List<EstAffecte> getAffectationsByPersonnelMatricule(String matricule) {
//        // Appel à l'API via Feign Client
//        ResponseEntity<Personnel> response = personnelClient.getPersonnelByMatricule(matricule);
//        Personnel personnel = response.getBody();
//
//        if (personnel == null || personnel.getE == null) {
//            throw new RuntimeException("Aucune affectation trouvée pour le personnel avec le matricule : " + matricule);
//        }
//
//        return personnel.get();
//    }


    public void regulationConge(CongeAdm congeAdm, int nbrJourConge){

        Planning planning = planningRepository.findById(congeAdm.getPlanning().getId())
                .orElseThrow(() -> new IllegalArgumentException("Planning avec l'ID "
                        + congeAdm.getPlanning().getId() + " n'existe pas."));


        int totalJoursDisponibles = planning.getReliqua()
                + planning.getReliqua1()
                + planning.getReliqua2();


        // Vérification de faisabilité
        if (nbrJourConge > totalJoursDisponibles) {
            throw new RuntimeException("Impossible de créer le congé : jours disponibles insuffisants.\n" +
                    "Nombre de jours disponibe est :"+ totalJoursDisponibles);
        }

        // Réduction des jours dans le Planning
        int reste = nbrJourConge;

        // Déduction sur reliquat
        if (reste <= planning.getReliqua2()) {
            planning.setReliqua2(planning.getReliqua2() - reste);
            reste = 0;
        } else {
            reste -= planning.getReliqua2();
            planning.setReliqua2(0);
        }

        // Déduction sur reliquat1
        if (reste > 0) {
            if (reste <= planning.getReliqua1()) {
                planning.setReliqua1(planning.getReliqua1() - reste);
                reste = 0;
            } else {
                reste -= planning.getReliqua1();
                planning.setReliqua1(0);
            }
        }

        // Déduction sur reliquat2
        if (reste > 0) {
            if (reste <= planning.getReliqua()) {
                planning.setReliqua(planning.getReliqua() - reste);
                reste = 0;
            } else {
                reste -= planning.getReliqua();
                planning.setReliqua(0);
            }
        }
        congeAdm.setPlanning(planning);
        planningRepository.save(planning);

    }

    private void deductionJoursPlanning(Planning planning, int nbrJourConge) {
        int reste = nbrJourConge;

        // Déduction sur reliquat2
        if (reste <= planning.getReliqua2()) {
            planning.setReliqua2(planning.getReliqua2() - reste);
            reste = 0;
        } else {
            reste -= planning.getReliqua2();
            planning.setReliqua2(0);
        }

        // Déduction sur reliquat1
        if (reste > 0) {
            if (reste <= planning.getReliqua1()) {
                planning.setReliqua1(planning.getReliqua1() - reste);
                reste = 0;
            } else {
                reste -= planning.getReliqua1();
                planning.setReliqua1(0);
            }
        }

        // Déduction sur reliquat
        if (reste > 0) {
            if (reste <= planning.getReliqua()) {
                planning.setReliqua(planning.getReliqua() - reste);
            } else {
                planning.setReliqua(0);
            }
        }


    }

    public CongeAdm createCongeAdm(CongeAdm congeAdm, Long personnelId) {
        // Trouver le planning le plus récent pour le personnel donné
        Planning planning = planningRepository.findAllByPersonnelIdOrderByAnneeDesc(personnelId).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Planning pour le personnel avec l'ID " + personnelId + " n'existe pas."));
        congeAdm.setPlanning(planning);

        // Récupérer les informations du personnel depuis le service distant
        Personnel response = personnelClient.getPersonnel(planning.getPersonnelId()).getBody();
        if (response == null) {
            throw new RuntimeException("Impossible de récupérer les informations du personnel depuis le service distant.");
        }
        planning.setPersonnel(response);
        congeAdm.setPlanning(planning);

        // Initialiser le statut du congé
        congeAdm.setStatut("En attente");
        long diffInDays;
        if (congeAdm.getDateDebut() != null && congeAdm.getDateReprise() != null) {
            long diffInMillies = Math.abs(congeAdm.getDateReprise().getTime() - congeAdm.getDateDebut().getTime());
            diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            congeAdm.setNbrJourConge((int) diffInDays);
        } else {
            throw new RuntimeException("Les dates de début et de reprise ne peuvent pas être nulles.");
        }

        int nbrJourConge = (int) diffInDays;

        // Vérifier si la différence est disponible dans les reliquats
        int totalJoursDisponibles = planning.getReliqua() + planning.getReliqua1() + planning.getReliqua2();
        if (nbrJourConge > totalJoursDisponibles) {
            throw new RuntimeException("Impossible de le congé : jours disponibles insuffisants.\n" +
                    "Nombre de jours disponibles est : " + totalJoursDisponibles
                    +"Nombre de jour demander :"+nbrJourConge);
        }

        // Enregistrer le congé sans faire de déduction
        return congeAdmRepository.save(congeAdm);
    }

    public CongeAdm validerCongeAdm(Long congeId) {
        // Récupérer le congé à valider
        CongeAdm congeAdm = congeAdmRepository.findById(congeId)
                .orElseThrow(() -> new RuntimeException("Congé avec l'ID " + congeId + " n'existe pas."));

        // Vérifier si le statut est déjà "Validé"
        if ("Validé".equalsIgnoreCase(congeAdm.getStatut())) {
            throw new RuntimeException("Le congé est déjà validé.");
        }

        // Vérification du planning associé
        Planning planning = planningRepository.findById(congeAdm.getPlanning().getId())
                .orElseThrow(() -> new RuntimeException("Planning associé au congé introuvable."));

        // Calculer les jours disponibles
        // Calculer le nombre de jours de congé
        if (congeAdm.getDateDebut() != null && congeAdm.getDateReprise() != null) {
            long diffInMillies = Math.abs(congeAdm.getDateReprise().getTime() - congeAdm.getDateDebut().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            congeAdm.setNbrJourConge((int) diffInDays); // Le nombre de jours est défini ici
        } else {
            throw new RuntimeException("Les dates de début et de reprise ne peuvent pas être nulles.");
        }
        int nbrJourConge = congeAdm.getNbrJourConge();
        int totalJoursDisponibles = planning.getReliqua()
                + planning.getReliqua1()
                + planning.getReliqua2();

        // Vérification de faisabilité
        if (nbrJourConge > totalJoursDisponibles) {
            throw new RuntimeException("Impossible de valider le congé : jours disponibles insuffisants. Nombre de jours disponibles : " + totalJoursDisponibles);
        }

        // Déduction des jours de congé
        deductionJoursPlanning(planning, nbrJourConge);

        // Mise à jour du statut du congé et sauvegarde
        congeAdm.setStatut("Validé");
        planningRepository.save(planning);
        return congeAdmRepository.save(congeAdm);
    }

    public List<CongeAdm> getAllCongeAdms() {

        List<CongeAdm> conges = congeAdmRepository.findAllOrderByCreationDateDesc();

        conges.forEach(conge -> {
            Planning planning = planningRepository.findById(conge.getPlanning().getId())
                    .orElseThrow(() -> new RuntimeException("Planning introuvable"));
            Personnel personnel = personnelClient.getPersonnel(conge.getPlanning().getPersonnelId()).getBody();
            planning.setPersonnel(personnel);
            conge.setPlanning(planning);
        });
        return conges;
    }

    public CongeAdm getCongeAdmById(Long congeId) {
        // Récupérer le congé
        CongeAdm congeAdm = congeAdmRepository.findById(congeId)
                .orElseThrow(() -> new RuntimeException("Congé introuvable"));

        // Récupérer le planning associé
        Planning planning = planningRepository.findById(congeAdm.getPlanning().getId())
                .orElseThrow(() -> new RuntimeException("Planning introuvable"));

        // Vérifier si le personnel est null
        if (planning.getPersonnelId() == null) {
            throw new RuntimeException("Aucun personnel associé au planning avec l'ID : " + planning.getId());
        }

        // Récupérer les informations du personnel depuis le service distant
        Personnel response = personnelClient.getPersonnel(planning.getPersonnelId()).getBody();
        if (response == null) {
            throw new RuntimeException("Impossible de récupérer les informations du personnel depuis le service distant.");
        }

        // Mettre à jour le planning et le congé
        planning.setPersonnel(response);
        congeAdm.setPlanning(planning);

        return congeAdm;
    }

    public List<CongeAdm> getCongesByAnnee(int annee) {
        return congeAdmRepository.findByAnnee(annee);
    }

    public List<CongeAdm> getCongesByPersonnelId(Long personnelId) {
        List<CongeAdm> conges = congeAdmRepository.findByPersonnelId(personnelId);
        if (conges.isEmpty()) {
            throw new EntityNotFoundException("Aucun congé administratif trouvé pour l'ID du personnel : " + personnelId);
        }
        return conges;
    }


    public CongeAdm updateCongeAdm(Long id, CongeAdm updatedCongeAdm) {
        // Récupérer le congé existant
        CongeAdm existingCongeAdm = congeAdmRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Congé administratif avec l'ID " + id + " n'existe pas."));

        // Vérifier l'existence du planning associé
        Planning planning = planningRepository.findById(updatedCongeAdm.getPlanning().getId())
                .orElseThrow(() -> new IllegalArgumentException("Planning avec l'ID "
                        + updatedCongeAdm.getPlanning().getId() + " n'existe pas."));

        //Mise a jour de date Reprise effective
        if (updatedCongeAdm.getDateRepriseEffective() != null) {
            long diffInMillies = Math.abs(updatedCongeAdm.getDateRepriseEffective().getTime() - existingCongeAdm.getDateDebut().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            existingCongeAdm.setNbrJourEffective((int) diffInDays);
        }

        // Récupérer les informations des jours
        int nbrJourCongeExistant = existingCongeAdm.getNbrJourConge();
        int nbrJourCongeEffective = updatedCongeAdm.getNbrJourEffective();

            // Cas 1 : Si le nouveau nombre de jours est supérieur à l'existant
            if (nbrJourCongeEffective > nbrJourCongeExistant) {
                // Calcul de la différence entre les deux
                int difference = nbrJourCongeEffective - nbrJourCongeExistant;

                // Vérifier si la différence est disponible dans les reliquats
                int totalJoursDisponibles = planning.getReliqua() + planning.getReliqua1() + planning.getReliqua2();
                if (difference > totalJoursDisponibles) {
                    throw new RuntimeException("Impossible de mettre à jour le congé : jours disponibles insuffisants.\n" +
                            "Nombre de jours disponibles est : " + totalJoursDisponibles);
                }

                // Déduire la différence des reliquats
                deductionJoursPlanning(planning, difference);

            } else {
                // Cas 2 : Si le nouveau nombre de jours est inférieur ou égal à l'existant
                int difference = nbrJourCongeExistant - nbrJourCongeEffective;

                // Restaurer la différence dans les reliquats
                restaurerJoursPlanning(planning, difference);
            }

        //Mise a jour du Nombre de jour de conge
        if (updatedCongeAdm.getDateReprise() != null && existingCongeAdm.getDateReprise() != null) {
            long diffInMillies = Math.abs(updatedCongeAdm.getDateReprise().getTime() - existingCongeAdm.getDateDebut().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            existingCongeAdm.setNbrJourConge((int) diffInDays); // Le nombre de jours est défini ici
        } else {
            throw new RuntimeException("Les dates de début et de reprise ne peuvent pas être nulles.");
        }



        // Mise à jour des informations du congé
        System.out.println(existingCongeAdm.getNbrJourEffective());
        existingCongeAdm.setDateDebut(updatedCongeAdm.getDateDebut());
        existingCongeAdm.setDateReprise(updatedCongeAdm.getDateReprise());
        existingCongeAdm.setAttestationCession(updatedCongeAdm.getAttestationCession());
        existingCongeAdm.setAttestationReprise(updatedCongeAdm.getAttestationReprise());
        existingCongeAdm.setArreteConge(updatedCongeAdm.getArreteConge());
        existingCongeAdm.setPlanning(planning);

        // Sauvegarder les modifications
        planningRepository.save(planning);
        return congeAdmRepository.save(existingCongeAdm);
    }


    // Méthode pour restaurer les jours dans le planning
    private void restaurerJoursPlanning(Planning planning, int nbrJourConge) {
        int reste = nbrJourConge;

        // Restauration sur reliquat
        if (reste <= (30 - planning.getReliqua())) {
            planning.setReliqua(planning.getReliqua() + reste);
            reste = 0;
        } else {
            reste -= (30 - planning.getReliqua());
            planning.setReliqua(30);
        }

        // Restauration sur reliquat1
        if (reste > 0) {
            if (reste <= (30 - planning.getReliqua1())) {
                planning.setReliqua1(planning.getReliqua1() + reste);
                reste = 0;
            } else {
                reste -= (30 - planning.getReliqua1());
                planning.setReliqua1(30);
            }
        }

        // Restauration sur reliquat2
        if (reste > 0) {
            if (reste <= (30 - planning.getReliqua2())) {
                planning.setReliqua2(planning.getReliqua2() + reste);
            } else {
                planning.setReliqua2(30);
            }
        }
    }

    // Méthode pour déduire les jours du planning


    public void deleteCongeAdm(Long id) {
        congeAdmRepository.deleteById(id);
    }


}


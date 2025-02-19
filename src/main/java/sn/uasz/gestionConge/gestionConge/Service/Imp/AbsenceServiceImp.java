package sn.uasz.gestionConge.gestionConge.Service.Imp;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.uasz.gestionConge.gestionConge.Client.PersonnelClient;
import sn.uasz.gestionConge.gestionConge.Entity.Absence;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Repository.AbsenceRepository;
import sn.uasz.gestionConge.gestionConge.Service.AbsenceService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class AbsenceServiceImp implements AbsenceService {

    @Autowired
    private AbsenceRepository absenceRepository;
    @Autowired
    private PersonnelClient personnelClient;;

    // Méthode pour créer une absence
    public Absence saveAbsence(@Valid Absence absence) {
        // Convertir Date en LocalDate
        LocalDate dateDebut = absence.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Ajouter le nombre de jours
        LocalDate dateReprise = dateDebut.plusDays(absence.getNbrDeJour());

        // Si la date de reprise tombe un dimanche, la repousser au lundi
        if (dateReprise.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dateReprise = dateReprise.plusDays(1);
        }

        // Convertir LocalDate en Date et enregistrer
        absence.setDateRepriseEffective(Date.from(dateReprise.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return absenceRepository.save(absence);
    }

    // Méthode pour récupérer toutes les absences
    public List<Absence> getAllAbsences() {
        List<Absence> absences = absenceRepository.findAllOrderByCreationDateDesc();
        absences.forEach(absence -> {
            Personnel personnel = personnelClient.getPersonnel(absence.getPersonnelId()).getBody();
            absence.setPersonnel(personnel);
        });
        return absences;
    }

    // Méthode pour récupérer une absence par son ID
    public Absence getAbsenceById(Long id) {

        Absence absence =absenceRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Prolongement non trouvé"));

        // Récupérer les informations du personnel depuis le service distant
        Personnel response = personnelClient.getPersonnel(absence.getPersonnelId()).getBody();
        if (response == null) {
            throw new RuntimeException("Impossible de récupérer les informations du personnel depuis le service distant.");
        }
        // Mettre à jour l'absence
        absence.setPersonnel(response);
        return absence;
    }

    // Méthode pour supprimer une absence
    public void deleteAbsence(Long id) {
        absenceRepository.deleteById(id);
    }

    public Absence updateAbsence(Long id, Absence absence) {
        Optional<Absence> existingAbsence = absenceRepository.findById(id);

        if (existingAbsence.isPresent()) {
            Absence updatedAbsence = existingAbsence.get();

            // Mise à jour des informations de l'absence
            updatedAbsence.setDateDebut(absence.getDateDebut());
            updatedAbsence.setDateRepriseEffective(absence.getDateRepriseEffective());
            updatedAbsence.setNbrDeJour(absence.getNbrDeJour());
            updatedAbsence.setStatut(absence.getStatut());
            updatedAbsence.setStatutDRH(absence.getStatutDRH());
            updatedAbsence.setEstDeductibleConge(absence.getEstDeductibleConge());
            updatedAbsence.setMotif(absence.getMotif());
            updatedAbsence.setPersonnelId(absence.getPersonnelId());

            // Sauvegarde des modifications
            return absenceRepository.save(updatedAbsence);
        } else {
            // Si l'absence n'existe pas, on renvoie une exception ou une réponse appropriée
            throw new RuntimeException("Absence with ID " + id + " not found");
        }
    }

}


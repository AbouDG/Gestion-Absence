package sn.uasz.gestionConge.gestionConge.Controller;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sn.uasz.gestionConge.gestionConge.Entity.Absence;
import sn.uasz.gestionConge.gestionConge.Service.AbsenceService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/absences")
@AllArgsConstructor @NoArgsConstructor
public class AbsenceController {

    @Autowired
    private AbsenceService absenceService;

    // Création d'une absence
    @PostMapping
    public ResponseEntity<?> createAbsence(@Valid @RequestBody Absence absence, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
        }

        Absence savedAbsence = absenceService.saveAbsence(absence);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAbsence);
    }

    // Récupérer toutes les absences
    @GetMapping
    public List<Absence> getAllAbsences() {
        return absenceService.getAllAbsences();
    }

    // Récupérer une absence par son ID
    @GetMapping("/{id}")
    public Absence getAbsenceById(@PathVariable Long id) {
        return absenceService.getAbsenceById(id);
    }

    @PutMapping("/{id}")
    public Absence updateAbsence(@PathVariable Long id, @RequestBody Absence absence) {
        return absenceService.updateAbsence(id, absence);
    }

    // Supprimer une absence
    @DeleteMapping("/{id}")
    public void deleteAbsence(@PathVariable Long id) {
        absenceService.deleteAbsence(id);
    }
}


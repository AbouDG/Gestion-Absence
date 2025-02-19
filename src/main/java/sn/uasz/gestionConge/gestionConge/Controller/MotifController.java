package sn.uasz.gestionConge.gestionConge.Controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.uasz.gestionConge.gestionConge.Entity.Motif;
import sn.uasz.gestionConge.gestionConge.Service.MotifService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/api/motifs")
public class MotifController {

    @Autowired
    private MotifService motifService;

    // Création d'un motif
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Motif> createMotif(@RequestBody Motif motif) {
        Motif createdMotif = motifService.saveMotif(motif);
        return new ResponseEntity<>(createdMotif, HttpStatus.CREATED);
    }

    // Récupérer tous les motifs
    @GetMapping
    public List<Motif> getAllMotifs() {
        return motifService.getAllMotifs();
    }

    // Récupérer un motif par son ID
    @GetMapping("/{id}")
    public Optional<Motif> getMotifById(@PathVariable Long id) {
        return motifService.getMotifById(id);
    }

    // Supprimer un motif
    @DeleteMapping("/{id}")
    public void deleteMotif(@PathVariable Long id) {
        motifService.deleteMotif(id);
    }

    @PutMapping("/{id}")
    public Motif updateMotif(@PathVariable Long id, @RequestBody Motif motif) {
        return motifService.updateMotif(id, motif);
    }
}


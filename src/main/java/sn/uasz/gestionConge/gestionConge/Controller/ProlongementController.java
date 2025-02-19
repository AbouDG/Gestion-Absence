package sn.uasz.gestionConge.gestionConge.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sn.uasz.gestionConge.gestionConge.Entity.Prolongement;
import sn.uasz.gestionConge.gestionConge.Service.Imp.ProlongementServiceImp;

import java.util.List;

@RestController
@RequestMapping("api/prolongement")
@AllArgsConstructor
public class ProlongementController {

    @Autowired
    private ProlongementServiceImp prolongementService;

    @PostMapping
    public Prolongement creerProlongement(@RequestBody Prolongement prolongement) {
        return prolongementService.creerProlongement(prolongement);
    }

    @GetMapping
    public List<Prolongement> obtenirTousLesProlongements() {
        return prolongementService.obtenirTousLesProlongements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prolongement> obtenirProlongementParId(@PathVariable Long id) {
        Prolongement prolongement = prolongementService.obtenirProlongementParId(id);
        return ResponseEntity.ok(prolongement) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prolongement> modifierProlongement(@PathVariable Long id,
            @RequestBody Prolongement prolongementDetails) {
        try {
            Prolongement updatedProlongement = prolongementService.modifierProlongement(id, prolongementDetails);
            return ResponseEntity.ok(updatedProlongement);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProlongement(@PathVariable Long id) {
        prolongementService.supprimerProlongement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}


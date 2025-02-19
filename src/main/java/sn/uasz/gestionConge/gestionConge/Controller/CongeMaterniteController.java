package sn.uasz.gestionConge.gestionConge.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import sn.uasz.gestionConge.gestionConge.Client.IndividuClient;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Service.Imp.CongeMaterniteServiceImp;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/congeMaternite")
@AllArgsConstructor
public class CongeMaterniteController {

    @Autowired
    private CongeMaterniteServiceImp congeMaterniteService;
    @Autowired
    private IndividuClient individuClient;


    @GetMapping("/all")
    public ResponseEntity<List<CongeMaternite>> getAllConges() {
        List<CongeMaternite> conges = congeMaterniteService.getAllCongesWithPersonnel();
        return new ResponseEntity<>(conges, HttpStatus.CREATED);
    }


    @PostMapping
    public ResponseEntity<CongeMaternite> creerCongeMaternite(@RequestBody CongeMaternite congeMaternite) {
        CongeMaternite nouveauConge = congeMaterniteService.creerCongeMaternite(congeMaternite);
        return new ResponseEntity<>(nouveauConge, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CongeMaternite> obtenirCongeMaternite(@PathVariable Long id) {
        CongeMaternite congeMaternite = congeMaterniteService.obtenirCongeMaternite(id);
        return ResponseEntity.ok(congeMaternite);

    }

    @GetMapping
    public ResponseEntity<List<CongeMaternite>> obtenirTousLesCongesMaternite() {
        List<CongeMaternite> congesMaternite = congeMaterniteService.obtenirTousLesCongesMaternite();
        return ResponseEntity.ok(congesMaternite);
    }
    @GetMapping("/personnel/{personnelId}")
    public ResponseEntity<List<CongeMaternite>> getCongeMByPersonnelId(@PathVariable Long personnelId) {
        List<CongeMaternite> conge = congeMaterniteService.getPersonnel(personnelId);
        return ResponseEntity.ok(conge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CongeMaternite> modifierCongeMaternite(@PathVariable Long id,
            @RequestBody CongeMaternite detailsCongeMaternite) {
        try {
            CongeMaternite congeMaterniteModifie = congeMaterniteService.modifierCongeMaternite(id,
                    detailsCongeMaternite);
            return ResponseEntity.ok(congeMaterniteModifie);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCongeMaternite(@PathVariable Long id) {
        congeMaterniteService.supprimerCongeMaternite(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

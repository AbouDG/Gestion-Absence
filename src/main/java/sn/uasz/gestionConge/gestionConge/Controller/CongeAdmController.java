package sn.uasz.gestionConge.gestionConge.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;
import sn.uasz.gestionConge.gestionConge.Service.CongeAdmService;

import java.util.List;

@RestController
@RequestMapping("/api/congeAdm")
@AllArgsConstructor
public class CongeAdmController {

    @Autowired
    private CongeAdmService congeAdmService;

    @PostMapping("/create")
    public ResponseEntity<CongeAdm> createCongeAdm(@RequestBody CongeAdm congeAdm, @RequestParam Long personnelId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(congeAdmService.createCongeAdm(congeAdm, personnelId));
    }

    @PutMapping("/valider/{id}")
    public ResponseEntity<CongeAdm> validerConge(@PathVariable("id") Long congeId) {
        CongeAdm congeAdm = congeAdmService.validerCongeAdm(congeId);
        return ResponseEntity.ok(congeAdm);
    }

    @GetMapping
    public ResponseEntity<List<CongeAdm>> getAllCongeAdms() {
        return ResponseEntity.ok(congeAdmService.getAllCongeAdms());
    }


    @GetMapping("/annee/{annee}")
    public List<CongeAdm> getCongesByAnnee(@PathVariable int annee) {
        return congeAdmService.getCongesByAnnee(annee);
    }

    @GetMapping("/personnel/{personnelId}")
    public ResponseEntity<List<CongeAdm>> getCongesByPersonnelId(@PathVariable Long personnelId) {
        List<CongeAdm> conges = congeAdmService.getCongesByPersonnelId(personnelId);
        return ResponseEntity.ok(conges);
    }



    @GetMapping("/{id}")
    public ResponseEntity<CongeAdm> getCongeAdmById(@PathVariable Long id) {
        return ResponseEntity.ok(congeAdmService.getCongeAdmById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CongeAdm> updateCongeAdm(@PathVariable Long id, @RequestBody CongeAdm congeAdm) {
        return ResponseEntity.ok(congeAdmService.updateCongeAdm(id, congeAdm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCongeAdm(@PathVariable Long id) {
        congeAdmService.deleteCongeAdm(id);
        return ResponseEntity.noContent().build();
    }
}

package sn.uasz.gestionConge.gestionConge.Controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Service.EssaieService;

import java.util.List;

@RestController
@RequestMapping("api/essaie")
@AllArgsConstructor
public class EssaieController {

    @Autowired
    private EssaieService essaieService;

    @GetMapping("/individus/{id}")
    public ResponseEntity<Individu> getIndividuByPlanning(@PathVariable Long id) {
        Individu individu = essaieService.getIndividuById(id);
        return ResponseEntity.ok(individu);
    }

    @GetMapping("/personnels")
    public List<Personnel> getAllPersonnels() {
        return essaieService.getAllPersonnels();
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Personnel> getPersonnelByMatricule(@PathVariable String matricule) {
        ResponseEntity<Personnel> pers= essaieService.getPersonnelByMatricule(matricule);
        return pers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personnel> getPersonnel(@PathVariable("id") Long PersonnelId){
        ResponseEntity<Personnel> pers= essaieService.getPersonnel(PersonnelId);
        return pers;
    }

}

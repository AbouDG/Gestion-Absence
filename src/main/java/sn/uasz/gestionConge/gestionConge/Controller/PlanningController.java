package sn.uasz.gestionConge.gestionConge.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Entity.Planning;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Service.Imp.PlanningServiceImp;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/planning")
public class PlanningController {

    @Autowired
    private PlanningServiceImp planningService;

    @GetMapping("/all")
    public ResponseEntity<List<Planning>> getAllConges() {
        List<Planning> plan = planningService.getAllPlanningWithPersonnel();
        return ResponseEntity.ok(plan);
    }

    // API pour récupérer tous les plannings
    @GetMapping
    public List<Planning> getAllPlannings() {
        return planningService.getAllPlannings();
    }

    // API pour récupérer un planning par ID
    @GetMapping("/{id}")
    public ResponseEntity<Planning> getPlanningById(@PathVariable Long id) {
        Optional<Planning> planning = planningService.getPlanningById(id);
        return planning.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/personnel/{personnelId}")
    public ResponseEntity<List<Planning>> getPlanningByPersonnelId(@PathVariable Long personnelId) {
        List<Planning> pla = planningService.getPlanningByPersonnelId(personnelId);
        return ResponseEntity.ok(pla);
    }

    // API pour créer un planning
    @PostMapping
    public Planning createPlanning(@RequestBody Planning planning) {
        return planningService.savePlanning(planning);
    }

    @PostMapping("/generate-all")
    public ResponseEntity<List<String>> generatePlanningsForAllPersonnel(@RequestBody Planning planningTemplate) {
        List<String> plannings = planningService.generatePlanningsForAllPersonnel(planningTemplate);
        return ResponseEntity.ok(plannings);
    }

    // API pour supprimer un planning par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanning(@PathVariable Long id) {
        planningService.deletePlanning(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planning> updatePlanning(@PathVariable Long id, @RequestBody Planning planningDetails) {
        Planning updatedPlanning = planningService.updatePlanning(id, planningDetails);
        return updatedPlanning != null ? ResponseEntity.ok(updatedPlanning) : ResponseEntity.notFound().build();
    }
}


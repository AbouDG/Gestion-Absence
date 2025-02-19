package sn.uasz.gestionConge.gestionConge.Client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;

import java.util.List;

// Remplacez "organisation-service" par le nom de votre microservice Organisation dans Eureka
@FeignClient(name = "PERSONNEL-SERVICE-CLIENT", url = "http://localhost:8083/api/personnels")
public interface PersonnelClient {

    @GetMapping
    @CircuitBreaker(name = "PersonnelService")
    List<Personnel> getAllPersonnel();

    @GetMapping("/matricule/{matricule}")
    @CircuitBreaker(name = "PersonnelService")
    ResponseEntity<Personnel> getPersonnelByMatricule(@PathVariable String matricule);

    @GetMapping("/{id}")
    @CircuitBreaker(name = "PersonnelService")
    ResponseEntity<Personnel> getPersonnel(@PathVariable("id") Long PersonnelId);


}


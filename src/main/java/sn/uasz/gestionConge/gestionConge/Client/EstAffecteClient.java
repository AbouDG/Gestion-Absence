package sn.uasz.gestionConge.gestionConge.Client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.uasz.gestionConge.gestionConge.Model.EstAffecte;

import java.util.List;

@FeignClient(name = "ESTAFFECTE-SERVICE-CLIENT", url = "http://localhost:8083/api/estAffectes")
public interface EstAffecteClient {

    @GetMapping("/{id}")
    ResponseEntity<EstAffecte> getEstAffecteById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<EstAffecte>> getAllEstAffecte();

    @GetMapping("/personnel/{personnelId}")
    @CircuitBreaker(name = "EstAffecteService", fallbackMethod = "getDefaultEstAffectes")
    ResponseEntity<List<EstAffecte>> getEstAffecteByIdPersonnel(@PathVariable Long personnelId);

    // Méthode de fallback en cas d'échec
    default ResponseEntity<List<EstAffecte>> getDefaultEstAffectes(Long personnelId, Throwable throwable) {
        // Retourner une réponse vide ou une valeur par défaut en cas d'échec
        return ResponseEntity.ok(List.of());
    }
}

package sn.uasz.gestionConge.gestionConge.Client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "AUTHORISATION-SERVICE",url = "http://localhost:8885")
public interface IndividuClient {

    @GetMapping("/CNI/{cni}")
    @CircuitBreaker(name = "IndividuService")
    Optional<Individu>  getIndividuByCni(@PathVariable String cni) ;


    @GetMapping("/api/individus/{id}")
    @CircuitBreaker(name = "IndividuService", fallbackMethod = "getDefaultIndividuId")
    ResponseEntity<Individu> getIndividuById(@PathVariable Long id) ;

    @GetMapping("api/individus/{keyword}")
    @CircuitBreaker(name = "IndividuService", fallbackMethod = "getDefaultAllIndividus")
    List<Individu> findByCniIsLikeIgnoreCaseOrPrenomIsLikeIgnoreCaseOrNomIsLikeIgnoreCase(String keyword);

}

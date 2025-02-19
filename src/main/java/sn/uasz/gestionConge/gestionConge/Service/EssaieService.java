package sn.uasz.gestionConge.gestionConge.Service;


import org.springframework.http.ResponseEntity;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;

import java.util.List;


public interface EssaieService {

    Individu getIndividuById(Long individuId);

    List<Personnel> getAllPersonnels();

    ResponseEntity<Personnel> getPersonnelByMatricule(String matricule);

    ResponseEntity<Personnel> getPersonnel(Long personnelId);
}

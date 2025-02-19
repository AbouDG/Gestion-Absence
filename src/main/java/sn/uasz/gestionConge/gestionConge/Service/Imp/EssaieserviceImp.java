package sn.uasz.gestionConge.gestionConge.Service.Imp;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sn.uasz.gestionConge.gestionConge.Client.IndividuClient;
import sn.uasz.gestionConge.gestionConge.Client.PersonnelClient;
import sn.uasz.gestionConge.gestionConge.Model.Individu;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;
import sn.uasz.gestionConge.gestionConge.Service.EssaieService;

import java.util.List;

@Service
@AllArgsConstructor
public class EssaieserviceImp implements EssaieService {

    @Autowired
    private IndividuClient individuClient;
    @Autowired
    private PersonnelClient personnelClient;

    public Individu getIndividuById(Long individuId) {
        try {
            return individuClient.getIndividuById(individuId).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Individu introuvable avec l'ID : " + individuId);
        }
    }

    // Nouvelle méthode pour obtenir la liste des personnels
    public List<Personnel> getAllPersonnels() {
        // Récupération de tous les personnels
        List<Personnel> personnels = personnelClient.getAllPersonnel();

        // Parcours de chaque personnel pour récupérer l'individu correspondant
        personnels.forEach(personnel -> {
            if (personnel.getIndividuId() != null) {
                Individu individu = getIndividuById(personnel.getIndividuId());
                personnel.setIndividu(individu);
            }
        });

        return personnels;
    }

    public ResponseEntity<Personnel> getPersonnelByMatricule(String matricule) {
        ResponseEntity<Personnel> mat=personnelClient.getPersonnelByMatricule(matricule);
        return mat;
    }

    public ResponseEntity<Personnel> getPersonnel(Long PersonnelId) {
        ResponseEntity<Personnel> mat=personnelClient.getPersonnel(PersonnelId);
        return mat;
    }
}

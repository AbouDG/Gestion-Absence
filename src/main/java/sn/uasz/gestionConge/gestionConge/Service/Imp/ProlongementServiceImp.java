package sn.uasz.gestionConge.gestionConge.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Entity.Prolongement;
import sn.uasz.gestionConge.gestionConge.Repository.CongeMaterniteRepository;
import sn.uasz.gestionConge.gestionConge.Repository.ProlongementRepository;
import sn.uasz.gestionConge.gestionConge.Service.ProlongementService;

import java.util.List;

@Service
@AllArgsConstructor
public class ProlongementServiceImp implements ProlongementService {

    @Autowired
    private ProlongementRepository prolongementRepository;

    @Autowired
    private CongeMaterniteRepository congeMaterniteRepository;

    @Transactional
    public Prolongement creerProlongement(Prolongement prolongement) {
        CongeMaternite conge_maternite = congeMaterniteRepository.findById(prolongement.getCongeMaternite().getId())
                .orElseThrow(() -> new IllegalArgumentException("Personnel avec le matricule "
                        + prolongement.getCongeMaternite().getId() + " n'existe pas."));

        prolongement.setCongeMaternite(conge_maternite);
        return prolongementRepository.save(prolongement);
    }

    @Transactional
    public List<Prolongement> obtenirTousLesProlongements() {
        return prolongementRepository.findAll();
    }

    @Transactional
    public Prolongement obtenirProlongementParId(Long id) {
        return prolongementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prolongement non trouvé"));
    }

    @Transactional
    public Prolongement modifierProlongement(Long id, Prolongement detailsProlongement) {
        return prolongementRepository.findById(id).map(prolongement -> {
            if (detailsProlongement.getCongeMaternite() != null &&
                    congeMaterniteRepository.existsById(detailsProlongement.getCongeMaternite().getId())) {

                prolongement.setDateDebut(detailsProlongement.getDateDebut());
                prolongement.setDateFin(detailsProlongement.getDateFin());
                prolongement.setCertM(detailsProlongement.getCertM());
                return prolongementRepository.save(prolongement);
            } else {
                throw new IllegalArgumentException("Le congé maternité spécifié n'existe pas.");
            }
        }).orElseThrow(() -> new IllegalArgumentException("Prolongement non trouvé"));
    }

    @Transactional
    public void supprimerProlongement(Long id) {
        prolongementRepository.deleteById(id);

    }
}

package sn.uasz.gestionConge.gestionConge.Service.Imp;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.uasz.gestionConge.gestionConge.Entity.Motif;
import sn.uasz.gestionConge.gestionConge.Repository.MotifRepository;
import sn.uasz.gestionConge.gestionConge.Service.MotifService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class MotifServiceImp implements MotifService {

    @Autowired
    private MotifRepository motifRepository;

    // Méthode pour créer un motif
    public Motif saveMotif(Motif motif) {
        return motifRepository.save(motif);
    }

    // Méthode pour récupérer tous les motifs
    public List<Motif> getAllMotifs() {
        return motifRepository.findAll();
    }

    // Méthode pour récupérer un motif par son ID
    public Optional<Motif> getMotifById(Long id) {
        return motifRepository.findById(id);
    }

    // Méthode pour supprimer un motif
    public void deleteMotif(Long id) {
        motifRepository.deleteById(id);
    }

    public Motif updateMotif(Long id, Motif motif) {
        Optional<Motif> existingMotif = motifRepository.findById(id);

        if (existingMotif.isPresent()) {
            Motif updatedMotif = existingMotif.get();

            // Mise à jour des informations du motif
            updatedMotif.setLibelle(motif.getLibelle());
            updatedMotif.setDescription(motif.getDescription());
            updatedMotif.setNbrDeJour(motif.getNbrDeJour());

            // Sauvegarde des modifications
            return motifRepository.save(updatedMotif);
        } else {
            // Si le motif n'existe pas, on renvoie une exception ou une réponse appropriée
            throw new RuntimeException("Motif with ID " + id + " not found");
        }
    }
}

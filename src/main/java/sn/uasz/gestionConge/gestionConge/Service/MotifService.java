package sn.uasz.gestionConge.gestionConge.Service;

import sn.uasz.gestionConge.gestionConge.Entity.Motif;

import java.util.List;
import java.util.Optional;

public interface MotifService {

    public Motif saveMotif(Motif motif);

    public List<Motif> getAllMotifs();

    public Optional<Motif> getMotifById(Long id);

    public void deleteMotif(Long id);

    public Motif updateMotif(Long id, Motif motif);


}

package sn.uasz.gestionConge.gestionConge.Service;

import sn.uasz.gestionConge.gestionConge.Entity.Absence;

import java.util.List;

public interface AbsenceService {

    public Absence saveAbsence(Absence absence);

    public List<Absence> getAllAbsences();

    public Absence getAbsenceById(Long id);

    public void deleteAbsence(Long id);

    public Absence updateAbsence(Long id, Absence absence);
}

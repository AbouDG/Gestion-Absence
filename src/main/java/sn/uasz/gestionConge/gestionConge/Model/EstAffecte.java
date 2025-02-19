package sn.uasz.gestionConge.gestionConge.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class EstAffecte implements Serializable {
    public Long id;
    public String referenceArrete;
    public LocalDate dateAffectation;
    public LocalDate dateFin;
    public String arrete;
    public Boolean estIndisponible;
    public Long professionId; // ID de la profession
    private Profession profession;
    public Long organisationId; // ID de l'organisation
    private Organisation organisation;
    public Long personnelId; // ID du personnel
    
}

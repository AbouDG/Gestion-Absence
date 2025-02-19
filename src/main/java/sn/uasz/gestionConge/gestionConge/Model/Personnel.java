package sn.uasz.gestionConge.gestionConge.Model;


import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Personnel {
    private Long id;
    private String matricule;
    private String type;
    private boolean estFonctionnaire;
    private String estInformaticienSynpics;
    private String statut;

    private List<EstAffecte> estAffectes;
    @Transient
    private Individu individu;
    private Long individuId;


}


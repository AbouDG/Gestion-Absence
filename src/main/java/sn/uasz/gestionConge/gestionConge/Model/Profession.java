package sn.uasz.gestionConge.gestionConge.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profession {

    private Long id;
    private String libelle;
    private List<EstAffecte> estAffectes ;
}

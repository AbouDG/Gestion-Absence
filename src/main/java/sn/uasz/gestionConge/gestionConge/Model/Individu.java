package sn.uasz.gestionConge.gestionConge.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public  class Individu implements Serializable {

    private Long id;
    private String nom;
    private String prenom;
    private String sexe;
    private String cni;
}

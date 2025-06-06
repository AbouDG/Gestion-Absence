package sn.uasz.gestionConge.gestionConge.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Responsabilites implements Serializable { public Long id;
    public String libelle;
    public Date dateDebutVadilite;
    public Date dateFinVadilite;
}

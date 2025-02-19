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
public class ResponsabiliteOrganisation implements Serializable {
    public Long id;
    public Date dateDebutValidite;
    public Date dateFinValidite;
    public Long organisationId; // ID de l'organisation
    public Long responsabiliteId; // ID de la responsabilité
}

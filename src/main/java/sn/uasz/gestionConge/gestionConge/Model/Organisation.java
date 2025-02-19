package sn.uasz.gestionConge.gestionConge.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class Organisation implements Serializable {
    public Long id;
    public String libelle;
    public String abreviation;
    public String email;
    public Long typeOrganisationId; // ID du type d'organisation
    public Date dateCreation;
    public String arreteCreation;
    public Long organisationMereId; // ID de l'organisation mère
    private List<Long> organisationsFilles; // Liste des IDs des organisations filles
    private List<Long> responsabiliteOrganisations; // Liste des IDs des responsabilités organisationnelles
    private List<Long> estAffectes;
}

package sn.uasz.gestionConge.gestionConge.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "absence")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDebut;
    private Date dateRepriseEffective;
    private int nbrDeJour;
    private String statut;
    private String statutDRH;
    private Boolean estDeductibleConge;
    private String areteAbsence;
    @Column(length = 500) // Permet de stocker des motifs plus longs
    private String motifRefus;

    @Transient
    private Personnel personnel;
    private Long personnelId;

    @ManyToOne
    @JoinColumn(name = "motif_id", referencedColumnName = "id")
    @NotNull(message = "Le motif est obligatoire !")
    private Motif motif;
}


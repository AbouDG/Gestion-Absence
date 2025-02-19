package sn.uasz.gestionConge.gestionConge.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class CongeAdm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateReprise;
    private Date dateRepriseEffective;

    private String attestationCession;
    private String attestationReprise;
    private String arreteConge;
    private int nbrJourConge;
    private int nbrJourEffective;
    private String statut;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "planning_id", referencedColumnName = "id")
    private Planning planning;
}


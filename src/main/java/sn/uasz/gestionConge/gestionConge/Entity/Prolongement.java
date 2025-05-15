package sn.uasz.gestionConge.gestionConge.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Prolongement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @Column(name = "cert_m")
    private String certM;

    // Relation avec CongeMaternite
    @ManyToOne
    @JoinColumn(name = "conge_maternite_id", nullable = false, referencedColumnName = "id")
    @JsonIgnore
    private CongeMaternite congeMaternite;

}


package sn.uasz.gestionConge.gestionConge.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.ResponseEntity;
import sn.uasz.gestionConge.gestionConge.Model.Personnel;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conge_maternite")
public class CongeMaternite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut", nullable = false)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin")
    private Date dateFin;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_reprise_effective")
    private Date dateRepriseEffective;

    @Column(name = "certificat_grossesse")
    private String certificatGrossesse;

    @Column(name = "attestation_cessation_service")
    private String attestationCessationService;

    @Column(name = "attestation_reprise_service")
    private String attestationRepriseService;

    @Column(name = "attestation_cessation_payement")
    private String attestationCessationPayement;

    @Column(name = "arete_conge_maternite")
    private String areteCongeMaternite;

    @Transient
    private Personnel personnel;

    private Long personnelId;

    @OneToMany(mappedBy = "congeMaternite", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Prolongement> prolongements;



}

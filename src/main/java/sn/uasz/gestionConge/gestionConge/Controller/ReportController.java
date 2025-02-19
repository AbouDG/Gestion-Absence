package sn.uasz.gestionConge.gestionConge.Controller;

import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sn.uasz.gestionConge.gestionConge.Config.DatabaseConnection;
import sn.uasz.gestionConge.gestionConge.Entity.Absence;
import sn.uasz.gestionConge.gestionConge.Entity.CongeAdm;
import sn.uasz.gestionConge.gestionConge.Entity.CongeMaternite;
import sn.uasz.gestionConge.gestionConge.Model.EstAffecte;
import sn.uasz.gestionConge.gestionConge.Repository.AbsenceRepository;
import sn.uasz.gestionConge.gestionConge.Repository.CongeAdmRepository;
import sn.uasz.gestionConge.gestionConge.Repository.CongeMaterniteRepository;
import sn.uasz.gestionConge.gestionConge.Service.AbsenceService;
import sn.uasz.gestionConge.gestionConge.Service.CongeAdmService;
import sn.uasz.gestionConge.gestionConge.Service.CongeMaterniteService;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ReportController {

    private final CongeAdmRepository congeAdmRepository;
    private final CongeMaterniteRepository congeMaterniteRepository;
    private final CongeAdmService congeAdmService;
    private final CongeMaterniteService congeMaterniteService;
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final AbsenceService absenceService;
    private final AbsenceRepository absenceRepository;


    public ReportController(CongeAdmRepository congeAdmRepository, CongeAdmService congeAdmService , CongeMaterniteRepository congeMaterniteRepository, CongeAdmService congeAdmService1, CongeMaterniteService congeMaterniteService, AbsenceService absenceService, AbsenceRepository absenceRepository) {
        this.congeAdmRepository = congeAdmRepository;
        this.congeMaterniteRepository = congeMaterniteRepository;
        this.congeAdmService = congeAdmService;
        this.congeMaterniteService = congeMaterniteService;
        this.absenceService = absenceService;
        this.absenceRepository = absenceRepository;
    }

    @GetMapping("/api/reports/conge/{id}")
    @Transactional
    public ResponseEntity<InputStreamResource> generateReport(@PathVariable Long id, @RequestParam String type) throws Exception {

        CongeAdm congeAdm = congeAdmService.getCongeAdmById(id);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Nom", congeAdm.getPlanning().getPersonnel().getIndividu().getNom());
        parameters.put("Prenom", congeAdm.getPlanning().getPersonnel().getIndividu().getPrenom());
        parameters.put("Matricule", congeAdm.getPlanning().getPersonnel().getMatricule());
        parameters.put("CONGE_ID", id);
        //parameters.put("profession", congeAdm.getPlanning().getPersonnel().getEstAffectes());

        List<EstAffecte> estAffectes = congeAdm.getPlanning().getPersonnel().getEstAffectes();

        if (estAffectes != null && !estAffectes.isEmpty()) {
            EstAffecte dernierEstAffecte = estAffectes.get(estAffectes.size() - 1); // Dernier élément
            System.out.println("Dernier arreteReference : " + dernierEstAffecte.getProfession().getLibelle());
            parameters.put("profession", dernierEstAffecte.getProfession().getLibelle());
        } else {
            System.out.println("Aucune affectation disponible.");
        }

//        CongeMaternite congeAdm = congeMaterniteRepository.findById(id).orElseThrow(() -> new RuntimeException("Conge Maternite not found"));

        // Construire dynamiquement le nom du fichier .jrxml
        String jrxmlFileName = type.toLowerCase().replace(" ", "_") + ".jrxml";
        InputStream jrxmlInput = getClass().getResourceAsStream("/reports/" + jrxmlFileName);

        if (jrxmlInput == null) {
            throw new FileNotFoundException("Le fichier " + jrxmlFileName + " est introuvable dans /reports/");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);



        Connection connection = DatabaseConnection.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        connection.close();

        ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutput);
        ByteArrayInputStream pdfInput = new ByteArrayInputStream(pdfOutput.toByteArray());

        String filePath = "C:/Users/AbouDiagneGaye/Desktop/StageADG/gestionConge/uploads/" + type + "_conge_adm_" + id + ".pdf";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            pdfOutput.writeTo(fileOutputStream);
        }

        String fileName = "http://localhost:8091/api/uploadFiles/files/" + type + "_conge_adm_" + id + ".pdf";

        switch (type) {
            case "attestationCession":
                congeAdm.setAttestationCession(fileName);
                break;
            case "attestationReprise":
                congeAdm.setAttestationReprise(fileName);
                break;
            case "arreteConge":
                congeAdm.setArreteConge(fileName);
                break;
        }
                congeAdmRepository.save(congeAdm);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + type + "_report.pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new InputStreamResource(pdfInput));

    }


    @GetMapping("/api/reports/congeMaternite/{id}")
    @Transactional
    public ResponseEntity<InputStreamResource> generateReportCongeMaternite(@PathVariable Long id, @RequestParam String type) throws Exception {
        
        Map<String, Object> parameters = new HashMap<>();

        CongeMaternite congeAdm = congeMaterniteService.obtenirCongeMaternite(id);
        parameters.put("personnelNom", congeAdm.getPersonnel().getIndividu().getNom());
        parameters.put("personnelPrenom", congeAdm.getPersonnel().getIndividu().getPrenom());
        parameters.put("matricule", congeAdm.getPersonnel().getMatricule());
        parameters.put("CONGE_ID", id);

//        CongeMaternite congeAdm = congeMaterniteRepository.findById(id).orElseThrow(() -> new RuntimeException("Conge Maternite not found"));

        // Construire dynamiquement le nom du fichier .jrxml
        String jrxmlFileName = type.toLowerCase().replace(" ", "_") + ".jrxml";
        InputStream jrxmlInput = getClass().getResourceAsStream("/reports/" + jrxmlFileName);

        if (jrxmlInput == null) {
            throw new FileNotFoundException("Le fichier " + jrxmlFileName + " est introuvable dans /reports/");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);

        Connection connection = DatabaseConnection.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        connection.close();

        ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutput);
        ByteArrayInputStream pdfInput = new ByteArrayInputStream(pdfOutput.toByteArray());

        String filePath = "C:/Users/AbouDiagneGaye/Desktop/StageADG/gestionConge/uploads/" + type + "_conge_maternite_" + id + ".pdf";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            pdfOutput.writeTo(fileOutputStream);
        }

        String fileName = "http://localhost:8091/api/uploadFiles/files/" + type + "_conge_maternite_" + id + ".pdf";

        switch (type) {
            case "attestationCessationService":
                congeAdm.setAttestationCessationService(fileName);
                break;
            case "attestationRepriseService":
                congeAdm.setAttestationRepriseService(fileName);
                break;
            case "attestationCessationPayement":
                congeAdm.setAttestationCessationPayement(fileName);
                break;
            case "areteCongeMaternite":
                congeAdm.setAreteCongeMaternite(fileName);
                break;
        }
        congeMaterniteRepository.save(congeAdm);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + type + "_report.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfInput));
    }

    @GetMapping("/api/reports/absence/{id}")
    @Transactional
    public ResponseEntity<InputStreamResource> generateReportAbsence(@PathVariable Long id, @RequestParam String type) throws Exception {

        Absence absence = absenceService.getAbsenceById(id);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Nom", absence.getPersonnel().getIndividu().getNom());
        parameters.put("Prenom", absence.getPersonnel().getIndividu().getPrenom());
        parameters.put("Matricule", absence.getPersonnel().getMatricule());
        parameters.put("Motif", absence.getMotif().getLibelle());
        parameters.put("CONGE_ID", id);

        List<EstAffecte> estAffectes = absence.getPersonnel().getEstAffectes();

        if (estAffectes != null && !estAffectes.isEmpty()) {
            EstAffecte dernierEstAffecte = estAffectes.get(estAffectes.size() - 1); // Dernier élément
            parameters.put("Service",dernierEstAffecte.getOrganisation().getLibelle());
            parameters.put("Fonction",dernierEstAffecte.getProfession().getLibelle());

        } else {
            System.out.println("Aucune affectation disponible.");
        }


        // Construire dynamiquement le nom du fichier .jrxml
        String jrxmlFileName = type.toLowerCase().replace(" ", "_") + ".jrxml";
        InputStream jrxmlInput = getClass().getResourceAsStream("/reports/" + jrxmlFileName);

        if (jrxmlInput == null) {
            throw new FileNotFoundException("Le fichier " + jrxmlFileName + " est introuvable dans /reports/");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);


        Connection connection = DatabaseConnection.getConnection();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
        connection.close();

        ByteArrayOutputStream pdfOutput = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfOutput);
        ByteArrayInputStream pdfInput = new ByteArrayInputStream(pdfOutput.toByteArray());


        String filePath = "C:/Users/AbouDiagneGaye/Desktop/StageADG/gestionConge/uploads/" + type + "_absence_" + id + ".pdf";
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            pdfOutput.writeTo(fileOutputStream);
        }

        String fileName = "http://localhost:8091/api/uploadFiles/files/" + type + "_absence_" + id + ".pdf";
        absence.setAreteAbsence(fileName);

        absenceRepository.save(absence);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + type + "_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfInput));

    }

}


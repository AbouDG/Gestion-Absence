package sn.uasz.gestionConge.gestionConge.Controller;

import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploadFiles")
public class FileUploadController {

    private static String UPLOADED_FOLDER = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // Obtenez le nom du fichier et enregistrez le fichier sur le disque
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            // Créez la réponse avec l'URL du fichier
            String fileUrl = "http://localhost:8091/api/uploadFiles/files/" + file.getOriginalFilename();
            Map<String, String> response = new HashMap<>();
            response.put("filePath", fileUrl);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files/{filename}")
    @ResponseBody
    public ResponseEntity<UrlResource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOADED_FOLDER).resolve(filename);
            UrlResource resource = new UrlResource(file.toUri());

            // Vérification du type de fichier (par exemple, PDF)
            String contentType = Files.probeContentType(file);

            // Si le type est PDF, on spécifie le type MIME correspondant
            if ("application/pdf".equals(contentType)) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.APPLICATION_PDF)  // Assurer que le type MIME est correct pour PDF
                        .body(resource);
            }

            // Si ce n'est pas un PDF, utiliser un en-tête générique
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)  // Type générique pour d'autres fichiers
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

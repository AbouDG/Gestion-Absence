package sn.uasz.gestionConge.gestionConge.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.uasz.gestionConge.gestionConge.Entity.GenericFile;
import sn.uasz.gestionConge.gestionConge.Service.FileService;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<GenericFile> uploadFile(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("entityType") String entityType,
                                                  @RequestParam("entityId") Long entityId,
                                                  @RequestParam("fileType") String fileType) {
        try {
            GenericFile genericFile = fileService.uploadFile(file, entityType, entityId, fileType);
            return ResponseEntity.ok(genericFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = fileService.loadFileAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

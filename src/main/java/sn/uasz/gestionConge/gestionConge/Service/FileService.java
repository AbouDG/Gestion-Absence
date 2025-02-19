package sn.uasz.gestionConge.gestionConge.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.uasz.gestionConge.gestionConge.Entity.GenericFile;
import sn.uasz.gestionConge.gestionConge.Repository.GenericFileRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileService {

    private static final String UPLOADED_FOLDER = "uploads/";

    @Autowired
    private GenericFileRepository genericFileRepository;

    public GenericFile uploadFile(MultipartFile file, String entityType, Long entityId, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);

        GenericFile genericFile = new GenericFile();
        genericFile.setEntityType(entityType);
        genericFile.setEntityId(entityId);
        genericFile.setFileType(fileType);
        genericFile.setFilePath("/api/files/" + file.getOriginalFilename());

        return genericFileRepository.save(genericFile);
    }

    public Resource loadFileAsResource(String filename) throws MalformedURLException {
        Path file = Paths.get(UPLOADED_FOLDER).resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new MalformedURLException("Could not read the file: " + filename);
        }
    }
}

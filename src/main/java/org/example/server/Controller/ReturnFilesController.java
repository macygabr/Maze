package org.example.server.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class ReturnFilesController {

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        File file = new File(uploadDirectory + fileName);
        if (file.exists() && !file.isDirectory()) {
            Resource resource = new FileSystemResource(file);
            String contentType;
            try {
                contentType = Files.probeContentType(file.toPath());
            } catch (IOException e) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

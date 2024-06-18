package org.example.server.Controller;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
    @RequestMapping("/upload")
    public String uploadForm() {
        return "pages/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            file.transferTo(new File("/home/resources/" + file.getOriginalFilename()));
            return "pages/upload";
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
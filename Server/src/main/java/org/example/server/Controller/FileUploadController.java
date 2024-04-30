package org.example.server.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileUploadController {
    @RequestMapping("/upload")
    public String uploadForm() {
        return "pages/upload";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String path = new File("").getAbsolutePath();
            file.transferTo(new File(path +"/src/main/resources/static/download/" + file.getOriginalFilename()));
            return "pages/upload";
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
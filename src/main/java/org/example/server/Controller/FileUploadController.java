package org.example.server.Controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    private final Server server;
    private final String uploadDirectory;

    @Autowired
    public FileUploadController(Server server, @Value("${file.upload.directory}") String uploadDirectory) {
        this.server = server;
        this.uploadDirectory = uploadDirectory;
    }

    @RequestMapping("/upload")
    public String uploadForm(HttpServletRequest request, Model model) {
        try {
            server.CheckAndAddUser(request);
            model.addAttribute("server", new JSONObject(server).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "pages/upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
            server.loadMap(filePath);
            model.addAttribute("status", true);
        } catch (IOException e) {
            model.addAttribute("status", false);
            e.printStackTrace();
        } finally {
            model.addAttribute("server", new JSONObject(server).toString());
        }
        return "pages/upload";
    }
}
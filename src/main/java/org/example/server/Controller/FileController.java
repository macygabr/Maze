package org.example.server.Controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileController {

    private final Server server;
    private final String uploadDirectory;
    
    @Autowired
    public FileController(Server server, @Value("${file.upload.directory}") String uploadDirectory) {
        this.server = server;
        this.uploadDirectory = uploadDirectory;
    }

    @GetMapping("/files")
    public String files(HttpServletRequest request, Model model) {
        try {
            server.CheckAndAddUser(request);
            File folder = new File(uploadDirectory);
            File[] files = folder.listFiles();
            String[] name = new String[files.length];
            
            for (int i = 0; i < files.length; i++)
                name[i] = files[i].getName();
            
            model.addAttribute("server", new JSONObject(server).toString());
            model.addAttribute("files", name);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "pages/files";
    }
}
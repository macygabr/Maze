package org.example.server.Controller;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileController {
    @GetMapping("/files")
    public String files(Map<String, Object> model) {
        try {
            File folder = new File("/home/resources");
            File[] files = folder.listFiles();
            String[] name = new String[files.length];

            for (int i = 0; i < files.length; i++) {
                name[i] = files[i].getName();
            }

            model.put("files", name);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return "pages/files";
    }
}
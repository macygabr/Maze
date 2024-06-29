package org.example.server.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final Server server;
    
    @Autowired
    public HomeController(Server server) {
        this.server = server;
    }
    
    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model) {
        try {
            server.CheckAndAddUser(request);
            model.addAttribute("server", new JSONObject(server).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "pages/home";
    }
}
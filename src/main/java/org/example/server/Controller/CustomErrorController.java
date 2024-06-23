package org.example.server.Controller;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private final Server server;
    
    @Autowired
    public CustomErrorController(Server server) {
        this.server = server;
    }
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        server.CheckCookies(request);
        model.addAttribute("server", new JSONObject(server).toString());
        return "pages/error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

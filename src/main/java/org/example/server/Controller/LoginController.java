package org.example.server.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.example.server.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    private final Server server;
    
    @Autowired
    public LoginController(Server server) {
        this.server = server;
    }

    @RequestMapping("/login")
    public String getPage(HttpServletRequest request, Model model) {
        try {
            server.CheckAndAddUser(request);
            model.addAttribute("server", new JSONObject(server.Private()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "pages/login";
    }

    @SendTo("/topic/authentication")
    @MessageMapping("/authentication")
    public String authentication(User user) {
        if (server.checkUserDB(user)) {
            String cookie = user.getCookie();

            user.setAuthentication(true);
            user.rebootLocation(server.getField());
            user.setCookie(cookie);
            server.setUser(user);
            server.getUsers().put(cookie, user);
        }
        return (new JSONObject(server.Private())).toString();
    }
}

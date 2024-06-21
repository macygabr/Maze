package org.example.server.Controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.example.server.model.Greeting;
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
    public String getPage(HttpServletRequest request, Model model){
        server.CheckCookies(request);
        model.addAttribute("server", new JSONObject(server).toString());
        return "pages/login";
    }

    @SendTo("/topic/authentication")
    @MessageMapping("/authentication")
    public String authentication(User user) {
        String cookie = user.getCookie();
        String oldCookie = "";

        if(server.checkUser(user)) {
            for(Map.Entry<String, User> it : server.getUsers().entrySet()) {
                if(it.getValue().getLogin()!=null && it.getValue().getLogin().equals(user.getLogin()))
                    cookie = it.getValue().getCookie();
            }

            server.getUsers().remove(oldCookie);
            user.setCookie(cookie);
            user.setAuthentication(true);
            user.rebootLocation(server.getFild());
            server.setUser(user);
        }
        
        return (new JSONObject(server)).toString();
    }
}
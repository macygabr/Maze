package org.example.server.Controller;

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
public class SignInController {
    private final Server server;
    
    @Autowired
    public SignInController(Server server) {
        this.server = server;
    }

    @RequestMapping("/sign")
    public String getPageSign(HttpServletRequest request, Model model) {
        server.CheckCookies(request);
        model.addAttribute("server", new JSONObject(server).toString());
        return "pages/sign";
    }

    @SendTo("/topic/authentication")
    @MessageMapping("/sign")
    public String sign(User user) {
        
        if(server.checkUser(user)) return (new JSONObject(server)).toString();
        String cookie = user.getCookie();
        
        try {
            User us = server.getUsers().get(cookie);
            us.setLogin(user.getLogin());
            us.setPass(user.getPass());
            us.rebootLocation(server.getField());
            us.setAuthentication(true);
            server.setUser(us);
            server.insertUser(us);
        } catch (Exception e) {
            e.printStackTrace();
            server.getUsers().get(cookie).setAuthentication(false);
            return (new JSONObject(server)).toString();
        }
        return (new JSONObject(server)).toString();
    }
}
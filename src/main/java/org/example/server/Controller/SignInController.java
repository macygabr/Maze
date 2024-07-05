package org.example.server.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.example.server.model.AuthenticationType;
import org.example.server.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        try {
            server.CheckAndAddUser(request);
            model.addAttribute("server", new JSONObject(server).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "pages/sign";
    }

    
    @PostMapping("/sign")
     public ResponseEntity<String> sign(@RequestBody User user, HttpServletRequest request) throws DataAccessException {
        user.setIp(request.getRemoteAddr());
        
        System.out.println(user);
        if(server.checkUserDB(user)) return ResponseEntity.ok(new JSONObject(server.Private()).toString());
        String cookie =user.getCookie();
        try {
            User us = User.builder()
                          .login(user.getLogin())
                          .pass(user.getPass())
                          .cookie(cookie)
                          .authentication(AuthenticationType.USER)
                          .ip(user.getIp())
                          .build();

            us.rebootLocation(server.getField());
            server.setUser(us);
            server.getUsers().put(cookie, us);
            server.insertUser(us);
        } catch (Exception e) {
            e.printStackTrace();
            User us = User.builder()
                          .authentication(AuthenticationType.CHEATER)
                          .ip(user.getIp())
                          .build();
            server.setUser(us);
            server.getUsers().remove(cookie);
            server.insertUser(us);
        }
        
        return ResponseEntity.ok(new JSONObject(server.Private()).toString());
    }
}
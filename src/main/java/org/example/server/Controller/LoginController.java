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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("server", new JSONObject(server.Private()).toString());
        return "pages/login";
    }

    @PostMapping("/authentication")
    public ResponseEntity<String> authentication(@RequestBody User user, HttpServletRequest request) throws DataAccessException {
        String cookie = user.getCookie();
        for(User us : server.getUsers().values()) {
            if(us.getLogin().equals(user.getLogin()) && us.getPass().equals(user.getPass())) {
                return ResponseEntity.ok(new JSONObject(server.Private()).toString());
            }
        }

        user.setIp(request.getRemoteAddr());
        user.setAuthentication(AuthenticationType.USER);
        user.setCookie(cookie);
        
        if (cookie!=null && !user.getCookie().isEmpty() && server.checkUserDB(user)) {
            user.rebootLocation(server.getField());
            server.setUser(user);
            server.getUsers().put(cookie, user);
        }
        
        return ResponseEntity.ok(new JSONObject(server.Private()).toString());
    }
}

package org.example.server.Controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.example.server.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
    public String authentication(User user, @Header("simpSessionId") String sessionId) {
        String cookie = user.getCookie();

        if(server.checkUser(user)) {
            System.out.println(cookie);
            for(Map.Entry<String, User> it : server.getUsers().entrySet()) {
                if(it.getValue().getLogin()!=null && it.getValue().getLogin().equals(user.getLogin()))
                    cookie = it.getValue().getCookie();
            }

            user.setCookie(cookie);
            user.setAuthentication(true);
            user.rebootLocation(server.getFild());
            server.setUser(user);
            server.getUsers().put(cookie, user);
            // messagingTemplate.convertAndSendToUser(sessionId, "/queue/reply", new JSONObject(user).toString());
        }

        return (new JSONObject(server)).toString();
    }
}
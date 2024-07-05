package org.example.server.Controller;

import javax.servlet.http.HttpServletRequest;

import org.example.server.Backend.Server;
import org.example.server.model.Greeting;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class FieldController {
    
    private final Server server;
    
    @Autowired
    public FieldController(Server server) {
        this.server = server;
    }
    
    @RequestMapping("/field")
    public String field(HttpServletRequest request, Model model) {
        try {
            server.CheckAndAddUser(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("server", new JSONObject(server.Private()).toString());
        return "pages/field";
    }

    @MessageMapping("/setSize")
    @SendTo("/topic/reboot")
    public String set(Greeting obj) {
        String cookie = obj.getCookie();

        if(server.getUsers().containsKey(cookie) && server.getUsers().get(cookie).getAuthentication()) {
                server.getField().setSize(obj.getSizeMap());
                server.Reboot(cookie);
        } else {
            System.out.println("Invalid user or authentication for cookie: " + cookie);
        }
    
        return (new JSONObject(server.Private())).toString();
    }
    

    @MessageMapping("/reboot")
    @SendTo("/topic/reboot")
    public String greeting(Greeting obj) {
        server.Reboot(obj.getCookie());
        return (new JSONObject(server.Private())).toString();
    }

    @SendTo("/topic/loadMap")
    @MessageMapping("/loadMap")
    public String loadMap(Greeting obj) {
        String cookie = obj.getCookie();

        if(server.getUsers().containsKey(cookie) && 
                server.getUsers().get(cookie).getAuthentication())
                    return (new JSONObject(server)).toString();

        return (new JSONObject()).toString();
    }

    @SendTo("/topic/greetings")
    @MessageMapping("/move")
    public String move(Greeting obj) {
        String cookie = obj.getCookie();

        if(server.getUsers().containsKey(cookie) && 
                server.getUsers().get(cookie).getAuthentication())
                    server.moveUser(obj);

        return (new JSONObject(server.Private())).toString();
    }

    @SendTo("/topic/greetings")
    @MessageMapping("/saveMap")
    public String save(Greeting obj) {
        try {
            server.saveMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new JSONObject(server.Private())).toString();
    }

    @SendTo("/topic/findPath")
    @MessageMapping("/findPath")
    public String findPath(Greeting obj) {
        try {
            String cookie = obj.getCookie();
            Server ser = server.FindPath(cookie);
            return (new JSONObject(ser.Private())).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new JSONObject(server.Private())).toString();
    }
}
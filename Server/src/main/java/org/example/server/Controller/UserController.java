package org.example.server.Controller;

import org.example.server.model.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping("/login")
    public String getFild(Fild message) {
        return "pages/login";
    }
}
package org.example.server.Controller;

import org.example.server.model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.core.MessageSendingOperations;

import org.json.JSONObject;
import org.json.JSONArray;

@Controller
public class FieldController {
    private final MessageSendingOperations<String> messageSendingOperations;

    public FieldController(MessageSendingOperations<String> messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }


//    private List<WebSocketSession> sessions = new ArrayList<>();

    private static final String[] result = {
            "01", "00", "01", "00",
            "10", "00", "11", "00",
            "01", "11", "00", "01",
            "00", "00", "00", "00"
    };

    private String[][] UserInMap;

    private static final int columns = 4;

    private static final User user = new User();

    @RequestMapping("/field")
    public String field(Model model) {
        model.addAttribute("result", result);
        model.addAttribute("columns", columns);

        model.addAttribute("name", "macygabr");
        model.addAttribute("x", user.getX());
        model.addAttribute("y", user.getY());
       return "pages/field";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(User new_user) {
        String direction = UserMove(new_user.getX(), new_user.getY());

        JSONObject response = new JSONObject();
        response.put("coordUser", user.getX() + " " + user.getY());
        response.put("direction", direction);
        return response.toString();
    }

//    @RequestMapping("/user")
//    @MessageMapping("/user")
//    @SendTo("/topic/user")
//    public String getUser() {
//        return "pages/test";
//    }

    private String UserMove(int x, int y) {
        if(Math.abs(user.getX() - x) + Math.abs(user.getY() - y) != 1) return "/img/stay.jpg";

        int index = user.getX() + user.getY()*columns;
        if(result[index].charAt(1) == '1' && user.getY() - y == -1) return "/img/stay.jpg";
        if(result[index].charAt(0) == '1' && user.getX() - x == -1) return "/img/stay.jpg";
        if(index>=columns && user.getY() - y == 1 && result[index-columns].charAt(1) == '1') return "/img/stay.jpg";
        if(index>=1 && user.getX() - x == 1 && result[index-1].charAt(0) == '1') return "/img/stay.jpg";

        int x_old = user.getX();
        int y_old = user.getY();
        user.setX(x);
        user.setY(y);

        if(y_old - y == 1) return "/img/up.jpg";
        if(x_old - x == 1) return "/img/left.jpg";
        if(y_old - y == -1) return "/img/down.jpg";
        if(x_old - x == -1) return "/img/right.jpg";

        return "/img/stay.jpg";
    }
}

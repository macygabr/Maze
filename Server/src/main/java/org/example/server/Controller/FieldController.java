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
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.servlet.http.Cookie;

@Controller
public class FieldController {
    private static final int columns = 10;
    private static final int countPlayers = 4;
    private static Fild fild = new Fild(columns);
    private static String[] result = fild.getMap();
    private static Map<String,User> userTokens = new HashMap<>();
    private User user = new User(columns);
    private final MessageSendingOperations<String> messageSendingOperations;
    private static int reboot = 0;

    public FieldController(MessageSendingOperations<String> messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @RequestMapping("/field")
    public String field(HttpServletRequest request, Model model) {

        Cookie cookie = CheckCookies(request);
//        System.out.println(cookie.getName() + " " + cookie.getValue());
        String UsersXCoord = "";
        String UsersYCoord = "";
        String UsersPng = "";

        for(String str : userTokens.keySet()) {
            UsersXCoord += userTokens.get(str).getX() + " ";
            UsersYCoord += userTokens.get(str).getY() + " ";
            UsersPng += userTokens.get(str).getPng() + " ";
        }

        model.addAttribute("UserX", UsersXCoord);
        model.addAttribute("UserY", UsersYCoord);
        model.addAttribute("png", UsersPng);
        model.addAttribute("reboot", 0);
        model.addAttribute("sizeMap", columns);
        model.addAttribute("result", result);
        model.addAttribute("uuid", cookie.getValue());
       return "pages/field";
    }


    @SendTo("/topic/greetings")
    @MessageMapping("/hello")
    public String greeting(User message) {
        System.out.print("Get: "  + message.toString());
        JSONObject response = new JSONObject();

        String UsersXCoord = "";
        String UsersYCoord = "";

        if(message.getReboot() == 0) UserMove(message.getX(), message.getY(), message.getCookieValue());
        else {
            fild = new Fild(columns);
            result = fild.getMap();
            userTokens.clear();
//            user = new User(columns);
//            userTokens.put(user.getCookieValue(), user);
        }

        user.setReboot(message.getReboot());

        for(User us : userTokens.values()) {
            UsersXCoord += String.valueOf(us.getX()) + " ";
            UsersYCoord += String.valueOf(us.getY()) + " ";
        }

        response.put("UsersXCoord", UsersXCoord);
        response.put("UsersYCoord", UsersYCoord);
        response.put("userPng",user.getPng());
        response.put("reboot", user.getReboot());

        System.out.println("| Send: " + response.toString());
        return response.toString();
    }


    private String UserMove(int x, int y, String cookieValue) {

        int curentX = 0;
        int curentY = 0;

        for(User us: userTokens.values()) {
            if(cookieValue.equals(us.getCookieValue())) {
                curentX = us.getX();
                curentY = us.getY();
                int index = us.getX() + us.getY()*columns;
                if((curentX + x < 0)
                        || (curentX + x >= columns)
                        || (curentY + y) < 0
                        || (curentY+y) >= columns
                        || (result[index].charAt(1) == '1' && y == 1)
                        || (result[index].charAt(0) == '1' && x == 1)
                        || (index>=columns && y == -1 && result[index-columns].charAt(1) == '1')  //верх
                        || (index>=1 && x == -1 && result[index-1].charAt(0) == '1') //лево
                ) return user.getPng();

                us.moveX(x);
                us.moveY(y);
                if(x == -1) user.setPng("/img/left.jpg");
                if(x == 1) user.setPng("/img/right.jpg");
                if(y == -1) user.setPng("/img/up.jpg");
                if(y == 1) user.setPng("/img/down.jpg");
                return user.getPng();
            }
        }

        return user.getPng();
    }

    private Cookie CheckCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null)
            for(Cookie cookie : cookies)
                if(cookie.getName().equals("player"))
                    for(String str : userTokens.keySet())
                        if(cookie.getValue().equals(str)) return cookie;

        user = new User(columns);
        reboot = 1;
        user.setReboot(reboot);
        if(userTokens.size() < countPlayers)
             userTokens.put(user.getCookieValue(), user);
        return new Cookie(user.getCookieName(), user.getCookieValue());
    }

}

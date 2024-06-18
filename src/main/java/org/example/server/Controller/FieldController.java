package org.example.server.Controller;

import org.example.server.model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
import org.example.server.Backend.Server;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import java.io.FileInputStream;
import java.io.File;
import java.nio.file.Files;
import java.io.InputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.CacheControl;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FieldController {
    private static int columns = 10;
    private static final int countPlayers = 4;
    private static Fild fild = new Fild(columns);
    private static String[] result = fild.getMap();
    private static Map<String,User> userTokens = new HashMap<>();
    private User user = new User(columns);
    private static Cheese cheese = new Cheese(columns);
    private final MessageSendingOperations<String> messageSendingOperations;
    private Server server;

    public FieldController(MessageSendingOperations<String> messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    @RequestMapping("/field")
    public String field(HttpServletRequest request, Model model) {

        String ipAddress = request.getRemoteAddr();
//        if(ipAddress.equals("10.54.202.33")) return "pages/error";

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
        model.addAttribute("CheeseX", cheese.getX());
        model.addAttribute("CheeseY", cheese.getY());
        model.addAttribute("CheesePng", cheese.getPath());
        model.addAttribute("png", UsersPng);
        model.addAttribute("reboot", user.getReboot());
        model.addAttribute("sizeMap", columns);
        model.addAttribute("result", result);
        model.addAttribute("uuid", cookie.getValue());
        model.addAttribute("Rotate", user.getRotate());
       return "pages/field";
    }


    @SendTo("/topic/greetings")
    @MessageMapping("/hello")
    public String greeting(User message) {
//       System.out.print("Get: "  + message.toString());
        JSONObject response = new JSONObject();

        String UsersXCoord = "";
        String UsersYCoord = "";
        String UsersPng = "";
        String Rotate = "";
        String pathX = "";
        String pathY = "";
        int x = message.getX();
        int y = message.getY();
//        System.out.println("TEST");
        if(message.getAuto() == 1) {
            server = new Server();
            server.FindPath(fild, user, cheese);
            pathX = server.getPathX();
            pathY = server.getPathY();
            x = 0;
            y = 0;
        }

        if(message.getReboot() == 0) UserMove(x, y, message.getCookieValue());
        else {
            fild.getResult().get(0).getResult().clear();
            userTokens.clear();
            fild = new Fild(columns);
            result = fild.getMap();
            cheese = new Cheese(columns);
//            System.out.println("fild " + fild.getSize());
        }


        for(User us : userTokens.values()) {
            UsersXCoord += String.valueOf(us.getX()) + " ";
            UsersYCoord += String.valueOf(us.getY()) + " ";
            UsersPng += us.getPng() + " ";
            Rotate += String.valueOf(us.getRotate()) + " ";
        }

        response.put("UsersXCoord", UsersXCoord);
        response.put("UsersYCoord", UsersYCoord);
        response.put("UsersPng",UsersPng);
        response.put("reboot", user.getReboot());
        response.put("Rotate", Rotate);
        response.put("CheeseX", cheese.getX());
        response.put("CheeseY", cheese.getY());
        response.put("CheesePng", cheese.getPath());
        response.put("pathX", pathX);
        response.put("pathY", pathY);

//       System.out.println("| Send: " + response.toString());
        return response.toString();
    }


    @MessageMapping("/fild")
    @ResponseBody
    public void set(String message) {
        JSONObject response = new JSONObject(message);
        columns = Integer.parseInt(response.get("sizeMap").toString());
    }

    @MessageMapping("/files")
    @ResponseBody
    public ResponseEntity<byte[]> downloadFile() throws IOException {
//        String filename = "scale_1200.jpg";
//        String filePath = getClass().getClassLoader().getResource("static/download/scale_1200.jpg").getPath();
//        File file = new File(filePath);
//
//        System.out.println("test");
//        if (!file.exists()) throw new IOException("Файл не найден: " + filePath);
//
//        byte[] fileContent = Files.readAllBytes(file.toPath());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", filename);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(fileContent.length)
//                .body(fileContent);

//            Path filePath = Paths.get("files", filename);
//            Resource fileResource = new FileSystemResource(filePath);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(HttpHeaders.CONTENT_DISPOSITION,
//                    "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(filePath.toFile().length())
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(fileResource);
        return null;
    }

    private String UserMove(int x, int y, String cookieValue) {

        int curentX = 0;
        int curentY = 0;

        for(User us: userTokens.values()) {
            if(cookieValue.equals(us.getCookieValue())) {
                curentX = us.getX();
                curentY = us.getY();
                int index = us.getY() + us.getX()*columns;
                if((curentX + x < 0)
                        || (curentX + x >= columns)
                        || (curentY + y) < 0
                        || (curentY+y) >= columns
                        || (result[index].charAt(1) == '1' && x == 1)
                        || (result[index].charAt(0) == '1' && y == 1)
                        || (index>=columns && x == -1 && result[index-columns].charAt(1) == '1')  //верх
                        || (index>=1 && y == -1 && result[index-1].charAt(0) == '1') //лево
                ) break;

                if(y == -1) us.Rotate(90);
                if(y == 1) us.Rotate(270);
                if(x == -1) us.Rotate(180);
                if(x == 1) us.Rotate(0);
                us.moveX(x);
                us.moveY(y);
                if(us.getX() == cheese.getX() && us.getY() == cheese.getY()) {
                    us.setBill(us.getBill() + 1);
                    cheese = new Cheese(columns);
                }
                break;
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
        if(userTokens.size() < countPlayers)
             userTokens.put(user.getCookieValue(), user);
        return new Cookie(user.getCookieName(), user.getCookieValue());
    }

}

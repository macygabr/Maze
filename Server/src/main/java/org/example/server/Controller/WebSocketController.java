package org.example.server.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

//@Controller
//public class WebSocketController {
//
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public String handleMessage(String message) {
//        // Thread.sleep(1000); // simulated delay
//        return "Hello, " + message + "!";
//    }
//}
package org.example.server.model;

import static java.lang.Math.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;



@Getter
@Setter
public class User {
    private int x;
    private int y;
    private String name;
    private String png;
    private int Reboot = 0;
    private int sizeMap;
    private String cookieName = "player";
    private String cookieValue = UUID.randomUUID().toString();
    private static Map<String,User> userTokens = new HashMap<>();
    public User(int sizeMap) {
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));
        name = "tester";

        if(userTokens.size() == 0) png = "/img/customMouse.png";
        if(userTokens.size() == 1) png = "/img/gachi.jpg";
        if(userTokens.size() == 2) png = "/img/customMouse.png";
        if(userTokens.size() == 3) png = "/img/gachi.jpg";

        for(User user : userTokens.values()) {
            if(user.getX() == x && user.getY() == y) {
                while (user.getX() == x && user.getY() == y) {
                    x = (int) (Math.random() * (sizeMap));
                    y = (int) (Math.random() * (sizeMap));
                }
            }
        }
        userTokens.put(cookieValue, this);
    }

    public User(int x, int y, int Reboot, String cookieValue) {
        this.x = x;
        this.y = y;
        this.Reboot = Reboot;
        this.cookieValue = cookieValue;
    }

    public void moveX(int x) {this.x += x;}

    public void moveY(int y) {this.y += y;}

    @Override
    public String toString() {
        return "User{" +
                "x=" + x +
                ", y=" + y  +
                ", name='" + name + '\'' +
                ", png='" + png + '\'' +
                ", cookieValue=" + cookieValue +
                ", Reboot=" + Reboot +
                '}';
    }
}

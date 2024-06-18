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
    private int Rotate;
    private int bill = 0;
    private int auto = 0;
    private int Download = 0;

    private String cookieName = "player";
    private String cookieValue = UUID.randomUUID().toString();
    private static Map<String,User> userTokens = new HashMap<>();

    public User(int sizeMap) {
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));

        int[] angles = {0, 90, 180, 270};
        Rotate = angles[(int) (Math.random() * angles.length)];

        GeneratePng();

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

    private void GeneratePng() {
        png = "/img/mouse/classic" + (int) (Math.random() * 4) + ".png";

    }

    public void Rotate(int angle) {
        Rotate = angle;
    }
}

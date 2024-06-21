package org.example.server.model;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("prototype")
public class User {
    private Fild fild;
    
    private int x;
    private int y;
    private Boolean authentication;
    private String login;
    private String pass;
    private String png;
    private int reboot;
    private int sizeMap;
    private int rotate;
    private String cookieName;
    private String cookieValue;
    private String ip;

    public User() {
        cookieValue = UUID.randomUUID().toString();
        cookieName = "player";
        authentication = false;
    }
    
    public User(Fild fild) {
        this();
        this.fild = fild;
        sizeMap = fild.getSize();
        authentication = false;
        rebootLocation(fild);
    }
    
    final public void rebootLocation(Fild fild) {
        this.fild = fild;
        sizeMap = fild.getSize();
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));    
        int[] angles = {0, 90, 180, 270};
        rotate = angles[(int) (Math.random() * angles.length)];
        png = "/img/mouse/classic" + (int) (Math.random() * 4) + ".png";
    }

    // public void copy(User user) {
    //     this.fild = user.getFild();
    //     this.x = user.getX();
    //     this.y = user.getY();
    //     this.authentication = user.getAuthentication();
    //     this.name = user.getName();
    //     this.png = user.getPng();
    //     this.reboot = user.getReboot();
    //     this.sizeMap = user.getSizeMap();
    //     this.rotate = user.getRotate();
    //     this.cookieName = user.getCookieName();
    //     this.cookieValue = user.getCookieValue();
    // }
    

    public void move(int divX, int divY) {
        int size = fild.getSize();
        int index = y + x * size;
        if((x + divX < 0)
                || (x + divX >= size)
                || (y + divY) < 0
                || (y+ divY ) >= size
                || (fild.getResult().get(index).getDown() == 1 && divX == 1) //низ
                || (fild.getResult().get(index).getRight() == 1 && divY == 1) //право
                || (index>=size && divX == -1 && fild.getResult().get(index-size).getDown() == 1)  //верх
                || (index>=1 && divY == -1 && fild.getResult().get(index-1).getRight() == 1) //лево
        ) return;

        if(divY == -1) setRotate(90);
        if(divY == 1)  setRotate(270);
        if(divX == -1) setRotate(180);
        if(divX == 1)  setRotate(0);
        x += divX;
        y += divY;
    }

    public void setCookie(String cookie){
        String[] res = cookie.split("=");
        if (res.length == 2) {
            cookieName = res[0];
            cookieValue = res[1];
        } else {
            System.err.println("Invalid cookie format: " + cookie);
        }
    }

    public String getCookie(){
        return cookieName+"="+cookieValue;
    }

    @Override
    public String toString() {
        return "User{" +
                "x=" + x +
                ", y=" + y  +
                ", login='" + login + '\'' +
                ", png='" + png + '\'' +
                ", cookieValue=" + cookieValue +
                ", reboot=" + reboot +
                '}';
    }
}

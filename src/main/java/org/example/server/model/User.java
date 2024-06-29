package org.example.server.model;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Component
@Scope("prototype")
public class User {
    private Field field;
    private int x;
    private int y;
    @Builder.Default private Boolean authentication = false;
    private String login;
    private String pass;
    private String png;
    private int sizeMap;
    private int rotate;
    private String ip;
    private String cookie;

    // public User() {}

    // public User(Field field) {
    //     this.field = field;
    //     sizeMap = field.getSize();
    //     rebootLocation(field);
    // }
    
    // public User(HttpServletRequest request) {
    //     setCookie(request);
    //     ip = request.getRemoteAddr();
    // }


    public User Private() {
        User user = User.builder()
                        .authentication(authentication)
                        .x(x)
                        .y(y)
                        .png(png)
                        .rotate(rotate)
                        .cookie(cookie)
                        .build();
        return user;
    }

    public User GetProfile(){
        User user = User.builder()
                    .authentication(authentication)
                    .login(login)
                    .build();
        return user;
    }

    final public void rebootLocation(Field field) {
        this.field = field;
        sizeMap = field.getSize();
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));    
        int[] angles = {0, 90, 180, 270};
        rotate = angles[(int) (Math.random() * angles.length)];
        png = "/img/mouse/classic" + (int) (Math.random() * 4) + ".png";
    }
    

    public void move(int divX, int divY) {
        int size = field.getSize();
        int index = y + x * size;
        if((x + divX < 0)
                || (x + divX >= size)
                || (y + divY) < 0
                || (y+ divY ) >= size
                || (field.getResult().get(index).getDown() == 1 && divX == 1) //низ
                || (field.getResult().get(index).getRight() == 1 && divY == 1) //право
                || (index>=size && divX == -1 && field.getResult().get(index-size).getDown() == 1)  //верх
                || (index>=1 && divY == -1 && field.getResult().get(index-1).getRight() == 1) //лево
        ) return;

        if(divY == -1) setRotate(90);
        if(divY == 1)  setRotate(270);
        if(divX == -1) setRotate(180);
        if(divX == 1)  setRotate(0);
        x += divX;
        y += divY;
    }

    public void setCookie(String cookie) {
        
    }

    @Override
    public String toString() {
        return "User{" +
                "field=" + field +
                ", x=" + x +
                ", y=" + y +
                ", authentication=" + authentication +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                ", png='" + png + '\'' +
                ", sizeMap=" + sizeMap +
                ", rotate=" + rotate +
                ", ip='" + ip + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}   
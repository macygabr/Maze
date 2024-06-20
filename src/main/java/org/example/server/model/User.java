package org.example.server.model;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
    private String name;
    private String png;
    private int reboot;
    private int sizeMap;
    private int rotate;
    private String cookieName;
    private String cookieValue;

    public User(Fild fild) {
        this.fild = fild;
        cookieValue = UUID.randomUUID().toString();
        authentication = false;
        cookieName = "player";
        sizeMap = fild.getSize();
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));
        name = "user";

        int[] angles = {0, 90, 180, 270};
        rotate = angles[(int) (Math.random() * angles.length)];

        GeneratePng();

        // for(User user : userTokens.values()) {
        //     if(user.getX() == x && user.getY() == y) {
        //         while (user.getX() == x && user.getY() == y) {
        //             x = (int) (Math.random() * (sizeMap));
        //             y = (int) (Math.random() * (sizeMap));
        //         }
        //     }
        // }
        // userTokens.put(cookieValue, this);
    }

    
    private void GeneratePng() {
        png = "/img/mouse/classic" + (int) (Math.random() * 4) + ".png";
    }
    

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

        // if(x == cheese.getX() && y == cheese.getY()) {
        //     setBill(getBill() + 1);
        //     cheese = new Cheese(fild.getSize());
        // }
    }

    @Override
    public String toString() {
        return "User{" +
                "x=" + x +
                ", y=" + y  +
                ", name='" + name + '\'' +
                ", png='" + png + '\'' +
                ", cookieValue=" + cookieValue +
                ", reboot=" + reboot +
                '}';
    }
}

package org.example.server.model;

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
    @Builder.Default private AuthenticationType authentication = AuthenticationType.GUEST;
    private String login;
    private String pass;
    private String png;
    private int rotate;
    private String ip;
    private String cookie;
    private int bill;

    public User Private() {
        User user = User.builder()
                        .authentication(authentication)
                        .x(x)
                        .y(y)
                        .bill(bill)
                        .png(png)
                        .rotate(rotate)
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
        int sizeMap = field.getSize();
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));    
        int[] angles = {0, 90, 180, 270};
        rotate = angles[(int) (Math.random() * angles.length)];
        if(png == null) png = "/img/mouse/classic" + (int) (Math.random() * 4) + ".png";
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
                ", rotate=" + rotate +
                ", ip='" + ip + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}   
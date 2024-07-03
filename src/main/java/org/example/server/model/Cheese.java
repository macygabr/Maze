package org.example.server.model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("prototype")
public class Cheese {
    private int x;
    private int y;
    private final String path;

    public Cheese(User user) {
        int sizeMap = user.getField().getSize();
        path = "/img/cheese.png";
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));

        while(x == user.getX() && y == user.getY()) {
            x = (int) (Math.random() * (sizeMap));
            y = (int) (Math.random() * (sizeMap));
        }
    }
    
    // public Cheese(int sizeMap) {
    //     path = "/img/cheese.png";
    //     x = (int) (Math.random() * (sizeMap));
    //     y = (int) (Math.random() * (sizeMap));
    // }

    public void reboot(User user) {
        int sizeMap = user.getField().getSize();
        while(x == user.getX() && y == user.getY()){
            x = (int) (Math.random() * (sizeMap));
            y = (int) (Math.random() * (sizeMap));
        }
    }
}

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
        do{
            x = (int) (Math.random() * (sizeMap));
            y = (int) (Math.random() * (sizeMap));
        } while(x == user.getX() && y == user.getY());
    }

    public void reboot(User user) {
        int sizeMap = user.getField().getSize();
        do {
            x = (int) (Math.random() * (sizeMap));
            y = (int) (Math.random() * (sizeMap));
        } while(x == user.getX() && y == user.getY());
    }
}

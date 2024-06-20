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
    private String path;

    public Cheese(int sizeMap) {
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));
        path = "/img/cheese.png";
    }
}

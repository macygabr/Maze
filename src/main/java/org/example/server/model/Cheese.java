package org.example.server.model;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Cheese {
    private int x;
    private int y;
    private int status;
    private static String path;
    public Cheese(int sizeMap) {
        x = (int) (Math.random() * (sizeMap));
        y = (int) (Math.random() * (sizeMap));
        status = 0;
        path = "/img/cheese.png";
    }

    public static String getPath() {
        return path;
    }
}

package org.example.server.model;

import static java.lang.Math.*;
public class User {
    private int x;
    private int y;
    private String name;

    public User() {
        x = (int) (Math.random() * 4);
        y = (int) (Math.random() * 4);
        name = "tester";
    }
    public User(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

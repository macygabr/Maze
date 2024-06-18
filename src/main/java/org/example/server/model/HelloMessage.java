package org.example.server.model;

public class HelloMessage {

    private String name;
    private int x;
    private int y;


    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }
    public HelloMessage(Integer x,  Integer y) {
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
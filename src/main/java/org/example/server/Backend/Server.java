package org.example.server.Backend;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.example.server.database.Database;
import org.example.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("singleton")
public class Server {
    private User user;
    private Cheese cheese;
    private Field field;
    private Map<String,User> users;
    private final Database database;
    
    @Value("${server.ip}")
    private String ip;
    @Value("${server.port}")
    private int port;
    @Value("${server.countPlayers}")
    private int countPlayers;
    @Value("${file.upload.directory}") 
    private String directory;

    @Autowired
    public Server(Field field, Database database) {
        this.database = database;
        this.field = field;
        this.user = User.builder().field(field).build();
        this.cheese = new Cheese(user);
        users = new HashMap<>();
    }

    public Server Private() {
        Server server = new Server(field, database);
        server.setIp(ip);
        server.setPort(port);
        server.setUser(user.GetProfile());
        server.setCheese(cheese);
        Map<String,User> usersPrivate = new HashMap<>();
        for(User us : users.values()) {
            usersPrivate.put("user", us.Private());
        }
        server.setUsers(usersPrivate);
        return server;
    }

    public void moveUser(Greeting obj) {
        String cookie = obj.getCookie();
        users.get(cookie).move(obj.getX(),obj.getY());
        if(users.get(cookie).getX() == cheese.getX() && users.get(cookie).getY() == cheese.getY()){
            cheese.reboot(users.get(cookie));
        }
    }

    public void Reboot(String cookie) {
        if(!users.containsKey(cookie) || !users.get(cookie).getAuthentication()) return;

        field = new Field(field.getSize());
        users.get(cookie).rebootLocation(field);
        cheese.reboot(users.get(cookie));

        for(User us : users.values())
            if(us.getAuthentication()) us.rebootLocation(field);
    }

    public Boolean CheckAndAddUser(HttpServletRequest request) throws IOException {
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0) return false;

        for(Cookie cookie : cookies){
            if(users.containsKey(cookie.getName()+"="+cookie.getValue())) {
                user = users.get(cookie.getName()+"="+cookie.getValue());
                return true;
            }
        }
        
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("maze")) {
                user.setField(field);
                user.setIp(request.getRemoteAddr());
                user.setCookie(user.getCookie());
                user.rebootLocation(field);
                users.put(user.getCookie(), user);
                return false;
            }
        }
        throw new IOException("Cookie not found");
    }

    public void saveMap(){
        try {
            field.saveMap(directory);
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public void loadMap(Path filePath) throws IOException {
        field.loadMap(filePath);

        // this.field = field;

        // this.user = User.builder().field(field).build();
        // this.cheese = new Cheese(user);
        // users = new HashMap<>();

        for(User us : users.values()) {
            us.rebootLocation(field);
        }
        this.user = User.builder().field(field).build();
        cheese.reboot(user);
    }

    public Boolean checkUserDB(User obj) {
        return database.check(obj);
    }

    public void insertUser(User obj) throws Exception {
        database.addUser(obj);
    }

    public Server FindPath(String cookie) {
        Field copyfield = field.copy();
        Server ser = new Server(copyfield, database);
        User us = users.get(cookie);
        SetPathCost(ser);
        AlgorithmAstar(us, ser);
        SetPath(us, ser);
        return ser;
    }

    public void SetPathCost(Server ser) {
        for(Cell cell : ser.getField().getResult()){
            cell.setPath(false);
            cell.setPathCost(0);
        }
    }

    private void AlgorithmAstar(User us, Server ser) {
        Cell start = ser.getField().getCell(us.getX(), us.getY());
        Cell goal =  ser.getField().getCell(cheese.getX(), cheese.getY());
        Set<Cell> openSet = new HashSet<>();
        Set<Cell> closedSet = new HashSet<>();
        // HashMap<Cell, Cell> cameFrom = new HashMap<>();
        openSet.add(start);
        while (!openSet.isEmpty()) {
            Cell current = null;
            
            for (Cell node : openSet)
            if (current == null)  current = node;
            if (current.equals(goal)) return;
            
            openSet.remove(current);
            closedSet.add(current);
            
            for (Cell neighbor : ser.getField().getNeighbors(current)) {
                // printMap(ser.getField());
                if (closedSet.contains(neighbor)) continue;
                if (!openSet.contains(neighbor)) openSet.add(neighbor);
                neighbor.setPathCost(current.getPathCost() + 1);
            }
        }
        System.out.println("End");
    }

    private void SetPath(User us, Server ser) {
        Cell goal = ser.getField().getCell(cheese.getX(), cheese.getY());
        Cell current = ser.getField().getCell(us.getX(), us.getY());
              
        while (!current.equals(goal)) {
            for(Cell neighbor : ser.getField().getNeighbors(goal)) {
                if(neighbor.getPathCost() == goal.getPathCost() - 1) {
                    goal = neighbor;
                    break;
                }
            }

            if(!goal.equals(current)){
                goal.setPath(true);
            }
        }
    }

    private void printMap(Field field){
        String color;
        String end = "\033[0m";
        for(int i=0; i<field.getResult().size(); i++) {
            if(i == user.getX()*field.getSize() + user.getY()) color = "\033[32m";
            else if(i == cheese.getX()*field.getSize() + cheese.getY()) color = "\033[34m";
            else color = "";

            if(field.getResult().get(i).getDown() == 1) System.out.print("_");
            else System.out.print(" ");

            System.out.print(color + field.getResult().get(i).getPathCost() + end);

            if(field.getResult().get(i).getDown() == 1) System.out.print( "_");
            else System.out.print(" ");

            if(field.getResult().get(i).getRight() == 1) System.out.print("|");
            else System.out.print(" ");

            if((i+1)%field.getSize() == 0) System.out.println();
        }
        System.out.println();
    }
}

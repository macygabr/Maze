package org.example.server.Backend;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
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
import org.springframework.dao.DataAccessException;
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
            usersPrivate.put(us.getLogin(), us.Private());
        }
        server.setUsers(usersPrivate);
        return server;
    }

    public void moveUser(Greeting obj) {
        String cookie = obj.getCookie();
        User curentUser = users.get(cookie);
        curentUser.move(obj.getX(),obj.getY());
        if(curentUser.getX() == cheese.getX() && curentUser.getY() == cheese.getY()) {
            curentUser.setBill(curentUser.getBill()+1);
            cheese.reboot(curentUser);
        }
    }

    public void Reboot(String cookie) throws IOException{
        if(!users.containsKey(cookie) || !(users.get(cookie).getAuthentication() == AuthenticationType.USER)) return;

        field = new Field(field.getSize());
        users.get(cookie).rebootLocation(field);
        cheese.reboot(users.get(cookie));

        for(User us : users.values())
            if(us.getAuthentication() == AuthenticationType.USER) us.rebootLocation(field);
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

        // for(Cookie cookie :cookies)
        //     System.err.println("\033[31m" + cookie.getName()+"="+cookie.getValue() + "\033[0m");
        
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("maze")) {
                User us = User.builder()
                                // .field(field)
                                .authentication(AuthenticationType.GUEST)
                                // .ip(request.getRemoteAddr())
                                // .cookie(cookie.getName()+"="+cookie.getValue())
                                .build();

                // user.setField(field);
                // user.setAuthentication(false);
                // user.setIp(request.getRemoteAddr());
                // user.setCookie(cookie.getName()+"="+cookie.getValue());
                // user.rebootLocation(field);
                // us.rebootLocation(field);
                user = us;
                // users.put(cookie.getName()+"="+cookie.getValue(), user);

                // for(User usit : users.values())
                //     System.err.println("\033[31m" + usit + "\033[0m");
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

    public void insertUser(User obj) throws DataAccessException {
        database.addUser(obj);
    }

    public Server FindPath(String cookie) throws IOException {
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

    public void disconnect(String cookie) throws SQLException {
        user = User.builder()
                        .authentication(AuthenticationType.GUEST)
                        .build();

        if(users.containsKey(cookie)) {
            users.remove(cookie);
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

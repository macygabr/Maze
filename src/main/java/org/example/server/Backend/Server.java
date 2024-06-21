package org.example.server.Backend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.example.server.database.Database;
import org.example.server.model.Cheese;
import org.example.server.model.Fild;
import org.example.server.model.Greeting;
import org.example.server.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class Server {
    private User user;
    private Cheese cheese;
    private Fild fild;
    private Map<String,User> users;
    private final Database database;
    
    @Value("${server.ip}")
    private String ip;
    @Value("${server.port}")
    private int port;
    @Value("${server.countPlayers}")
    private int countPlayers;

    @Autowired
    public Server(Fild fild, Database database) {
        this.database = database;
        this.user = null;
        this.fild = fild;
        this.cheese = new Cheese(fild.getSize());
        users = new HashMap<>();
    }

    public void Reboot(String cookie) {
        if(!users.containsKey(cookie) || !users.get(cookie).getAuthentication()) return;

        fild = new Fild(fild.getSize());
        cheese = new Cheese(fild.getSize());

        for(User us : users.values())
            if(us.getAuthentication()) us.rebootLocation(fild);
    }

    public Boolean CheckCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null)
            for(Cookie cookie : cookies)
                if(users.containsKey(cookie.getName()+"="+cookie.getValue())){
                    user = users.get(cookie.getName()+"="+cookie.getValue());
                    return true;
                }

        user = new User();
        user.setIp(request.getRemoteAddr());
        users.put(user.getCookieName()+"="+user.getCookieValue(), user);
        return false;
    }


    public Boolean checkUser(User obj) {
        if(!database.check(obj)) return false;
        return true;
    }

    public void insertUser(User obj) throws Exception {
        database.addUser(obj);
    }
    // private void SetPathCost(){
    //     for(Cell cell : fild.getResult())
    //         cell.setPathCost(0);
    // }

    // private void AlgorithmAstar() {
    //     Cell start = fild.getCell(user.getX(), user.getY());
    //     Cell goal = fild.getCell(cheese.getX(), cheese.getY());
    //     Set<Cell> openSet = new HashSet<>();
    //     Set<Cell> closedSet = new HashSet<>();
    //     HashMap<Cell, Cell> cameFrom = new HashMap<>();

    //     openSet.add(start);

    //     while (!openSet.isEmpty()) {
    //         Cell current = null;

    //         for (Cell node : openSet)
    //             if (current == null)  current = node;

    //         if (current.equals(goal)) return;

    //         openSet.remove(current);
    //         closedSet.add(current);

    //         for (Cell neighbor : fild.getNeighbors(current)) {
    //             if (closedSet.contains(neighbor)) continue;
    //             if (!openSet.contains(neighbor)) openSet.add(neighbor);
    //             neighbor.setPathCost(current.getPathCost()  + 1);
    //             printMap();
    //         }
    //     }
    // }

    // public void FindPath(Fild fild, User user, Cheese cheese) {
    //     this.fild = fild;
    //     this.user = user;
    //     this.cheese = cheese;
    //     SetPathCost();
    //     AlgorithmAstar();
    //     FindPath();
    // }

    // private void FindPath() {
    //     Cell goal = fild.getCell(cheese.getX(), cheese.getY());
    //     Cell current = fild.getCell(user.getX(), user.getY());
    //     while (!current.equals(goal)) {

    //         for(Cell neighbor : fild.getNeighbors(goal)) {
    //             if(neighbor.getPathCost() == goal.getPathCost() - 1) {
    //                 goal = neighbor;
    //                 break;
    //             }
    //         }

    //         if(!goal.equals(current)){
    //             PathX += String.valueOf(goal.getIndex()/fild.getSize()) + " ";
    //             PathY += String.valueOf(goal.getIndex()%fild.getSize()) + " ";
    //         }
    //     }
    // }

    // private void printMap(){
    //     String color;
    //     String end = "\033[0m";
    //     for(int i=0; i<fild.getResult().size(); i++) {
    //         if(i == user.getX()*fild.getSize() + user.getY()) color = "\033[32m";
    //         else if(i == cheese.getX()*fild.getSize() + cheese.getY()) color = "\033[34m";
    //         else color = "";

    //         if(fild.getMap()[i].charAt(1) == '1') System.out.print("_" );
    //         else System.out.print(" ");

    //         System.out.print(color + fild.getResult().get(i).getPathCost() + end);

    //         if(fild.getMap()[i].charAt(1) == '1') System.out.print( "_");
    //         else System.out.print(" ");

    //         if(fild.getMap()[i].charAt(0) == '1') System.out.print("|");
    //         else System.out.print(" ");

    //         if((i+1)%fild.getSize() == 0) System.out.println();
    //     }
    //     System.out.println();
    // }
}

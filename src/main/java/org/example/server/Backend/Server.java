package org.example.server.Backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import org.example.server.model.*;

@Getter
@Setter
public class Server {
    private User user;
    private Fild fild;
    private Cheese cheese;
    private String PathX;
    private String PathY;

    public Server() {
        PathX = "";
        PathY = "";
    }

    public void FindPath(Fild fild, User user, Cheese cheese) {
        this.fild = fild;
        this.user = user;
        this.cheese = cheese;
        SetPathCost();
        AlgorithmAstar();
        FindPath();
    }

    private void SetPathCost(){
        for(Cell cell : fild.getResult())
            cell.setPathCost(0);
    }

    private void AlgorithmAstar() {
        Cell start = fild.getCell(user.getX(), user.getY());
        Cell goal = fild.getCell(cheese.getX(), cheese.getY());
        Set<Cell> openSet = new HashSet<>();
        Set<Cell> closedSet = new HashSet<>();
        HashMap<Cell, Cell> cameFrom = new HashMap<>();

        openSet.add(start);

        while (!openSet.isEmpty()) {
            Cell current = null;

            for (Cell node : openSet)
                if (current == null)  current = node;

            if (current.equals(goal)) return;

            openSet.remove(current);
            closedSet.add(current);

            for (Cell neighbor : fild.getNeighbors(current)) {
                if (closedSet.contains(neighbor)) continue;
                if (!openSet.contains(neighbor)) openSet.add(neighbor);
                neighbor.setPathCost(current.getPathCost()  + 1);
                printMap();
            }
        }
    }

    private void FindPath(){
        Cell goal = fild.getCell(cheese.getX(), cheese.getY());
        Cell current = fild.getCell(user.getX(), user.getY());
        while (!current.equals(goal)) {

            for(Cell neighbor : fild.getNeighbors(goal)) {
                if(neighbor.getPathCost() == goal.getPathCost() - 1) {
                    goal = neighbor;
                    break;
                }
            }

            if(!goal.equals(current)){
                PathX += String.valueOf(goal.getIndex()/fild.getSize()) + " ";
                PathY += String.valueOf(goal.getIndex()%fild.getSize()) + " ";
            }
        }
    }

    private void printMap(){
        String color = "";
        String end = "\033[0m";
        for(int i=0; i<fild.getResult().size(); i++) {
            if(i == user.getX()*fild.getSize() + user.getY()) color = "\033[32m";
            else if(i == cheese.getX()*fild.getSize() + cheese.getY()) color = "\033[34m";
            else color = "";

            if(fild.getMap()[i].charAt(1) == '1') System.out.print("_" );
            else System.out.print(" ");

            System.out.print(color + fild.getResult().get(i).getPathCost() + end);
//            System.out.print(color + fild.getResult().get(i).getIndex() + end);

            if(fild.getMap()[i].charAt(1) == '1') System.out.print( "_");
            else System.out.print(" ");

            if(fild.getMap()[i].charAt(0) == '1') System.out.print("|");
            else System.out.print(" ");

            if((i+1)%fild.getSize() == 0) System.out.println();
        }
        System.out.println();
    }
}

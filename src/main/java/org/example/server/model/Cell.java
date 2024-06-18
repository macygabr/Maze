package org.example.server.model;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cell {

    private int index;
    private int value;
    private int sets;
    private int right;
    private int down;
    private String RightWall;
    private String DownWall;
    private static ArrayList<Cell> result = new ArrayList<>();
    private static int size;

    //AStar
    private int pathCost;
    private List<Cell> neighbors;

    public Cell() {
        result.add(this);
        right = Math.random() < 0.5 ? 0 : 1;
        down = Math.random() < 0.5 ? 0 : 1;
        sets = result.size();
        RightWall = "0";
        DownWall = "0";
        pathCost = 0;
        neighbors = new ArrayList<>();
        size =  (int)Math.round(Math.sqrt(result.size()));
    }

    public Cell(int index) {
        this();
        this.index = index;
    }

    public Cell getCell(int x, int y) {
        return result.get(y*size + x);
    }

    public static int getCount() {
        return result.size();
    }

    public void setResult(ArrayList<Cell> result){
        this.result = result;
    }

    public ArrayList<Cell> getResult(){
        return result;
    }
}

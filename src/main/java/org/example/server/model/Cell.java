package org.example.server.model;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private static int index;
    private int sets;
    private int right;
    private int down;
    // private int right;
    // private int down;

    // //AStar
    // private int pathCost;
    // private List<Cell> neighbors;

    public Cell() {
        // right = Math.random() < 0.5 ? 0 : 1;
        // down = Math.random() < 0.5 ? 0 : 1;
        sets = index;
        index++;
    }

    // public Cell(int index) {
    //     this();
    //     this.index = index;
    // }

    // public Cell getCell(int x, int y) {
    //     return result.get(y*size + x);
    // }

    // public static int getCount() {
    //     return result.size();
    // }

    // public void setResult(ArrayList<Cell> result){
    //     this.result = result;
    // }

    // public ArrayList<Cell> getResult(){
    //     return result;
    // }
}

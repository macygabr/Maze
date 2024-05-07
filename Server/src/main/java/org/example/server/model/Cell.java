package org.example.server.model;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Cell {
    private int value;
    private int sets;
    private int right;
    private int down;
    private String RightWall;
    private String DownWall;
    private static ArrayList<Cell> result = new ArrayList<>();
//    private static int[] WallRightVal = {
//            0, 1, 0, 0,
//            1, 0, 0, 0,
//            0, 1, 0, 0,
//            0, 1, 1, 0,
//    };
//
//    private static int[] WallDownVal = {
//            0, 1, 1, 0,
//            0, 0, 1, 1,
//            1, 1, 0, 1,
//            0, 0, 0, 0,
//    };


    public Cell() {
//        right = result.size() < WallRightVal.length ? WallRightVal[result.size()] : 0;
//        down = result.size() < WallDownVal.length ? WallDownVal[result.size()] : 0;
        result.add(this);
        right = Math.random() < 0.5 ? 0 : 1;
        down = Math.random() < 0.5 ? 0 : 1;
        sets = result.size();
        RightWall = "0";
        DownWall = "0";
    }
    public static int getCount() {
        return result.size();
    }
}

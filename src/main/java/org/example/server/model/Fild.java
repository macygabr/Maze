package org.example.server.model;

import java.util.UUID;

import static java.lang.Math.*;
import org.example.server.model.Cell;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Fild {
    private UUID id;
    private int size;
    private ArrayList<Cell> result = new ArrayList<>();

    public Fild(int size) {
        this.size = size;
        id = UUID.randomUUID();
        FillMap();
        Generate();
    }

    public String[] getMap() {
        String[] map = new String[size * size];
        for (int i = 0; i < size * size && i < result.size(); i++)
            map[i] = (result.get(i).getRightWall() + result.get(i).getDownWall());
        return map;
    }

    public UUID getUUID() {
        return id;
    }

    private void Generate() {
        for (int i = 0; i < size; i++) {
            GenerateVerticalWall(i);
             String color = "";
             for (Cell cell : result){
                 if(cell.getRightWall().equals("1") && cell.getDownWall().equals("1")) color = "\033[33m";
                 else if(cell.getRightWall().equals("1")) color = "\033[31m";
                 else if(cell.getDownWall().equals("1")) color = "\033[32m";
                 else color = "";
                 System.out.print(color + cell.getSets() + "\033[0m" + " ");
                 if((cell.getIndex()+1)%(size) == 0) System.out.println();
             }
             System.out.println();
            AddDownWall(i);
            GenerateHorizontalWall(i);
            if (i == size - 1) GenerateLastLine();

            color = "";
             for (Cell cell : result){
                 if(cell.getRightWall().equals("1") && cell.getDownWall().equals("1")) color = "\033[33m";
                 else if(cell.getRightWall().equals("1")) color = "\033[31m";
                 else if(cell.getDownWall().equals("1")) color = "\033[32m";
                 else color = "";
                 System.out.print(color + cell.getSets() + "\033[0m" + " ");
                 if((cell.getIndex()+1)%(size) == 0) System.out.println();
             }
             System.out.println();
        }
        DeleteWall();
    }
    private void FillMap() {
        for (int i = 0; i < size * size; i++)
            result.add(new Cell(i));
    }
    private void GenerateVerticalWall(int j) {
        for (int i = j * size, k = 0; k < size && j * size + k < result.size(); i++, k++) {
            if (result.get(i).getRight() == 0) ChangeSets(i);
            else result.get(i).setRightWall("1");
        }
    }

    private void AddDownWall(int j) {
        for (int i = j * size; i < j * size + size && i < result.size(); i++) {
            if (result.get(i).getDown() == 1) {
                int count = 0;
                int sets = result.get(i).getSets();
                for (int k = j * size; k < j * size + size && k < result.size(); k++)
                    if (result.get(k).getDownWall().equals("0") && sets == result.get(k).getSets()) count++;
                if (count > 1) result.get(i).setDownWall("1");
            }
        }
    }

    private void GenerateHorizontalWall(int j) {
        if (j >= size - 1) return;

        //copy
        for (int i = j * size; i < j * size + size; i++) {
            result.get(size + i).setSets(result.get(i).getSets());
            result.get(size + i).setValue(result.get(i).getValue());
            result.get(size + i).setRightWall(result.get(i).getRightWall());
            result.get(size + i).setDownWall(result.get(i).getDownWall());
        }

        //dell right wall
        for (int i = (j + 1) * size; i < (j + 1) * size + size && j + 1 < result.size(); i++)
            result.get(i).setRightWall("0");

        //new Sets
        for (int i = (j + 1) * size; i < (j + 1) * size + size && j + 1 < result.size(); i++)
            if (result.get(i).getDownWall().equals("1")) {
                result.get(i).setSets(new Cell().getSets());
            }

        //del down wall
        for (int i = (j + 1) * size; i < (j + 1) * size + size && j + 1 < result.size(); i++)
            result.get(i).setDownWall("0");

    }

    private void GenerateLastLine() {
//        //copy
//        int j = size - 1;
//        for (int i = (j-1) * size; i < (j-1) * size + size; i++) {
//            result.get(size + i).setSets(result.get(i).getSets());
//            result.get(size + i).setValue(result.get(i).getValue());
//            result.get(size + i).setRightWall(result.get(i).getRightWall());
//            result.get(size + i).setDownWall(result.get(i).getDownWall());
//        }

//        for (int i = size * (size - 1); i < size * size && i + 1 < result.size(); i++) {
//            result.get(i).setRightWall("1");
//        }

        for (int i = size * (size - 1); i < size * size && i + 1 < result.size(); i++) {
            if (result.get(i).getSets() != result.get(i + 1).getSets()) {
                result.get(i).setRightWall("0");
                result.get(i+1).setSets(result.get(i).getSets());
            }
        }
    }

    private void ChangeSets(int i) {
        int x = result.get(i).getSets();
        if (i + 1 < result.size()) x = result.get(i + 1).getSets();
        if (x == result.get(i).getSets()) result.get(i).setRightWall("1");
        else {
            for (int j = size * (int) Math.floor(i / size); j < size * (int) Math.floor(i / size) + size; j++) {
                if (result.get(j).getSets() == x)
                    result.get(j).setSets(result.get(i).getSets());
            }
        }
    }


    private void DeleteWall() {
        for (int i = 0, k = size - 1; i < result.size(); i++, k += size) {
            if (i >= size * (size - 1)) result.get(i).setDownWall("0");
            if (k < result.size()) result.get(k).setRightWall("0");
        }
    }

    public Cell getCell(int x, int y) {
        return result.get(x * size + y);
    }

    public ArrayList<Cell> getNeighbors(Cell cell) {
        ArrayList<Cell> res = new ArrayList<>();
        int sets = cell.getIndex();

        if (sets - 1 >= 0 && result.get(sets - 1) != null && (sets) % (size) != 0)
            if (result.get(sets - 1).getRightWall().equals("0"))
                res.add(result.get(sets - 1));

        if (sets + 1 < result.size() && result.get(sets + 1) != null && (sets + 1)%(size) != 0)
            if (result.get(sets).getRightWall().equals("0"))
                res.add(result.get(sets + 1));

        if (sets - size >= 0 && result.get(sets - size) != null)
            if (result.get(sets - size).getDownWall().equals("0"))
                res.add(result.get(sets - size));

        if (sets + size < result.size() && result.get(sets + size) != null)
            if (result.get(sets).getDownWall().equals("0"))
                res.add(result.get(sets + size));

        return res;
    }
}

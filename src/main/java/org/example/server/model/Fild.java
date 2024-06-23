package org.example.server.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("prototype")
public class Fild {
    private UUID id;
    private int size;
    private ArrayList<Cell> result;

    public Fild() {
        this.size = 10;
        id = UUID.randomUUID();
        result = new ArrayList<>();
        FillMap();
        Generate();
    }
    
    public Fild(int size) {
        this.size = size;
        id = UUID.randomUUID();
        result = new ArrayList<>();
        FillMap();
        Generate();
    }

    private void Generate() {
        for (int i = 0; i < size; i++) {
            
            GenerateVerticalWall(i);
            AddDownWall(i);
            if (i < size - 1) GenerateHorizontalWall(i);
            // Print();
            if (i == size - 1) GenerateLastLine();
            // Print();
        }
        DeleteWall();
    }
    private void FillMap() {
        for (int i = 0; i < size * size; i++)
            result.add(new Cell());
    }

    private void GenerateVerticalWall(int j) {
        for (int i = j * size; i < j * size + size; i++) {
            if (Math.random() < 0.5) ChangeSets(i);
            else result.get(i).setRight(1);
        }
    }

    private void AddDownWall(int j) {
        for (int i = j * size; i < j * size + size && i < result.size(); i++) {
            if (Math.random() < 0.5) {
                int count = 0;
                int sets = result.get(i).getSets();
                for (int k = j * size; k < j * size + size && k < result.size(); k++)
                    if (result.get(k).getDown() == 0 && sets == result.get(k).getSets()) count++;
                if (count > 1) result.get(i).setDown(1);
            }
        }
    }

    private void GenerateHorizontalWall(int j) {
        //copy
        for (int i = j * size; i < j * size + size; i++) {
            result.get(size + i).setSets(result.get(i).getSets());
            result.get(size + i).setRight(result.get(i).getRight());
            result.get(size + i).setDown(result.get(i).getDown());
        }

        //dell right wall
        for (int i = (j + 1) * size; i < (j + 1) * size + size && j + 1 < result.size(); i++)
            result.get(i).setRight(0);

        //new Sets
        for (int i = (j + 1) * size; i < (j + 1) * size + size && j + 1 < result.size(); i++)
            if (result.get(i).getDown() == 1)
                result.get(i).setSets(new Cell().getSets());

        //del down wall
        for (int i = (j + 1) * size; i < (j + 1) * size + size && j + 1 < result.size(); i++)
            result.get(i).setDown(0);
    }

    private void GenerateLastLine() {
        for (int i = size * (size - 1); i < size * size && i + 1 < result.size(); i++) {
            if (result.get(i).getSets() != result.get(i + 1).getSets()) {
                result.get(i).setRight(0);
            } else {
                result.get(i+1).setSets(result.get(i).getSets());
            }
        }
    }

    private void ChangeSets(int i) {
        int x = result.get(i).getSets();
        if (i + 1 < result.size()) x = result.get(i + 1).getSets();
        if (x == result.get(i).getSets()) result.get(i).setRight(1);
        else {
            for (int j = size * (int) Math.floor(i / size); j < size * (int) Math.floor(i / size) + size; j++) {
                if (result.get(j).getSets() == x)
                    result.get(j).setSets(result.get(i).getSets());
            }
        }
    }


    private void DeleteWall() {
        for (int i = 0, k = size - 1; i < result.size(); i++, k += size) {
            if (i >= size * (size - 1)) result.get(i).setDown(0);
            if (k < result.size()) result.get(k).setRight(0);
        }
    }

    public Cell getCell(int x, int y) {
        return result.get(x * size + y);
    }

    public ArrayList<Cell> getNeighbors(Cell cell) {
        ArrayList<Cell> res = new ArrayList<>();
        int sets = cell.getSets();

        if (sets - 1 >= 0 && result.get(sets - 1) != null && (sets) % (size) != 0)
            if (result.get(sets - 1).getRight() == 0)
                res.add(result.get(sets - 1));

        if (sets + 1 < result.size() && result.get(sets + 1) != null && (sets + 1)%(size) != 0)
            if (result.get(sets).getRight() == 0)
                res.add(result.get(sets + 1));

        if (sets - size >= 0 && result.get(sets - size) != null)
            if (result.get(sets - size).getDown() == 0)
                res.add(result.get(sets - size));

        if (sets + size < result.size() && result.get(sets + size) != null)
            if (result.get(sets).getDown() == 0)
                res.add(result.get(sets + size));

        return res;
    }

    public void saveMap(String directory) throws Exception {
        String fileName = id + "_" + "map.txt";
        Path filePath = Paths.get(directory, fileName);
        
        File file = new File(filePath.toString());
        if (!file.exists())
            file.createNewFile();

        StringBuilder mapData = new StringBuilder();
        mapData.append(size).append(' ').append(size).append('\n');

        for(int i =0; i<size*size; i++) {
            mapData.append(result.get(i).getRight()).append(' ');
            if ((i + 1) % size == 0)  mapData.append('\n');
        }
        mapData.append('\n');
        for(int i =0; i<size*size; i++) {
            mapData.append(result.get(i).getDown()).append(' ');
            if ((i + 1) % size == 0)  mapData.append('\n');
        }

        Files.write(filePath, mapData.toString().getBytes(), StandardOpenOption.WRITE);
    }

    public void loadMap(Path filePath) throws Exception {
        File file = new File(filePath.toString());
        
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath.toString());
        }

        List<String> lines = Files.readAllLines(filePath);
        if (lines.size() < 2) {
            throw new IOException("File format is incorrect");
        }

        String[] sizeParts = lines.get(0).split(" ");
        if (sizeParts.length != 2) {
            throw new IOException("File format is incorrect");
        }

        size = Integer.parseInt(sizeParts[0]);

        int index = 1;
        for (int i = 0; i < size; i++) {
            String[] rightParts = lines.get(index).split(" ");
            for (int k=0; k<size; k++) {
                result.get(i*(k+1) + k).setRight(Integer.parseInt(rightParts[k]));
            }
            index++;
        }

        index++;

        for (int i = 0; i < size; i++) {
            String[] downParts = lines.get(index).split(" ");
            for (int k=0; k<size; k++) {
                result.get(i*(k+1) + k).setDown(Integer.parseInt(downParts[k]));
            }
            index++;
        }
        Print();
    }

    public void Print() {
        String color;
         for (int k = 0; k<result.size(); k++) {
             if(result.get(k).getRight() == 1 && result.get(k).getDown() == 1) color = "\033[33m"; //yellow 
             else if(result.get(k).getRight() == 1) color = "\033[31m"; //red  
             else if(result.get(k).getDown() == 1) color = "\033[32m"; //green 
             else color = "";
             System.out.print(color + result.get(k).getSets() + "\033[0m" + " ");
             if((k+1)%(size) == 0) System.out.println();
         }
         System.out.println();
    }
}

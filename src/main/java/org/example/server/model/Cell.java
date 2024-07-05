package org.example.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private static int size;
    private int index;
    private int sets;
    private int right;
    private int down;
    private Boolean path;

    // AStar
    private int pathCost;

    public Cell() {
        this.sets = size;
        size++;
    }

    public Cell(int sets) {
        this.sets = sets;
        this.index = sets;
    }

    public Cell copy(){
        Cell cell = new Cell();
        cell.setIndex(index);
        cell.setSets(sets);
        cell.setRight(right);
        cell.setDown(down);
        cell.setPath(path);
        cell.setPathCost(pathCost);
        return cell;
    }
    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;

    //     Cell cell = (Cell) o;

    //     if (right != cell.right) return false;
    //     if (down != cell.down) return false;
    //     if (pathCost != cell.pathCost) return false;
    //     return path != null ? path.equals(cell.path) : cell.path == null;
    // }

    // @Override
    // public int hashCode() {
    //     int result = sets;
    //     result = 31 * result + right;
    //     result = 31 * result + down;
    //     result = 31 * result + (path != null ? path.hashCode() : 0);
    //     result = 31 * result + pathCost;
    //     return result;
    // }
}
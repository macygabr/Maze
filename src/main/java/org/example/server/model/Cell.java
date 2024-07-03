package org.example.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private int sets;
    private int right;
    private int down;
    private Boolean path;

    //AStar
    private int pathCost;
}

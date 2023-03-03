package ru.zalimannard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Edge {

    private Node begin;
    private Node end;
    private int weight;

}

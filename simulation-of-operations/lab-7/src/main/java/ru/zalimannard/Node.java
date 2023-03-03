package ru.zalimannard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node {

    private String name;

    public Node(Node other) {
        this.name = other.getName();
    }

}

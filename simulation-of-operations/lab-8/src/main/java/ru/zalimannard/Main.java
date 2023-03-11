package ru.zalimannard;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addEdge("7", "6", 5, 7);
        graph.addEdge("6", "3", 15, 4);
        graph.addEdge("6", "5", 7, 5);
        graph.addEdge("5", "3", 7, 9);
        graph.addEdge("5", "2", 5, 9);
        graph.addEdge("6", "2", 1, 5);
        graph.addEdge("2", "1", 11, 3);
        graph.addEdge("2", "4", 7, 2);
        graph.addEdge("1", "8", 13, 8);
        graph.addEdge("6", "4", 5, 9);
        graph.addEdge("4", "8", 11, 14);
        graph.addEdge("8", "9", 9, 4);
        graph.addEdge("4", "9", 7, 2);
        graph.addEdge("7", "3", 5, 8);
        System.out.println(graph);
    }

}
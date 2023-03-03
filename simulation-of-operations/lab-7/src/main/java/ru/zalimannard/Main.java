package ru.zalimannard;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.createNode("1");
        graph.createNode("2");
        graph.createNode("3");
        graph.createNode("4");
        graph.createNode("5");
        graph.createNode("6");
        graph.createNode("7");
        graph.createNode("8");
        graph.createNode("9");

        graph.createEdge(new Node("1"), new Node("2"), 11);
        graph.createEdge(new Node("1"), new Node("3"), 20);
        graph.createEdge(new Node("1"), new Node("4"), 14);
        graph.createEdge(new Node("2"), new Node("3"), 12);
        graph.createEdge(new Node("2"), new Node("5"), 13);
        graph.createEdge(new Node("2"), new Node("7"), 18);
        graph.createEdge(new Node("3"), new Node("4"), 17);
        graph.createEdge(new Node("3"), new Node("6"), 10);
        graph.createEdge(new Node("4"), new Node("6"), 0);
        graph.createEdge(new Node("4"), new Node("8"), 21);
        graph.createEdge(new Node("5"), new Node("7"), 13);
        graph.createEdge(new Node("6"), new Node("7"), 13);
        graph.createEdge(new Node("6"), new Node("8"), 14);
        graph.createEdge(new Node("6"), new Node("9"), 17);
        graph.createEdge(new Node("7"), new Node("9"), 15);
        graph.createEdge(new Node("8"), new Node("9"), 11);

        calc(graph);

        System.out.println("Теперь сделаем увеличение из задания");
        Edge incEdge = graph.getEdge(new Node("6"), new Node("9"));
        graph.setEdge(incEdge, incEdge.getWeight() + 8);

        calc(graph);
    }

    public static void calc(Graph graph) {
        System.out.println(graph);
    }

}
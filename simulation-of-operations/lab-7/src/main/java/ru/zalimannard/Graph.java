package ru.zalimannard;

import java.util.ArrayList;

public class Graph {

    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();

    public void createNode(String name) {
        nodes.add(new Node(name));
    }

    public void createEdge(Node begin, Node end, int weight) {
        if ((nodes.contains(new Node(begin))) && (nodes.contains(new Node(end)))) {
            edges.add(new Edge(new Node(begin), new Node(end), weight));
        } else {
            throw new RuntimeException("Нет такой вершины");
        }
    }

    public void setEdgeWeight(Edge targetEdge, int weight) {
        for (Edge edge : getEdges()) {
            if (edge.equals(targetEdge)) {
                edge.setWeight(weight);
            }
        }
    }

    public Edge getEdge(Node begin, Node end) {
        for (Edge edge : getEdges()) {
            if ((edge.getBegin().equals(begin)) && (edge.getEnd().equals(end))) {
                return new Edge(edge);
            }
        }
        throw new RuntimeException("Нет такого ребра");
    }

    public ArrayList<Node> getNodes() {
        return new ArrayList<>(nodes);
    }

    public ArrayList<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public ArrayList<Edge> allOutgoing(Node node) {
        if (!isNodeExist(node)) {
            throw new RuntimeException("Вершины не существует");
        }

        ArrayList<Edge> answer = new ArrayList<>();
        for (Edge edge : getEdges()) {
            if (edge.getBegin().equals(node)) {
                Edge newEdgeForAnswer = new Edge(edge.getBegin(), edge.getEnd(), edge.getWeight());
                answer.add(newEdgeForAnswer);
            }
        }

        return answer;
    }

    public ArrayList<Edge> allIncoming(Node node) {
        if (!isNodeExist(node)) {
            throw new RuntimeException("Вершины не существует");
        }

        ArrayList<Edge> answer = new ArrayList<>();
        for (Edge edge : getEdges()) {
            if (edge.getEnd().equals(node)) {
                Edge newEdgeForAnswer = new Edge(edge.getBegin(), edge.getEnd(), edge.getWeight());
                answer.add(newEdgeForAnswer);
            }
        }

        return answer;
    }

    private boolean isNodeExist(Node targetNode) {
        for (Node node : getNodes()) {
            if (node.getName().equals(targetNode.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

}

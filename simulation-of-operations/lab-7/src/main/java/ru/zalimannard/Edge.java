package ru.zalimannard;

import java.util.Objects;

public class Edge {

    private Node begin;
    private Node end;
    private int weight;

    public Edge(Node begin, Node end, int weight) {
        this.begin = begin;
        this.end = end;
        this.weight = weight;
    }

    public Edge(Edge other) {
        this.begin = other.getBegin();
        this.end = other.getEnd();
        this.weight = other.getWeight();
    }

    public Node getBegin() {
        return begin;
    }

    public void setBegin(Node begin) {
        this.begin = begin;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return getWeight() == edge.getWeight() && Objects.equals(getBegin(), edge.getBegin()) && Objects.equals(getEnd(), edge.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBegin(), getEnd(), getWeight());
    }

    @Override
    public String toString() {
        return "Edge{" +
                "begin=" + begin +
                ", end=" + end +
                ", weight=" + weight +
                '}';
    }

}

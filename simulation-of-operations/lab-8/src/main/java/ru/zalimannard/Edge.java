package ru.zalimannard;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Edge {

    @EqualsAndHashCode.Include
    private final String begin;
    @EqualsAndHashCode.Include
    private final String end;
    private Integer weight = 0;
    private Integer bandwidth = 0;
    private Integer flow = 0;

    public Edge(String begin, String end, Integer weight, Integer bandwidth) {
        this.begin = begin;
        this.end = end;
        this.setWeight(weight);
        this.setBandwidth(bandwidth);
    }

    public Edge(String begin, String end, Integer bandwidth) {
        this.begin = begin;
        this.end = end;
        this.setBandwidth(bandwidth);
    }

    public Edge(String begin, String end, Integer weight, Integer bandwidth, Integer flow) {
        this.begin = begin;
        this.end = end;
        this.setWeight(weight);
        this.setBandwidth(bandwidth);
        this.setFlow(flow);
    }

    public Edge(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public Edge(Edge edge) {
        this.begin = edge.getBegin();
        this.end = edge.getEnd();
        this.setWeight(edge.getWeight());
        this.setBandwidth(edge.getBandwidth());
        this.setFlow(edge.getFlow());
    }

}

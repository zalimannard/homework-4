package ru.zalimannard;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
public class Graph {

    private final ArrayList<Edge> edges = new ArrayList<>();

    public void addEdge(String begin, String end, Integer weight, Integer bandwidth) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                return;
            }
        }
        Edge newEdge = new Edge(begin, end, weight, bandwidth);
        edges.add(newEdge);
    }

    public void addEdge(String begin, String end, Integer weight, Integer bandwidth, Integer flow) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                return;
            }
        }
        Edge newEdge = new Edge(begin, end, weight, bandwidth, flow);
        edges.add(newEdge);
    }

    public void delEdge(String begin, String end) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                edges.remove(i);
            }
        }
    }

    public Edge getEdge(String begin, String end) {
        if (!edges.contains(new Edge(begin, end))) {
            return null;
        }
        return edges.get(edges.indexOf(new Edge(begin, end)));
    }

    public void setWeight(String begin, String end, Integer weight) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                edges.get(i).setWeight(weight);
            }
        }
    }

    public void setBandwidth(String begin, String end, Integer bandwidth) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                edges.get(i).setBandwidth(bandwidth);
            }
        }
    }

    public void setFlow(String begin, String end, Integer flow) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                edges.get(i).setFlow(flow);
            }
        }
    }

    public ArrayList<String> allNodeNames() {
        Set<String> answer = new HashSet<>();
        for (int i = edges.size() - 1; i >= 0; --i) {
            answer.add(edges.get(i).getBegin());
            answer.add(edges.get(i).getEnd());
        }
        ArrayList<String> answerArrayList = new ArrayList<>(answer);
        Collections.sort(answerArrayList);
        return answerArrayList;
    }

    public ArrayList<Edge> allIncoming(String nodeName) {
        if (!allNodeNames().contains(nodeName)) {
            throw new RuntimeException("Нет вершины " + nodeName);
        }

        ArrayList<Edge> answer = new ArrayList<>();
        for (Edge edge : edges) {
            if ((edge.getEnd().equals(nodeName)) && (edge.getWeight() != null) && (edge.getBandwidth() != 0)) {
                Edge newEdgeForAnswer = new Edge(edge);
                answer.add(newEdgeForAnswer);
            }
        }
        return answer;
    }

    public ArrayList<Edge> allOutgoing(String nodeName) {
        if (!allNodeNames().contains(nodeName)) {
            throw new RuntimeException("Нет вершины " + nodeName);
        }

        ArrayList<Edge> answer = new ArrayList<>();
        for (Edge edge : edges) {
            if ((edge.getBegin().equals(nodeName)) && (edge.getWeight() != null) && (edge.getBandwidth() != 0)) {
                Edge newEdgeForAnswer = new Edge(edge);
                answer.add(newEdgeForAnswer);
            }
        }
        return answer;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String horizontalTableSymbol = "-";
        String verticalTableSymbol = "|";
        int cellWidthIncludeLines = 10;
        ArrayList<String> nodeNames = allNodeNames();
        String horizontalLine = String.join("",
                Collections.nCopies(cellWidthIncludeLines * (nodeNames.size() + 1) + 1, horizontalTableSymbol)) + "\n";

        // Первая строка
        result.append(horizontalLine);
        result.append(verticalTableSymbol).append(genCellValue("из\\в", cellWidthIncludeLines)).append(verticalTableSymbol);
        for (String nodeName : nodeNames) {
            result.append(genCellValue(nodeName, cellWidthIncludeLines)).append(verticalTableSymbol);
        }
        result.append("\n");
        result.append(horizontalLine);

        // Остальные
        for (String lineName : nodeNames) {
            result.append(verticalTableSymbol).append(genCellValue(lineName, cellWidthIncludeLines)).append(verticalTableSymbol);

            for (String columnName : nodeNames) {
                result.append(genCellValue(getEdge(lineName, columnName), cellWidthIncludeLines)).append(verticalTableSymbol);
            }

            result.append("\n");
            result.append(horizontalLine);
        }

        return result.toString();
    }

    private String genCellValue(Edge edge, int cellWidthIncludeLines) {
        if (edge == null) {
            return genCellValue("", cellWidthIncludeLines);
        }
        if (edge.getWeight() == null) {
            return genCellValue("", cellWidthIncludeLines);
        }
        if (edge.getBandwidth() == null) {
            return genCellValue(String.valueOf(edge.getWeight()), cellWidthIncludeLines);
        }
        String preAnswer = edge.getWeight() + "/" + edge.getBandwidth() + "/" + edge.getFlow();
        return " " + preAnswer + genSpace(preAnswer, cellWidthIncludeLines - 2);
    }

    private String genCellValue(String text, int cellWidthIncludeLines) {
        return " " + text + genSpace(text, cellWidthIncludeLines - 2);
    }

    private String genSpace(String value, int cellWidth) {
        return String.join("", Collections.nCopies(cellWidth - value.length(), " "));
    }

}

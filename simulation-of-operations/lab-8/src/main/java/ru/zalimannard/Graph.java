package ru.zalimannard;

import lombok.EqualsAndHashCode;

import java.util.*;

@EqualsAndHashCode
public class Graph {
    private final ArrayList<Edge> edges = new ArrayList<>();

    public void addEdge(String begin, String end, int weight, int bandwidth) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                return;
            }
        }
        Edge newEdge = new Edge(begin, end, weight, bandwidth);
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

    public void setWeight(String begin, String end, int weight) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                edges.get(i).setWeight(weight);
            }
        }
    }

    public void setBandwidth(String begin, String end, int bandwidth) {
        for (int i = edges.size() - 1; i >= 0; --i) {
            if (edges.get(i).equals(new Edge(begin, end))) {
                edges.get(i).setBandwidth(bandwidth);
            }
        }
    }

    public void setFlow(String begin, String end, int flow) {
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

    @Override
    public String toString() {
        String result = "";
        String horizontalTableSymbol = "-";
        String verticalTableSymbol = "|";
        int cellWidthIncludeLines = 10;
        ArrayList<String> nodeNames = allNodeNames();
        String horizontalLine = String.join("",
                Collections.nCopies(cellWidthIncludeLines * (nodeNames.size() + 1) + 1, horizontalTableSymbol)) + "\n";

        // Первая строка
        result += horizontalLine;
        result += verticalTableSymbol + genCellValue("из\\в", cellWidthIncludeLines) + verticalTableSymbol;
        for (String nodeName : nodeNames) {
            result += genCellValue(nodeName, cellWidthIncludeLines) + verticalTableSymbol;
        }
        result += "\n";
        result += horizontalLine;

        // Остальные
        for (String lineName : nodeNames) {
            result += verticalTableSymbol + genCellValue(lineName, cellWidthIncludeLines) + verticalTableSymbol;

            for (String columnName : nodeNames) {
                result += genCellValue(getEdge(lineName, columnName), cellWidthIncludeLines) + verticalTableSymbol;
            }

            result += "\n";
            result += horizontalLine;
        }

        return result;
    }

    private String genCellValue(Edge edge, int cellWidthIncludeLines) {
        if (edge == null) {
            return genCellValue("", cellWidthIncludeLines);
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

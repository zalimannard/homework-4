package ru.zalimannard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Теперь сделаем увеличение из задания");
        Edge incEdge = graph.getEdge(new Node("6"), new Node("9"));
        graph.setEdgeWeight(incEdge, incEdge.getWeight() + 8);

        calc(graph);
    }

    public static void calc(Graph graph) {
        System.out.println();
        System.out.println("Входы:");
        System.out.println(input(graph));

        System.out.println();
        System.out.println("Выходы:");
        System.out.println(output(graph));

        MatrixFractional earlyAction = earlyAction(graph);
        System.out.println();
        System.out.println("Ранние времена:");
        System.out.println(earlyAction(graph));

        MatrixFractional table = earlyAction(graph);
        Cell criticalWayCell = table.maxInColumnCell("Max", new ArrayList<>());
        Double criticalWayLength = table.get(criticalWayCell);
        System.out.println();
        System.out.println("Длина критического пути:");
        System.out.println(criticalWayLength);

        Graph invertedGraph = invertGraph(graph);
        MatrixFractional lateAction = earlyAction(invertedGraph);
        for (String row : lateAction.rowNames()) {
            lateAction.set(new Cell("Max", row), criticalWayLength - lateAction.get(new Cell("Max", row)));
        }
        System.out.println();
        System.out.println("Поздние сроки совершения:");
        System.out.println(lateAction);

        MatrixFractional fullTimeReserves = fullTimeReserves(graph, earlyAction, lateAction);
        System.out.println();
        System.out.println("Полные резервы времени:");
        System.out.println(fullTimeReserves);

        MatrixFractional freeTimeReserves = freeTimeReserves(graph, earlyAction);
        System.out.println();
        System.out.println("Свободные резервы времени:");
        System.out.println(freeTimeReserves);

        MatrixFractional critical = new MatrixFractional(fullTimeReserves);
        Graph graphCritical = new Graph();
        graphCritical.createNode(input(graph).get(0));
        for (String row : fullTimeReserves.rowNames()) {
            if (Math.abs(critical.get(new Cell("ВЕС", row))) > 0.000001) {
                critical.removeRow(row);
            } else {
                if (!graphCritical.getNodes().contains(new Node(row.split("->")[0]))) {
                    graphCritical.createNode(row.split("->")[0]);
                }
                if (!graphCritical.getNodes().contains(new Node(row.split("->")[1]))) {
                    graphCritical.createNode(row.split("->")[1]);
                }
                graphCritical.createEdge(new Node(row.split("->")[0]),
                        new Node(row.split("->")[1]),
                        0);
            }
        }
        System.out.println();
        System.out.println("Критические работы:");
        System.out.println(critical);

        System.out.println();
        System.out.println("Критический путь:");
        System.out.println(critical);
        System.out.print(input(graph).get(0));
        String currentRowName = "1";
        while (!currentRowName.equals(output(graph).get(0))) {
            System.out.print("->");
            System.out.print(graphCritical.allOutgoing(new Node(currentRowName)).get(0).getEnd().getName());
            currentRowName = graphCritical.allOutgoing(new Node(currentRowName)).get(0).getEnd().getName();
        }
        System.out.println();
    }

    public static List<String> input(Graph graph) {
        List<String> answer = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            answer.add(node.getName());
        }
        for (Edge edge : graph.getEdges()) {
            answer.remove(edge.getEnd().getName());
        }

        return answer;
    }

    public static List<String> output(Graph graph) {
        List<String> answer = new ArrayList<>();

        for (Node node : graph.getNodes()) {
            answer.add(node.getName());
        }
        for (Edge edge : graph.getEdges()) {
            answer.remove(edge.getBegin().getName());
        }

        return answer;
    }

    public static MatrixFractional earlyAction(Graph targetGraph) {
        MatrixFractional table = new MatrixFractional();
        ArrayList<Node> nodes = targetGraph.getNodes();
        ArrayList<Node> completeNode = new ArrayList<>();

        while (completeNode.size() != nodes.size()) {
            for (Node node : nodes) {
                if (!completeNode.contains(node)) {
                    if (subSet(completeNode, beginNode(targetGraph.allIncoming(node)))) {
                        ArrayList<Edge> beginNodes = targetGraph.allIncoming(node);
                        double max = Double.MIN_VALUE;
                        for (Edge edge : beginNodes) {
                            max = Math.max(edge.getWeight() + table.get(new Cell("Max", edge.getBegin().getName())), max);
                        }
                        table.set(new Cell("Max", node.getName()), max);
                        completeNode.add(node);
                    }
                }
            }
        }

        return table;
    }

    private static ArrayList<Node> beginNode(ArrayList<Edge> edges) {
        Set<Node> nodes = new HashSet<>();
        for (Edge edge : edges) {
            nodes.add(edge.getBegin());
        }
        return new ArrayList<>(nodes);
    }

    private static ArrayList<Node> endNode(ArrayList<Edge> edges) {
        Set<Node> nodes = new HashSet<>();
        for (Edge edge : edges) {
            nodes.add(edge.getEnd());
        }
        return new ArrayList<>(nodes);
    }

    private static boolean subSet(ArrayList<Node> big, ArrayList<Node> small) {
        ArrayList<Node> smallLocal = new ArrayList<>(small);
        for (Node node : big) {
            smallLocal.remove(node);
        }
        return smallLocal.size() == 0;
    }

    private static Graph invertGraph(Graph targetGraph) {
        Graph answer = new Graph();
        for (Node node : targetGraph.getNodes()) {
            answer.createNode(node.getName());
        }
        for (Edge edge : targetGraph.getEdges()) {
            answer.createEdge(new Node(edge.getEnd().getName()), new Node(edge.getBegin().getName()), edge.getWeight());
        }
        return answer;
    }

    private static MatrixFractional fullTimeReserves(Graph graph, MatrixFractional earlyAction,
                                                     MatrixFractional lateAction) {
        MatrixFractional answer = new MatrixFractional();
        for (Edge edge : graph.getEdges()) {
            double value = lateAction.get(new Cell("Max", edge.getEnd().getName())) -
                    earlyAction.get(new Cell("Max", edge.getBegin().getName())) -
                    edge.getWeight();
            answer.set(new Cell("ВЕС", edge.getBegin().getName() + "->" + edge.getEnd().getName()), value);
        }
        return answer;
    }

    private static MatrixFractional freeTimeReserves(Graph graph, MatrixFractional earlyAction) {
        MatrixFractional answer = new MatrixFractional();
        for (Edge edge : graph.getEdges()) {
            double value = earlyAction.get(new Cell("Max", edge.getEnd().getName())) -
                    earlyAction.get(new Cell("Max", edge.getBegin().getName())) -
                    edge.getWeight();
            answer.set(new Cell("ВЕС", edge.getBegin().getName() + "->" + edge.getEnd().getName()), value);
        }
        return answer;
    }

}
package ru.zalimannard;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addEdge("1", "2", 5, 4);
        graph.addEdge("1", "3", 1, 2);
        graph.addEdge("3", "2", 1, 3);
        graph.addEdge("2", "4", 1, 2);
        graph.addEdge("3", "4", 6, 3);

//        graph.addEdge("7", "6", 5, 7);
//        graph.addEdge("6", "3", 15, 4);
//        graph.addEdge("6", "5", 7, 5);
//        graph.addEdge("5", "3", 7, 9);
//        graph.addEdge("5", "2", 5, 9);
//        graph.addEdge("6", "2", 1, 5);
//        graph.addEdge("2", "1", 11, 3);
//        graph.addEdge("2", "4", 7, 2);
//        graph.addEdge("1", "8", 13, 8);
//        graph.addEdge("6", "4", 5, 9);
//        graph.addEdge("4", "8", 11, 14);
//        graph.addEdge("8", "9", 9, 4);
//        graph.addEdge("4", "9", 7, 2);
//        graph.addEdge("7", "3", 5, 8);
//        graph.addEdge("3", "9", 10, 3);
        System.out.println(graph);

        String input = input(graph);
        System.out.println("Вход графа: " + input);
        System.out.println();

        String output = output(graph);
        System.out.println("Выход графа: " + output);
        System.out.println();

        getMinCostWay(graph, input, output);
    }

    public static String input(Graph graph) {
        List<String> answer = new ArrayList<>(graph.allNodeNames());

        for (int i = answer.size() - 1; i >= 0; --i) {
            if (graph.allIncoming(answer.get(i)).size() > 0) {
                answer.remove(i);
            }
        }

        if (answer.size() > 1) {
            throw new RuntimeException("Должен быть один вход");
        }
        return answer.get(0);
    }

    public static String output(Graph graph) {
        List<String> answer = new ArrayList<>(graph.allNodeNames());

        for (int i = answer.size() - 1; i >= 0; --i) {
            if (graph.allOutgoing(answer.get(i)).size() > 0) {
                answer.remove(i);
            }
        }

        if (answer.size() > 1) {
            throw new RuntimeException("Должен быть один выход");
        }
        return answer.get(0);
    }

    public static ArrayList<Edge> getMinCostWay(Graph targetGraph, String begin, String end) {
        Graph minimalWays = getMinimalWays(targetGraph);
        System.out.println("Таблица расстояний после алгоритма Флойда:");
        System.out.println(minimalWays);
        System.out.println();

        System.out.println("Минимальная стоимость пути от точки входа до точки выхода: ");
        System.out.println(minimalWays.getEdge(begin, end).getWeight());
        System.out.println();

        ArrayList<Edge> answer = minWayByMinCost(minimalWays, begin, end, minimalWays.getEdge(begin, end).getWeight());
        System.out.println("Порядок вершин в минимальном пути: ");
        System.out.print(begin);
        for (int i = 0; i < answer.size(); ++i) {
            System.out.print(" -> " + answer.get(i).getEnd());
        }
        System.out.println();

        return answer;
    }

    private static ArrayList<Edge> minWayByMinCost(Graph graph, String begin, String end, int minCost) {
        String currentNodeName = begin;
        ArrayList<Edge> answer = new ArrayList<>();

        while (!currentNodeName.equals(end)) {
            for (Edge applicant : graph.allOutgoing(currentNodeName)) {
                if ((graph.getEdge(begin, applicant.getEnd()).getWeight() == minCost) && applicant.getEnd().equals(end)) {
                    answer.add(new Edge(applicant));
                    currentNodeName = applicant.getEnd();
                } else if (graph.getEdge(begin, applicant.getEnd()).getWeight() +
                        graph.getEdge(applicant.getEnd(), end).getWeight() == minCost) {
                    answer.add(new Edge(applicant));
                    currentNodeName = applicant.getEnd();
                    break;
                }
            }
        }
        return answer;
    }

    private static Graph getMinimalWays(Graph targetGraph) {
        Graph graph = new Graph();
        for (String begin : targetGraph.allNodeNames()) {
            for (String end : targetGraph.allNodeNames()) {
                Edge edge = targetGraph.getEdge(begin, end);
                if (edge != null) {
                    graph.addEdge(begin, end, edge.getWeight(), edge.getBandwidth(), edge.getFlow());
                } else {
                    graph.addEdge(begin, end, null, null);
                }
            }
        }

        // Алгоритм Флойда
        for (String k : graph.allNodeNames()) {
            for (String i : graph.allNodeNames()) {
                for (String j : graph.allNodeNames()) {

                    if ((graph.getEdge(i, k).getWeight() != null) && (graph.getEdge(k, j).getWeight() != null)) {
                        if (graph.getEdge(i, j).getWeight() != null) {
                            if (graph.getEdge(i, j).getWeight() >
                                    graph.getEdge(i, k).getWeight() + graph.getEdge(k, j).getWeight()) {
                                graph.setWeight(i, j, graph.getEdge(i, k).getWeight() + graph.getEdge(k, j).getWeight());
                                graph.setBandwidth(i, j, 0);
                                graph.setFlow(i, j, 0);
                            }
                        } else {
                            graph.setWeight(i, j, graph.getEdge(i, k).getWeight() + graph.getEdge(k, j).getWeight());
                            graph.setBandwidth(i, j, 0);
                            graph.setFlow(i, j, 0);
                        }
                    }

                }
            }
        }
        return graph;
    }

}
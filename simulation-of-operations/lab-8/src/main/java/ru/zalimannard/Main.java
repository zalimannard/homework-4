package ru.zalimannard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        // Сколько единиц нужно провести через схему. Если столько провести невозможно, будет либо исключение, либо
        // бесконечный цикл, либо сообщение "Такой большой поток невозможен"
        int targetFlow = 15;

        Graph graph = new Graph();
        graph.addEdge("1", "2", 5, 5);
        graph.addEdge("1", "3", 6, 12);
        graph.addEdge("1", "4", 6, 10);
        graph.addEdge("2", "3", 3, 2);
        graph.addEdge("3", "5", 5, 5);
        graph.addEdge("3", "6", 3, 3);
        graph.addEdge("3", "8", 16, 5);
        graph.addEdge("4", "5", 10, 4);
        graph.addEdge("4", "7", 12, 7);
        graph.addEdge("5", "6", 5, 6);
        graph.addEdge("5", "7", 5, 7);
        graph.addEdge("6", "8", 10, 8);
        graph.addEdge("7", "8", 4, 4);
        System.out.println("Изначальный граф:");
        System.out.println(graph);

        String input = input(graph);
        System.out.println("Вход графа: " + input);
        System.out.println();

        String output = output(graph);
        System.out.println("Выход графа: " + output);
        System.out.println();

        int currentFlow = 0;
        while (currentFlow < targetFlow) {
            System.out.println("Новая итерация. Найден поток на " + currentFlow + " из " + targetFlow);
            ArrayList<Edge> minCostWay = getMinCostWay(graph, input, output);
            graph = updateFlowAndGraph(graph, minCostWay, output, targetFlow);

            if (currentFlow == calcFlow(graph, output)) {
                throw new RuntimeException("Такой большой поток невозможен");
            } else {
                currentFlow = calcFlow(graph, output);
                System.out.println("Новый поток: " + currentFlow);
                System.out.println();
            }
        }

        int answerCost = 0;
        System.out.println("Задача решена");
        System.out.println("из / в / цена * поток = стоимость");
        for (Edge edge : graph.getEdges()) {
            if (edge.getFlow() != null) {
                if ((edge.getFlow() > 0) && (edge.getWeight() > 0)) {
                    System.out.println(edge.getBegin() + "--->" + edge.getEnd() + ": " + edge.getWeight() + "*" + edge.getFlow() + "=" + edge.getWeight() * edge.getFlow());
                    answerCost += edge.getWeight() * edge.getFlow();
                }
            }
        }
        System.out.println("Итоговая стоимость: " + answerCost);
    }

    private static Graph updateFlowAndGraph(Graph graph, ArrayList<Edge> minCostWay, String output, int targetFlow) {
        int newFlow = minFreeBandwidth(minCostWay);
        while ((newFlow > 0) && (targetFlow != calcFlow(graph, output))) {
            for (Edge edge : minCostWay) {
                if (edge.getWeight() > 0) {
                    graph.setFlow(edge.getBegin(), edge.getEnd(),
                            graph.getEdge(edge.getBegin(), edge.getEnd()).getFlow() + 1);
                    if (graph.getEdge(edge.getEnd(), edge.getBegin()) == null) {
                        graph.addEdge(edge.getEnd(), edge.getBegin(), -edge.getWeight(), edge.getBandwidth(), 1);
                    } else {
                        graph.setFlow(edge.getEnd(), edge.getBegin(),
                                graph.getEdge(edge.getEnd(), edge.getBegin()).getFlow() + 1);
                    }
                } else if (edge.getWeight() < 0) {
                    graph.setFlow(edge.getBegin(), edge.getEnd(),
                            graph.getEdge(edge.getBegin(), edge.getEnd()).getFlow() - 1);
                    if (graph.getEdge(edge.getEnd(), edge.getBegin()) == null) {
                        graph.addEdge(edge.getEnd(), edge.getBegin(), edge.getWeight(), edge.getBandwidth(), 1);
                    } else {
                        graph.setFlow(edge.getEnd(), edge.getBegin(),
                                graph.getEdge(edge.getEnd(), edge.getBegin()).getFlow() - 1);
                    }
                }

            }
            --newFlow;
        }
        System.out.println(graph);
        return graph;
    }

    private static int calcFlow(Graph graph, String output) {
        int answer = 0;
        for (Edge edge : graph.allIncoming(output)) {
            answer += edge.getFlow();
        }
        return answer;
    }

    private static String input(Graph graph) {
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

    private static String output(Graph graph) {
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

    private static ArrayList<Edge> getMinCostWay(Graph targetGraph, String begin, String end) {
        Graph minimalWays = getMinimalWays(targetGraph);
        System.out.println("Таблица расстояний после алгоритма Флойда:");
        System.out.println(minimalWays);
        System.out.println();

        Integer minimalWayCost = minimalWays.getEdge(begin, end).getWeight();
        System.out.println("Удельная стоимость от " + begin + " до " + end + ": ");
        System.out.println(minimalWayCost);
        System.out.println();

        ArrayList<Edge> answer = minWayByMinCost(targetGraph, minimalWays, begin, end,
                minimalWays.getEdge(begin, end).getWeight());
        System.out.println("Порядок вершин в минимальном пути: ");
        System.out.print(answer.get(0).getBegin());
        for (int i = 0; i < answer.size(); ++i) {
            System.out.print(" --" + "(" + answer.get(i).getWeight() + ")-> " + answer.get(i).getEnd());
        }
        System.out.println();
        System.out.println();

        return answer;
    }

    private static int minFreeBandwidth(ArrayList<Edge> way) {
        int min = way.get(0).getBandwidth() - way.get(0).getFlow();
        for (Edge edge : way) {
            if (edge.getBandwidth() - edge.getFlow() < min) {
                min = edge.getBandwidth() - edge.getFlow();
            }
        }
        return min;
    }

    private static ArrayList<Edge> minWayByMinCost(Graph targetGraph, Graph graph, String begin, String end,
                                                   int minCost) {
        String currentNodeName = begin;
        ArrayList<Edge> answer = new ArrayList<>();

        while (!currentNodeName.equals(end)) {
            ArrayList<Edge> outgoingEdges = targetGraph.allOutgoing(currentNodeName);
            Collections.shuffle(outgoingEdges);
            for (Edge applicant : outgoingEdges) {
                if ((!Objects.equals(applicant.getBandwidth(), applicant.getFlow()))) {
                    if (applicant.getWeight().equals(graph.getEdge(applicant.getBegin(), applicant.getEnd()).getWeight())) {
                        if ((graph.getEdge(begin, applicant.getEnd()).getWeight() == minCost) && (applicant.getBandwidth() != null) && (applicant.getEnd().equals(end))) {
                            answer.add(new Edge(applicant));
                            currentNodeName = applicant.getEnd();
                            break;
                        } else if (graph.getEdge(begin, applicant.getEnd()).getWeight() +
                                graph.getEdge(applicant.getEnd(), end).getWeight() == minCost) {
                            answer.add(new Edge(applicant));
                            currentNodeName = applicant.getEnd();
                            break;
                        }
                    }
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
                    if (Objects.equals(edge.getBandwidth(), edge.getFlow())) {
                        graph.addEdge(begin, end, null, null);
                    } else {
                        graph.addEdge(begin, end, edge.getWeight(), edge.getBandwidth(), edge.getFlow());
                    }
                } else {
                    graph.addEdge(begin, end, null, null);
                }
            }
        }

        System.out.println(graph);
        // Алгоритм Флойда
        for (String k : graph.allNodeNames()) {
            for (String i : graph.allNodeNames()) {
                for (String j : graph.allNodeNames()) {

                    if ((graph.getEdge(i, k).getWeight() != null) && (graph.getEdge(k, j).getWeight() != null)) {
                        if (graph.getEdge(i, j).getWeight() != null) {
                            if (graph.getEdge(i, j).getWeight() >
                                    graph.getEdge(i, k).getWeight() + graph.getEdge(k, j).getWeight()) {
                                graph.setWeight(i, j, graph.getEdge(i, k).getWeight() + graph.getEdge(k, j).getWeight());
                                graph.setBandwidth(i, j, graph.getEdge(i, j).getBandwidth());
                                graph.setFlow(i, j, graph.getEdge(i, j).getFlow());
                            }
                        } else {
                            graph.setWeight(i, j, graph.getEdge(i, k).getWeight() + graph.getEdge(k, j).getWeight());
                            graph.setBandwidth(i, j, null);
                            graph.setFlow(i, j, null);
                        }
                    }

                }
            }
        }
        return graph;
    }

}
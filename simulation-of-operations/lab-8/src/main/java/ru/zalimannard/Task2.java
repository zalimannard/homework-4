package ru.zalimannard;

import java.util.ArrayList;
import java.util.Stack;

public abstract class Task2 {

    public static void execute() {
        Graph graph = new Graph();
        graph.addEdge("1", "2", 2);
        graph.addEdge("1", "3", 6);
        graph.addEdge("1", "4", 3);
        graph.addEdge("2", "4", 1);
        graph.addEdge("2", "5", 4);
        graph.addEdge("3", "4", 3);
        graph.addEdge("3", "6", 2);
        graph.addEdge("4", "2", 1);
        graph.addEdge("4", "3", 3);
        graph.addEdge("4", "5", 1);
        graph.addEdge("4", "6", 3);
        graph.addEdge("5", "6", 6);

        System.out.println("Изначальный граф:");
        System.out.println(graph);

        String input = Common.input(graph);
        System.out.println("Вход графа: " + input);
        System.out.println();

        String output = Common.output(graph);
        System.out.println("Выход графа: " + output);
        System.out.println();

        graph = randomFill(graph);
        System.out.println("Случайно зальём:");
        System.out.println(graph);
        System.out.println();

        int sumFlowAfterRandomFill = incomingSum(graph, output);
        System.out.println("Получившийся поток:");
        System.out.println(sumFlowAfterRandomFill);
        System.out.println();

        int newSumFlow = sumFlowAfterRandomFill;
        int oldSumFlow = newSumFlow;
        do {
            oldSumFlow = newSumFlow;
            graph = improve(graph);
            newSumFlow = incomingSum(graph, output);
            System.out.println("После итерации улучшения:");
            System.out.println(graph);
            System.out.println("Сумма:");
            System.out.println(newSumFlow);
            System.out.println();
        } while (oldSumFlow != newSumFlow);

        System.out.println("После всех улучшений получился ответ");
        System.out.println(newSumFlow);
        System.out.println("Напомню, что было");
        System.out.println(sumFlowAfterRandomFill);
    }

    private static Graph randomFill(Graph graph) {
        ArrayList<Edge> randomWay = findWay(graph);
        while (randomWay != null) {
            for (Edge edge : randomWay) {
                graph.setFlow(edge.getBegin(), edge.getEnd(),
                        graph.getEdge(edge.getBegin(), edge.getEnd()).getFlow() + getMinFreeBandwidth(randomWay));
            }
            randomWay = findWay(graph);
        }
        return graph;
    }

    private static ArrayList<Edge> findWay(Graph graph) {
        ArrayList<Edge> fromInput = graph.allOutgoing(Common.input(graph));
        ArrayList<ArrayList<Edge>> ways = new ArrayList<>();

        // Заполняем рёбрами выходящими из 0
        for (Edge edge : fromInput) {
            ArrayList<Edge> way = new ArrayList<>();
            if (!edge.getBandwidth().equals(edge.getFlow())) {
                way.add(edge);
                ways.add(way);
            }
        }

        // Максимум сколько понадобится итераций - количество вершин минус 1. Если до конца не дошли, то и не дойдём
        for (int i = 0; i < graph.allNodeNames().size(); ++i) {
            ArrayList<ArrayList<Edge>> nextWays = new ArrayList<>();

            // Обрабатываем каждое возможное развитие событий
            for (int j = 0; j < ways.size(); ++j) {
                ArrayList<Edge> outgoing = graph.allOutgoing(ways.get(j).get(ways.get(j).size() - 1).getEnd());

                // Удаляем те, по которым уже не получится двигаться из-за нагрузки
                for (int k = outgoing.size() - 1; k >= 0; --k) {
                    if (outgoing.get(k).getBandwidth().equals(outgoing.get(k).getFlow())) {
                        outgoing.remove(k);
                    }
                }

                // Добавляем оставшиеся рёбра как варианты
                for (int k = outgoing.size() - 1; k >= 0; --k) {
                    ArrayList<Edge> way = new ArrayList<>(ways.get(j));
                    way.add(outgoing.get(k));
                    nextWays.add(way);

                    // Хоть как-то дошли до конца
                    if (way.get(way.size() - 1).getEnd().equals(Common.output(graph))) {
                        return way;
                    }
                }
            }

            ways = new ArrayList<>(nextWays);
        }
        // Не смогли найти путь
        return null;
    }

    private static int getMinFreeBandwidth(ArrayList<Edge> way) {
        int min = way.get(0).getBandwidth() - way.get(0).getFlow();
        for (Edge edge : way) {
            if (edge.getBandwidth() - edge.getFlow() < min) {
                min = edge.getBandwidth() - edge.getFlow();
            }
        }
        return min;
    }

    private static int incomingSum(Graph graph, String nodeName) {
        int answer = 0;
        for (Edge edge : graph.allIncoming(nodeName)) {
            answer += edge.getFlow();
        }
        return answer;
    }

    private static Graph improve(Graph graph) {
        ArrayList<Edge> fromInput = graph.allOutgoing(Common.input(graph));
        ArrayList<ArrayList<Edge>> ways = new ArrayList<>();

        // Заполняем рёбрами выходящими из 0
        for (Edge edge : fromInput) {
            ArrayList<Edge> way = new ArrayList<>();
            if (!edge.getBandwidth().equals(edge.getFlow())) {
                way.add(edge);
                // 1 будет для обычных рёбер, -1 для обратных
                edge.setWeight(1);
                ways.add(way);
            }
        }

        // Максимум сколько понадобится итераций - количество вершин минус 1. Если до конца не дошли, то и не дойдём
        for (int i = 0; i < graph.allNodeNames().size(); ++i) {
            ArrayList<ArrayList<Edge>> nextWays = new ArrayList<>();

            // Обрабатываем каждое возможное развитие событий
            for (int j = 0; j < ways.size(); ++j) {
                ArrayList<Edge> outgoing = new ArrayList<>();
                ArrayList<Edge> incoming = new ArrayList<>();
                if (ways.get(j).get(ways.get(j).size() - 1).getWeight() == 1) {
                    outgoing = graph.allOutgoing(ways.get(j).get(ways.get(j).size() - 1).getEnd());
                    // Удаляем те, по которым уже не получится двигаться из-за нагрузки
                    for (int k = outgoing.size() - 1; k >= 0; --k) {
                        if (outgoing.get(k).getBandwidth().equals(outgoing.get(k).getFlow())) {
                            outgoing.remove(k);
                        }
                    }

                    incoming = graph.allIncoming(ways.get(j).get(ways.get(j).size() - 1).getEnd());
                    // Удаляем те, по которым уже не получится двигаться из-за пустоты
                    for (int k = incoming.size() - 1; k >= 0; --k) {
                        if (incoming.get(k).getFlow().equals(0)) {
                            incoming.remove(k);
                        }
                    }
                } else if (ways.get(j).get(ways.get(j).size() - 1).getWeight() == -1) {
                    outgoing = graph.allOutgoing(ways.get(j).get(ways.get(j).size() - 1).getBegin());
                    // Удаляем те, по которым уже не получится двигаться из-за нагрузки
                    for (int k = outgoing.size() - 1; k >= 0; --k) {
                        if (outgoing.get(k).getBandwidth().equals(outgoing.get(k).getFlow())) {
                            outgoing.remove(k);
                        }
                    }

                    incoming = graph.allIncoming(ways.get(j).get(ways.get(j).size() - 1).getEnd());
                    // Удаляем те, по которым уже не получится двигаться из-за пустоты
                    for (int k = incoming.size() - 1; k >= 0; --k) {
                        if (incoming.get(k).getFlow().equals(0)) {
                            incoming.remove(k);
                        }
                    }
                }

                for (Edge edge : outgoing) {
                    edge.setWeight(1);
                }
                for (Edge edge : incoming) {
                    edge.setWeight(-1);
                }

                outgoing.addAll(incoming);
                // Добавляем оставшиеся рёбра как варианты
                for (int k = outgoing.size() - 1; k >= 0; --k) {
                    ArrayList<Edge> way = new ArrayList<>(ways.get(j));
                    way.add(outgoing.get(k));
                    nextWays.add(way);

                    // Хоть как-то дошли до конца
                    if ((way.get(way.size() - 1).getEnd().equals(Common.output(graph)))
                            || (way.get(way.size() - 1).getBegin().equals(Common.output(graph)))) {
                        for (Edge edge : way) {
                            if (edge.getWeight() == 1) {
                                graph.setFlow(edge.getBegin(), edge.getEnd(), edge.getFlow() + 1);
                            }
                            if (edge.getWeight() == -1) {
                                graph.setFlow(edge.getBegin(), edge.getEnd(), edge.getFlow() - 1);
                            }
                        }
                        for (Edge edge : graph.getEdges()) {
                            edge.setWeight(0);
                        }
                        return graph;
                    }
                }
            }

            ways = new ArrayList<>(nextWays);
        }
        // Не смогли найти путь
        return graph;
    }

}

package ru.zalimannard;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MathMethods {
    public static ArrayList<String> getOptimalWay(Table targetTable) {
        ArrayList<Node> answerNodes = getOptimalWay(targetTable, 0L);
        ArrayList<String> answer = new ArrayList<>(Arrays.asList(targetTable.getDepartures().get(0)));
        while (answer.size() < targetTable.getDepartures().size()) {
            for (Node answerNode : answerNodes) {
                if (answerNode.departure().equals(answer.get(answer.size() - 1))) {
                    answer.add(answerNode.arrival());
                }
            }
        }
        return answer;
    }

    private static ArrayList<Node> getOptimalWay(Table targetTable, Long sumOfConstants) {

        Table table = new Table(targetTable);
        System.out.println("\nИзначальная таблица:\n" + table);

        Table tableFullReduced = reduction(table);
        Long previousSumOfConstant = sumOfConstants + tableFullReduced.get("_Минимум", "_Минимум");
        Table tableWithoutMinimum = new Table(tableFullReduced);
        tableWithoutMinimum.removeDeparture("_Минимум");
        tableWithoutMinimum.removeArrival("_Минимум");
        Table tableWithBranchingEdges = getBranchEdges(tableWithoutMinimum);
        System.out.println("\nТаблица с рёбрами ветвления\n" + tableWithBranchingEdges);

        ArrayList<Node> maxElementNodes = new ArrayList<>();
        Long maxElement = tableWithBranchingEdges.getMaxValue();
        for (String departure : targetTable.getDepartures()) {
            for (String arrival : targetTable.getArrivals()) {
                Long currentValue = tableWithBranchingEdges.get(departure, arrival);
                if (currentValue == null) {
                    continue;
                } else if (currentValue.equals(maxElement)) {
                    maxElementNodes.add(new Node(departure, arrival));
                }
            }
        }

        for (Node maxElementNode : maxElementNodes) {
            System.out.println("Изначальная таблица:\n" + tableWithoutMinimum);

            Table tableFullReducedWhenExcludeOrInclude = reduction(tableWithoutMinimum);
            tableFullReducedWhenExcludeOrInclude.removeDeparture("_Минимум");
            tableFullReducedWhenExcludeOrInclude.removeArrival("_Минимум");

            Table tableWhenExclude = new Table(tableFullReducedWhenExcludeOrInclude);

            tableWhenExclude.set(maxElementNode.departure(), maxElementNode.arrival(), null);
            System.out.println("Таблица если исключаем ребро:\n" + tableWhenExclude);
            Long sumOfConstantsWhenExclude = calcEdge(tableWhenExclude);
            System.out.println("Сумма констант если исключаем ребро: " + sumOfConstantsWhenExclude);

            Table tableWhenInclude = new Table(tableFullReducedWhenExcludeOrInclude);

            tableWhenInclude.set(maxElementNode.arrival(), maxElementNode.departure(), null);
            tableWhenInclude.removeDeparture(maxElementNode.departure());
            tableWhenInclude.removeArrival(maxElementNode.arrival());
            if (tableWhenInclude.getDepartures().size() == 1) {
                return new ArrayList<>(Arrays.asList(
                        new Node(tableWhenInclude.getDepartures().get(0), tableWhenInclude.getArrivals().get(0)),
                        new Node(maxElementNode.departure(), maxElementNode.arrival())));
            }
            System.out.println("Таблица если включаем ребро:\n" + tableWhenInclude);
            Long sumOfConstantsWhenInclude = calcEdge(tableWhenInclude);
            System.out.println("Сумма констант если включаем ребро: " + sumOfConstantsWhenInclude);

            if (sumOfConstantsWhenInclude <= sumOfConstantsWhenExclude) {
                ArrayList<Node> answer = getOptimalWay(tableWhenInclude, previousSumOfConstant + sumOfConstantsWhenInclude);
                if (answer == null) {
                    tableWithoutMinimum.set(maxElementNode.departure(), maxElementNode.arrival(), null);
                } else {
                    answer.add(maxElementNode);
                    return answer;
                }
            }
        }

        return null;
    }

    private static Table reduction(Table targetTable) {
        Table table = new Table(targetTable);

        Table tableRowReduced = reduceRows(table);
        System.out.println("\nПосле приведения строк:\n" + tableRowReduced);

        Table tableColumnReduced = reduceColumns(tableRowReduced);
        System.out.println("\nПосле приведения столбцов:\n" + tableColumnReduced);

        Table tableFullReduced = setSumOfMinimumsToRightBottomCorner(tableColumnReduced);
        System.out.println("\nТаблица после приведений:\n" + tableFullReduced);

        return tableFullReduced;
    }

    private static Long calcEdge(Table targetTable) {
        Table table = new Table(targetTable);
        Table tableRowReduced = reduceRows(table);
        Table tableColumnReduced = reduceColumns(tableRowReduced);
        Table tableFullReduced = setSumOfMinimumsToRightBottomCorner(tableColumnReduced);
        return tableFullReduced.get("_Минимум", "_Минимум");
    }

    private static Table reduceRows(Table targetTable) {
        Table table = new Table(targetTable);
        for (String departure : table.getDepartures()) {
            if (departure.startsWith("_")) {
                continue;
            }
            Long min = table.getMinInDeparture(departure);
            table.set(departure, "_Минимум", min);
            for (String arrival : table.getArrivals()) {
                if (arrival.startsWith("_")) {
                    continue;
                }
                table.dec(departure, arrival, min);
            }
        }
        return table;
    }

    private static Table reduceColumns(Table targetTable) {
        Table table = new Table(targetTable);
        for (String arrival : table.getArrivals()) {
            if (arrival.startsWith("_")) {
                continue;
            }
            Long min = table.getMinInArrival(arrival);
            table.set("_Минимум", arrival, min);
            for (String departure : table.getDepartures()) {
                if (departure.startsWith("_")) {
                    continue;
                }
                table.dec(departure, arrival, min);
            }
        }
        return table;
    }

    private static Table setSumOfMinimumsToRightBottomCorner(Table targetTable) {
        Table table = new Table(targetTable);
        Long sumOfMinimums = 0L;
        for (String departure : table.getDepartures()) {
            if (departure.startsWith("_")) {
                continue;
            }
            Long value = table.get(departure, "_Минимум");
            if (value != null) {
                sumOfMinimums += value;
            }
        }
        for (String arrival : table.getArrivals()) {
            if (arrival.startsWith("_")) {
                continue;
            }
            Long value = table.get("_Минимум", arrival);
            if (value != null) {
                sumOfMinimums += value;
            }
        }
        table.set("_Минимум", "_Минимум", sumOfMinimums);
        return table;
    }

    private static Table getBranchEdges(Table targetTable) {
        Table table = new Table(targetTable);
        table.removeAllElements();

        for (String departure : targetTable.getDepartures()) {
            for (String arrival : targetTable.getArrivals()) {
                Long value = targetTable.get(departure, arrival);
                if (value == null) {
                    continue;
                } else if (value.equals(0L)) {
                    Table tmpTable = new Table(targetTable);
                    tmpTable.set(departure, arrival, null);
                    table.set(departure, arrival,
                            tmpTable.getMinInDeparture(departure) + tmpTable.getMinInArrival(arrival));
                }
            }
        }

        return table;
    }
}

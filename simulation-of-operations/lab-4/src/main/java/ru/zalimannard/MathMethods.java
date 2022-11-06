package ru.zalimannard;

import java.util.ArrayList;

public abstract class MathMethods {
    public static ArrayList<String> getOptimalWay(Table targetTable) {
        Table table = new Table(targetTable);
        System.out.println("\nИзначальная таблица:\n" + table);

        Table tableRowReduced = reduceRows(table);
        System.out.println("\nПосле приведения строк:\n" + tableRowReduced);
        Table tableColumnReduced = reduceColumns(tableRowReduced);
        System.out.println("\nПосле приведения столбцов:\n" + tableColumnReduced);
        Table tableFullReduced = setSumOfMinimumsToRightBottomCorner(tableColumnReduced);
        System.out.println("\nТаблица после приведений:\n" + tableFullReduced);
        Table tableWithoutMinimum = new Table(tableFullReduced);
        tableWithoutMinimum.removeDeparture("_Минимум");
        tableWithoutMinimum.removeArrival("_Минимум");
        System.out.println("\nУДАЛЕНИЕ:\n" + tableWithoutMinimum);

        return null;
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
}

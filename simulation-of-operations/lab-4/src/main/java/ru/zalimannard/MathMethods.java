package ru.zalimannard;

import java.util.ArrayList;

public abstract class MathMethods {
    public static ArrayList<String> getOptimalWay(Table targetTable) {
        Table table = new Table(targetTable);
        System.out.println("\nИзначальная таблица:\n" + table);

        table.addColumn("min i");
        table.addRow("min j");
        Table tableRowReduced = reduceRows(table);
        System.out.println("\nПосле приведения строк:\n" + tableRowReduced);
        Table tableColumnReduced = reduceColumns(tableRowReduced);
        System.out.println("\nПосле приведения столбцов:\n" + tableColumnReduced);
        Table tableFullReduced = setSumOfMinimumsToRightBottomCorner(tableColumnReduced);
        System.out.println("\nТаблица после приведений:\n" + tableFullReduced);

        return null;
    }

    private static Table reduceRows(Table targetTable) {
        Table table = new Table(targetTable);
        for (int y = 1; y < table.getHeight(); ++y) {
            Long min = table.getMinInRow(y);
            table.set(table.getWidth(), y, min);

            for (int x = 1; x < table.getWidth(); ++x) {
                table.dec(x, y, min);
            }
        }
        return table;
    }

    private static Table reduceColumns(Table targetTable) {
        Table table = new Table(targetTable);
        for (int x = 1; x < table.getWidth(); ++x) {
            Long min = table.getMinInColumn(x);
            table.set(x, table.getHeight(), min);

            for (int y = 1; y < table.getWidth(); ++y) {
                table.dec(x, y, min);
            }
        }
        return table;
    }

    private static Table setSumOfMinimumsToRightBottomCorner(Table targetTable) {
        Table table = new Table(targetTable);
        Long sumOfMinimums = 0L;
        for (int y = 1; y < table.getHeight(); ++y) {
            if (table.get(table.getWidth(), y) == null) {
                continue;
            }
            sumOfMinimums += table.get(table.getWidth(), y);
        }
        for (int x = 1; x < table.getWidth(); ++x) {
            if (table.get(x, table.getHeight()) == null) {
                continue;
            }
            sumOfMinimums += table.get(x, table.getHeight());
        }
        table.set(table.getWidth(), table.getHeight(), sumOfMinimums);
        return table;
    }
}

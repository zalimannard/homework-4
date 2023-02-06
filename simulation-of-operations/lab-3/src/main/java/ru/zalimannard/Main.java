package ru.zalimannard;

import ru.zalimannard.mathelement.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        InequalitySystem inequalitySystem = new InequalitySystem();
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(-1.0, +4.0)), Operator.GREATER_OR_EQUAL, 6));
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(+0.0, -3.0)), Operator.LESS_OR_EQUAL, 4));
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(+2.0, +1.0)), Operator.LESS_OR_EQUAL, 8));
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(+3.0, -3.0)), Operator.GREATER_OR_EQUAL, 0));
        System.out.println("Изначальная система неравенств:");
        System.out.println(inequalitySystem);

        EquationSystem equationSystem = inequalitySystem.toEquationSystem();
        System.out.println("Она же, но в уравнениях:");
        System.out.println(equationSystem);

        // -1 - найти минимум, +1 - найти максимум
        Equation targetFunction = new Equation(new ArrayList<>(Arrays.asList(+1.0, +3.0)), +1);
        GomoryTable gomoryTable = new GomoryTable(equationSystem, targetFunction);
        System.out.println("Перевели в симплекс-таблицу:");
        System.out.println(gomoryTable);

        // Добиваемся хоть какого-нибудь оптимального решения
        getOptimal(gomoryTable);

        // Приводим решение к целочисленному
        while (!isIntegerSolution(gomoryTable, targetFunction.getNumberOfVariables())) {
            System.out.println("Нашли оптимальное нецелочисленное решение.");

            String maxFractionalPartRow = gomoryTable.maxFractionalPartRow(targetFunction.getNumberOfVariables());
            double maxFractionalPart = gomoryTable.fractionalPart(gomoryTable.get("b", maxFractionalPartRow));
            System.out.println("Максимальная дробная часть у: " + maxFractionalPartRow);
            System.out.println("И она равна: " + maxFractionalPart);
            System.out.println();
            System.out.println("Для " + maxFractionalPartRow + " введём дополнительное ограничение");

            gomoryTable.createNewRestriction(maxFractionalPartRow);
            System.out.println("Теперь таблица имеет вид:");
            System.out.println(gomoryTable);
            System.out.println();

            System.out.println("Сделаем решение допустимым:");
            getOptimal(gomoryTable);
            System.out.println(gomoryTable);
            System.out.println();
        }

        System.out.println("Нашли оптимальное целочисленное решение.");
        System.out.println();
        System.out.println("Значения переменных:");
        for (int i = 1; i <= targetFunction.getNumberOfVariables(); ++i) {
            System.out.print("x" + i + " = ");
            if (gomoryTable.exist("b", "x" + i)) {
                System.out.println(Utils.compressDouble(gomoryTable.get("b", "x" + i)));
            } else {
                System.out.println(0);
            }
        }
        System.out.print("Итоговый ответ: ");
        if (targetFunction.getResult() == 1) {
            System.out.println(Utils.compressDouble(gomoryTable.get("b", "F")));
        } else {
            System.out.println(Utils.compressDouble(-gomoryTable.get("b", "F")));
        }
    }

    private static void getOptimal(GomoryTable gomoryTable) {

        String minXRowForBColumn = gomoryTable.minB();
        int iteration = 0;
        while (gomoryTable.get("b", minXRowForBColumn) < 0.0) {
            System.out.println("Минимальный элемент среди свободных членов: " + gomoryTable.get("b", minXRowForBColumn));
            System.out.println("Среди свободных членов есть отрицательные. Нужно перейти к допустимому решению");

            String minXColumnForMinBRow = gomoryTable.minInRow(minXRowForBColumn);
            if (gomoryTable.get(minXColumnForMinBRow, minXRowForBColumn) >= 0.0) {
                System.out.println("Задачу решить нельзя");
                System.exit(0);
            }
            System.out.println("Ведущая строка: " + minXRowForBColumn);
            System.out.println("Ведущий столбец: " + minXColumnForMinBRow);
            System.out.println();

            ++iteration;
            System.out.println("Пересчитываем таблицу. Итерация " + iteration);
            gomoryTable.recalc(minXColumnForMinBRow, minXRowForBColumn);
            System.out.println(gomoryTable);

            // Обновляем минимум в b
            minXRowForBColumn = gomoryTable.minB();
        }

        // Удостоверимся, что нет -0.0
        gomoryTable.checkZero();

        // У целевой функции не должно быть отрицательных элементов
        iteration = 0;
        System.out.println(gomoryTable.get(gomoryTable.minInRow("F"), "F"));
        while (gomoryTable.get(gomoryTable.minInRow("F"), "F") < 0.0) {
            System.out.println("У целевой функции есть отрицательные элементы. Избавимся. Итерация " + iteration);

            // Разрешающий столбец
            String columnWithMinF = gomoryTable.minInRow("F");
            String rowPair = null;
            Double minDiv = null;

            // Вычисляем разрешающую строку
            for (String rowName : gomoryTable.getRowNames()) {
                if (gomoryTable.get(columnWithMinF, rowName) > 0.0) {
                    if (rowPair == null) {
                        rowPair = rowName;
                        minDiv = gomoryTable.get("b", rowName) / gomoryTable.get(columnWithMinF, rowName);
                    } else {
                        if (gomoryTable.get("b", rowName) / gomoryTable.get(columnWithMinF, rowName) < minDiv) {
                            rowPair = rowName;
                            minDiv = gomoryTable.get("b", rowName) / gomoryTable.get(columnWithMinF, rowName);
                        }
                    }
                }
            }
            if (rowPair == null) {
                System.out.println("Посчитать не получится");
                System.exit(0);
            }

            System.out.println("Разрешающий столбец: " + columnWithMinF);
            System.out.println("Разрешающая строка: " + rowPair);

            gomoryTable.recalc(columnWithMinF, rowPair);
        }
    }

    private static boolean isIntegerSolution(GomoryTable gomoryTable, int numberOfVariables) {
        for (int i = 1; i <= numberOfVariables; ++i) {
            if (gomoryTable.exist("b", "x" + i)) {
                double value = gomoryTable.get("b", "x" + i);
                int valueInteger = (int) value;
                if (Math.abs(value - valueInteger) >= 0.0001) {
                    return false;
                }
            }
        }
        return true;
    }
}
package ru.zalimannard;

import ru.zalimannard.mathelement.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        InequalitySystem inequalitySystem = new InequalitySystem();
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(-1.0, +5.0)), Operator.GREATER_OR_EQUAL, 6));
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(+0.0, -3.0)), Operator.LESS_OR_EQUAL, 4));
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(+2.0, +1.0)), Operator.LESS_OR_EQUAL, 8));
        inequalitySystem.add(new Inequality(new ArrayList<>(Arrays.asList(+3.0, -3.0)), Operator.GREATER_OR_EQUAL, 0));
        System.out.println("Изначальная система неравенств:");
        System.out.println(inequalitySystem);

        EquationSystem equationSystem = inequalitySystem.toEquationSystem();
        System.out.println("Она же, но в уравнениях:");
        System.out.println(equationSystem);

        Equation targetFunction = new Equation(new ArrayList<>(Arrays.asList(+1.0, +3.0)), -1);
        GomoryTable gomoryTable = new GomoryTable(equationSystem, targetFunction);
        System.out.println("Перевели в симплекс-таблицу:");
        System.out.println(gomoryTable);

        String minXRowForBColumn = gomoryTable.minB();
        int iteration = 0;
        while (gomoryTable.get("b", minXRowForBColumn) < 0.0) {
            System.out.println("Минимальный элемент среди свободных членов: " + gomoryTable.get("b", minXRowForBColumn));
            System.out.println("Среди свободных членов есть отрицательные. Нужно перейти к допустимому решению");

            String minXColumnForMinBRow = gomoryTable.minInRow(minXRowForBColumn);
            if (gomoryTable.get(minXColumnForMinBRow, minXRowForBColumn) >= 0.0) {
                System.out.println("Задачу решить нельзя");
                return;
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

        while (!isIntegerSolution(gomoryTable, targetFunction.getNumberOfVariables())) {
            System.out.println("Нашли оптимальное нецелочисленное решение.");

            String maxFractionalPartRow = gomoryTable.maxFractionalPartRow(targetFunction.getNumberOfVariables());
            double maxFractionalPart = gomoryTable.fractionalPart(gomoryTable.get("b", maxFractionalPartRow));
            System.out.println("Максимальная дробная часть у: " + maxFractionalPartRow);
            System.out.println("И она равна: " + maxFractionalPart);
            System.out.println();
            System.out.println("Для " + maxFractionalPartRow + " введём дополнительное ограничение");
            break;
        }

        System.out.println("Нашли оптимальное целочисленное решение.");
        System.out.println();
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
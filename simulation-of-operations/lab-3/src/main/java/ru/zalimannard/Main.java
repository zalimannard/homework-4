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
        while (gomoryTable.get("b", minXRowForBColumn) < 0) {
            System.out.println("Среди свободных членов есть отрицательные. Нужно перейти к допустимому решению");
            break;
        }
    }
}
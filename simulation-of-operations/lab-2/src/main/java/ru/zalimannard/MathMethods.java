package ru.zalimannard;

import java.util.ArrayList;

public class MathMethods {
    public long calcSumThroughPotentialAndNorthwestCornerMethods(Table table) {
        Table northwestCornerTable = calcBasicPlanUsingNorthwestCorner(table);
        System.out.println("\nТаблица после метода северозападного угла:\n" + northwestCornerTable + "\n\n\n\n\n");

        ArrayList<Table> solutions = new ArrayList<>();
        solutions.add(new Table(northwestCornerTable));
        for (int i = 0; i < solutions.size(); ++i) {
            if (isOptimalTransportationTable(solutions.get(i), table)) {
                System.out.println("\nЛучшее решение:\n" + solutions.get(i));
                return calcPrice(solutions.get(i), table);
            }

            ArrayList<Table> preparedTables = prepareForThePotentialMethod(solutions.get(i));
            for (int j = 0; j < preparedTables.size(); ++j) {
                Table improvedTable = improveThePlan(solutions.get(i), table, preparedTables.get(j));
                if (!solutions.contains(improvedTable)) {
                    solutions.add(improvedTable);
                }
            }
        }

        System.out.println("Не получается найти решения");

        return 0;
    }

    private Table calcBasicPlanUsingNorthwestCorner(Table targetTable) {
        Table table = new Table(targetTable);
        Table answer = new Table(targetTable);
        answer.fill(1, 1, answer.getWidth(), answer.getHeight(), "0");

        Long product = 0L;
        Long request = 0L;
        for (int i = 1; i < table.getHeight() - 1; ++i) {
            product += Long.parseLong(table.get(table.getWidth() - 1, i));
        }
        System.out.println("\nОбщее количество товара: " + product);
        for (int i = 1; i < table.getWidth() - 1; ++i) {
            request += Long.parseLong(table.get(i, table.getHeight() - 1));
        }
        System.out.println("\nОбщее требуемое количество: " + request);

        if (!product.equals(request)) {
            throw new IllegalArgumentException("Несбалансированная таблица");
        }

        int x = 1;
        int y = 1;
        while (request != 0) {
            Long transported = Math.min(
                    Long.parseLong(table.get(x, table.getHeight() - 1)),
                    Long.parseLong(table.get(table.getWidth() - 1, y))
            );
            request -= transported;

            answer.set(x, y, String.valueOf(transported));
            answer.set(x, answer.getHeight() - 1,
                    String.valueOf(transported + Long.parseLong(answer.get(x, answer.getHeight() - 1))));
            answer.set(answer.getWidth() - 1, y,
                    String.valueOf(transported + Long.parseLong(answer.get(answer.getWidth() - 1, y))));
            answer.set(answer.getWidth() - 1, answer.getHeight() - 1, String.valueOf(
                    transported + Long.parseLong(answer.get(answer.getWidth() - 1, answer.getHeight() - 1))));

            table.set(x, table.getHeight() - 1,
                    String.valueOf(Long.parseLong(table.get(x, table.getHeight() - 1)) - transported));
            table.set(table.getWidth() - 1, y,
                    String.valueOf(Long.parseLong(table.get(table.getWidth() - 1, y)) - transported));

            if (table.get(x, table.getHeight() - 1).equals("0")) {
                ++x;
            }
            if (table.get(table.getWidth() - 1, y).equals("0")) {
                ++y;
            }

            System.out.println("\nОчередная таблица в методе северозападного угла:\n" + answer);
        }

        return answer;
    }

    private ArrayList<Table> prepareForThePotentialMethod(Table targetTable) {
        Table table = new Table(targetTable);
        ArrayList<Table> answer = new ArrayList<>();

        // Нулей быть не должно. Они будут считаться как базисные элементы
        for (int x = 1; x < table.getWidth(); ++x) {
            for (int y = 1; y < table.getHeight(); ++y) {
                if (table.get(x, y).equals("0")) {
                    table.set(x, y, "X");
                }
            }
        }
        table.fill(1, table.getHeight() - 1,
                table.getWidth(), table.getHeight(), "X");
        table.fill(table.getWidth() - 1, 1,
                table.getWidth(), table.getHeight(), "X");

        if (isTableReadyForThePotentialMethod(table)) {
            answer.add(table);
            return answer;
        }

        // Добавление базисных элементов
        int prevStartY = 1;
        int currentNumber = 0;
        Table tableToAdd = new Table(table);
        for (int x1 = 1; x1 < tableToAdd.getWidth() - 1; ++x1) {
            for (int y1 = 1; y1 < tableToAdd.getHeight() - 1; ++y1) {
                if (tableToAdd.get(x1, y1).equals("X")) {

                    boolean isWithoutCycle = true;
                    for (int x2 = 1; x2 < tableToAdd.getWidth() - 1; ++x2) {
                        for (int y2 = 1; y2 < tableToAdd.getHeight() - 1; ++y2) {
                            if ((!tableToAdd.get(x1, y2).equals("X"))
                                    && (!tableToAdd.get(x2, y1).equals("X"))
                                    && (!tableToAdd.get(x2, y2).equals("X"))) {
                                isWithoutCycle = false;
                            }
                        }
                    }
                    if (isWithoutCycle) {
                        tableToAdd.set(x1, y1, "0");
                        if (currentNumber == 0) {
                            prevStartY = y1;
                            ++currentNumber;
                        }
                    }
                    if (isTableReadyForThePotentialMethod(tableToAdd)) {
                        answer.add(new Table(tableToAdd));
                        tableToAdd = new Table(table);
                        y1 = prevStartY;
                        prevStartY = 1;
                        currentNumber = 0;
                    }
                }
            }
        }

        return answer;
    }

    private boolean isTableReadyForThePotentialMethod(Table table) {
        int requiredNumberOfNumbersInTheBasis = table.getHeight() + table.getWidth() - 5;
        int currentNumberOfNumbersInTheBasis = 0;

        for (int x = 1; x < table.getWidth() - 1; ++x) {
            for (int y = 1; y < table.getHeight() - 1; ++y) {
                if (!table.get(x, y).equals("X")) {
                    ++currentNumberOfNumbersInTheBasis;
                }
            }
        }

        return currentNumberOfNumbersInTheBasis >= requiredNumberOfNumbersInTheBasis;
    }

    private Table calcPotentials(Table transportationTable, Table priceTable) {
        Table table = new Table(transportationTable);
        table.fill(1, 1, table.getWidth(), table.getHeight(), "X");
        table.set(table.getWidth() - 1, 1, "0");

        // Заранее не знаем количество нужных проверок, берём наверняка
        for (int repeats = 1; repeats < table.getHeight() - 1; ++repeats) {
            // Для каждого известного потенциала справа находим доступные потенциалы снизу
            for (int y = 1; y < table.getHeight() - 1; ++y) {
                if (!table.get(table.getWidth() - 1, y).equals("X")) {

                    for (int x = 1; x < table.getWidth() - 1; ++x) {
                        if (!transportationTable.get(x, y).equals("X")) {
                            table.set(x, table.getHeight() - 1,
                                    String.valueOf(Long.parseLong(priceTable.get(x, y))
                                            - Long.parseLong(table.get(table.getWidth() - 1, y))
                                    ));

                            // Для найденного потенциала снизу находим все доступные потенциалы снизу
                            for (int y2 = 1; y2 < table.getHeight() - 1; ++y2) {
                                if (!transportationTable.get(x, y2).equals("X")) {

                                    table.set(table.getWidth() - 1, y2,
                                            String.valueOf(Long.parseLong(priceTable.get(x, y2))
                                                    - Long.parseLong(table.get(x, table.getHeight() - 1))));
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("\nДля таблицы:\n" + transportationTable);
        System.out.println("\nПолучились потенциалы:\n" + table + "\n\n\n\n\n");
        return table;
    }

    private Table calcDeltas(Table potentials, Table prices, Table basis) {
        Table table = new Table(potentials);
        table.fill(1, 1, table.getWidth(), table.getHeight(), "X");

        for (int x = 1; x < table.getWidth() - 1; ++x) {
            for (int y = 1; y < table.getHeight() - 1; ++y) {
                if (basis.get(x, y).equals("X")) {
                    table.set(x, y, String.valueOf(
                            Long.parseLong(potentials.get(x, potentials.getHeight() - 1))
                                    + Long.parseLong(potentials.get(potentials.getWidth() - 1, y))
                                    - Long.parseLong(prices.get(x, y))
                    ));
                }
            }
        }

        System.out.println("\nДля базиса:\n" + basis);
        System.out.println("\nИ для потенциалов:\n" + potentials);
        System.out.println("\nПолучились дельты:\n" + table + "\n\n\n\n\n");
        return table;
    }

    private boolean isOptimalTransportationTable(Table targetTable, Table priceTable) {
        ArrayList<Table> preparedTables = prepareForThePotentialMethod(targetTable);

        int errorCounter = 0;
        for (Table preparedTable : preparedTables) {
            try {
                Table potentialTable = calcPotentials(preparedTable, priceTable);
                Table deltaTable = calcDeltas(potentialTable, priceTable, preparedTable);

                boolean isOptimal = true;
                for (int x = 1; x < deltaTable.getWidth() - 1; ++x) {
                    for (int y = 1; y < deltaTable.getHeight() - 1; ++y) {
                        if (!deltaTable.get(x, y).equals("X")) {
                            if (Long.parseLong(deltaTable.get(x, y)) > 0) {
                                isOptimal = false;
                            }
                        }
                    }
                }
                if (isOptimal) {
                    return true;
                }
            } catch (Exception e) {
                // Если в одном из вариантов пришли к тупику, то это ещё ничего не значит. Но если это произошло со
                // всеми таблицами, то оптимального решения, видимо, нет.
            }
        }

        return false;
    }

    private Table improveThePlan(Table plan, Table priceTable, Table potentialPreparedPlan) {
        if (isOptimalTransportationTable(plan, priceTable)) {
            return new Table(plan);
        }

        try {
            Table improvedPlan = new Table(plan);
            Table preparedTable = new Table(potentialPreparedPlan);
            Table potentialTable = calcPotentials(preparedTable, priceTable);
            Table deltaTable = calcDeltas(potentialTable, priceTable, preparedTable);

            int startCycleX = 1;
            int startCycleY = 1;
            Long maxValue = 0L;

            // Находим максимальное положительное
            for (int x = 1; x < deltaTable.getWidth() - 1; ++x) {
                for (int y = 1; y < deltaTable.getHeight() - 1; ++y) {
                    if (!deltaTable.get(x, y).equals("X")) {
                        if (Long.parseLong(deltaTable.get(x, y)) > maxValue) {
                            startCycleX = x;
                            startCycleY = y;
                            maxValue = Long.parseLong(deltaTable.get(x, y));
                        }
                    }
                }
            }


            // ЗАХАРДКОЖЕНЫЕ НЕКОТОРЫЕ ЦИКЛЫ ДЛЯ ТАБЛИЦ 3X3
            if (((startCycleX == 1) && (startCycleY == 1))
                    || ((startCycleX == 1) && (startCycleY == 2))
                    || ((startCycleX == 2) && (startCycleY == 1))
                    || ((startCycleX == 2) && (startCycleY == 3))
                    || ((startCycleX == 3) && (startCycleY == 2))
                    || ((startCycleX == 3) && (startCycleY == 3))) {
                int zeroCounter = 0;
                if (improvedPlan.get(1, 1).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(1, 2).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(2, 1).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(2, 3).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(3, 2).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(3, 3).equals("0")) {
                    ++zeroCounter;
                }
                if (zeroCounter == 1) {
                    if (((startCycleX == 1) && (startCycleY == 1))
                            || ((startCycleX == 2) && (startCycleY == 3))
                            || ((startCycleX == 3) && (startCycleY == 2))) {
                        Long min = Math.min(Long.parseLong(improvedPlan.get(2, 1)), Long.parseLong(improvedPlan.get(1, 2)));
                        min = Math.min(min, Long.parseLong(improvedPlan.get(3, 3)));
                        improvedPlan.set(1, 1, String.valueOf(Long.parseLong(improvedPlan.get(1, 1)) + min));
                        improvedPlan.set(2, 3, String.valueOf(Long.parseLong(improvedPlan.get(2, 3)) + min));
                        improvedPlan.set(3, 2, String.valueOf(Long.parseLong(improvedPlan.get(3, 2)) + min));
                        improvedPlan.set(1, 2, String.valueOf(Long.parseLong(improvedPlan.get(1, 2)) - min));
                        improvedPlan.set(2, 1, String.valueOf(Long.parseLong(improvedPlan.get(2, 1)) - min));
                        improvedPlan.set(3, 3, String.valueOf(Long.parseLong(improvedPlan.get(3, 3)) - min));
                    } else {
                        Long min = Math.min(Long.parseLong(improvedPlan.get(1, 1)), Long.parseLong(improvedPlan.get(3, 2)));
                        min = Math.min(min, Long.parseLong(improvedPlan.get(2, 3)));
                        improvedPlan.set(1, 1, String.valueOf(Long.parseLong(improvedPlan.get(1, 1)) - min));
                        improvedPlan.set(2, 3, String.valueOf(Long.parseLong(improvedPlan.get(2, 3)) - min));
                        improvedPlan.set(3, 2, String.valueOf(Long.parseLong(improvedPlan.get(3, 2)) - min));
                        improvedPlan.set(1, 2, String.valueOf(Long.parseLong(improvedPlan.get(1, 2)) + min));
                        improvedPlan.set(2, 1, String.valueOf(Long.parseLong(improvedPlan.get(2, 1)) + min));
                        improvedPlan.set(3, 3, String.valueOf(Long.parseLong(improvedPlan.get(3, 3)) + min));
                    }
                }
            }
            // ЗАХАРДКОЖЕНЫЕ НЕКОТОРЫЕ ЦИКЛЫ ДЛЯ ТАБЛИЦ 3X3
            if (((startCycleX == 3) && (startCycleY == 1))
                    || ((startCycleX == 1) && (startCycleY == 2))
                    || ((startCycleX == 2) && (startCycleY == 1))
                    || ((startCycleX == 2) && (startCycleY == 3))
                    || ((startCycleX == 3) && (startCycleY == 2))
                    || ((startCycleX == 1) && (startCycleY == 3))) {
                int zeroCounter = 0;
                if (improvedPlan.get(3, 1).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(1, 2).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(2, 1).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(2, 3).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(3, 2).equals("0")) {
                    ++zeroCounter;
                }
                if (improvedPlan.get(1, 3).equals("0")) {
                    ++zeroCounter;
                }
                if (zeroCounter == 1) {
                    if (((startCycleX == 3) && (startCycleY == 1))
                            || ((startCycleX == 2) && (startCycleY == 3))
                            || ((startCycleX == 1) && (startCycleY == 2))) {
                        Long min = Math.min(Long.parseLong(improvedPlan.get(1, 3)), Long.parseLong(improvedPlan.get(2, 1)));
                        min = Math.min(min, Long.parseLong(improvedPlan.get(3, 2)));
                        improvedPlan.set(3, 1, String.valueOf(Long.parseLong(improvedPlan.get(3, 1)) + min));
                        improvedPlan.set(2, 3, String.valueOf(Long.parseLong(improvedPlan.get(2, 3)) + min));
                        improvedPlan.set(1, 2, String.valueOf(Long.parseLong(improvedPlan.get(1, 2)) + min));
                        improvedPlan.set(1, 3, String.valueOf(Long.parseLong(improvedPlan.get(1, 3)) - min));
                        improvedPlan.set(2, 1, String.valueOf(Long.parseLong(improvedPlan.get(2, 1)) - min));
                        improvedPlan.set(3, 2, String.valueOf(Long.parseLong(improvedPlan.get(3, 2)) - min));
                    } else {
                        Long min = Math.min(Long.parseLong(improvedPlan.get(3, 1)), Long.parseLong(improvedPlan.get(2, 3)));
                        min = Math.min(min, Long.parseLong(improvedPlan.get(1, 2)));
                        improvedPlan.set(3, 1, String.valueOf(Long.parseLong(improvedPlan.get(3, 1)) - min));
                        improvedPlan.set(2, 3, String.valueOf(Long.parseLong(improvedPlan.get(2, 3)) - min));
                        improvedPlan.set(1, 2, String.valueOf(Long.parseLong(improvedPlan.get(1, 2)) - min));
                        improvedPlan.set(1, 3, String.valueOf(Long.parseLong(improvedPlan.get(1, 3)) + min));
                        improvedPlan.set(2, 1, String.valueOf(Long.parseLong(improvedPlan.get(2, 1)) + min));
                        improvedPlan.set(3, 2, String.valueOf(Long.parseLong(improvedPlan.get(3, 2)) + min));
                    }
                }
            }
            // КОНЕЦ ЗАХАРДКОЖЕНЫХ КРЕСТОВЫХ ЦИКЛОВ

            int finishCycleX = 0;
            int finishCycleY = 0;
            // ТОЛЬКО ПРЯМОУГОЛЬНЫЙ ЦИКЛ НАЧАЛО. НАХОДИТСЯ ТОЛЬКО ОДИН И, ВЕРОЯТНО, КОРЯВО.
            // Находим цикл
            for (int x = 1; x < improvedPlan.getWidth() - 1; ++x) {
                for (int y = 1; y < improvedPlan.getHeight() - 1; ++y) {
                    if ((x != startCycleX) && (y != startCycleY)
                            && (!improvedPlan.get(startCycleX, y).equals("0"))
                            && (!improvedPlan.get(x, startCycleY).equals("0"))) {
                        finishCycleX = x;
                        finishCycleY = y;
                    }
                }
            }
            Long minimumNegative = Math.min(
                    Long.parseLong(improvedPlan.get(startCycleX, finishCycleY)),
                    Long.parseLong(improvedPlan.get(finishCycleX, startCycleY))
            );

            // Меняем значения в цикле
            improvedPlan.set(startCycleX, finishCycleY,
                    String.valueOf(Long.parseLong(improvedPlan.get(startCycleX, finishCycleY)) - minimumNegative));
            improvedPlan.set(finishCycleX, startCycleY,
                    String.valueOf(Long.parseLong(improvedPlan.get(finishCycleX, startCycleY)) - minimumNegative));

            if (improvedPlan.get(startCycleX, startCycleY).equals("X")) {
                improvedPlan.set(startCycleX, startCycleY, "0");
            }
            improvedPlan.set(startCycleX, startCycleY,
                    String.valueOf(Long.parseLong(improvedPlan.get(startCycleX, startCycleY)) + minimumNegative));
            improvedPlan.set(finishCycleX, finishCycleY,
                    String.valueOf(Long.parseLong(improvedPlan.get(finishCycleX, finishCycleY)) + minimumNegative));
            // ТОЛЬКО ПРЯМОУГОЛЬНЫЙ ЦИКЛ КОНЕЦ
            return improvedPlan;
        } catch (Exception e) {
            // Если в процессе оптимизации совершилась ошибка, то, наверное, его нельзя нормально улучшить
            return new Table(plan);
        }
    }

    private Long calcPrice(Table transportationTable, Table priceTable) {
        Long answer = 0L;

        for (int x = 1; x < transportationTable.getWidth() - 1; ++x) {
            for (int y = 1; y < transportationTable.getHeight() - 1; ++y) {
                answer += Long.parseLong(transportationTable.get(x, y)) * Long.parseLong(priceTable.get(x, y));
            }
        }

        return answer;
    }

    public long calcSumThroughPotentialAndMinimalCostMethods(Table table) {
        Table minimalCostTable = calcBasicPlanUsingMinimalCost(table);
        System.out.println("\nТаблица после метода минимальных стоимостей:\n" + minimalCostTable + "\n\n\n\n\n");

        ArrayList<Table> solutions = new ArrayList<>();
        solutions.add(new Table(minimalCostTable));
        for (int i = 0; i < solutions.size(); ++i) {
            if (isOptimalTransportationTable(solutions.get(i), table)) {
                System.out.println("\nЛучшее решение:\n" + solutions.get(i));
                return calcPrice(solutions.get(i), table);
            }

            ArrayList<Table> preparedTables = prepareForThePotentialMethod(solutions.get(i));
            for (int j = 0; j < preparedTables.size(); ++j) {
                Table improvedTable = improveThePlan(solutions.get(i), table, preparedTables.get(j));
                if (!solutions.contains(improvedTable)) {
                    solutions.add(improvedTable);
                }
            }
        }

        System.out.println("Не получается найти решения");

        return 0;
    }

    private Table calcBasicPlanUsingMinimalCost(Table targetTable) {
        Table table = new Table(targetTable);
        Table answer = new Table(targetTable);
        answer.fill(1, 1, answer.getWidth(), answer.getHeight(), "0");

        Long product = 0L;
        Long request = 0L;
        for (int i = 1; i < table.getHeight() - 1; ++i) {
            product += Long.parseLong(table.get(table.getWidth() - 1, i));
        }
        System.out.println("\nОбщее количество товара: " + product);
        for (int i = 1; i < table.getWidth() - 1; ++i) {
            request += Long.parseLong(table.get(i, table.getHeight() - 1));
        }
        System.out.println("\nОбщее требуемое количество: " + request);

        if (!product.equals(request)) {
            throw new IllegalArgumentException("Несбалансированная таблица");
        }

        while (request > 0) {
            int minCostX = 1;
            int minCostY = 1;
            Long minCost = Long.MAX_VALUE;
            for (int x = 1; x < table.getWidth() - 1; ++x) {
                for (int y = 1; y < table.getHeight() - 1; ++y) {
                    if (Long.parseLong(table.get(x, y)) < minCost) {
                        minCost = Long.parseLong(table.get(x, y));
                        minCostX = x;
                        minCostY = y;
                    }
                }
            }

            Long transported = Math.min(
                    Long.parseLong(table.get(minCostX, table.getHeight() - 1)),
                    Long.parseLong(table.get(table.getWidth() - 1, minCostY))
            );
            request -= transported;

            answer.set(minCostX, minCostY, String.valueOf(transported));
            answer.set(minCostX, answer.getHeight() - 1,
                    String.valueOf(transported + Long.parseLong(answer.get(minCostX, answer.getHeight() - 1))));
            answer.set(answer.getWidth() - 1, minCostY,
                    String.valueOf(transported + Long.parseLong(answer.get(answer.getWidth() - 1, minCostY))));
            answer.set(answer.getWidth() - 1, answer.getHeight() - 1, String.valueOf(
                    transported + Long.parseLong(answer.get(answer.getWidth() - 1, answer.getHeight() - 1))));

            table.set(minCostX, minCostY, String.valueOf(Long.MAX_VALUE));
            table.set(minCostX, table.getHeight() - 1,
                    String.valueOf(Long.parseLong(table.get(minCostX, table.getHeight() - 1)) - transported));
            table.set(table.getWidth() - 1, minCostY,
                    String.valueOf(Long.parseLong(table.get(table.getWidth() - 1, minCostY)) - transported));

        System.out.println("\nТа таблица в методе минимальных стоимостей:\n" + table);
            System.out.println("\nОчередная таблица в методе минимальных стоимостей:\n" + answer);
        }

        return answer;
    }

    public Long minimalCost(ArrayList<ArrayList<String>> table) {
        Long product = 0L;
        Long request = 0L;

        for (int i = 1; i < table.get(0).size() - 1; ++i) {
            product += Long.parseLong(table.get(table.size() - 1).get(i));
        }
        System.out.println("Всего товара есть: " + product);
        for (int i = 1; i < table.size() - 1; ++i) {
            request += Long.parseLong(table.get(i).get(table.size() - 1));
        }
        System.out.println("Всего товара надо: " + request);
        if (!request.equals(product)) {
            System.out.println("Баланса нет");
            return -1L;
        }
        System.out.println();


        Long answer = 0L;
        while (request != 0) {
            int posX = 0;
            int posY = 0;
            Long minimalCost = Long.MAX_VALUE;
            for (int i = 1; i < table.size() - 1; ++i) {
                for (int j = 1; j < table.get(0).size() - 1; ++j) {
                    if (Long.parseLong(table.get(i).get(j)) < minimalCost) {
                        posX = j;
                        posY = i;
                        minimalCost = Long.parseLong(table.get(i).get(j));
                    }
                }
            }

            Long transported = Math.min(
                    Long.parseLong(table.get(posY).get(table.size() - 1)),
                    Long.parseLong(table.get(table.size() - 1).get(posX))
            );
            request -= transported;
            Long sum = transported * Long.parseLong(table.get(posY).get(posX));
            answer += sum;
            System.out.println(table.get(posY).get(0) + "->" + table.get(0).get(posX) + ": " + transported + " | " + sum + "/" + answer);
            table.get(posY).remove(posX);
            table.get(posY).add(posX, String.valueOf(Long.MAX_VALUE));

            String newRightValue = String.valueOf(Long.parseLong(table.get(posY).get(table.size() - 1)) - transported);
            String newDownValue = String.valueOf(Long.parseLong(table.get(table.size() - 1).get(posX)) - transported);
            table.get(posY).remove(table.size() - 1);
            table.get(posY).add(newRightValue);
            table.get(table.size() - 1).remove(posX);
            table.get(table.size() - 1).add(posX, newDownValue);

//            printTable(table);
            System.out.println();
        }

        return answer;
    }
}

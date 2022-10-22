package ru.zalimannard;

import java.util.ArrayList;

public class MathMethods {
    public long calcSumThroughPotentialAndNorthwestCornerMethods(Table table) {
        Table northwestCornerTable = calcBasicPlanUsingNorthwestCorner(table);
        System.out.println("\nТаблица после метода северозападного угла:\n" + northwestCornerTable);

        Table preparedTable = prepareForThePotentialMethod(northwestCornerTable);
        System.out.println("\nТаблица, подготовленная к методу потенциалов:\n" + preparedTable);

        Table potentialTable = calcPotentials(preparedTable, table);
        System.out.println("\nТаблица с потенциалами:\n" + potentialTable);

        Table deltaTable = calcDeltas(potentialTable, table, preparedTable);
        System.out.println("\nТаблица с дельтами:\n" + deltaTable);

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

    private Table prepareForThePotentialMethod(Table targetTable) {
        Table table = new Table(targetTable);

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

        if (!isTableReadyForThePotentialMethod(table)) {
            System.out.println("\nПлан вырожденный. Нужно добавить новые базисные элементы");
        }

        // Добавление базисных элементов
        for (int x1 = 1; x1 < table.getWidth() - 1; ++x1) {
            for (int y1 = 1; y1 < table.getHeight() - 1; ++y1) {
                if (table.get(x1, y1).equals("X")) {
                    boolean isOk = true;
                    for (int x2 = 1; x2 < table.getWidth() - 1; ++x2) {
                        for (int y2 = 1; y2 < table.getHeight() - 1; ++y2) {
                            if ((!table.get(x1, y2).equals("X"))
                                    && (!table.get(x2, y1).equals("X"))
                                    && (!table.get(x2, y2).equals("X"))) {
                                isOk = false;
                            }
                        }
                    }
                    if ((isOk) && (!isTableReadyForThePotentialMethod(table))) {
                        table.set(x1, y1, "0");
                    }
                }
            }
        }

        return table;
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

        return table;
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

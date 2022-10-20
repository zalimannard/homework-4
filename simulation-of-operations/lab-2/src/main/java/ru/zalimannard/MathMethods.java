package ru.zalimannard;

import java.util.ArrayList;
import java.util.Arrays;

public class MathMethods {
    public Long hungarianMethodThroughNorthwestCorner(ArrayList<ArrayList<String>> startTable) {
        ArrayList<ArrayList<String>> table = new ArrayList<>(startTable);
        ArrayList<ArrayList<String>> answer = northwestCorner(table);
        System.out.println("После метода северо-западного угла цена: " + calcPrice(table, answer) + " руб");


        return null;
    }

    public ArrayList<ArrayList<String>> northwestCorner(ArrayList<ArrayList<String>> startTable) {
        ArrayList<ArrayList<String>> table = Utils.cloneArrayListArrayListString(startTable);
        ArrayList<ArrayList<String>> answer = createCleanTable(table);
        Long product = 0L;
        Long request = 0L;

        for (int i = 1; i < table.get(0).size() - 1; ++i) {
            product += Long.parseLong(table.get(table.size() - 1).get(i));
        }
        System.out.println("Общее количество товара: " + product);
        for (int i = 1; i < table.size() - 1; ++i) {
            request += Long.parseLong(table.get(i).get(table.get(i).size() - 1));
        }
        System.out.println("Общее требуемое количество: " + request);
        if (!product.equals(request)) {
            throw new IllegalArgumentException("Несбалансированная таблица");
        }
        System.out.println();

        int posX = 1;
        int posY = 1;
        while (request != 0) {
            Long transported = Math.min(
                    Long.parseLong(table.get(posY).get(table.get(posY).size() - 1)),
                    Long.parseLong(table.get(table.size() - 1).get(posX))
            );
            request -= transported;
            // Обновление значений в возвращаемой таблице
            // Самого значения
            answer.get(posY).set(posX, String.valueOf(transported));
            // Справа
            answer.get(posY).set(answer.get(0).size() - 1,
                    String.valueOf(Long.parseLong(answer.get(posY).get(answer.get(0).size() - 1)) + transported));
            // Снизу
            answer.get(answer.size() - 1).set(posX,
                    String.valueOf(Long.parseLong(answer.get(answer.size() - 1).get(posX)) + transported));
            // Сумма
            answer.get(answer.size() - 1).set(answer.get(answer.size() - 1).size() - 1,
                    String.valueOf(Long.parseLong(answer.get(answer.size() - 1).get(answer.get(answer.size() - 1).size() - 1)) + transported));

            // Обновление значений нужных и возможных поставок
            table.get(posY).set(table.get(0).size() - 1,
                    String.valueOf(Long.parseLong(table.get(posY).get(table.get(0).size() - 1)) - transported));
            table.get(table.size() - 1).set(posX,
                    String.valueOf(Long.parseLong(table.get(table.size() - 1).get(posX)) - transported));

            // Сдвиг по лесенке
            if (table.get(table.size() - 1).get(posX).equals("0")) {
                posX += 1;
            }
            if (table.get(posY).get(table.get(posY).size() - 1).equals("0")) {
                posY += 1;
            }

            Utils.printTable(answer, "Очередная таблица в методе северо-западного угла: ");
        }

        Utils.printTable(answer, "Таблица после метода северо-западного угла: ");
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

    public Long calcPrice(ArrayList<ArrayList<String>> costs, ArrayList<ArrayList<String>> roads) {
        Long answer = 0L;
        for (int i = 1; i < costs.size() - 1; ++i) {
            for (int j = 1; j < costs.get(0).size() - 1; ++j) {
                Long cost = Long.valueOf(costs.get(i).get(j));
                Long road = Long.valueOf(roads.get(i).get(j));
                answer += cost * road;
            }
        }
        return answer;
    }

    private ArrayList<ArrayList<String>> createCleanTable(ArrayList<ArrayList<String>> otherTable) {
        ArrayList<ArrayList<String>> table = Utils.cloneArrayListArrayListString(otherTable);
        for (int i = 1; i < table.size(); ++i) {
            for (int j = 1; j < table.get(0).size(); ++j) {
                table.get(i).set(j, "0");
            }
        }
        return table;
    }
}

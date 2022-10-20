package ru.zalimannard;

import java.util.ArrayList;

public class MathMethods {
    public Long northwestCorner(ArrayList<ArrayList<String>> table) {
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


        int posX = 1;
        int posY = 1;
        Long answer = 0L;
        while (request != 0) {
            Long transported = Math.min(
                    Long.parseLong(table.get(posY).get(table.size() - 1)),
                    Long.parseLong(table.get(table.size() - 1).get(posX))
            );
            request -= transported;
            Long sum = transported + Long.parseLong(table.get(posY).get(posX));
            answer += sum * Long.parseLong(table.get(posY).get(posX));
            System.out.println(table.get(posY).get(0) + "->" + table.get(0).get(posX) + ": " + transported + " | " + sum * Long.parseLong(table.get(posY).get(posX)) + "/" + answer);

            String newRightValue = String.valueOf(Long.parseLong(table.get(posY).get(table.size() - 1)) - transported);
            String newDownValue = String.valueOf(Long.parseLong(table.get(table.size() - 1).get(posX)) - transported);
            table.get(posY).remove(table.size() - 1);
            table.get(posY).add(newRightValue);
            table.get(table.size() - 1).remove(posX);
            table.get(table.size() - 1).add(posX, newDownValue);
//
            if (newRightValue.equals("0")) {
                posY += 1;
            }
            if (newDownValue.equals("0")) {
                posX += 1;
            }

            printTable(table);
            System.out.println();
        }


        return answer;
    }

    private void printTable(ArrayList<ArrayList<String>> table) {
        for (ArrayList<String> line : table) {
            System.out.println(line);
        }
    }
}

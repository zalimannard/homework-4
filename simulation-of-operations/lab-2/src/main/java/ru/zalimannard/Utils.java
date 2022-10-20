package ru.zalimannard;

import java.util.ArrayList;

public abstract class Utils {
    public static void printTable(ArrayList<ArrayList<String>> table, String text) {
        System.out.println(text);
        int maxItemLength = getMaximumStringLengthForPrint(table);
        for (ArrayList<String> line : table) {
            for (int i = 0; i < table.size() * (maxItemLength + 3) + 1; ++i) {
                System.out.print("~");
            }
            System.out.println();
            System.out.print("| ");
            for (String item : line) {
                for (int i = 0; i < maxItemLength - item.length(); ++i) {
                    System.out.print(" ");
                }
                System.out.print(item + " | ");
            }
            System.out.println();
        }
        for (int i = 0; i < table.size() * (maxItemLength + 3) + 1; ++i) {
            System.out.print("~");
        }
        System.out.println();
        System.out.println();
    }

    public static ArrayList<ArrayList<String>> cloneArrayListArrayListString(ArrayList<ArrayList<String>> list) {
        ArrayList<ArrayList<String>> answer = new ArrayList<>();
        for (ArrayList<String> line : list) {
            ArrayList<String> lin = new ArrayList<>();
            lin.addAll(line);
            answer.add(lin);
        }
        return answer;
    }

    private static int getMaximumStringLengthForPrint(ArrayList<ArrayList<String>> table) {
        int answer = 0;
        for (int i = 0; i < table.size(); ++i) {
            for (int j = 0; j < table.get(i).size(); ++j) {
                if (table.get(i).get(j).length() > answer) {
                    answer = table.get(i).get(j).length();
                }
            }
        }
        return answer;
    }
}

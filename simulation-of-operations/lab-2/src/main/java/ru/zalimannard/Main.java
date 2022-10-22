package ru.zalimannard;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Неверное количество аргументов");
            return;
        }
        TableReader tableReader = new TableReader();
        Table table = null;
        try {
            table = tableReader.read(args[0]);
        } catch (CsvValidationException e) {
            System.out.println("Неудача с распознаванием CSV");
            return;
        } catch (IOException e) {
            System.out.println("Ошибка ввода-вывода. Видимо, файла нет");
            return;
        }
        System.out.println("Считанные данные:\n" + table);

        MathMethods mathMethods = new MathMethods();
        System.out.println("Методом северо-западного угла: "
                + mathMethods.calcSumThroughPotentialAndNorthwestCornerMethods(table) + " руб");

//        table = readTable();
//        System.out.println("Методом минимальных стоимостей: " + mathMethods.minimalCost(table) + " руб");
    }
}

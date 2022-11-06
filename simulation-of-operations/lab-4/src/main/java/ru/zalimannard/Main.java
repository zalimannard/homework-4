package ru.zalimannard;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        TableReader tableReader = new TableReader();
        Table table = null;
        try {
            table = tableReader.read(args[0]);
        } catch (CsvValidationException e) {
            System.err.println("Не удалось распознать Csv");
        } catch (IOException e) {
            System.err.println("Не удалось считать файл");
        }
        System.out.println(table);
    }
}
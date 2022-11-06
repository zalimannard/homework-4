package ru.zalimannard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class TableReader {
    public Table read(String path) throws CsvValidationException, IOException {
        Reader reader;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CSVReader csvReader = new CSVReader(reader);
        ArrayList<ArrayList<String>> tableAsArrayListOfArraysList = new ArrayList<>();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            ArrayList<String> elementsAsArrayList = new ArrayList<>();
            Collections.addAll(elementsAsArrayList, nextRecord);
            tableAsArrayListOfArraysList.add(elementsAsArrayList);
        }

        Table table = new Table(tableAsArrayListOfArraysList.get(0).get(0));
        for (ArrayList<String> line : tableAsArrayListOfArraysList) {
            for (String element : line) {
                try {
                    Long value = Long.parseLong(element);
                    table.set(line.get(0), tableAsArrayListOfArraysList.get(0).get(line.indexOf(element)), value);
                } catch (NumberFormatException e) {
                    // Если наткнулись на пустоту, то это элемент таблицы. Заносим и удаляем для создания красивых строк
                    // и столбцов
                    if (element.equals("")) {
                        table.set(line.get(0), tableAsArrayListOfArraysList.get(0).get(line.indexOf(element)), 0L);
                        table.set(line.get(0), tableAsArrayListOfArraysList.get(0).get(line.indexOf(element)), null);
                    }
                } catch (IndexOutOfBoundsException e) {
                    // Невозможно определить имя точки, не добавляем ничего
                    e.printStackTrace();
                }
            }
        }
        return table;
    }
}
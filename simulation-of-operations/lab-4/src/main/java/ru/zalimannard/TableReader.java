package ru.zalimannard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TableReader {
    public Table read(String path) throws CsvValidationException, IOException {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CSVReader csvReader = new CSVReader(reader);
        String[] nextRecord;
        Table table = null;
        // Считывание верней строки с заголовками
        if ((nextRecord = csvReader.readNext()) != null) {
            if (nextRecord.length > 0) {
                table = new Table(nextRecord[0]);
            } else {
                return new Table("");
            }
            for (int i = 1; i < nextRecord.length; ++i) {
                table.addColumn(nextRecord[i]);
            }
        }
        System.out.println(table);
        // Считывание остальной таблицы
        while ((nextRecord = csvReader.readNext()) != null) {
            table.addRow(nextRecord[0]);
            for (int i = 1; i < nextRecord.length; ++i) {
                if (i == table.getWidth()) {
                    table.addColumn("");
                }
                System.out.println(table.getHeight());
                table.set(i, table.getHeight(), Long.valueOf(nextRecord[i]));
            }
        }
        return table;
    }
}
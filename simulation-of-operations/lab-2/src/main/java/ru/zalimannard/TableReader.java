package ru.zalimannard;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        Table table = new Table(0, 0);
        while ((nextRecord = csvReader.readNext()) != null) {
            table.addRow();
            for (int i = 0; i < nextRecord.length; ++i) {
                if (i >= table.getWidth()) {
                    table.addColumn();
                }
                table.set(i, table.getHeight() - 1, nextRecord[i]);
            }
            ArrayList<String> line = new ArrayList<>();
            for (String recordItem : nextRecord) {
                line.add(recordItem);
            }
        }
        return table;
    }
}

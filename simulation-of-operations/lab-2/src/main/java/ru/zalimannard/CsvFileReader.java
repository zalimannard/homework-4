package ru.zalimannard;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CsvFileReader {
    public ArrayList<ArrayList<String>> read(String path) {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CSVReader csvReader = new CSVReader(reader);
        // Reading Records One by One in a String array
        String[] nextRecord;
        ArrayList<ArrayList<String>> answer = new ArrayList<>();
        try {
            while ((nextRecord = csvReader.readNext()) != null) {
                ArrayList<String> line = new ArrayList<>();
                for (String recordItem : nextRecord) {
                    line.add(recordItem);
                }
                answer.add(line);
            }
        } catch (Exception e) {

        }

        return answer;
    }
}

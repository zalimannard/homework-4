package ru.zalimannard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Main {
    private static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {
        ArrayList<ArrayList<String>> table = readTable();
        printTable(table);
    }

    private static ArrayList<ArrayList<String>> readTable() {
        CsvFileReader csvFileReader = new CsvFileReader();

        ArrayList<ArrayList<String>> table = csvFileReader.read("transportation-cost.csv");
        ArrayList<ArrayList<String>> consumers = csvFileReader.read("consumers.csv");
        ArrayList<ArrayList<String>> suppliers = csvFileReader.read("suppliers.csv");

        table.get(0).add("");
        for (int i = 1; i < table.size(); ++i) {
            table.get(i).add(suppliers.get(1).get(i - 1));
        }

        ArrayList<String> lastLine = new ArrayList<>();
        lastLine.add("");
        for (int i = 0; i < consumers.get(1).size(); ++i) {
            lastLine.add(consumers.get(1).get(i));
        }
        lastLine.add("");
        table.add(lastLine);

        return table;
    }

    private static void printTable(ArrayList<ArrayList<String>> table) {
        for (ArrayList<String> line : table) {
            System.out.println(line);
        }
    }
}

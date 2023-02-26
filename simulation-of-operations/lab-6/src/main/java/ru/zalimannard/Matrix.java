package ru.zalimannard;

import lombok.NoArgsConstructor;

import java.util.*;

public class Matrix<T> {
    private Map<Cell, T> content = new HashMap<>();

    public Matrix() {

    }

    public Matrix(Matrix other) {
        List<String> columnNames = other.columnNames();
        List<String> rowNames = other.rowNames();
        for (String columnName : columnNames) {
            for (String rowName : rowNames) {
                Cell cell = new Cell(columnName, rowName);
                if (get(cell) != null) {
                    set(cell, (T) other.get(cell));
                }
            }
        }
    }

    public List<String> columnNames() {
        Set<String> uniqueColumnName = new HashSet<>();
        for (Cell cell : content.keySet()) {
            uniqueColumnName.add(cell.getX());
        }
        return uniqueColumnName.stream().toList();
    }

    public List<String> rowNames() {
        Set<String> uniqueRowName = new HashSet<>();
        for (Cell cell : content.keySet()) {
            uniqueRowName.add(cell.getY());
        }
        return uniqueRowName.stream().toList();
    }

    public T get(Cell cell) {
        return content.getOrDefault(cell, null);
    }

    public void set(Cell cell, T value) {
        content.put(cell, value);
    }

    public int width() {
        Set<String> uniqueColumnName = new HashSet<>();
        for (Cell cell : content.keySet()) {
            uniqueColumnName.add(cell.getX());
        }
        return uniqueColumnName.size();
    }

    public int height() {
        Set<String> uniqueRowName = new HashSet<>();
        for (Cell cell : content.keySet()) {
            uniqueRowName.add(cell.getY());
        }
        return uniqueRowName.size();
    }

    public void removeRow(String rowName) {
        List<Cell> cellsToRemove = new ArrayList<>();

        for (Cell cell : content.keySet()) {
            if (cell.getY().equals(rowName)) {
                cellsToRemove.add(new Cell(cell.getX(), cell.getY()));
            }
        }

        for (Cell cell : cellsToRemove) {
            content.remove(cell);
        }
    }

    public void removeColumn(String columnName) {
        List<Cell> cellsToRemove = new ArrayList<>();

        for (Cell cell : content.keySet()) {
            if (cell.getX().equals(columnName)) {
                cellsToRemove.add(new Cell(cell.getX(), cell.getY()));
            }
        }

        for (Cell cell : cellsToRemove) {
            content.remove(cell);
        }
    }

    @Override
    public String toString() {
        String answer = "";
        int cellLength = Math.max(4, getMaxLength());
        String horizontalLine = String.join("", Collections.nCopies((cellLength + 3) * width() + 1, "~"));
        List<String> columnNames = columnNames();
        List<String> rowNames = rowNames();

        answer += horizontalLine + "\n";
        for (String rowName : rowNames) {
            for (String columnName : columnNames) {
                Cell cell = new Cell(columnName, rowName);
                String valueAsString = "";
                if (get(cell) != null) {
                    valueAsString = String.valueOf(get(cell));
                }
                String spaces = String.join("", Collections.nCopies(cellLength - valueAsString.length(), " "));
                answer += "| " + spaces + valueAsString + " ";
            }
            answer += "|\n";
        }
        answer += horizontalLine;
        return answer;
    }

    private int getMaxLength() {
        int maxLength = 0;
        List<String> columnNames = columnNames();
        List<String> rowNames = rowNames();


        for (String columnName : columnNames) {
            maxLength = Math.max(maxLength, columnName.length());
        }
        for (String rowName : rowNames) {
            maxLength = Math.max(maxLength, rowName.length());
        }
        for (String columnName : columnNames) {
            for (String rowName : rowNames) {
                Cell cell = new Cell(columnName, rowName);
                if (get(cell) != null) {
                    maxLength = get(cell).toString().length();
                }
            }
        }
        return maxLength;
    }
}

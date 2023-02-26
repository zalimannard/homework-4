package ru.zalimannard;

import java.util.List;

public class MatrixFractional extends Matrix<Double>{
    public Double minInRow(String rowName, List<String> excludedColumns) {
        Double min = null;

        for (String columnName : columnNames()) {
            if (!excludedColumns.contains(columnName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (min == null) {
                    min = cellValue;
                } else if (cellValue < min) {
                    min = cellValue;
                }
            }
        }

        return min;
    }

    public Double maxInRow(String rowName, List<String> excludedColumns) {
        Double max = null;

        for (String columnName : columnNames()) {
            if (!excludedColumns.contains(columnName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (max == null) {
                    max = cellValue;
                } else if (cellValue > max) {
                    max = cellValue;
                }
            }
        }

        return max;
    }

    public Double minInColumn(String columnName, List<String> excludedRows) {
        Double min = null;

        for (String rowName : rowNames()) {
            if (!excludedRows.contains(rowName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (min == null) {
                    min = cellValue;
                } else if (cellValue < min) {
                    min = cellValue;
                }
            }
        }

        return min;
    }

    public Double maxInColumn(String columnName, List<String> excludedRows) {
        Double max = null;

        for (String rowName : rowNames()) {
            if (!excludedRows.contains(rowName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (max == null) {
                    max = cellValue;
                } else if (cellValue > max) {
                    max = cellValue;
                }
            }
        }

        return max;
    }
}

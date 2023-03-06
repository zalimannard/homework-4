package ru.zalimannard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MatrixFractional extends Matrix<Double> {

    public MatrixFractional() {

    }

    public MatrixFractional(MatrixFractional matrix) {
        super(matrix);
    }

    public Double minInRowValue(String rowName, List<String> excludedColumns) {
        Double min = null;

        for (String columnName : columnNames()) {
            if (!excludedColumns.contains(columnName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (min == null) {
                        min = cellValue;
                    } else if (cellValue < min) {
                        min = cellValue;
                    }
                }
            }
        }

        return min;
    }

    public Double maxInRowValue(String rowName, List<String> excludedColumns) {
        Double max = null;

        for (String columnName : columnNames()) {
            if (!excludedColumns.contains(columnName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (max == null) {
                        max = cellValue;
                    } else if (cellValue > max) {
                        max = cellValue;
                    }
                }
            }
        }

        return max;
    }

    public Double minInColumnValue(String columnName, List<String> excludedRows) {
        Double min = null;

        for (String rowName : rowNames()) {
            if (!excludedRows.contains(rowName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (min == null) {
                        min = cellValue;
                    } else if (cellValue < min) {
                        min = cellValue;
                    }
                }
            }
        }

        return min;
    }

    public Double maxInColumnValue(String columnName, List<String> excludedRows) {
        Double max = null;

        for (String rowName : rowNames()) {
            if (!excludedRows.contains(rowName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (max == null) {
                        max = cellValue;
                    } else if (cellValue > max) {
                        max = cellValue;
                    }
                }
            }
        }

        return max;
    }

    public Cell minInRowCell(String rowName, List<String> excludedColumns) {
        Cell minCell = null;
        Double min = null;

        for (String columnName : columnNames()) {
            if (!excludedColumns.contains(columnName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (min == null) {
                        minCell = cell;
                        min = cellValue;
                    } else if (cellValue < min) {
                        minCell = cell;
                        min = cellValue;
                    }
                }
            }
        }

        return minCell;
    }

    public Cell maxInRowCell(String rowName, List<String> excludedColumns) {
        Cell maxCell = null;
        Double max = null;

        for (String columnName : columnNames()) {
            if (!excludedColumns.contains(columnName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (max == null) {
                        maxCell = cell;
                        max = cellValue;
                    } else if (cellValue > max) {
                        maxCell = cell;
                        max = cellValue;
                    }
                }
            }
        }

        return maxCell;
    }

    public Cell minInColumnCell(String columnName, List<String> excludedRows) {
        Cell minCell = null;
        Double min = null;

        for (String rowName : rowNames()) {
            if (!excludedRows.contains(rowName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (min == null) {
                        minCell = cell;
                        min = cellValue;
                    } else if (cellValue < min) {
                        minCell = cell;
                        min = cellValue;
                    }
                }
            }
        }

        return minCell;
    }

    public Cell maxInColumnCell(String columnName, List<String> excludedRows) {
        Cell maxCell = null;
        Double max = null;

        for (String rowName : rowNames()) {
            if (!excludedRows.contains(rowName)) {
                Cell cell = new Cell(columnName, rowName);
                Double cellValue = get(cell);
                if (cellValue != null) {
                    if (max == null) {
                        maxCell = cell;
                        max = cellValue;
                    } else if (cellValue > max) {
                        maxCell = cell;
                        max = cellValue;
                    }
                }
            }
        }

        return maxCell;
    }

    @Override
    public String toString() {
        String answer = "";
        int cellLength = Math.max(4, getMaxLength() + 3);
        String horizontalLine = String.join("", Collections.nCopies((cellLength + 3) * (width() + 1) + 1, "~"));
        ArrayList<String> columnNames = columnNames();
        Collections.sort(columnNames);
        ArrayList<String> rowNames = rowNames();
        Collections.sort(rowNames);

        if (Utils.isInteger(columnNames.get(0))) {
            Collections.sort(columnNames, Comparator.comparing(Integer::valueOf));
        }
        if (Utils.isInteger(rowNames.get(0))) {
            Collections.sort(rowNames, Comparator.comparing(Integer::valueOf));
        }

        answer += horizontalLine + "\n";
        answer += "|  " + String.join("", Collections.nCopies(cellLength, " "));
        for (String columnName : columnNames) {
            String spaces = String.join("", Collections.nCopies(cellLength - columnName.length(), " "));
            answer += "| " + spaces + columnName + " ";
        }
        answer += "|";

        answer += "\n";
        for (String rowName : rowNames) {
            String firstColumnSpace = String.join("", Collections.nCopies(cellLength - rowName.length(), " "));
            answer += "| " + firstColumnSpace + rowName + " ";

            for (String columnName : columnNames) {
                Cell cell = new Cell(columnName, rowName);
                String valueAsString = "";
                if (get(cell) != null) {
                    valueAsString = String.valueOf(Utils.compress(get(cell)));
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
                    maxLength = Utils.compress(get(cell)).toString().length();
                }
            }
        }
        return maxLength;
    }

}

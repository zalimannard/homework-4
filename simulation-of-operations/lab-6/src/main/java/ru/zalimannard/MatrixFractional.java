package ru.zalimannard;

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
}

package ru.zalimannard;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        MatrixFractional matrix1 = new MatrixFractional();
        matrix1.set(new Cell("x1", "y1"), +3.0);
        matrix1.set(new Cell("x2", "y1"), -5.0);
        matrix1.set(new Cell("x3", "y1"), +1.0);
        matrix1.set(new Cell("x4", "y1"), -2.0);

        matrix1.set(new Cell("x1", "y2"), +4.0);
        matrix1.set(new Cell("x2", "y2"), +2.0);
        matrix1.set(new Cell("x3", "y2"), -4.0);
        matrix1.set(new Cell("x4", "y2"), +3.0);

        matrix1.set(new Cell("x1", "y3"), +2.0);
        matrix1.set(new Cell("x2", "y3"), -3.0);
        matrix1.set(new Cell("x3", "y3"), +5.0);
        matrix1.set(new Cell("x4", "y3"), +4.0);

        task1(matrix1);
        System.out.println("\n\n\n");



        MatrixFractional matrix2 = new MatrixFractional();

        matrix2.set(new Cell("x1", "y1"), -2.0);
        matrix2.set(new Cell("x2", "y1"), +8.0);
        matrix2.set(new Cell("x3", "y1"), +5.0);
        matrix2.set(new Cell("x4", "y1"), +7.0);

        matrix2.set(new Cell("x1", "y2"), -3.0);
        matrix2.set(new Cell("x2", "y2"), +9.0);
        matrix2.set(new Cell("x3", "y2"), +3.0);
        matrix2.set(new Cell("x4", "y2"), +5.0);

        matrix2.set(new Cell("x1", "y3"), +8.0);
        matrix2.set(new Cell("x2", "y3"), -1.0);
        matrix2.set(new Cell("x3", "y3"), +9.0);
        matrix2.set(new Cell("x4", "y3"), +3.0);

        MatrixFractional matrix3 = task2(matrix2);
        System.out.println("\n\n\n");



//        task3(matrix3);
    }

    private static void task1(MatrixFractional targetMatrix) {
        MatrixFractional matrix = new MatrixFractional(targetMatrix);

        System.out.println("Задание 1:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);

        for (String columnName : targetMatrix.columnNames()) {
            Cell cell = new Cell(columnName, "y" + (targetMatrix.height() + 1));
            matrix.set(cell, targetMatrix.maxInColumn(columnName, new ArrayList<>()));
        }
        for (String rowName : targetMatrix.rowNames()) {
            Cell cell = new Cell("x" + (targetMatrix.width() + 1), rowName);
            matrix.set(cell, targetMatrix.minInRow(rowName, new ArrayList<>()));
        }

        System.out.println("Таблица с минимумами:");
        System.out.println(matrix);

        Double maxInLastColumn = matrix.maxInColumn("x" + matrix.width(), new ArrayList<>());
        System.out.println("Нижняя цена игры (максимум из нового столбца): " + maxInLastColumn);
        Double minInLastRow = matrix.minInRow("y" + matrix.height(), new ArrayList<>());
        System.out.println("Верхняя цена игры (минимум из новой строки): " + minInLastRow);

        if (maxInLastColumn.equals(minInLastRow)) {
            for (String columnName : targetMatrix.columnNames()) {
                for (String rowName : targetMatrix.rowNames()) {
                    Cell cell = new Cell(columnName, rowName);
                    if (matrix.get(cell).equals(maxInLastColumn)) {
                        System.out.println("Её координаты (" + cell.getX() + "; " + cell.getY() + ") и значение в ней " + maxInLastColumn);
                        return;
                    }
                }
            }
        } else {
            System.out.println("Седловой точки нет");
        }
    }

    private static MatrixFractional task2(MatrixFractional targetMatrix) {
        MatrixFractional matrix = new MatrixFractional(targetMatrix);

        System.out.println("Задание 2:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);

        for (String columnName : targetMatrix.columnNames()) {
            Cell cell = new Cell(columnName, "y" + (targetMatrix.height() + 1));
            matrix.set(cell, targetMatrix.maxInColumn(columnName, new ArrayList<>()));
        }
        for (String rowName : targetMatrix.rowNames()) {
            Cell cell = new Cell("x" + (targetMatrix.width() + 1), rowName);
            matrix.set(cell, targetMatrix.minInRow(rowName, new ArrayList<>()));
        }

        System.out.println("Таблица с минимумами:");
        System.out.println(matrix);

        Double maxInLastColumn = matrix.maxInColumn("x" + matrix.width(), new ArrayList<>());
        System.out.println("Нижняя цена игры (максимум из нового столбца): " + maxInLastColumn);
        Double minInLastRow = matrix.minInRow("y" + matrix.height(), new ArrayList<>());
        System.out.println("Верхняя цена игры (минимум из новой строки): " + minInLastRow);

        if (maxInLastColumn.equals(minInLastRow)) {
            System.out.println("Есть седловая точка. Если ты это видишь, то задание неправильное");
        } else {
            System.out.println("Седловой точки нет");
        }

        matrix.removeRow("y" + matrix.height());
        matrix.removeColumn("x" + matrix.width());
        System.out.println("Проверка на наличие доминирующих столбцов и строк:");
        List<String> rowNames = matrix.rowNames();
        for (String dominatedRowName : rowNames) {
            for (String obeyRowName : rowNames) {
                if (isDominatedRow(matrix, dominatedRowName, obeyRowName)) {
                    matrix.removeRow(obeyRowName);
                }
            }
        }
        List<String> columnNames = matrix.columnNames();
        for (String dominatedColumnName : columnNames) {
            for (String obeyColumnName : columnNames) {
                if (isDominatedColumn(matrix, dominatedColumnName, obeyColumnName)) {
                    matrix.removeColumn(obeyColumnName);
                }
            }
        }
        System.out.println();
        System.out.println("Матрица без доминирующих элементов:");
        System.out.println(matrix);

        System.out.println("Применим симплекс-метод:");
        MatrixFractional simplexedMatrix = simplex(matrix);

        return matrix;
    }

    private static boolean isDominatedRow(MatrixFractional matrix, String dominatedRow, String obeyRow) {
        boolean answer = false;
        for (String columnName : matrix.columnNames()) {
            if ((matrix.get(new Cell(columnName, dominatedRow)) == null) || (matrix.get(new Cell(columnName, obeyRow)) == null)) {
                return false;
            }
            if (matrix.get(new Cell(columnName, dominatedRow)) < matrix.get(new Cell(columnName, obeyRow))) {
                return false;
            }
            if (matrix.get(new Cell(columnName, dominatedRow)) > matrix.get(new Cell(columnName, obeyRow))) {
                answer = true;
            }
        }
        return answer;
    }

    private static boolean isDominatedColumn(MatrixFractional matrix, String dominatedColumn, String obeyColumn) {
        boolean answer = false;
        for (String rowName : matrix.rowNames()) {
            if ((matrix.get(new Cell(dominatedColumn, rowName)) == null) || (matrix.get(new Cell(obeyColumn, rowName)) == null)) {
                return false;
            }
            if (matrix.get(new Cell(dominatedColumn, rowName)) > matrix.get(new Cell(obeyColumn, rowName))) {
                return false;
            }
            if (matrix.get(new Cell(dominatedColumn, rowName)) < matrix.get(new Cell(obeyColumn, rowName))) {
                answer = true;
            }
        }
        return answer;
    }

    private static MatrixFractional simplex(MatrixFractional targetMatrix) {
        MatrixFractional table = genSimplexTable(targetMatrix);
        System.out.println();
        System.out.println("Изначальная симплекс-таблица:");
        System.out.println(table);

        return table;
    }

    private static MatrixFractional genSimplexTable(MatrixFractional targetMatrix) {
        MatrixFractional table = new MatrixFractional();

        List<String> columnNames = targetMatrix.columnNames();
        List<String> rowNames = targetMatrix.rowNames();

        for (int row = 0; row < rowNames.size(); ++row) {
            Cell simplexTableCell = new Cell("C", "z" + (row + 1));
            table.set(simplexTableCell, 1.0);
        }
        table.set(new Cell("C", "Fun"), 0.0);

        for (int column = 0; column < columnNames.size(); ++column) {
            for (int row = 0; row < rowNames.size(); ++row) {
                Cell targetMatrixCell = new Cell(columnNames.get(column), rowNames.get(row));
                Cell simplexTableCell = new Cell("z" + (column + 1), "z" + (row + 1));
                table.set(simplexTableCell, targetMatrix.get(targetMatrixCell));
            }
            Cell targetMatrixCellFun = new Cell("z" + (column + 1), "Fun");
            table.set(targetMatrixCellFun, -1.0);
        }

        for (int column = columnNames.size(); column < columnNames.size() + rowNames.size(); ++column) {
            for (int row = 0; row < rowNames.size(); ++row) {
                Cell simplexTableCell = new Cell("z" + (column + 1), "z" + (row + 1));
                if (row == column - columnNames.size()) {
                    table.set(simplexTableCell, 1.0);
                } else {
                    table.set(simplexTableCell, 0.0);
                }
            }
            Cell targetMatrixCellFun = new Cell("z" + (column + 1), "Fun");
            table.set(targetMatrixCellFun, 0.0);
        }

        return table;
    }

    private static void task3(MatrixFractional matrix) {
        System.out.println("Задание 3:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);
    }
}
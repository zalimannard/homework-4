package ru.zalimannard;

import java.util.ArrayList;

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



//        MatrixFractional matrix2 = new MatrixFractional();
//
//        matrix2.set(new Cell("x1", "y1"), -2.0);
//        matrix2.set(new Cell("x2", "y1"), +8.0);
//        matrix2.set(new Cell("x3", "y1"), +5.0);
//        matrix2.set(new Cell("x4", "y1"), +7.0);
//
//        matrix2.set(new Cell("x1", "y2"), -3.0);
//        matrix2.set(new Cell("x2", "y2"), +9.0);
//        matrix2.set(new Cell("x3", "y2"), +3.0);
//        matrix2.set(new Cell("x4", "y2"), +5.0);
//
//        matrix2.set(new Cell("x1", "y3"), +8.0);
//        matrix2.set(new Cell("x2", "y3"), -1.0);
//        matrix2.set(new Cell("x3", "y3"), +9.0);
//        matrix2.set(new Cell("x4", "y3"), +3.0);
//        System.out.println(matrix2);
//
//        MatrixFractional matrix3 = task2(matrix2);
//        System.out.println("\n\n\n");



        // task3(matrix3);
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

    private static MatrixFractional task2(MatrixFractional matrix) {
        System.out.println("Задание 2:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);

        return matrix;
    }

    private static void task3(MatrixFractional matrix) {
        System.out.println("Задание 3:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);
    }
}
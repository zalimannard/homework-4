package ru.zalimannard;

public class Main {
    public static void main(String[] args) {
        Matrix matrix1 = new MatrixFractional();
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

        Matrix matrix2 = new MatrixFractional();

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
        System.out.println(matrix2);

        Matrix matrix3 = task2(matrix2);
        System.out.println("\n\n\n");

        // task3(matrix3);
    }

    private static void task1(Matrix matrix) {
//        System.out.println("Задание 1:");
//        System.out.println("Изначальная матрица:");
//        System.out.println(matrix);
//
//        Matrix<Double> matrixWithMinAndMax = new Matrix<>(matrix.width() + 1,  matrix.height() + 1, 0.0);
//        copy(matrix, matrixWithMinAndMax);
//        for (int x = 1; x <= matrix.width(); ++x) {
//            matrixWithMinAndMax.set(x, matrixWithMinAndMax.height(), maxInColumn(matrix, x));
//        }
//        for (int y = 1; y <= matrix.height(); ++y) {
//            matrixWithMinAndMax.set(matrixWithMinAndMax.width(), y, minInRow(matrix, y));
//        }
//        matrixWithMinAndMax.set(matrixWithMinAndMax.width(), matrixWithMinAndMax.height(), null);
//        System.out.println("Найденные минимумы и максимумы:");
//        System.out.println(matrixWithMinAndMax);
//
//        Double maxInLastColumn = maxInColumn(matrixWithMinAndMax, matrixWithMinAndMax.width());
//        System.out.println("Нижняя цена игры (максимум из нового столбца): " + maxInLastColumn);
//        Double minInLastRow = minInRow(matrixWithMinAndMax, matrixWithMinAndMax.height());
//        System.out.println("Верхняя цена игры (минимум из новой строки): " + minInLastRow);
//
//        if (maxInLastColumn.equals(minInLastRow)) {
//            System.out.println("Седловая точка есть");
//            for (int x = 1; x <= matrix.width(); ++x) {
//                for (int y = 1; y <= matrix.height(); ++y) {
//                    if (matrixWithMinAndMax.get(x, y).equals(maxInLastColumn)) {
//                        System.out.println("Её координаты (" + x + "; " + y + ") и значение в ней " + maxInLastColumn);
//                        return;
//                    }
//                }
//            }
//        } else {
//            System.out.println("Седловой точки нет");
//        }
    }

    private static Matrix task2(Matrix matrix) {
        System.out.println("Задание 2:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);

        return matrix;
    }

    private static void task3(Matrix matrix) {
        System.out.println("Задание 3:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);
    }
}
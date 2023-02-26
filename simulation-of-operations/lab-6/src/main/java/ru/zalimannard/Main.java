package ru.zalimannard;

public class Main {
    public static void main(String[] args) {
        task1();
    }

    private static void task1() {
        System.out.println("Задание 1:");
        Matrix<Double> matrix = new Matrix<>(4, 3, 0.0);
        matrix.set(1,1, 3.0); matrix.set(2, 1, -5.0); matrix.set(3, 1, 1.0); matrix.set(4, 1, -2.0);
        matrix.set(1,2, 4.0); matrix.set(2, 2, 2.0); matrix.set(3, 2, -4.0); matrix.set(4, 2, 3.0);
        matrix.set(1,3, 2.0); matrix.set(2, 3, -3.0); matrix.set(3, 3, 5.0); matrix.set(4, 3, 4.0);
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);

        Matrix<Double> matrixWithMinAndMax = new Matrix<>(matrix.getWidth() + 1,  matrix.getHeight() + 1, 0.0);
        copy(matrix, matrixWithMinAndMax);
        for (int x = 1; x <= matrix.getWidth(); ++x) {
            matrixWithMinAndMax.set(x, matrixWithMinAndMax.getHeight(), maxInColumn(matrix, x));
        }
        for (int y = 1; y <= matrix.getHeight(); ++y) {
            matrixWithMinAndMax.set(matrixWithMinAndMax.getWidth(), y, minInRow(matrix, y));
        }
        matrixWithMinAndMax.set(matrixWithMinAndMax.getWidth(), matrixWithMinAndMax.getHeight(), null);
        System.out.println("Найденные минимумы и максимумы:");
        System.out.println(matrixWithMinAndMax);

        Double maxInLastColumn = maxInColumn(matrixWithMinAndMax, matrixWithMinAndMax.getWidth());
        System.out.println("Нижняя цена игры (максимум из нового столбца): " + maxInLastColumn);
        Double minInLastRow = minInRow(matrixWithMinAndMax, matrixWithMinAndMax.getHeight());
        System.out.println("Верхняя цена игры (минимум из новой строки): " + minInLastRow);

        if (maxInLastColumn.equals(minInLastRow)) {
            System.out.println("Седловая точка есть");
            for (int x = 1; x <= matrix.getWidth(); ++x) {
                for (int y = 1; y <= matrix.getHeight(); ++y) {
                    if (matrixWithMinAndMax.get(x, y).equals(maxInLastColumn)) {
                        System.out.println("Её координаты (" + x + "; " + y + ") и значение в ней " + maxInLastColumn);
                        return;
                    }
                }
            }
        } else {
            System.out.println("Седловой точки нет");
        }
    }

    private static void copy(Matrix<Double> a, Matrix<Double> b) {
        for (int x = 1; x <= a.getWidth(); ++x) {
            for (int y = 1; y <= a.getHeight(); ++y) {
                b.set(x, y, a.get(x, y));
            }
        }
    }

    private static double maxInColumn(Matrix<Double> matrix, int x) {
        double max = matrix.get(x, 1);
        for (int y = 1; y < matrix.getWidth(); ++y) {
            // Чтобы не учитывать нижнюю правую ячейку
            if (matrix.get(x, y) == null) {
                continue;
            }
            if (matrix.get(x, y) > max) {
                max = matrix.get(x, y);
            }
        }
        return max;
    }

    private static double minInRow(Matrix<Double> matrix, int y) {
        double min = matrix.get(1, y);
        for (int x = 1; x < matrix.getWidth(); ++x) {
            // Чтобы не учитывать нижнюю правую ячейку
            if (matrix.get(x, y) == null) {
                continue;
            }
            if (matrix.get(x, y) < min) {
                min = matrix.get(x, y);
            }
        }
        return min;
    }
}
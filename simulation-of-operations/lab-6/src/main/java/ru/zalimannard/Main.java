package ru.zalimannard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        task2(matrix2);
        System.out.println("\n\n\n");

        task3(matrix2);
    }

    private static void task1(MatrixFractional targetMatrix) {
        MatrixFractional matrix = new MatrixFractional(targetMatrix);

        System.out.println("Задание 1:");
        System.out.println("Изначальная матрица:");
        System.out.println(matrix);

        for (String columnName : targetMatrix.columnNames()) {
            Cell cell = new Cell(columnName, "y" + (targetMatrix.height() + 1));
            matrix.set(cell, targetMatrix.maxInColumnValue(columnName, new ArrayList<>()));
        }
        for (String rowName : targetMatrix.rowNames()) {
            Cell cell = new Cell("x" + (targetMatrix.width() + 1), rowName);
            matrix.set(cell, targetMatrix.minInRowValue(rowName, new ArrayList<>()));
        }

        System.out.println("Таблица с минимумами:");
        System.out.println(matrix);

        Double maxInLastColumn = matrix.maxInColumnValue("x" + matrix.width(), new ArrayList<>());
        System.out.println("Нижняя цена игры (максимум из нового столбца): " + maxInLastColumn);
        Double minInLastRow = matrix.minInRowValue("y" + matrix.height(), new ArrayList<>());
        System.out.println("Верхняя цена игры (минимум из новой строки): " + minInLastRow);

        if (maxInLastColumn.equals(minInLastRow)) {
            for (String columnName : targetMatrix.columnNames()) {
                for (String rowName : targetMatrix.rowNames()) {
                    Cell cell = new Cell(columnName, rowName);
                    if (matrix.get(cell).equals(maxInLastColumn)) {
                        System.out.println("Её координаты (" + cell.getColumn() + "; " + cell.getRow() + ") и значение в ней " + maxInLastColumn);
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
            matrix.set(cell, targetMatrix.maxInColumnValue(columnName, new ArrayList<>()));
        }
        for (String rowName : targetMatrix.rowNames()) {
            Cell cell = new Cell("x" + (targetMatrix.width() + 1), rowName);
            matrix.set(cell, targetMatrix.minInRowValue(rowName, new ArrayList<>()));
        }

        System.out.println("Таблица с минимумами:");
        System.out.println(matrix);

        Double maxInLastColumn = matrix.maxInColumnValue("x" + matrix.width(), new ArrayList<>());
        System.out.println("Нижняя цена игры (максимум из нового столбца): " + maxInLastColumn);
        Double minInLastRow = matrix.minInRowValue("y" + matrix.height(), new ArrayList<>());
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

        List<Double> xList = new ArrayList<>();
        for (int i = 0; i < simplexedMatrix.height() - 1; ++i) {
            Cell cell = new Cell("C", "z" + (i + 1));
            if (simplexedMatrix.get(cell) != null) {
                xList.add(simplexedMatrix.get(cell));
            } else {
                xList.add(0.0);
            }
        }
        System.out.println("Все x (значения из столбца C):");
        System.out.println(xList);

        List<Double> yList = new ArrayList<>();
        for (int i = 0; i < (simplexedMatrix.width() - 1) / 2; ++i) {
            Cell cell = new Cell("z" + ((i + (simplexedMatrix.width() - 1) / 2) % (simplexedMatrix.width() - 1) + 1), "Fun");
            yList.add(simplexedMatrix.get(cell));
        }
        System.out.println("Все y (значения из строки Fun):");
        System.out.println(yList);

        System.out.println("Линейная форма оптимальных планов (сумма x-ов): " + sumList(xList));
        System.out.println("Линейная форма оптимальных планов (сумма y-ов): " + sumList(yList));

        double gameCost = 1 / sumList(xList);
        System.out.println("Цена игры: " + gameCost);

        System.out.println("Оптимальная смешанная стратегия 1 игрока: " + multiplyList(yList, gameCost));
        System.out.println("Оптимальная смешанная стратегия 2 игрока: " + multiplyList(xList, gameCost));

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

        int iteration = 0;
        while (table.minInColumnValue("C", List.of("Fun")) < 0.0) {
            Cell minInCCell = table.minInColumnCell("C", List.of("Fun"));
            System.out.println("Минимальный элемент среди свободных членов: " + table.get(minInCCell));
            System.out.println("Среди свободных членов есть отрицательные. Нужно перейти к допустимому решению");

            Cell minCellInRowWithMinC = table.minInRowCell(minInCCell.getRow(), List.of("C"));
            if (table.get(minCellInRowWithMinC) >= 0.0) {
                System.out.println("Задачу решить нельзя");
                System.exit(0);
            }
            System.out.println("Ведущая строка: " + minCellInRowWithMinC.getRow());
            System.out.println("Ведущий столбец: " + minCellInRowWithMinC.getColumn());
            System.out.println();

            ++iteration;
            System.out.println("Пересчитываем таблицу. Итерация " + iteration);
            table = recalc(table, minCellInRowWithMinC);
            table = checkZero(table);
            System.out.println(table);
        }

        // У целевой функции не должно быть отрицательных элементов
        iteration = 0;
        while (table.minInRowValue("Fun", List.of("C")) < 0.0) {
            ++iteration;
            System.out.println("У целевой функции есть отрицательные элементы. Избавимся. Итерация " + iteration);

            // Разрешающий столбец
            Cell baseColumnCell = table.minInRowCell("Fun", List.of("C"));
            String rowPair = null;
            Double minDiv = null;

            // Вычисляем разрешающую строку
            for (String rowName : table.rowNames()) {
                if (table.get(new Cell(baseColumnCell.getColumn(), rowName)) > 0.0) {
                    if (rowPair == null) {
                        rowPair = rowName;
                        minDiv = table.get(new Cell("C", rowName)) / table.get(new Cell(baseColumnCell.getColumn(), rowName));
                    } else {
                        if (table.get(new Cell("C", rowName)) / table.get(new Cell(baseColumnCell.getColumn(), rowName)) < minDiv) {
                            rowPair = rowName;
                            minDiv = table.get(new Cell("C", rowName)) / table.get(new Cell(baseColumnCell.getColumn(), rowName));
                        }
                    }
                }
            }
            if (rowPair == null) {
                System.out.println("Посчитать не получится");
                System.exit(0);
            }

            System.out.println("Разрешающий столбец: " + baseColumnCell.getColumn());
            System.out.println("Разрешающая строка: " + rowPair);
            Cell baseCell = new Cell(baseColumnCell.getColumn(), rowPair);

            table = recalc(table, baseCell);
            System.out.println(table);
        }
        return table;
    }

    private static MatrixFractional recalc(MatrixFractional targetMatrix, Cell baseCell) {
        MatrixFractional table = new MatrixFractional(targetMatrix);

        List<String> columnNames = targetMatrix.columnNames();
        List<String> rowNames = targetMatrix.rowNames();

        // Главную строку и столбец пересчитаем потом, потому что они используются для подсчётов
        for (String columnName : columnNames) {
            for (String rowName : rowNames) {
                if ((!columnName.equals(baseCell.getColumn())) && (!rowName.equals(baseCell.getRow()))) {
                    Cell cell = new Cell(columnName, rowName);
                    double coefficient = targetMatrix.get(new Cell(baseCell.getColumn(), cell.getRow())) /
                            targetMatrix.get(new Cell(baseCell.getColumn(), baseCell.getRow()));
                    double value = targetMatrix.get(cell) - coefficient * targetMatrix.get(new Cell(cell.getColumn(), baseCell.getRow()));
                    checkZero(table);
                    table.set(cell, value);
                }
            }
        }

        // Главный столбец без главной строки
        for (String rowName : rowNames) {
            if (!rowName.equals(baseCell.getRow())) {
                table.set(new Cell(baseCell.getColumn(), rowName), 0.0);
            }
        }

        // Главная строка
        for (String columnName : columnNames) {
            double value = targetMatrix.get(new Cell(columnName, baseCell.getRow())) / targetMatrix.get(baseCell);
            table.set(new Cell(columnName, baseCell.getRow()), value);
        }

        // Меняем заголовок строки
        table.changeRowName(baseCell.getRow(), baseCell.getColumn());

        checkZero(table);
        return table;
    }

    // Удостоверимся, что нет -0.0
    private static MatrixFractional checkZero(MatrixFractional targetMatrix) {
        MatrixFractional matrix = new MatrixFractional(targetMatrix);
        List<String> columnNames = matrix.columnNames();
        List<String> rowNames = matrix.rowNames();

        for (String columnName : columnNames) {
            for (String rowName : rowNames) {
                Cell cell = new Cell(columnName, rowName);
                if (Math.abs(matrix.get(cell)) <= 0.000001) {
                    matrix.set(cell, 0.0);
                }
            }
        }

        return matrix;
    }

    private static double sumList(List<Double> list) {
        double answer = 0.0;
        for (double value : list) {
            answer += value;
        }
        return answer;
    }

    private static List<Double> multiplyList(List<Double> list, double value) {
        List<Double> answer = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            answer.add(list.get(i) * value);
        }
        return answer;
    }

    private static MatrixFractional genSimplexTable(MatrixFractional targetMatrix) {
        MatrixFractional table = new MatrixFractional();

        List<String> columnNames = targetMatrix.columnNames();
        List<String> rowNames = targetMatrix.rowNames();

        for (int row = 0; row < rowNames.size(); ++row) {
            Cell simplexTableCell = new Cell("C", "z" + (row + targetMatrix.width() + 1));
            table.set(simplexTableCell, 1.0);
        }
        table.set(new Cell("C", "Fun"), 0.0);

        for (int column = 0; column < columnNames.size(); ++column) {
            for (int row = 0; row < rowNames.size(); ++row) {
                Cell targetMatrixCell = new Cell(columnNames.get(column), rowNames.get(row));
                Cell simplexTableCell = new Cell("z" + (column + 1), "z" + (row + targetMatrix.width() + 1));
                table.set(simplexTableCell, targetMatrix.get(targetMatrixCell));
            }
            Cell targetMatrixCellFun = new Cell("z" + (column + 1), "Fun");
            table.set(targetMatrixCellFun, -1.0);
        }

        for (int column = columnNames.size(); column < columnNames.size() + rowNames.size(); ++column) {
            for (int row = 0; row < rowNames.size(); ++row) {
                Cell simplexTableCell = new Cell("z" + (column + 1), "z" + (row + targetMatrix.width() + 1));
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

        MatrixFractional braunMatrix = new MatrixFractional();
        List<String> forCalcX = new ArrayList<>();
        List<String> forCalcY = new ArrayList<>();

        forCalcX.add("i");
        forCalcY.add("i");
        forCalcX.add("j");
        forCalcY.add("j");
        forCalcX.add("Vmin");
        forCalcY.add("Vmin");
        forCalcX.add("Vmax");
        forCalcY.add("Vmax");
        forCalcX.add("V*");
        forCalcY.add("V*");

        // Первую итерацию вручную
        braunMatrix.set(new Cell("i", "1"), 1.0);
        for (String columnName : matrix.columnNames()) {
            braunMatrix.set(new Cell(columnName, "1"), matrix.get(new Cell(columnName, "y1")));
            forCalcY.add(columnName);
        }
        int firstJValue = Integer.parseInt(dumpFirstLetter(braunMatrix.minInRowCell("1", forCalcX).getColumn()));
        braunMatrix.set(new Cell("j", "1"), (double) firstJValue);
        for (String rowName : matrix.rowNames()) {
            braunMatrix.set(new Cell(rowName, "1"), matrix.get(new Cell("x" + firstJValue, rowName)));
            forCalcX.add(rowName);
        }
        braunMatrix.set(new Cell("Vmin", "1"), braunMatrix.minInRowValue("1", forCalcX));
        braunMatrix.set(new Cell("Vmax", "1"), braunMatrix.maxInRowValue("1", forCalcY));
        braunMatrix.set(new Cell("V*", "1"), (braunMatrix.minInRowValue("1", forCalcX) + braunMatrix.maxInRowValue("1", forCalcY)) / 2);


        // Все остальные итерации
        for (int i = 2; i < 3000; ++i) {
            Cell maxYInPreviousIteration = braunMatrix.maxInRowCell(String.valueOf(i - 1), forCalcY);
            int currentMatrixRowNumber = Integer.parseInt(dumpFirstLetter(maxYInPreviousIteration.getColumn()));
            braunMatrix.set(new Cell("i", String.valueOf(i)), (double) currentMatrixRowNumber);

            for (String columnName : matrix.columnNames()) {
                braunMatrix.set(new Cell(columnName, String.valueOf(i)),
                        braunMatrix.get(new Cell(columnName, String.valueOf(i - 1))) +
                                matrix.get(new Cell(columnName, "y" + currentMatrixRowNumber)));
            }

            int currentMatrixColumnNumber = Integer.parseInt(dumpFirstLetter(
                    braunMatrix.minInRowCell(String.valueOf(i), forCalcX).getColumn()));
            braunMatrix.set(new Cell("j", String.valueOf(i)), (double) currentMatrixColumnNumber);

            for (String rowName : matrix.rowNames()) {
                braunMatrix.set(new Cell(rowName, String.valueOf(i)),
                        braunMatrix.get(new Cell(rowName, String.valueOf(i - 1))) +
                                matrix.get(new Cell("x" + currentMatrixColumnNumber, rowName)));
            }

            braunMatrix.set(new Cell("Vmin", String.valueOf(i)), braunMatrix.minInRowValue(String.valueOf(i), forCalcX) / i);
            braunMatrix.set(new Cell("Vmax", String.valueOf(i)), braunMatrix.maxInRowValue(String.valueOf(i), forCalcY) / i);
            braunMatrix.set(new Cell("V*", String.valueOf(i)), (braunMatrix.minInRowValue(String.valueOf(i), forCalcX) / i + braunMatrix.maxInRowValue(String.valueOf(i), forCalcY) / i) / 2);

        }

        System.out.println(braunMatrix);

        Map<Double, Integer> xArr = new HashMap<>();
        Map<Double, Integer> yArr = new HashMap<>();

        for (String rowNames : braunMatrix.rowNames()) {
            double xKey = braunMatrix.get(new Cell("i", rowNames));
            if (xArr.containsKey(xKey)) {
                xArr.put(xKey, xArr.get(xKey) + 1);
            } else {
                xArr.put(xKey, 1);
            }

            double yKey = braunMatrix.get(new Cell("j", rowNames));
            if (yArr.containsKey(yKey)) {
                yArr.put(yKey, yArr.get(yKey) + 1);
            } else {
                yArr.put(yKey, 1);
            }
        }

        for (Double key : xArr.keySet()) {
            System.out.println("A" + String.valueOf(key).charAt(0) + ": " + ((double) xArr.get(key) / braunMatrix.height()));
        }
        System.out.println();
        for (Double key : yArr.keySet()) {
            System.out.println("B" + String.valueOf(key).charAt(0) + ": " + ((double) yArr.get(key) / braunMatrix.height()));
        }
    }

    private static String dumpFirstLetter(String s) {
        return s.substring(1);
    }
}
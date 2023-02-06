package ru.zalimannard;

import ru.zalimannard.mathelement.Equation;
import ru.zalimannard.mathelement.EquationSystem;

import java.util.*;

public class GomoryTable {
    private Map<GomoryNode, Double> table;

    // Если нужно найти минимум, то уравнение =-1. Максимим - 1.
    public GomoryTable(EquationSystem equationSystem, Equation targetFunction) {
        createTable(equationSystem.getEquation(0).getNumberOfVariables() + 1,
                equationSystem.getNumberOfEquation() + 1);

        // Все пары x - x
        for (int i = 0; i < equationSystem.getNumberOfEquation(); ++i) {
            String rowName = "x" + (i + (equationSystem.getEquation(i).getNumberOfVariables() - equationSystem.getNumberOfEquation() + 1));
            for (int j = 0; j < equationSystem.getEquation(i).getNumberOfVariables(); ++j) {
                String columnName = "x" + (j + 1);
                double value = equationSystem.getEquation(i).getVariable(j);
                // Убираем случаи -0.0
                if (Math.abs(value) <= 0.000001) {
                    value = 0.0;
                }
                table.put(new GomoryNode(columnName, rowName), value);
            }
        }

        // Пары b - x
        for (int i = 0; i < equationSystem.getNumberOfEquation(); ++i) {
            String rowName = "x" + (i + (equationSystem.getEquation(i).getNumberOfVariables() - equationSystem.getNumberOfEquation() + 1));
            double value = equationSystem.getEquation(i).getResult();
            // Убираем случаи -0.0
            if (Math.abs(value) <= 0.000001) {
                value = 0.0;
            }
            table.put(new GomoryNode("b", rowName), value);
        }

        // Все пары x - F
        for (int j = 0; j < targetFunction.getNumberOfVariables(); ++j) {
            String columnName = "x" + (j + 1);
            double value = targetFunction.getVariable(j);
            // Убираем случаи -0.0
            if (Math.abs(value) <= 0.000001) {
                value = 0.0;
            }
            table.put(new GomoryNode(columnName, "F"), value);
        }
    }

    private void createTable(int width, int height) {
        table = new HashMap<>();
        // Заполнение всеми x
        for (int x = 1; x < width; ++x) {
            for (int y = width - height + 1; y < width; ++y) {
                GomoryNode newNode = new GomoryNode("x" + x, "x" + y);
                table.put(newNode, 0.0);
            }
        }
        // Добавляем свободные члены
        for (int y = width - height + 1; y < width; ++y) {
            GomoryNode newNode = new GomoryNode("b", "x" + y);
            table.put(newNode, 0.0);
        }
        // Добавляем целевую функцию
        for (int x = 1; x < width; ++x) {
            GomoryNode newNode = new GomoryNode("x" + x, "F");
            table.put(newNode, 0.0);
        }
        GomoryNode newNodeB = new GomoryNode("b", "F");
        table.put(newNodeB, 0.0);
    }

    public String minB() {
        Double min = null;
        String answer = null;
        for (GomoryNode gomoryNode : table.keySet()) {
            if ((gomoryNode.getColumnName().equals("b")) && (!gomoryNode.getRowName().equals("F"))) {
                if (answer == null) {
                    min = table.get(gomoryNode);
                    answer = gomoryNode.getRowName();
                } else if (min > table.get(gomoryNode)) {
                    min = table.get(gomoryNode);
                    answer = gomoryNode.getRowName();
                }
            }
        }
        return answer;
    }

    public double get (String columnName, String rowName) {
        return table.get(new GomoryNode(columnName, rowName));
    }

    @Override
    public String toString() {
        Set<String> columnNamesSet = new HashSet<>();
        Set<String> rowNamesSet = new HashSet<>();

        for (GomoryNode gomoryNode : table.keySet()) {
            if (!gomoryNode.getColumnName().equals("b")) {
                columnNamesSet.add(gomoryNode.getColumnName());
            }
            if (!gomoryNode.getRowName().equals("F")) {
                rowNamesSet.add(gomoryNode.getRowName());
            }
        }

        List<String> columnNames = new ArrayList<>(columnNamesSet);
        List<String> rowNames = new ArrayList<>(rowNamesSet);

        String answer = "";

        answer += "\t\tb\t\t";
        for (String columnName : columnNames) {
            answer += columnName + "\t\t";
        }
        answer += "\n";

        for (String rowName : rowNames) {
            answer += rowName + "\t\t";
            answer += table.get(new GomoryNode("b", rowName)) + spaces(table.get(new GomoryNode("b", rowName)));
            for (String columnName : columnNames) {
                answer += table.get(new GomoryNode(columnName, rowName)) + spaces(table.get(new GomoryNode(columnName, rowName)));
            }
            answer += "\n";
        }

        answer += "F\t\t";
        answer += table.get(new GomoryNode("b", "F")) + spaces(table.get(new GomoryNode("b", "F")));
        for (String columnName : columnNames) {
            answer += table.get(new GomoryNode(columnName, "F")) + spaces(table.get(new GomoryNode(columnName, "F")));
        }
        answer += "\n";

        return answer;
    }

    private double compressDouble(double value) {
        int valueInt = (int) (value * 100);
        return ((double) valueInt) / 100;
    }

    private String spaces(double value) {
        int number = 8 - String.valueOf(value).length();
        String answer = "";
        for (int i = 0; i < number; ++i) {
            answer += " ";
        }
        return answer;
    }
}

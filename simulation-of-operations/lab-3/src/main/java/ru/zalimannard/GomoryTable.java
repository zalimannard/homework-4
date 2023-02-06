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
            if (targetFunction.getResult() == 1) {
                value = -value;
            }
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

    public String minInRow(String rowName) {
        Double min = null;
        String answer = null;
        for (GomoryNode gomoryNode : table.keySet()) {
            if ((gomoryNode.getColumnName().startsWith("x")) && (gomoryNode.getRowName().equals(rowName))) {
                if (answer == null) {
                    min = table.get(gomoryNode);
                    answer = gomoryNode.getColumnName();
                } else if (min > table.get(gomoryNode)) {
                    min = table.get(gomoryNode);
                    answer = gomoryNode.getColumnName();
                }
            }
        }
        return answer;
    }

    public String maxFractionalPartRow(int numberOfVariables) {
        Double max = null;
        String answer = null;
        for (GomoryNode gomoryNode : table.keySet()) {
            if ((gomoryNode.getColumnName().equals("b")) && (!gomoryNode.getRowName().equals("F")) &&
                    (Integer.valueOf(gomoryNode.getRowName().substring(1)) <= numberOfVariables)) {

                if (answer == null) {
                    max = fractionalPart(table.get(gomoryNode));
                    answer = gomoryNode.getRowName();
                } else if ((fractionalPart(max)) <= fractionalPart(table.get(gomoryNode))) {
                    max = fractionalPart(table.get(gomoryNode));
                    answer = gomoryNode.getRowName();
                }
            }
        }
        return answer;
    }

    public double fractionalPart(double value) {
        return value - Math.floor(value);
    }

    public void recalc(String mainColumnName, String mainRowName) {
        // Главную строку и столбец пересчитаем потом, потому что они используются для подсчётов
        for (GomoryNode node : table.keySet()) {
            if ((!node.getRowName().equals(mainRowName)) && (!node.getColumnName().equals(mainColumnName))) {
                double coefficient = table.get(new GomoryNode(mainColumnName, node.getRowName())) /
                        table.get(new GomoryNode(mainColumnName, mainRowName));
                double value = table.get(node) - coefficient * table.get(new GomoryNode(node.getColumnName(), mainRowName));
                // Убираем случаи -0.0
                if (Math.abs(value) <= 0.000001) {
                    value = 0.0;
                }
                table.put(node, value);
            }
        }

        // Главный столбец без главной строки
        for (GomoryNode node : table.keySet()) {
            if ((!node.getRowName().equals(mainRowName)) && (node.getColumnName().equals(mainColumnName))) {
                table.put(node, 0.0);
            }
        }

        // Главная строка
        double coefficient = table.get(new GomoryNode(mainColumnName, mainRowName));
        for (GomoryNode node : table.keySet()) {
            if (node.getRowName().equals(mainRowName)) {
                double value = table.get(node) / coefficient;
                table.put(node, value);
            }
        }

        // Меняем заголовок строки
        table.put(new GomoryNode("b", mainColumnName), table.get(new GomoryNode("b", mainRowName)));
        table.remove(new GomoryNode("b", mainRowName));
        List<String> columnNames = getColumnNames();
        for (String columnName : columnNames) {
            table.put(new GomoryNode(columnName, mainColumnName), table.get(new GomoryNode(columnName, mainRowName)));
            table.remove(new GomoryNode(columnName, mainRowName));
        }
    }

    public void createNewRestriction(String baseRow) {
        List<String> columnNames = getColumnNames();
        table.put(new GomoryNode("b", "x0"), -fractionalPart(table.get(new GomoryNode("b", baseRow))));
        for (String columnName : columnNames) {
            table.put(new GomoryNode(columnName, "x0"), -fractionalPart(table.get(new GomoryNode(columnName, baseRow))));
        }

        String newXNumber = "x" + (getColumnNames().size() + 1);
        table.put(new GomoryNode(newXNumber, "F"), 0.0);
        List<String> rowNames = getRowNames();
        for (String rowName : rowNames) {
            if (!rowName.equals("x0")) {
                table.put(new GomoryNode(newXNumber, rowName), 0.0);
            } else {
                table.put(new GomoryNode(newXNumber, rowName), 1.0);
            }
        }
    }

    public double get(String columnName, String rowName) {
        return table.get(new GomoryNode(columnName, rowName));
    }

    public boolean exist(String columnName, String rowName) {
        return table.containsKey(new GomoryNode(columnName, rowName));
    }

    public void checkZero() {
        for (GomoryNode node : table.keySet()) {
            if (Math.abs(table.get(node)) <= 0.000001) {
                table.put(node, 0.0);
            }
        }
    }

    @Override
    public String toString() {
        List<String> columnNames = getColumnNames();
        List<String> rowNames = getRowNames();

        String answer = "";

        answer += "\t\tb\t\t";
        for (String columnName : columnNames) {
            answer += columnName + "\t\t";
        }
        answer += "\n";

        for (String rowName : rowNames) {
            answer += rowName + "\t\t";
            answer += Utils.compressDouble(table.get(new GomoryNode("b", rowName))) +
                    spaces(table.get(new GomoryNode("b", rowName)));
            for (String columnName : columnNames) {
                answer += Utils.compressDouble(table.get(new GomoryNode(columnName, rowName))) +
                        spaces(table.get(new GomoryNode(columnName, rowName)));
            }
            answer += "\n";
        }

        answer += "F\t\t";
        answer += Utils.compressDouble(table.get(new GomoryNode("b", "F"))) +
                spaces(table.get(new GomoryNode("b", "F")));
        for (String columnName : columnNames) {
            answer += Utils.compressDouble(table.get(new GomoryNode(columnName, "F"))) +
                    spaces(table.get(new GomoryNode(columnName, "F")));
        }
        answer += "\n";

        return answer;
    }

    public List<String> getRowNames() {
        Set<String> rowNamesSet = new HashSet<>();

        for (GomoryNode gomoryNode : table.keySet()) {
            if (!gomoryNode.getRowName().equals("F")) {
                rowNamesSet.add(gomoryNode.getRowName());
            }
        }

        return new ArrayList<>(rowNamesSet);
    }

    public List<String> getColumnNames() {
        Set<String> columnNamesSet = new HashSet<>();

        for (GomoryNode gomoryNode : table.keySet()) {
            if (!gomoryNode.getColumnName().equals("b")) {
                columnNamesSet.add(gomoryNode.getColumnName());
            }
        }

        return new ArrayList<>(columnNamesSet);
    }

    private String spaces(double value) {
        int number = 8 - String.valueOf(Utils.compressDouble(value)).length();
        String answer = "";
        for (int i = 0; i < number; ++i) {
            answer += " ";
        }
        return answer;
    }
}

package ru.zalimannard.mathelement;

import java.util.ArrayList;

public class Equation {
    private ArrayList<Double> variables;
    private double result;

    public Equation(ArrayList<Double> variables, double result) {
        if (variables.size() == 0) {
            throw new RuntimeException("В уравнении не может не быть переменных");
        }
        this.variables = variables;
        this.result = result;
    }

    public double getVariable(int variableIndex) {
        return variables.get(variableIndex);
    }

    public int getNumberOfVariables() {
        return variables.size();
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        String answer = "";
        for (int i = 0; i < variables.size(); ++i) {
            int variableInt = ((Double) (variables.get(i) * 100)).intValue();

            if (variables.get(i) < 0) {
                variableInt = -variableInt;
                if (i > 0) {
                    answer += " - ";
                } else {
                    answer += "- ";
                }
            } else {
                if (i > 0) {
                    answer += " + ";
                }
            }
            answer += ((double) variableInt) / 100;
            answer += " x" + (i + 1);
        }
        answer += " = ";
        if (result < 0) {
            answer += "- ";
        }
        int resultInt = Math.abs(((Double) (result * 100)).intValue());
        answer += ((double) resultInt) / 100;

        return answer;
    }
}

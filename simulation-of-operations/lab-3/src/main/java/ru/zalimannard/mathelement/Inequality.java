package ru.zalimannard.mathelement;

import java.util.ArrayList;

public class Inequality {
    private ArrayList<Double> variables;
    private Operator operator;
    private double result;

    public Inequality(ArrayList<Double> variables, Operator operator, double result) {
        if (variables.size() == 0) {
            throw new RuntimeException("В неравенстве не может не быть переменных");
        }
        this.variables = variables;
        this.operator = operator;
        this.result = result;
    }

    public Inequality(Inequality other) {
        this.variables = other.variables;
        this.operator = other.operator;
        this.result = other.result;
    }

    public void invert() {
        for (int i = 0; i < variables.size(); ++i) {
            variables.set(i, -variables.get(i));
        }
        if (operator == Operator.LESS_OR_EQUAL) {
            operator = Operator.GREATER_OR_EQUAL;
        } else if (operator == Operator.GREATER_OR_EQUAL) {
            operator = Operator.LESS_OR_EQUAL;
        } else {
            throw new RuntimeException("Неизвестный оператор");
        }
        result = -result;
    }

    public void add(double value) {
        variables.add(value);
    }

    public double getVariable(int variableIndex) {
        return variables.get(variableIndex);
    }

    public int getNumberOfVariables() {
        return variables.size();
    }

    public Operator getOperator() {
        return operator;
    }

    public double getResult() {
        return result;
    }

    public Equation toEquation() {
        return new Equation(variables, result);
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
        if (operator == Operator.LESS_OR_EQUAL) {
            answer += " <= ";
        } else {
            answer += " >= ";
        }
        int resultInt = ((Double) (result * 100)).intValue();
        answer += ((double) resultInt) / 100;

        return answer;
    }
}

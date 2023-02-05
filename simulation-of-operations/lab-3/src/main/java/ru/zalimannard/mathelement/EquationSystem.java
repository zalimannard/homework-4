package ru.zalimannard.mathelement;

import java.util.ArrayList;

public class EquationSystem {
    private ArrayList<Equation> equations = new ArrayList<>();

    public EquationSystem() {
    }

    public void add(Equation equation) {
        if (equations.size() > 0) {
            if (equations.get(0).getNumberOfVariables() != equation.getNumberOfVariables()) {
                throw new RuntimeException("У уравнений должно быть одинаковое количество переменных");
            }
        }
        equations.add(equation);
    }

    public Equation getEquation(int equationsIndex) {
        return equations.get(equationsIndex);
    }

    public int getNumberOfVariables() {
        return equations.size();
    }

    @Override
    public String toString() {
        String answer = "";
        for (Equation equation : equations) {
            answer += "{ " + equation.toString() + "\n";
        }
        return answer;
    }
}

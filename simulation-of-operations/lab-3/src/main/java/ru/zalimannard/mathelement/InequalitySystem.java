package ru.zalimannard.mathelement;

import java.util.ArrayList;
import java.util.List;

public class InequalitySystem {
    private List<Inequality> inequalities = new ArrayList<>();

    public InequalitySystem() {
    }

    public void add(Inequality inequality) {
        if (inequalities.size() > 0) {
            if (inequalities.get(0).getNumberOfVariables() != inequality.getNumberOfVariables()) {
                throw new RuntimeException("У неравенств должно быть одинаковое количество переменных");
            }
        }
        inequalities.add(inequality);
    }

    public Inequality getInequality(int inequalityIndex) {
        return inequalities.get(inequalityIndex);
    }

    public int getNumberOfVariables() {
        return inequalities.size();
    }

    public EquationSystem toEquationSystem() {
        if (inequalities.size() == 0) {
            throw new RuntimeException("Неравенство пустое");
        }

        // Добавляем переменные для создание уравнения
        List<Inequality> augmentedInequalities = new ArrayList<>(inequalities);
        for (int i = 0; i < inequalities.size(); ++i) {
            for (int j = 0; j < inequalities.size(); ++j) {
                if (i == j) {
                    if (inequalities.get(i).getOperator() == Operator.LESS_OR_EQUAL) {
                        augmentedInequalities.get(i).add(+1.0);
                    } else if (inequalities.get(i).getOperator() == Operator.GREATER_OR_EQUAL) {
                        augmentedInequalities.get(i).add(-1.0);
                    } else {
                        throw new RuntimeException("Неверный оператор");
                    }
                } else {
                    augmentedInequalities.get(i).add(0.0);
                }
            }
        }

        // В неравенствах с оператором >= добавили отрицательную базисную переменную. Чтобы она стала положительной
        // умножим эти неравенства на -1
        for (Inequality inequality : augmentedInequalities) {
            if (inequality.getOperator() == Operator.GREATER_OR_EQUAL) {
                inequality.invert();
            }
        }

        // Перенос в вид системы уравнений
        EquationSystem equationSystem = new EquationSystem();
        for (Inequality inequality : augmentedInequalities) {
            equationSystem.add(inequality.toEquation());
        }

        return equationSystem;
    }

    @Override
    public String toString() {
        String answer = "";
        for (Inequality inequality : inequalities) {
            answer += "{ " + inequality.toString() + "\n";
        }
        return answer;
    }
}

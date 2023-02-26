package ru.zalimannard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix<T> {
    private List<List<T>> content = new ArrayList<>();

    public Matrix(int width, int height, T initialValue) {
        for (int y = 0; y < height + 1; ++y) {
            ArrayList<T> row = new ArrayList<>();
            for (int x = 0; x < width + 1; ++x) {
                row.add(initialValue);
            }
            content.add(row);
        }
    }

    public T get(int x, int y) {
        return content.get(y - 1).get(x - 1);
    }

    public void set(int x, int y, T value) {
        content.get(y - 1).set(x - 1, value);
    }

    public int getWidth() {
        return content.get(0).size() - 1;
    }

    public int getHeight() {
        return content.size() - 1;
    }

    @Override
    public String toString() {
        String answer = "";
        int cellLength = Math.max(4, getMaxLength());
        String horizontalLine = String.join("", Collections.nCopies((cellLength + 3) * getWidth() + 1, "~"));

        answer += horizontalLine + "\n";
        for (int y = 1; y <= getHeight(); ++y) {
            for (int x = 1; x <= getWidth(); ++x) {
                String valueAsString = "";
                if (get(x, y) != null) {
                    valueAsString = get(x, y).toString();
                }
                String spaces = String.join("", Collections.nCopies(cellLength - valueAsString.length(), " "));
                answer += "| " + spaces + valueAsString + " ";
            }
            answer += "|\n";
        }
        answer += horizontalLine;

        return answer;
    }

    private int getMaxLength() {
        int maxLength = 0;
        for (int x = 1; x <= getWidth(); ++x) {
            for (int y = 1; y <= getHeight(); ++y) {
                if (get(x, y) == null) {
                    continue;
                }
                if (get(x, y).toString().length() > maxLength) {
                    maxLength = get(x, y).toString().length();
                }
            }
        }
        return maxLength;
    }
}

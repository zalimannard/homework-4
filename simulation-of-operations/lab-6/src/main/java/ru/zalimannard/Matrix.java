package ru.zalimannard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Matrix<T> {
    private List<List<T>> content = new ArrayList<>();

    public Matrix(int width, int height, T initialValue) {
        for (int y = 0; y < height; ++y) {
            ArrayList<T> row = new ArrayList<>();
            for (int x = 0; x < width; ++x) {
                row.add(initialValue);
            }
            content.add(row);
        }
    }

    public T get(int x, int y) {
        return content.get(y).get(x);
    }

    public void set(int x, int y, T value) {
        content.get(y).set(x, value);
    }

    public int getWidth() {
        if (getHeight() > 0) {
            return content.get(0).size();
        }
        return 0;
    }

    public int getHeight() {
        return content.size();
    }

    @Override
    public String toString() {
        String answer = "";
        int cellLength = Math.max(5, getMaxLength());
        String horizontalLine = String.join("", Collections.nCopies((cellLength + 3) * getWidth() + 1, "~"));

        for (int y = 0; y < getHeight(); ++y) {
            answer += horizontalLine + "\n";
            for (int x = 0; x < getWidth(); ++x) {
                String spaces = String.join("", Collections.nCopies(cellLength - get(x, y).toString().length(), " "));
                answer += "| " + spaces + get(x, y).toString() + " ";
            }
            answer += "|\n";
        }
        answer += horizontalLine;

        return answer;
    }

    private int getMaxLength() {
        int maxLength = 0;
        for (int x = 0; x < getWidth(); ++x) {
            for (int y = 0; y < getHeight(); ++y) {
                if (get(x, y).toString().length() > maxLength) {
                    maxLength = get(x, y).toString().length();
                }
            }
        }
        return maxLength;
    }
}

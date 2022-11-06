package ru.zalimannard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Table {
    private String leftTopCorner;
    private ArrayList<String> columnName;
    private ArrayList<String> rowName;
    private ArrayList<ArrayList<Long>> content;

    public Table(String leftTopCorner) {
        this.leftTopCorner = leftTopCorner;
        columnName = new ArrayList<>();
        rowName = new ArrayList<>();
        content = new ArrayList<>();
    }

    public Table(Table other) {
        leftTopCorner = other.getLeftTopCorner();
        for (int column = 1; column <= other.getWidth(); ++column) {
            addColumn(other.getColumnName(column));
        }
        for (int row = 1; row <= other.getHeight(); ++row) {
            addRow(other.getRowName(row));
        }

        for (int column = 1; column <= other.getWidth(); ++column) {
            for (int row = 1; row <= other.getHeight(); ++row) {
                set(row, column, other.get(row, column));
            }
        }
    }

    public void fill(int x1, int y1, int x2, int y2, Long value) {
        if (x1 > x2) {
            int tmp = x2;
            x2 = x1;
            x1 = tmp;
        }
        if (y1 > y2) {
            int tmp = y2;
            y2 = y1;
            y1 = tmp;
        }
        x1 = Math.max(1, x1);
        y1 = Math.max(1, y1);
        x2 = Math.min(getWidth(), x2);
        y2 = Math.min(getHeight(), y2);
        for (int x = x1; x < x2; ++x) {
            for (int y = y1; y < y2; ++y) {
                set(x, y, value);
            }
        }
    }

    public void addRow(String name) {
        rowName.add(name);
        ArrayList<Long> lineToAdd = new ArrayList<>();
        for (int i = 0; i < getWidth(); ++i) {
            lineToAdd.add(null);
        }
        content.add(lineToAdd);
    }

    public void addColumn(String name) {
        columnName.add(name);
        for (int i = 0; i < getHeight(); ++i) {
            content.get(i).add(null);
        }
    }

    public String getLeftTopCorner() {
        return leftTopCorner;
    }

    public int getWidth() {
        return columnName.size();
    }

    public int getHeight() {
        return rowName.size();
    }

    public Long get(int x, int y) {
        return content.get(y - 1).get(x - 1);
    }

    public void set(int x, int y, Long value) {
        content.get(y - 1).set(x - 1, value);
    }

    public String getColumnName(int number) {
        return columnName.get(number - 1);
    }

    public String getRowName(int number) {
        return rowName.get(number - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(leftTopCorner, table.leftTopCorner) && Objects.equals(columnName, table.columnName) && Objects.equals(rowName, table.rowName) && Objects.equals(content, table.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftTopCorner, columnName, rowName, content);
    }

    @Override
    public String toString() {
        StringBuilder answer = new StringBuilder();
        int maxItemLength = getMaximumElementLength();
        String spacesForNullElement = getSpaces(maxItemLength, 0);
        String horizontalLine = String.join("", Collections.nCopies((maxItemLength + 3) * (getWidth() + 1) + 1, "~"));

        // Верхняя строка
        answer.append(horizontalLine).append("\n");
        answer.append("| ");
        if (getLeftTopCorner() == null) {
            answer.append(spacesForNullElement);
        } else {
            answer.append(getSpaces(maxItemLength, getLeftTopCorner().length())).append(getLeftTopCorner());
        }
        answer.append(" |");
        for (int x = 1; x <= getWidth(); ++x) {
            answer.append(" ");
            if (getColumnName(x) == null) {
                answer.append(spacesForNullElement);
            } else {
                answer.append(getSpaces(maxItemLength, getColumnName(x).length())).append(getColumnName(x));
            }
            answer.append(" |");
        }
        answer.append("\n");

        // Остальная таблица
        for (int y = 1; y < getHeight() + 1; ++y) {
            answer.append(horizontalLine).append("\n");
            answer.append("| ").append(getSpaces(maxItemLength, getRowName(y).length())).append(getRowName(y)).append(" |");
            for (int x = 1; x < getWidth() + 1; ++x) {
                answer.append(" ");
                if (get(x, y) == null) {
                    answer.append(spacesForNullElement);
                } else {
                    answer.append(getSpaces(maxItemLength, get(x, y).toString().length())).append(get(x, y));
                }
                answer.append(" |");
            }
            answer.append("\n");
        }
        answer.append(horizontalLine);

        return answer.toString();
    }

    private String getSpaces(int maxItemLength, int elementLength) {
        return String.join("", Collections.nCopies(maxItemLength - elementLength, " "));
    }

    private int getMaximumElementLength() {
        int maxLength = 4;
        for (int x = 1; x < getWidth(); ++x) {
            for (int y = 1; y < getHeight(); ++y) {
                if (get(x, y) != null) {
                    maxLength = Math.max(maxLength, get(x, y).toString().length());
                }
            }
        }
        for (int column = 1; column <= getWidth(); ++column) {
            if (getColumnName(column) != null) {
                maxLength = Math.max(maxLength, getColumnName(column).length());
            }
        }
        for (int row = 1; row <= getHeight(); ++row) {
            if (getRowName(row) != null) {
                maxLength = Math.max(maxLength, getRowName(row).length());
            }
        }
        if (leftTopCorner != null) {
            maxLength = Math.max(maxLength, leftTopCorner.length());
        }
        return maxLength;
    }
}

package ru.zalimannard;

import java.util.Objects;

public class Cell {

    private final String column;
    private final String row;

    public Cell(String column, String row) {
        this.column = column;
        this.row = row;
    }

    public String getColumn() {
        return column;
    }

    public String getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(getColumn(), cell.getColumn()) && Objects.equals(getRow(), cell.getRow());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColumn(), getRow());
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x='" + column + '\'' +
                ", y='" + row + '\'' +
                '}';
    }

}

package ru.zalimannard;

import java.util.Objects;

public class Cell {
    private String x;
    private String y;

    public Cell(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Objects.equals(getX(), cell.getX()) && Objects.equals(getY(), cell.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}

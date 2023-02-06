package ru.zalimannard;

import java.util.Objects;

public class GomoryNode {
    private final String columnName;
    private final String rowName;

    public GomoryNode(String columnName, String rowName) {
        this.columnName = columnName;
        this.rowName = rowName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getRowName() {
        return rowName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GomoryNode that = (GomoryNode) o;
        return Objects.equals(columnName, that.columnName) && Objects.equals(rowName, that.rowName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, rowName);
    }

    @Override
    public String toString() {
        return "GomoryNode{" +
                "columnName='" + columnName + '\'' +
                ", rowName='" + rowName + '\'' +
                '}';
    }
}

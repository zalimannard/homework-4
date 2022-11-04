package ru.zalimannard;

public class Main {
    public static void main(String[] args) {
        Table table = new Table("Угол");
        System.out.println(table);
        table.addRow("Строка");
        System.out.println(table);
        table.addColumn("Столбец");
        System.out.println(table);
    }
}
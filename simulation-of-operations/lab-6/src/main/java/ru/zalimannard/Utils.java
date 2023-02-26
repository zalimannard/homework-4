package ru.zalimannard;

public class Utils {
    public static Double compress(Double value) {
        int valueInt = (int) Math.round(value * 1000);
        return ((double) valueInt) / 1000;
    }
}

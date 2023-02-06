package ru.zalimannard;

public abstract class Utils {
    public static double compressDouble(double value) {
        int valueInt = (int) Math.round(value * 100);
        return ((double) valueInt) / 100;
    }
}

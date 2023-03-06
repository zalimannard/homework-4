package ru.zalimannard;

public class Utils {

    public static Double compress(Double value) {
        int valueInt = (int) Math.round(value * 1000);
        return ((double) valueInt) / 1000;
    }

    public static boolean isInteger(String s) {
        String digits = "0123456789";
        for (int i = 0; i < s.length(); ++i) {
            if (!digits.contains(String.valueOf(s.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

}

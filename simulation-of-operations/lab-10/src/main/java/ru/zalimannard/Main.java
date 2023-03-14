package ru.zalimannard;

public class Main {

    public static void main(String[] args) {
        double personInMinute = 0.4;
        double timeForPerson = 2.5;
        System.out.println("Если n равно 1: --------------------------------------------------");
        calc(1, personInMinute, timeForPerson);
        System.out.println();

        System.out.println("Если n равно 2: --------------------------------------------------");
        calc(2, personInMinute, timeForPerson);
        System.out.println();
    }

    public static void calc(double n, double personInMinute, double timeForPerson) {
        double personInHour = personInMinute * 60;
        double applicationInHour = 60 / timeForPerson;


        double ro = personInHour / applicationInHour;
        System.out.println("Ro: " + ro);
        System.out.println();

        double P0 = 0;
        for (int i = 0; i < n; ++i) {
            P0 += Math.pow(ro, i) / factorial(i);
        }
        P0 = 1 / P0;
        System.out.println("Вероятность простоя системы. P0: " + P0);
        System.out.println();

        double Poch = Math.pow(ro, n + 1) / (factorial(n) * (n - ro)) * P0;
        System.out.println("Вероятность образования очереди Poch: " + Poch);
        System.out.println();

        double Loch = n / (n - ro) * Poch;
        System.out.println("Среднее число заявок, находящихся в очереди Loch: " + Loch);

        double Toch = Loch / personInHour;
        System.out.println("Среднее время ожидания в очереди: " + Toch);
    }

    public static double factorial(double number) {
        double result = 1;
        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }
        return result;
    }

}
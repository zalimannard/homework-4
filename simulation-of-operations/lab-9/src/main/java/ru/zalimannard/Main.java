package ru.zalimannard;

public class Main {

    public static void main(String[] args) {

        System.out.println("Стратегия 1:");
        System.out.println(calc1(0.02, 20, 150, 50, 10) + " американовых рублей в день");
        System.out.println();

        System.out.println("Стратегия 2:");
        System.out.println(calc1(0.02, 20, 200, 75, 15) + " американовых рублей в день");
        System.out.println();

        calc2(0.02, 20, (double) 75 / 15);

    }

    public static double calc1(double s,
                               double K,
                               double Q,
                               double h0,
                               double td) {
        double v = h0 / td;
        return K * v / Q + s * Q / 2;
    }

    public static void calc2(double s,
                             double K,
                             double v) {
        double Qw = Math.sqrt(2 * K * v / s);
        System.out.println("Qw: " + Qw + " штук");

        double L = K * v / Qw + s * Qw / 2;
        System.out.println("L: " + L + " американовых рублей в день");

        double tau = Qw / v;
        System.out.println("Tau: " + tau + " дней");

        double h0 = Qw;
        System.out.println("h0" + h0 + " остаток на складе");

        double td = tau;
        System.out.println("td" + td + " время доставки");
    }

}
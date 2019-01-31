package cz.ktweb.harmonicalgebraquiz;

import android.graphics.Color;

public class MathUtils {
    public static int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return (a * b) / gcd(a, b);
    }

    public static int gcd(int a, int b) {
        if (a < 1 || b < 1) {
            throw new IllegalArgumentException("a or b is less than 1");
        }
        int remainder = 0;
        do {
            remainder = a % b;
            a = b;
            b = remainder;
        } while (b != 0);
        return a;
    }

    public static int rgb(int r, int g, int b) {
        return Color.rgb(Math.max(r, 0), Math.max(g, 0), Math.max(b, 0));
    }
}

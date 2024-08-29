package github.andredimaz.plugin.core.utils.basics;

import java.util.Random;

public class MathUtils {
    public static double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clamp(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }

    public static int max(Integer... numbers) {
        int max = 0;

        for(Integer i : numbers) {
            max = Math.max(max, i);
        }

        return max;
    }

    public static double max(Double... numbers) {
        double max = 0.0D;

        for(Double i : numbers) {
            max = Math.max(max, i);
        }

        return max;
    }

    public static int min(Integer... numbers) {
        int min = 0;

        for(Integer i : numbers) {
            min = Math.min(min, i);
        }

        return min;
    }

    public static double min(Double... numbers) {
        double min = 0.0D;

        for(Double i : numbers) {
            min = Math.min(min, i);
        }

        return min;
    }

    public static int generateNumber(int min, int max) {
        return (new Random()).nextInt(max + 1 - min) + min;
    }

    public static double generateNumber(double min, double max) {
        return min + (new Random()).nextDouble() * (max - min);
    }

    public static double generateNumberWithout0(double min, double max) {
        double o;
        for(o = 0.0D; o == 0.0D; o = min + (new Random()).nextDouble() * (max - min)) {
        }

        return o;
    }

    public static int generateNumberWithout0(int min, int max) {
        int o;
        for(o = 0; o == 0; o = (new Random()).nextInt(max + 1 - min) + min) {
        }

        return o;
    }
}


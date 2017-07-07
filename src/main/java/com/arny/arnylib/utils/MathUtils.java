package com.arny.arnylib.utils;

import java.math.BigDecimal;
import java.util.Random;

public class MathUtils {

    /**
     * дробная часть числа
     *
     * @param x
     * @return
     */
    public static double fracal(double x) {
        return x - (int) x;
    }

    /**
     * целая часть числа
     *
     * @param x
     * @return
     */
    public static int intact(double x) {
        return (int) x;
    }

    /**
     * Отстаток от деления
     *
     * @param x
     * @param y
     * @return
     */
    public static double modulo(double x, double y) {
        return y * (fracal(x / y));
    }

    public static double round(double val, int scale) {
        return new BigDecimal(val).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	public static double summ(double num1, double num2, int scale) {
		return new BigDecimal(num1).add(new BigDecimal(num2)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double subtract(double num1, double num2, int scale) {
		return new BigDecimal(num1).subtract(new BigDecimal(num2)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double multiply(double num1, double num2, int scale) {
		return new BigDecimal(num1).multiply(new BigDecimal(num2)).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double divide(double num1, double num2, int scale) {
		return new BigDecimal(num1).divide(new BigDecimal(num2),BigDecimal.ROUND_HALF_UP).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

    public static long randLong(long min, long max) {
        Random rnd = new Random();
        if (min > max) {
            throw new IllegalArgumentException("min>max");
        }
        if (min == max) {
            return min;
        }
        long n = rnd.nextLong();
        n = n == Long.MIN_VALUE ? 0 : n < 0 ? -n : n;
        n = n % (max - min);
        return min + n;
    }

    public static double randDouble(double min, double max) {
        Random rnd = new Random();
        double range = max - min;
        double scaled = rnd.nextDouble() * range;
        return scaled + min; // == (rand.nextDouble() * (max-min)) + min;
    }

    public static int randInt(int min, int max) {
        Random rnd = new Random();
        int range = max - min + 1;
        return rnd.nextInt(range) + min;
    }

    public static double Cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    public static double Acos(double rad) {
        return Math.toDegrees(Math.acos(rad));
    }

    public static double Sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    public static double Asin(double rad) {
        return Math.toDegrees(Math.asin(rad));
    }

    public static double Tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    public static double Atan(double rad) {
        return Math.toDegrees(Math.atan(rad));
    }

    public static double Sqrt(double num) {
        return Math.sqrt(num);
    }

    public static double Exp(double num, double exp) {
        return Math.pow(num, exp);
    }

    public static double Abs(double num) {
        return Math.abs(num);
    }

    public static double getDecGrad(int D, double M, double S) {
        double sign = 1.0;
        if (D < 0 || M < 0 || S < 0) {
            sign = -1.0;
        }
        D = Math.abs(D);
        M = Math.abs(M);
        S = Math.abs(S);
        return sign * (D + (M / 60) + (S / 3600));
    }

	public enum AngleFormat {
        Dd,
        DMM,
        DMMm,
        DMMSS,
        DMMSSs
    }

    public static String getGradMinSec(double grad, AngleFormat format) {
        //TODO
        double sign = 1.0;
        if (grad < 0) {
            sign = -1.0;
        }
        double x = Math.abs(grad);
        int D = (int) x;
        double y = (x - D) * 60;
        int M = (int) y;
        double z = (y - M) * 60;
        double S = round(z, 2);

        System.out.println(D + " " + M + " " + S);
        System.out.println(sign * D);
        switch (format) {
            case Dd:
                return "" + sign * (int) grad;
            case DMM:
                return "" + (int) grad + " " + (int) grad * 60;
            default:
                return "" + (int) grad;
        }
    }

}

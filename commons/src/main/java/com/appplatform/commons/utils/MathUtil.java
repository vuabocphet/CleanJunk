package com.appplatform.commons.utils;

/**
 * Created by HoangSon on 1/11/2018.
 */

public class MathUtil {

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        long factor = (long) Math.pow(10.0d, (double) places);
        return ((double) Math.round(value * ((double) factor))) / ((double) factor);
    }

    public static int abs(int value) {
        return value >= 0 ? value : Math.abs(value) - 1;
    }

    public static Double getValueTemp(Double value) {
        while (value.doubleValue() > 100.0d) {
            value = Double.valueOf(value.doubleValue() / 10.0d);
            if (value.doubleValue() <= 100.0d) {
                break;
            }
        }
        return value;
    }

    public static double celsius2Fahrenheit(double celsius) {
        return 32.0d + ((9.0d * celsius) / 5.0d);
    }

    public static double fahrenheit2Celsius(double fahrenheit) {
        return ((5.0d * fahrenheit) / 9.0d) - 32.0d;
    }

    public static double getRealTemp(double value) {
//        if (AppPreference.getInstance(BatteryApplication.instance).isFahrenheit()) {
//            return celsius2Fahrenheit(value);
//        }
        return value;
    }



}

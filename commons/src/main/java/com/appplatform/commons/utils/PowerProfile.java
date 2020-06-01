package com.appplatform.commons.utils;

import android.content.Context;
import android.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by HoangSon on 1/12/2018.
 */

public class PowerProfile {
    public static final Pair<String, Double> POWER_NONE = new Pair<>("POWER_NONE", 0.0d);
    public static final Pair<String, Double> POWER_CPU_IDLE = new Pair<>("POWER_CPU_IDLE", 3.8d);
    public static final Pair<String, Double> POWER_CPU_AWAKE = new Pair<>("POWER_CPU_AWAKE", 11.1d);
    public static final Pair<String, Double> POWER_CPU_ACTIVE = new Pair<>("POWER_CPU_ACTIVE", 109.0d);
    public static final Pair<String, Double> POWER_WIFI_SCAN = new Pair<>("POWER_WIFI_SCAN", 286.0d);
    public static final Pair<String, Double> POWER_WIFI_ON = new Pair<>("POWER_WIFI_ON", 1.0d);
    public static final Pair<String, Double> POWER_WIFI_ACTIVE = new Pair<>("POWER_WIFI_ACTIVE", 601.0d);
    public static final Pair<String, Double> POWER_GPS_ON = new Pair<>("POWER_GPS_ON", 55.0d);
    public static final Pair<String, Double> POWER_BLUETOOTH_ON = new Pair<>("POWER_BLUETOOTH_ON", 2.0d);
    public static final Pair<String, Double> POWER_BLUETOOTH_ACTIVE = new Pair<>("POWER_BLUETOOTH_ACTIVE", 127.0d);
    public static final Pair<String, Double> POWER_BLUETOOTH_AT_CMD = new Pair<>("POWER_BLUETOOTH_AT_CMD", 2.0d);
    public static final Pair<String, Double> POWER_SCREEN_ON = new Pair<>("POWER_SCREEN_ON", 49.0d);
    public static final Pair<String, Double> POWER_RADIO_ON = new Pair<>("POWER_RADIO_ON", 6.8d);
    public static final Pair<String, Double> POWER_RADIO_SCANNING = new Pair<>("POWER_RADIO_SCANNING", 140.0d);
    public static final Pair<String, Double> POWER_RADIO_ACTIVE = new Pair<>("POWER_RADIO_ACTIVE", 186.0d);
    public static final Pair<String, Double> POWER_SCREEN_FULL = new Pair<>("POWER_SCREEN_FULL", 377.0);
    public static final Pair<String, Double> POWER_AUDIO = new Pair<>("POWER_AUDIO", 47.0d);
    public static final Pair<String, Double> POWER_VIDEO = new Pair<>("POWER_VIDEO", 207.0d);
    private static ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    private static Class<?> clazzz;
    private static Constructor<?> constructor;
    private static Method getBatteryCapacityMethod;
    private static Method getAveragePowerMethod;
    private Object object = null;

    static PowerProfile instance;

    public static PowerProfile getInstance(Context context) {
        if (instance == null) {
            instance = new PowerProfile(context);
        }
        return instance;
    }

    private PowerProfile(Context context) {
        try {
            clazzz = systemClassLoader.loadClass("com.android.internal.os.PowerProfile");
            constructor = clazzz.getConstructor(systemClassLoader.loadClass("android.content.Context"));
            getBatteryCapacityMethod = clazzz.getMethod("getBatteryCapacity");
            getAveragePowerMethod = clazzz.getMethod("getAveragePower", String.class);
            this.object = constructor.newInstance(context);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBatteryCapacity() {
        if (this.object == null) {
            return 1000.0d;
        }
        try {
            return (Double) getBatteryCapacityMethod.invoke(this.object, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return 1000.0d;
        }
    }

    public double getAveragePower(Pair<String, Double> pair) {
        if (this.object == null) {
            return pair.second;
        }
        try {
            double value = (double) getAveragePowerMethod.invoke(this.object, new Object[]{clazzz.getField(pair.first).get(this.object)});
            if (value <= 1.0d) {
                return pair.second;
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return pair.second;
        }
    }

    public static long getDisChargingTimeForOnePercent(Context context) {
        double batteryCapacity = getInstance(context).getBatteryCapacity();
        long value = batteryCapacity < 3000.0d ? 93600000 : batteryCapacity < 4500.0d ? 129600000 : 216000000;
        return value / 100;
    }

}
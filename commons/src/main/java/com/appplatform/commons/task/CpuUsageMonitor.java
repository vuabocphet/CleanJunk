package com.appplatform.commons.task;

import android.content.Context;
import android.content.SharedPreferences;

public class CpuUsageMonitor {
    public static final String PREF_KEY = "cpu_usage_history";
    public static final String TAG = CpuUsageMonitor.class.getSimpleName();


    private static SharedPreferences getPref(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_KEY, 0);
    }

    public interface OnCpuUsageMonitorListener {
        void onCpuUsageMonitor(String packageName, int count);
    }


    static int getHighCpuUsageTime(Context context, String packageName) {
        int time = getPref(context).getInt(packageName, 0);
        if (time > 0) {
            return time - 1;
        }
        return time;
    }

    public static void removeFromHighCpuUsageList(Context context, String packageName) {
        getPref(context).edit().remove(packageName).apply();
    }

    private static void clearHighCpuUsageHistory(Context context) {
        getPref(context).edit().clear().apply();
    }
}

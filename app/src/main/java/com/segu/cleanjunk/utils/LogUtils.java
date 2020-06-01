package com.segu.cleanjunk.utils;

import android.util.Log;

import com.google.gson.Gson;

public class LogUtils {

    public static void logJson(Object o) {
        try {
            Log.e("TINH-NV", new Gson().toJson(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.appplatform.commons.utils;

import android.os.Build;
import android.os.Environment;

/**
 * Created by camlh.segu on 9/9/2016.
 */
public class DeviceUtil {
    public static boolean isXiaomiPhone() {
        try {
            return ((Build.MODEL.toUpperCase().contains("MI"))
                    || (Build.DISPLAY.toLowerCase().contains("miui"))
                    || (Build.HOST.toLowerCase().contains("miui")))
                    && (Environment.getExternalStorageState().equals("mounted"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

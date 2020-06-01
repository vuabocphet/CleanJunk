package com.appplatform.commons;

/**
 * Created by HoangSon on 3/21/2018.
 */

public enum  AppPlatformFlag {
    BATTERY_OPTIMIZE("BATTERY_OPTIMIZE"),
    WIFI_BOOSTER("WIFI_BOOSTER"),
    PHONE_COOLER("PHONE_COOLER");

    private String value;

    AppPlatformFlag(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }


    public static AppPlatformFlag getType(String value) {
        for (AppPlatformFlag v : AppPlatformFlag.values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        return BATTERY_OPTIMIZE;
    }

}

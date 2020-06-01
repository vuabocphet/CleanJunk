package com.appplatform.commons;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HoangSon on 1/15/2018.
 */

public class AppPlatformPreference {
    private final String PREF_LAST_TIME_COOLING = "key_last_time_cooling";
    private final String PREF_TEMPERATURE = "key_temperature";
    private final String PREF_TEMPERATURE_UNIT = "key_temperature_unit";
    private SharedPreferences sharedPreferences;

    public AppPlatformPreference(Context context) {
        this.sharedPreferences = context.getSharedPreferences("app_platform_pref", Context.MODE_PRIVATE);
    }

    public void setCurrentTimeOptimize(long time) {
        this.sharedPreferences.edit().putLong("current_time_optimize", time).apply();
    }

    public long getCurrentTimeOptimize() {
        return this.sharedPreferences.getLong("current_time_optimize", 0);
    }


    public boolean isNeedOptimize() {
        return (System.currentTimeMillis() - getCurrentTimeOptimize()) >1800 * 1000;
    }

    public int getTimeIncrement() {
        return this.sharedPreferences.getInt("time_increment", 0);
    }

    public void setTimeIncrement(int timeIncrement) {
        this.sharedPreferences.edit().putInt("time_increment", timeIncrement).apply();
    }

    public void setTimeBoosted(long timeBoosted) {
        this.sharedPreferences.edit().putLong("time_boosted", timeBoosted).apply();
    }

    public long getTimeBoosted() {
        return this.sharedPreferences.getLong("time_boosted", 0);
    }


    public boolean isMemoryBoosted() {
        return (System.currentTimeMillis() - getTimeBoosted()) <= 1800 * 1000;
    }

    public boolean isPhoneCoolerCooled() {
        return (System.currentTimeMillis() - getLastTimeCooling()) <= 1800 * 1000;
    }
    public void setLastTimeCooling(long time) {
        this.sharedPreferences.edit().putLong(PREF_LAST_TIME_COOLING, time).apply();
    }

    public long getLastTimeCooling() {
        return this.sharedPreferences.getLong(PREF_LAST_TIME_COOLING, 0);
    }

    public void setTemperature(String value){
        this.sharedPreferences.edit().putString(PREF_TEMPERATURE, value).apply();
    }

    public String getTemperature(){
        return this.sharedPreferences.getString(PREF_TEMPERATURE, "");
    }

    public int getTemperatureUnit(){
        return this.sharedPreferences.getInt(PREF_TEMPERATURE_UNIT, 0);
    }


    public void setTemperatureUnit(int unit){
        this.sharedPreferences.edit().putInt(PREF_TEMPERATURE_UNIT, unit).apply();
    }
}

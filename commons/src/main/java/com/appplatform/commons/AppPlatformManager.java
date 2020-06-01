package com.appplatform.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.appplatform.commons.task.CacheManager;
import com.appplatform.commons.utils.Timber;

/**
 * Created by HoangSon on 1/15/2018.
 */

public class AppPlatformManager {

    private static AppPlatformManager instance;
    private SharedPreferences sharedPreferences;
    private Typeface typefaceTitle;
    private Typeface typefaceNormal;

    public static AppPlatformManager init(Context context) {
        if (instance == null) {
            instance = new AppPlatformManager(context);
        }
        return instance;
    }


    public static AppPlatformManager getInstance() {
        return instance;
    }

    private CacheManager cacheManager;

    private AppPlatformManager(Context context) {
        this.cacheManager = new CacheManager(context, "ignore_list");
        this.sharedPreferences = context.getSharedPreferences("app_platform_pef", Context.MODE_PRIVATE);

    }

    public void setEnableLogging(boolean isEnableLog) {
        if (isEnableLog) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public void setFontTextView(String path) {
        sharedPreferences.edit().putString("font_text_view", path).apply();
    }

    public String getFontTextView() {
        return this.sharedPreferences.getString("font_text_view", "");
    }

    public void setTypeFaceTextView(Typeface typefaceTitle, Typeface typefaceNormal) {
        this.typefaceTitle = typefaceTitle;
        this.typefaceNormal = typefaceNormal;
    }

    public Typeface[] getTypefaceTextView() {
        return new Typeface[]{this.typefaceNormal, this.typefaceTitle};
    }


    public void setFontButton(String path) {
        sharedPreferences.edit().putString("font_text_view", path).apply();
    }

    public String getFontButton() {
        return this.sharedPreferences.getString("font_text_view", "");
    }


    public CacheManager getIgnoreList() {
        return cacheManager;
    }


    public static String getString(Context context, int resource) {
        return context.getString(resource);
    }
}

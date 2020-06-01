package com.appplatform.commons.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by HoangSon on 1/15/2018.
 */

public class DisplayUtil {
    private static float density = 0.0f;
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    public static float getDensity(Context context) {
        if (density == 0.0f) {
            try {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
                density = displayMetrics.density;
            } catch (Exception e) {
                density = 1.0f;
            }
        }
        return density;
    }
    public static int getDisplayMetrics(Context context, int i) {
        return (int) TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics());
    }
    public static int getDensity(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }
    public static int getDensity(Context context, int i) {
        return (int) ((((float) i) * getDensity(context)) + 0.5f);
    }

    public static int getDensityRound(Context context, float f) {
        return Math.round(context.getResources().getDisplayMetrics().density * f);
    }

    public static int radius(int i, int i2) {
        return (int) (((1.0f * ((float) i)) * ((float) i2)) / 1920.0f);
    }



    public static int dp2Px(int i) {
        return Math.round(((float) i) * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int sp2px(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().scaledDensity * f) + 0.5f);
    }



}

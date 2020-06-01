package com.appplatform.commons.utils;

import android.graphics.Color;

/**
 * Created by HoangSon on 9/15/2016.
 */
public class ColorSupport {
    private static int currentColor1;
    private static int currentColor2;
    private static int alphaColor1;
    private static int redColor1;
    private static int greenColor1;
    private static int blueColor1;
    private static int alphaColor2;
    private static int redColor2;
    private static int greenColor2;
    private static int blueColor2;

    public static int getColor(int color1, int color2, float alpha) {
        if (color1 != currentColor1 || color2 != currentColor2) {
            currentColor1 = color1;
            currentColor2 = color2;
            alphaColor1 = Color.alpha(color1);
            redColor1 = Color.red(color1);
            greenColor1 = Color.green(color1);
            blueColor1 = Color.blue(color1);
            alphaColor2 = Color.alpha(color2) - alphaColor1;
            redColor2 = Color.red(color2) - redColor1;
            greenColor2 = Color.green(color2) - greenColor1;
            blueColor2 = Color.blue(color2) - blueColor1;
        }
        return Color.argb((int) ((float) alphaColor2 * alpha + (float) alphaColor1), (int) ((float) redColor2 * alpha + (float) redColor1), (int) ((float) greenColor2 * alpha + (float) greenColor1), (int) ((float) blueColor2 * alpha + (float) blueColor1));
    }

    public static int alpha(int i, float f) {
        int alpha = Color.alpha(i);
        return (((int) (((float) alpha) * f)) << 24) + (i - (alpha << 24));
    }
}

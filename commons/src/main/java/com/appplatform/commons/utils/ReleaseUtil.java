package com.appplatform.commons.utils;

import android.database.Cursor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;

/**
 * Created by HoangSon on 1/11/2018.
 */

public class ReleaseUtil {
    public static void release(Cursor cursor) {
        try {
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void release(DataInputStream stream) {
        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void release(FileInputStream stream) {
        try {
            if (stream == null) {
                return;
            }
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void release(BufferedReader reader) {
        try {
            if (reader == null) {
                return;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void release(FileReader reader) {
        try {
            if (reader == null) {
                return;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

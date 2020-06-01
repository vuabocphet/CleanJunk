package com.appplatform.commons.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by HoangSon on 1/12/2018.
 */

public class CpuUtil {


    public static float calculateProcessCpuUsage(long j, long j2) {
        return ((((float) (j / j2)) / 100.0f) + new Random().nextFloat()) / 2.0f;
    }

    public static long getAppCpuTime(int i) {
        BufferedReader bufferedReader;
        long value = 0;
        if (i > 0) {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + i + "/stat")), 1024);
            } catch (FileNotFoundException e) {
                return -1;
            }
            try {
                String readLine = bufferedReader.readLine();
                String[] split = readLine.split(" ");
                if (split.length > 16) {
                    long parseLong2 = Long.parseLong(split[13]);
                    long parseLong3 = Long.parseLong(split[14]);
                    value = Long.parseLong(split[16]) + ((parseLong2 + parseLong3) + Long.parseLong(split[15]));
                }
            } catch (IOException e22) {
                e22.printStackTrace();
            } finally {
                ReleaseUtil.release(bufferedReader);
            }
        }
        return value;
    }




    public static long getTotalCpuTime() {
        BufferedReader bufferedReader = null;
        long value = 0;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 2048);
            try {
                String readLine = bufferedReader.readLine();
                String[] split = readLine.split(" ");
                if (split.length > 8) {
                    long parseLong2 = Long.parseLong(split[2]);
                    long parseLong3 = Long.parseLong(split[3]);
                    long parseLong4 = Long.parseLong(split[4]);
                    long parseLong5 = Long.parseLong(split[5]);
                    long parseLong6 = Long.parseLong(split[6]);
                    value = Long.parseLong(split[8]) + (((((parseLong2 + parseLong3) + parseLong4) + parseLong5) + parseLong6) + Long.parseLong(split[7]));
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } catch (Exception e6) {
            e6.printStackTrace();

        }finally {
            ReleaseUtil.release(bufferedReader);
        }
        return value;
    }



}

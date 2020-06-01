package com.appplatform.commons.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HoangSon on 1/11/2018.
 */

public class MemoryManager {
    private static final int BYTE = 0;
    private static final int GIGA_BYTE = 3;
    private static final int KILO_BYTE = 1;
    private static final int MEGA_BYTE = 2;
    private static final int PETA_BYTE = 5;
    private static final int TERA_BYTE = 4;

    public static long getAvailableMemory(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            return memoryInfo.availMem;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMemorySizeText(Context context, long memorySize) {
        return Formatter.formatFileSize(context, memorySize);
    }

    public static long getTotalMemorySize() {
        return readAvailMem().get("MemTotal");
    }

    public static long getActiveMemorySize() {
        return readAvailMem().get("Active");
    }

    public static long getEmptyMemorySize() {
        Map<String, Long> memoryMap = readAvailMem();
        return memoryMap.get("MemTotal") - memoryMap.get("Active");
    }

    public static double getUsageMemoryPercent(Context context) {
        final long totalMemory = getTotalRamSize();
        final long freeMemory = getAvailableMemory(context);
        return (100 * ((double) (totalMemory - freeMemory) / (double) totalMemory));
    }

    public static Map<String, Long> readAvailMem() {
        Map<String, Long> map = new HashMap<>();
        try {
            byte[] mBuffer = new byte[2084];
            FileInputStream is = new FileInputStream("/proc/meminfo");
            int len = is.read(mBuffer);
            is.close();
            int BUFLEN = mBuffer.length;
            int i = BYTE;
            while (i < len) {
                if (matchText(mBuffer, i, "MemTotal")) {
                    i += 8;
                    map.put("MemTotal", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Active(anon)")) {
                    i += 12;
                    map.put("Active(anon)", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Inactive(anon)")) {
                    i += 14;
                    map.put("Inactive(anon)", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Active(file)")) {
                    i += 12;
                    map.put("Active(file)", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Inactive(file)")) {
                    i += 14;
                    map.put("Inactive(file)", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "KernelStack")) {
                    i += 11;
                    map.put("KernelStack", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "MemFree")) {
                    i += 7;
                    map.put("MemFree", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Buffers")) {
                    i += 7;
                    map.put("Buffers", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Cached")) {
                    i += 6;
                    map.put("Cached", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Inactive")) {
                    i += 8;
                    map.put("Inactive", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Unevictable")) {
                    i += 11;
                    map.put("Unevictable", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "HighTotal")) {
                    i += 9;
                    map.put("HighTotal", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "HighFree")) {
                    i += 8;
                    map.put("HighFree", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "LowTotal")) {
                    i += 8;
                    map.put("LowTotal", extractMemValue(mBuffer, i));
                } else if (matchText(mBuffer, i, "Active")) {
                    i += 6;
                    map.put("Active", extractMemValue(mBuffer, i));
                }
                while (i < BUFLEN && mBuffer[i] != (byte) 10) {
                    i += KILO_BYTE;
                }
                i += KILO_BYTE;
            }
        } catch (IOException ignored) {
        }
        return map;
    }

    private static boolean matchText(byte[] buffer, int index, String text) {
        int N = text.length();
        if (index + N >= buffer.length) {
            return false;
        }
        for (int i = BYTE; i < N; i += KILO_BYTE) {
            if (buffer[index + i] != text.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private static long extractMemValue(byte[] buffer, int index) {
        while (index < buffer.length && buffer[index] != (byte) 10) {
            if (buffer[index] < (byte) 48 || buffer[index] > (byte) 57) {
                index += KILO_BYTE;
            } else {
                int start = index;
                index += KILO_BYTE;
                while (index < buffer.length && buffer[index] >= (byte) 48 && buffer[index] <= (byte) 57) {
                    index += KILO_BYTE;
                }
                return ((long) Integer.parseInt(new String(buffer, BYTE, start, index - start))) * 1024;
            }
        }
        return 0;
    }

    public static long getTotalRamSize() {
        String str1 = "proc/meminfo";
        String str2 = "";
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(str1), 8192);
            str2 = localBufferedReader.readLine();
            String[] arrayOfString = str2.split("\\s+");
//            for (String anArrayOfString : arrayOfString) {
//                Log.i(str2, anArrayOfString + "\t");
//            }
            long initialMemory = Long.valueOf(arrayOfString[KILO_BYTE]) * 1024;
            localBufferedReader.close();
            return initialMemory;
        } catch (IOException e) {
            return 1;
        }
    }


    public static float getPercentageCPU(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 26) {
                int pId = Process.myPid();
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                am.getProcessMemoryInfo(new int[]{pId});
                BufferedReader reader;
                String[] sa;
                long work;
                long total = 0;

                reader = new BufferedReader(new FileReader("/proc/stat"));
                sa = reader.readLine().split("[ ]+", 9);
                work = Long.parseLong(sa[1]) + Long.parseLong(sa[2]) + Long.parseLong(sa[3]);
                total = work + Long.parseLong(sa[4]) + Long.parseLong(sa[5]) + Long.parseLong(sa[6]) + Long.parseLong(sa[7]);
                reader.close();

                reader = new BufferedReader(new FileReader("/proc/" + pId + "/stat"));
                sa = reader.readLine().split("[ ]+", 18);
                long workAM = Long.parseLong(sa[13]) + Long.parseLong(sa[14]) + Long.parseLong(sa[15]) + Long.parseLong(sa[16]);
                reader.close();
                return workAM / total;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;

    }

    private static float restrictPercentage(float percentage) {
        if (percentage > 100)
            return 100;
        else if (percentage < 0)
            return 0;
        else return percentage;
    }

}

package com.appplatform.commons.utils;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Created by HoangSon on 1/11/2018.
 */

public class BatteryUtil {
    public static float getBatteryUsage(int pid, long j, long ram, long totalRam) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        float value = 0.0f;
        try {
            fileReader = new FileReader("/proc/" + pid + "/stat");
            try {
                bufferedReader = new BufferedReader(fileReader);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String[] split = bufferedReader.readLine().trim().split("\\s+");
                long l1 = parseLong(split[13]);
                long l2 = parseLong(split[14]);
                long l3 = parseLong(split[16]);
                long l4 = parseLong(split[15]);
                float abs = Math.abs((float) ((double) (l3 + l1 + l2 + l4 * 100) * 100 / ((double) j) / 100.0d));
                value = (0.6f * abs) + (0.4f * Math.abs((float) (1 + ((100 * ram) / totalRam))));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        } finally {
            ReleaseUtil.release(fileReader);
            ReleaseUtil.release(bufferedReader);
        }
        return value;
    }

    public static long parseLong(String string) {
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getBatteryLevel(int level, int plugg) {
        int batteryLevel = 50;
        try {
            String model = Build.MODEL;
            if (model.equalsIgnoreCase("SCH-i909") || model.equalsIgnoreCase("SCH-I535") || model.equalsIgnoreCase("SCH-W899")) {
                if (level > 100) {
                    level /= 10;
                }
                batteryLevel = level;
            } else if (model.trim().toUpperCase().contains("XT702")) {
                batteryLevel = level;
            } else if (model.trim().toUpperCase().contains("XT907")) {
                batteryLevel = level;
            } else if (model.trim().toUpperCase().contains("XT1058")) {
                batteryLevel = level;
            } else if (model.trim().toUpperCase().contains("XT1080")) {
                batteryLevel = level;
            } else {
                batteryLevel = level;
                if (Build.MANUFACTURER.equalsIgnoreCase("motorola")) {
                    String data;
                    File file = new File("/sys/class/power_supply/battery/charge_counter");
                    batteryLevel = 0;
                    if (file.exists()) {
                        data = readFile(file);
                        if (!TextUtils.isEmpty(data)) {
                            batteryLevel = Integer.parseInt(data);
                        }
                    }
                    if (batteryLevel <= 0) {
                        file = new File("/sys/class/power_supply/battery/capacity");
                        if (file.exists()) {
                            data = readFile(file);
                            if (!TextUtils.isEmpty(data)) {
                                batteryLevel = Integer.parseInt(data);
                            }
                        }
                    }
                    if (batteryLevel > 100 || batteryLevel <= 0) {
                        batteryLevel = level;
                    }
                }
            }
            if (batteryLevel <= 100) {
                return batteryLevel;
            }
            if (plugg != 0 && batteryLevel < 110) {
                return 100;
            }
            while (batteryLevel > 100) {
                batteryLevel /= 10;
            }
            return batteryLevel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batteryLevel;
    }

    private static String readFile(File file) {
        FileInputStream fileInputStream = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] cArr = new char[1024];
            while (true) {
                int read = inputStreamReader.read(cArr);
                if (read >= 0) {
                    parseFile(read, cArr, stringBuilder);
                } else {
                    break;
                }
            }
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    private static void parseFile(int i, char[] cArr, StringBuilder stringBuilder) {
        int i2 = 0;
        while (i2 < i) {
            if (!(cArr[i2] == '\n' || cArr[i2] == '\r')) {
                stringBuilder.append(cArr[i2]);
            }
            i2++;
        }
    }


    public static int getMinuteTimer(double times) {
        return ((int) ((times * 10.0d) % 10.0d)) * 6;
    }

    public static int getHourTimer(double times) {
        return (int) ((times * 10.0d) / 10.0d);
    }


}

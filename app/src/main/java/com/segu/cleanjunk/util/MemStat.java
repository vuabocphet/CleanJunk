package com.segu.cleanjunk.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

public class MemStat {
    private long mTotalMemory;
    private long mUsedMemory;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public MemStat(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        if (am == null) {
            return;
        }
        am.getMemoryInfo(memInfo);
        mTotalMemory = memInfo.totalMem;
        mUsedMemory = memInfo.totalMem - memInfo.availMem;
    }

    public long getTotalMemory() {
        return mTotalMemory;
    }

    public long getUsedMemory() {
        return mUsedMemory;
    }
}

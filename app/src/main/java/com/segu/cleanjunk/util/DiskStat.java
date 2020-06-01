package com.segu.cleanjunk.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class DiskStat {

    private long mExternalBlockSize;
    private long mExternalBlockCount;
    private long mExternalAvailableBlocks;

    private long mInternalBlockSize;
    private long mInternalBlockCount;
    private long mInternalAvailableBlocks;

    public DiskStat() {
        calculateInternalSpace();
        calculateExternalSpace();
    }

    public long getTotalSpace() {
        return mInternalBlockSize * mInternalBlockCount
                + mExternalBlockSize * mExternalBlockCount;
    }

    public long getUsedSpace() {
        return mInternalBlockSize * (mInternalBlockCount - mInternalAvailableBlocks)
                + mExternalBlockSize * (mExternalBlockCount - mExternalAvailableBlocks);
    }

    public long getUsableSpace() {
        return mInternalBlockSize * mInternalAvailableBlocks
                + mExternalBlockSize * mExternalAvailableBlocks;
    }

    private void calculateExternalSpace() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            mExternalBlockSize = sf.getBlockSizeLong();
            mExternalBlockCount = sf.getBlockCountLong();
            mExternalAvailableBlocks = sf.getAvailableBlocksLong();

        }
    }

    private void calculateInternalSpace() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        mInternalBlockSize = sf.getBlockSizeLong();
        mInternalBlockCount = sf.getBlockCountLong();
        mInternalAvailableBlocks = sf.getAvailableBlocksLong();

    }
}

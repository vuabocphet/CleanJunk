package com.segu.cleanjunk.model;


import com.segu.cleanjunk.R;

import com.segu.cleanjunk.MyApplication;

import java.util.ArrayList;

/**
 * Created by mazhuang on 16/1/14.
 */
public class JunkInfo implements Comparable<JunkInfo> {
    public String name;
    public long mSize;
    public String mPackageName;
    public String mPath;
    public ArrayList<JunkInfo> mChildren = new ArrayList<>();
    public boolean mIsVisible = false;
    public boolean mIsChild = true;

    @Override
    public int compareTo(JunkInfo another) {
        String top = MyApplication.getInstance().getString(R.string.system_cache);

        if (this.name != null && this.name.equals(top)) {
            return 1;
        }

        if (another.name != null && another.name.equals(top)) {
            return -1;
        }

        if (this.mSize > another.mSize) {
            return 1;
        } else if (this.mSize < another.mSize) {
            return -1;
        } else {
            return 0;
        }
    }
}

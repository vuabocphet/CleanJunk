package com.appplatform.commons.task;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangSon on 1/22/2018.
 */

public class CacheManager {
    private List<String> mCachedList;
    private StringBuilder mCachedListBuilder;
    private Context mContext;
    private int mDefaultCachedItemsRes;
    private String mKey;

    public CacheManager(Context context, String key) {
        this(context, key, -1);
    }

    public CacheManager(Context context, String key, int defaultCachedItemsRes) {
        this.mContext = context;
        this.mKey = key;
        this.mDefaultCachedItemsRes = defaultCachedItemsRes;
        this.load();
    }

    public void add(String packageName) {
        int size = this.mCachedList.size();
        int i = 0;
        while (i < size) {
            if (!this.mCachedList.get(i).equalsIgnoreCase(packageName)) {
                i++;
            } else {
                return;
            }
        }
        this.mCachedList.add(packageName);
        addToStringCache(packageName);
        save();
    }

    private void addToStringCache(String packageName) {
        if (this.mCachedListBuilder.length() == 0) {
            this.mCachedListBuilder.append(packageName);
            return;
        }
        this.mCachedListBuilder.append("|");
        this.mCachedListBuilder.append(packageName);
    }

    public void add(List<String> packageNameList) {
        int size = packageNameList.size();
        for (int i = 0; i < size; i++) {
            String packageName = packageNameList.get(i);
            boolean found = false;
            for (int j = 0; j < this.mCachedList.size(); j++) {
                if (packageName.equalsIgnoreCase(this.mCachedList.get(j))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.mCachedList.add(packageName);
                addToStringCache(packageName);
            }
        }
        save();
    }

    public void update(List<String> packageNameList) {
        this.mCachedList.clear();
        this.mCachedListBuilder.delete(0, this.mCachedListBuilder.length());
        this.mContext.getSharedPreferences("ignore_list_pref", Context.MODE_PRIVATE).edit().remove(this.mKey).apply();
        add(packageNameList);
    }

    public void save() {
        this.mContext.getSharedPreferences("ignore_list_pref", Context.MODE_PRIVATE).edit().putString(this.mKey, this.mCachedListBuilder.toString()).apply();
    }

    public void load() {
        String defaultValue = "";
        if (this.mDefaultCachedItemsRes != -1) {
            defaultValue = join(this.mContext.getResources().getStringArray(this.mDefaultCachedItemsRes));
        }
        String ignoreListStr = this.mContext.getSharedPreferences("ignore_list_pref", Context.MODE_PRIVATE).getString(this.mKey, defaultValue);
        this.mCachedList = new ArrayList<>();
        this.mCachedListBuilder = new StringBuilder();
        if (!ignoreListStr.equals("")) {
            String[] ignores = ignoreListStr.split("\\|");
            for (String ignore : ignores) {
                if (ignore.length() != 0 && ignore.length() < 100) {
                    if (this.mCachedList != null) {
                        this.mCachedList.add(ignore);
                    }
                    addToStringCache(ignore);
                }
            }
        }
    }

    public void remove(String packageName) {
        this.mCachedListBuilder.delete(0, this.mCachedListBuilder.length());
        int i = 0;
        while (i < this.mCachedList.size()) {
            if (this.mCachedList.get(i).equalsIgnoreCase(packageName)) {
                this.mCachedList.remove(i);
                i--;
            } else {
                addToStringCache(this.mCachedList.get(i));
            }
            i++;
        }
        save();
    }

    public void remove(List<String> packageNameList) {
        this.mCachedListBuilder.delete(0, this.mCachedListBuilder.length());
        int i = 0;
        while (i < this.mCachedList.size()) {
            boolean found = false;
            for (int j = 0; j < packageNameList.size(); j++) {
                if (this.mCachedList.get(i).equalsIgnoreCase(packageNameList.get(j))) {
                    found = true;
                    packageNameList.remove(j);
                    break;
                }
            }
            if (found) {
                this.mCachedList.remove(i);
                i--;
            } else {
                addToStringCache(this.mCachedList.get(i));
            }
            i++;
        }
        save();
    }

    public void removeAll() {
        this.mCachedList.clear();
        this.mCachedListBuilder.delete(0, this.mCachedListBuilder.length());
        save();
    }

    public boolean exists(String packageName) {
        for (int i = 0; i < this.mCachedList.size(); i++) {
            if (this.mCachedList.get(i).equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getCachedList() {
        return this.mCachedList;
    }

    public String[] getCachedStringArray() {
        return this.mCachedList.toArray(new String[this.mCachedList.size()]);
    }

    private String join(String[] stringArray) {
        if (stringArray == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int len = stringArray.length;
        for (int i = 0; i < len; i++) {
            sb.append(stringArray[i]);
            if (i < len - 1) {
                sb.append("|");
            }
        }
        return sb.toString();
    }
}
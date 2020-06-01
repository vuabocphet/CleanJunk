package com.appplatform.commons.task;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppInfo {
    private static Integer pendingStats = 0;
    private long appCacheSize;
    private long appCodeSize;
    private long appDataSize;
    private Bitmap appIconBitmap;
    private long appLastModified;
    private String appLastModifiedStr;
    private String appName;
    private long appSize;
    private String appSizeStr;
    private int appVersion;
    private String appVersionName;
    private boolean defaultAndroidIcon;
    private int highCpuUsageTime;
    private boolean isCurrentApp;
    private ArrayList<Integer> mPidList;
    private int mRamSize;
    private String packageName;
    private String path;
    private boolean selected;
    private Drawable iconDrawable;
    private float batteryUse;
    public AppInfo(String packageName) {
        this.mRamSize = 0;
        this.mPidList = new ArrayList();
        this.packageName = packageName;
        this.appName = "";
        this.appVersionName = "";
        this.appIconBitmap = null;
        this.defaultAndroidIcon = false;
        this.appSize = 0;
        this.appSizeStr = "";
        this.appLastModified = 0;
        this.appLastModifiedStr = "";
        this.path = "";
        this.selected = true;
        this.highCpuUsageTime = 0;
        this.isCurrentApp = false;
    }

    public AppInfo(Context context, PackageInfo packageInfo, AppIconCached iconCache, boolean isSelected) {
        this(packageInfo.packageName);
        PackageManager packageManager = context.getPackageManager();
        AppIconCached.CacheEntry entry = iconCache.getCacheEntry(this.packageName);
        if (entry == null) {
            this.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString().trim();
            int mIconSize = 72;
            Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager);
            if(drawable == null){
                drawable = ContextCompat.getDrawable(context, android.R.drawable.sym_def_app_icon);
            }
            setIconDrawable(drawable);
            setAppIcon(drawable, mIconSize, mIconSize);
            iconCache.addToCache(this.packageName, this.appName, getAppIcon(),drawable);
        } else {
            this.appName = entry.title;
            setAppIcon(entry.icon);
            setIconDrawable(entry.iconDrawable);
        }
        this.appVersionName = packageInfo.versionName;
        this.path = packageInfo.applicationInfo.sourceDir;
        File f = new File(this.path);
        setAppSize(f.length());
        setAppLastModified(f.lastModified());
        this.selected = isSelected;
    }


    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public boolean matches(AppInfo appInfo) {
        return appInfo != null && getPackageName().equals(appInfo.getPackageName()) && getAppVersion() == appInfo.getAppVersion() && getAppVersionName().equals(appInfo.getAppVersionName());
    }

    public String generateUniqueApkFileName() {
        String APK_FILE_EXT = ".apk";
        return getPackageName() + "-" + getAppVersion() + "-" + getAppVersionName() + APK_FILE_EXT;
    }

    public String generateUniqueApzFileName() {
        String ARCHIVED_PROTECTED_APP_FILE_EXT = ".apz";
        return getPackageName() + "-" + getAppVersion() + "-" + getAppVersionName() + ARCHIVED_PROTECTED_APP_FILE_EXT;
    }

    public String getAppNameWithVersion() {
        String appNameWithVersion = this.appName;
        if (this.appVersionName != null) {
            return appNameWithVersion + " " + this.appVersionName;
        }
        return appNameWithVersion;
    }


    public void setBatteryUse(float batteryUse) {
        this.batteryUse = batteryUse;
    }

    public float getBatteryUse() {
        return batteryUse;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = filterAbnormalVersionName(this.packageName, appVersionName);
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public void setAppIcon(Bitmap appIconBitmap) {
        this.appIconBitmap = appIconBitmap;
    }

    public void setAppIcon(Drawable appIcon, int width, int height) {
        this.appIconBitmap = resizeDrawableBitmap(appIcon, width, height, !isDefaultAndroidIcon());
    }

    public Bitmap getAppIcon() {
        return this.appIconBitmap;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
        this.appSizeStr =formatSize(appSize)[0] +formatSize(appSize)[1];
    }

    public long getAppSize() {
        return this.appSize;
    }

    public String getAppSizeStr() {
        return this.appSizeStr;
    }

    public void setAppLastModified(long appLastModified) {
        this.appLastModified = appLastModified;
        this.appLastModifiedStr = formatDate(appLastModified);
    }

    public long getAppLastModified() {
        return this.appLastModified;
    }

    public String getAppLastModifiedStr() {
        return this.appLastModifiedStr;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public int getAppVersion() {
        return this.appVersion;
    }

    public void setDefaultAndroidIcon(boolean defaultAndroidIcon) {
        this.defaultAndroidIcon = defaultAndroidIcon;
    }

    public boolean isDefaultAndroidIcon() {
        return this.defaultAndroidIcon;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    private String filterAbnormalVersionName(String packageName, String versionName) {
        if (!packageName.equals("com.teslacoilsw.flashlight")) {
            return versionName;
        }
        Matcher matcher = Pattern.compile("[0-9\\.]+").matcher(versionName);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return versionName;
    }

    void setPackageStats(PackageStats pStats) {
        this.appDataSize = pStats.dataSize;
        this.appCacheSize = pStats.cacheSize;
        this.appCodeSize = pStats.codeSize;
        setAppSize(getTotalSize());
        synchronized (pendingStats) {
            Integer num = pendingStats;
            pendingStats = pendingStats - 1;
        }
    }

    public long getDataSize() {
        return this.appDataSize;
    }

    public long getCodeSize() {
        return this.appCodeSize;
    }

    public long getCacheSize() {
        return this.appCacheSize;
    }

    public long getTotalSize() {
        return (this.appCacheSize + this.appCodeSize) + this.appDataSize;
    }

    public static boolean hadFinishedStats() {
        return pendingStats <= 0;
    }

    public int getMemoryUsage() {
        return this.mRamSize;
    }

    public void setMemoryUsage(int ramSize) {
        this.mRamSize = ramSize;
    }

    public void addPid(int pid) {
        synchronized (this) {
            this.mPidList.add(pid);
        }
    }

    public int getHighCpuUsageTime() {
        return this.highCpuUsageTime;
    }

    public void setHighCpuUsageTime(int time) {
        this.highCpuUsageTime = time;
    }

    public ArrayList<Integer> getPidList() {
        return this.mPidList;
    }

    public int[] getPids() {
        int[] pids;
        synchronized (this) {
            ArrayList<Integer> list = getPidList();
            pids = new int[list.size()];
            for (int i = 0; i < pids.length; i++) {
                pids[i] = list.get(i);
            }
        }
        return pids;
    }

    public boolean isCurrentApp() {
        return this.isCurrentApp;
    }

    public void markAsCurrentApp() {
        this.isCurrentApp = true;
    }


    public static String[] formatSize(long size) {
        String[] result = {"0", "KB"};
        if (size >= 1073741824) {
            return new String[]{formatDecimal(((double) size) / ((double) 1073741824), 1), "GB"};
        }
        if (size >= 1048576) {
            return new String[]{formatDecimal(((double) size) / ((double) 1048576), 1), "MB"};
        }
        if (size < 1048576) {
            return new String[]{formatDecimal(((double) size) / ((double) 1024), 1), "KB"};
        }
        return result;
    }
    public static String formatDecimal(double d, int scale) {
        return new BigDecimal(d).setScale(scale, 5).toString();
    }

    public static String formatDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(time));
    }
    public static Bitmap resizeDrawableBitmap(Drawable drawable, int width, int height, boolean recycle) {
        Bitmap bitmap = drawableToBitmap(drawable);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        if (bitmap != resizedBitmap && recycle) {
            bitmap.recycle();
        }
        return resizedBitmap;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_4444 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()));
        drawable.draw(canvas);
        return bitmap;
    }

}

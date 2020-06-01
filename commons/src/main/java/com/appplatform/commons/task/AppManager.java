package com.appplatform.commons.task;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangSon on 1/22/2018.
 */

public class AppManager {
    public static List<AppInfo> getInstalledAppList(Context context, AppIconCached iconCache, boolean isSelected, boolean ignoreCurrentApp) {
        new Intent("android.intent.action.MAIN", null).addCategory("android.intent.category.LAUNCHER");
        List<PackageInfo> list = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        List<AppInfo> appList = new ArrayList();
        String currentPackageName = context.getPackageName();
        for (PackageInfo pi : list) {
            if (!ignoreCurrentApp || pi.packageName == null || !pi.packageName.equals(currentPackageName)) {
                if (!(((pi.applicationInfo.flags & 1) != 0) && (pi.applicationInfo.sourceDir.startsWith("/system/") || pi.applicationInfo.sourceDir.startsWith("/vendor/")))) {
                    boolean found = false;
                    for (int i = 0; i < appList.size(); i++) {
                        if (appList.get(i).getPackageName().equals(pi.packageName)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        try {
                            appList.add(new AppInfo(context, pi, iconCache, isSelected));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return appList;
    }

    public static List<AppInfo> getAllAppList(Context context, AppIconCached iconCache, boolean isSelected, boolean ignoreCurrentApp, OnTaskLoadRunningAppListener listener) {
        List<PackageInfo> list = context.getPackageManager().getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> riList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        List<AppInfo> appList = new ArrayList<>();
        String currentPackageName = context.getPackageName();
        for (PackageInfo pi : list) {
            if (!ignoreCurrentApp || pi.packageName == null || !pi.packageName.equals(currentPackageName)) {
                boolean found = false;
                for (ResolveInfo ri : riList) {
                    if (pi.packageName != null && pi.packageName.equals(ri.activityInfo.packageName)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    found = false;
                    for (int i = 0; i < appList.size(); i++) {
                        if (appList.get(i).getPackageName().equals(pi.packageName)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        try {
                            AppInfo appInfo = new AppInfo(context, pi, iconCache, isSelected);
                            boolean isAdded = appList.add(appInfo);
                            if (isAdded) {
                                listener.onTaskLoading(appInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return appList;
    }

    public static void uninstallPackage(Context context, String packageName) {
        context.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + packageName)));
    }

    public static String getClassNameByPackageName(Context context, String packageName) {
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        String className = "";
        for (ResolveInfo ri : context.getPackageManager().queryIntentActivities(mainIntent, 0)) {
            if (ri.activityInfo.packageName.equals(packageName)) {
                return ri.activityInfo.name;
            }
        }
        return className;
    }

    public static void launchApp(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setClassName(packageName, getClassNameByPackageName(context, packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static int getAndroidVersion() {
        return Integer.parseInt(Build.VERSION.SDK);
    }

    public static boolean isGEAndroidV22() {
        return getAndroidVersion() >= 8;
    }

    public static boolean isAndroidV22() {
        return getAndroidVersion() == 8;
    }

    public static boolean isAndroidV23() {
        return getAndroidVersion() == 9;
    }

    public static boolean isGEAndroidV23() {
        return getAndroidVersion() >= 9;
    }

    public static boolean isGEAndroidV30() {
        return getAndroidVersion() >= 11;
    }

    public static void showApplicationInfo(Context context, String packageName) {
        Intent intent;
        if (isGEAndroidV23()) {
            intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        } else {
            intent = new Intent("android.intent.action.VIEW");
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
        }
        if (isAndroidV22()) {
            intent.putExtra("pkg", packageName);
        } else if (isGEAndroidV23()) {
            intent.setData(Uri.parse("package:" + packageName));
        } else {
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        context.startActivity(intent);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isGoogleMarketInstalled(Context context) {
        return isAppInstalled(context, "com.android.vending");
    }

    public static boolean isServiceExists(Context context, ComponentName component) {
        try {
            return context.getPackageManager().getServiceInfo(component, 0) != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}

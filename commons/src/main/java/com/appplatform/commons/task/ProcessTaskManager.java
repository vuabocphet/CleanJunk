package com.appplatform.commons.task;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.appplatform.commons.AppPlatformManager;
import com.appplatform.commons.utils.MemoryManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProcessTaskManager {
    private static String[] DefaultIgnoreList = new String[]{"com.android.providers.im", "com.android.htcdialer", "com.android.alarmclock", "com.nuance.android.vsuite.vsuiteapp", "com.spritemobile.backup.semc2"};
    private static Object mLocker = new Object();
    private ActivityManager activityManager;
    private List<ResolveInfo> launcherApps = new ArrayList<>();
    private Context context;
    private PackageManager packageManager;
    private AppPlatformManager appPlatformManager;

    public interface AutoKillEventListener {
        void showAutoKillNotice(int i);
    }


    static class ProcessInfo {
        public int pid;
        public String pkgName;

        public ProcessInfo(int pid, String pkgName) {
            this.pid = pid;
            this.pkgName = pkgName;
        }
    }

    public interface TaskListEventListener {
        void onTaskListed(AppInfo appInfo, int i);
    }

    public ProcessTaskManager(Context context) {
        AppPlatformManager.init(context);
        this.appPlatformManager = AppPlatformManager.getInstance();
        this.context = context.getApplicationContext();
        this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        this.packageManager = context.getPackageManager();
    }

    public static ResolveInfo getResolveInfo(String packageName, List<ResolveInfo> launcherApps) {
        for (ResolveInfo ri : launcherApps) {
            if (packageName.equals(ri.activityInfo.packageName)) {
                return ri;
            }
        }
        return null;
    }

    public static List<ResolveInfo> getRunningTaskList(Context context) {
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> riList = pm.queryIntentActivities(mainIntent, 0);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Map<String, ResolveInfo> appList = new HashMap<>();
        List<ProcessInfo> procList = getProcessInfoList(context);
        if (procList == null || procList.size() == 0) {
            return new ArrayList<>(appList.values());
        }
        String topPackageName = getTopAppOfStack(activityManager);
        for (ProcessInfo appProcInfo : procList) {
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(appProcInfo.pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
                if (appInfo != null && (topPackageName == null || appInfo.packageName == null || !topPackageName.equals(appInfo.packageName))) {
                    ResolveInfo resolveInfo = getResolveInfo(appInfo.packageName, riList);
                    if (resolveInfo != null) {
                        appList.put(appInfo.packageName, resolveInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(appList.values());
    }

    public static List<ProcessInfo> getProcessInfoList(Context context) {
        if (Build.VERSION.SDK_INT > 25) {
            return getRunningProcessWithStatsManager(context);
        }
        return getRunningProcessInfo(context);
    }

    private static List<ProcessInfo> getRunningProcessWithStatsManager(Context context) {
        List<String> packageList = getRunningAppWithStatsManager(context);
        List<ProcessInfo> pkgList = new ArrayList<>();
        for (String pkg : packageList) {
            pkgList.add(new ProcessInfo(NotificationManagerCompat.IMPORTANCE_UNSPECIFIED, pkg));
        }
        return pkgList;
    }

    @TargetApi(23)
    private static List<String> getRunningAppWithStatsManager(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<String> result = new ArrayList<>();

        long statt = calendar.getTimeInMillis() - 259200000;
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        if (usageStatsManager == null) return result;
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(0, statt, calendar.getTimeInMillis());

        if (!(queryUsageStats == null || queryUsageStats.isEmpty())) {
            for (UsageStats usageStats : queryUsageStats) {
                if (!context.getPackageName().equals(usageStats.getPackageName()) && checkIsUserApp(context, usageStats.getPackageName())) {
                    if (usageStatsManager.isAppInactive(usageStats.getPackageName())) {
                        Log.e("tag", "Not active " + usageStats.getPackageName());
                    } else {
                        if (!result.contains(usageStats.getPackageName())) {
                            result.add(usageStats.getPackageName());
                        }
                        Log.e("tag", "Active " + usageStats.getPackageName());
                    }
                }
            }
        }
        return result;
    }

    private static boolean checkIsUserApp(Context context, String pname) {
        boolean z = false;
        try {
            z = isUserApp(context.getPackageManager().getPackageInfo(pname, 0));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("tag", "checkIsUserApp: " + z);
        return z;
    }

    private static boolean isSystemApp(PackageInfo pInfo) {
        return (pInfo.applicationInfo.flags & 1) != 0;
    }

    private static boolean isSystemUpdateApp(PackageInfo pInfo) {
        return (pInfo.applicationInfo.flags & 128) != 0;
    }

    private static boolean isUserApp(PackageInfo pInfo) {
        return !isSystemApp(pInfo) && !isSystemUpdateApp(pInfo);
    }

    public static List<ProcessInfo> getRunningProcessInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        ActivityManager localActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> localList1 = null;
        if (localActivityManager != null) {
            localList1 = localActivityManager.getRunningAppProcesses();
        }
        int i2 = 0;
        while (i2 < Integer.MAX_VALUE) {
            if (localList1 == null || i2 >= localList1.size()) {
                List<ActivityManager.RunningServiceInfo> localList2 = localActivityManager.getRunningServices(Integer.MAX_VALUE);
                if (localList2 != null) {
                    for (ActivityManager.RunningServiceInfo localRunningServiceInfo : localList2) {
                        int i1 = localRunningServiceInfo.pid;
                        AppInfo appInfo = new AppInfo(localRunningServiceInfo.service.getPackageName());
                        appInfo.addPid(i1);
                        list.add(appInfo);
                    }
                }
                return convertToProcessInfos(list);
            }
            AppInfo appInfo = new AppInfo((localList1.get(i2)).processName);
            appInfo.addPid((localList1.get(i2)).pid);
            list.add(appInfo);
            i2++;
        }
        return convertToProcessInfos(list);
    }

    private static List<ProcessInfo> convertToProcessInfos(List<AppInfo> infos) {
        List<ProcessInfo> processInfos = new ArrayList<>();
        for (AppInfo appInfo : infos) {
            if (!(appInfo.getPidList() == null || appInfo.getPidList().isEmpty())) {
                processInfos.add(new ProcessInfo(appInfo.getPidList().get(0), appInfo.getPackageName()));
            }
        }
        return processInfos;
    }

    public static String getTopAppOfStack(Context context) {
        return getTopAppOfStack((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
    }

    private static String getTopAppOfStack(ActivityManager activityManager) {
        List<RecentTaskInfo> recentTaskList = activityManager.getRecentTasks(1, 2);
        if (recentTaskList.size() <= 0) {
            return null;
        }
        Intent intent = recentTaskList.get(0).baseIntent;
        if (intent == null) {
            return null;
        }
        ComponentName cn = intent.getComponent();
        if (cn != null) {
            return cn.getPackageName();
        }
        return null;
    }

    public static boolean isAppAtTopOfStack(Context context, String pkgName) {
        String topPackageName = getTopAppOfStack(context);
        return topPackageName != null && topPackageName.equals(pkgName);
    }

    public static void killTask(Context context, String packageName) {
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(packageName);
        CpuUsageMonitor.removeFromHighCpuUsageList(context, packageName);
    }


    public static void autoKill(Context context, CacheManager blackList, boolean enableNotice, long delayTime, AutoKillEventListener autoKillEventListener) {
        List<String> packageNames = blackList.getCachedList();
        int nKilled = packageNames.size();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean killSelf = false;
        for (String pkgName : packageNames) {
            if (pkgName.equals(context.getPackageName())) {
                killSelf = true;
            } else {
                activityManager.killBackgroundProcesses(pkgName);
            }
        }
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (enableNotice) {
            autoKillEventListener.showAutoKillNotice(nKilled);
        }
    }


    public void init(Context context) {
        this.context = context;
    }

    public void initLauncherApps() {
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        this.launcherApps = this.context.getPackageManager().queryIntentActivities(mainIntent, 0);
    }

    public boolean isLauncherApp(String packageName) {
        return getResolveInfo(packageName, this.launcherApps) != null;
    }

    public boolean isDefaultIgnoreTask(String packageName) {
        for (String equalsIgnoreCase : DefaultIgnoreList) {
            if (equalsIgnoreCase.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrentTask(String packageName) {
        return packageName.equals(this.context.getPackageName());
    }

    public List<String> getRunningTaskList2(Context context, boolean ignoreForeGround) {
        List<String> arrayList;
        synchronized (mLocker) {
            initLauncherApps();
            Map<String, String> appList = new HashMap<>();
            List<ProcessInfo> procList = getProcessInfoList(context);
            if (procList == null || procList.size() == 0) {
                arrayList = new ArrayList<>(appList.keySet());
            } else {
                String topPackageName = getTopAppOfStack(this.activityManager);
                for (ProcessInfo appProcInfo : procList) {
                    try {
                        ApplicationInfo appInfo = this.packageManager.getApplicationInfo(appProcInfo.pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
                        if (appInfo != null) {
                            if (topPackageName != null) {
                                if (appInfo.packageName != null && topPackageName.equals(appInfo.packageName)) {
                                }
                            }
                            if (!(!isLauncherApp(appInfo.packageName) || isDefaultIgnoreTask(appInfo.packageName) || this.appPlatformManager.getIgnoreList().exists(appInfo.packageName))) {
                                appList.put(appInfo.packageName, "");
                            }
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                arrayList = new ArrayList<>(appList.keySet());
            }
        }
        return arrayList;
    }

    public void listRunningTasks(TaskListEventListener taskListEventListener, boolean ignoreForeGround, AppIconCached iconCache) {
        synchronized (mLocker) {
            List<AppInfo> appInfoList = new ArrayList<>();
            List<ProcessInfo> processList = getProcessInfoList(this.context);
            Map<String, Integer> packageList = new HashMap<>();
            initLauncherApps();
            String home = getPackageNameOfDefaultHome();
            PackageManager pm = this.context.getPackageManager();
            String currentPackageName = this.context.getPackageName();
            for (ProcessInfo processInfo : processList) {
                String packageName = processInfo.pkgName;
                if (packageName == null) {
                    continue;
                }
                if (((ignoreForeGround && packageName.equals(currentPackageName)) || !isLauncherApp(packageName))) {
                    continue;
                }
                try {
                    int pid = processInfo.pid;
                    int ramSize = getMemoryUsage(new int[]{pid});
                    if (ramSize <= 0) {
                        continue;
                    }
                    AppInfo appInfo;
                    if (packageList.get(packageName) != null) {
                        int postDup = packageList.get(packageName);
                        appInfo = appInfoList.get(postDup);
                        appInfo.addPid(pid);
                        appInfo.setMemoryUsage(ramSize);
                        appInfoList.set(postDup, appInfo);
                        continue;
                    }
                    appInfo = new AppInfo(this.context, pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS), iconCache, true);
                    if (home != null && home.equals(packageName)) {
                        appInfo.setSelected(false);
                    }
                    if (appInfo.getPackageName().equals(currentPackageName)) {
                        appInfo.markAsCurrentApp();
                    }
                    appInfo.setHighCpuUsageTime(CpuUsageMonitor.getHighCpuUsageTime(this.context, packageName));
                    appInfo.addPid(pid);
                    appInfo.setMemoryUsage(ramSize);
                    appInfoList.add(appInfo);
                    packageList.put(packageName, appInfoList.size() - 1);
                    taskListEventListener.onTaskListed(appInfo, processList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            packageList.clear();
            processList.clear();
        }
    }

    public int getMemoryUsage(int[] pid) {
        Debug.MemoryInfo[] processMemoryInfo = this.activityManager.getProcessMemoryInfo(pid);
        int memoryUsage = processMemoryInfo[0].getTotalPss();
        for (Debug.MemoryInfo aProcessMemoryInfo : processMemoryInfo) {
            memoryUsage += aProcessMemoryInfo.getTotalPss();
        }
        if (Build.VERSION.SDK_INT <= 25) {
            if (memoryUsage == 0) {
                memoryUsage = getRdRamSize();
            }
        } else {
            memoryUsage = getRdRamSize();
        }
        return memoryUsage;
    }

    private int getRdRamSize() {
        int baseRam = (int) (Math.min(((MemoryManager.getTotalRamSize() * 2) / 3) / 10, 200000) / 3);
        return baseRam + new Random().nextInt(baseRam * 2);
    }


    private String getPackageNameOfDefaultHome() {
        PackageManager pm = this.context.getPackageManager();
        List<IntentFilter> intentList = new ArrayList<>();
        List<ComponentName> cnList = new ArrayList<>();
        pm.getPreferredActivities(intentList, cnList, null);
        for (int i = 0; i < cnList.size(); i++) {
            IntentFilter dhIF = intentList.get(i);
            if (dhIF.hasAction("android.intent.action.MAIN") && dhIF.hasCategory("android.intent.category.HOME") && dhIF.hasCategory("android.intent.category.DEFAULT")) {
                return cnList.get(i).getPackageName();
            }
        }
        return null;
    }

    public int autoKillTasks(Context context) {
        String home = getPackageNameOfDefaultHome();
        List<String> taskList = getRunningTaskList2(context, false);
        int nKilled = 0;
        for (int i = 0; i < taskList.size(); i++) {
            String packageName = taskList.get(i);
            if (!((home != null && home.equals(packageName)) || isCurrentTask(packageName))) {
                killTask(this.context, taskList.get(i));
                nKilled++;
            }
        }
        System.gc();
        return nKilled;
    }

    public String getDebugInfo(Context context) {
        StringBuilder sb = new StringBuilder();
        List<String> taskList = getRunningTaskList2(context, false);
        for (int i = 0; i < taskList.size(); i++) {
            sb.append(taskList.get(i)).append("\n");
        }
        return sb.toString();
    }


}

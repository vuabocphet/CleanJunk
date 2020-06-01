package com.appplatform.commons.task;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangSon on 1/11/2018.
 */

public class TaskHandler {
    private static TaskHandler instance;
    private AppIconCached appsIconCached;
    private LoadRunningApps loadRunningApps;
    private LoadListInstalledApps loadListInstalledApps;

    public static synchronized TaskHandler get() {
        if (instance == null) {
            instance = new TaskHandler();
        }
        return instance;
    }

    private TaskHandler() {
        this.appsIconCached = new AppIconCached();
    }


    public AppIconCached getAppsIconCached() {
        return appsIconCached;
    }


    public void releaseTask() {
        if (loadRunningApps != null) {
            loadRunningApps.cancel(true);
        }
        if (loadListInstalledApps != null) {
            loadListInstalledApps.cancel(true);
        }
    }


    public void loadListInstalledApps(Context context, OnTaskLoadRunningAppListener listener) {
        if (loadListInstalledApps != null) {
            loadListInstalledApps.cancel(true);
        }
        loadListInstalledApps = new LoadListInstalledApps(context, getAppsIconCached(), listener);
        loadListInstalledApps.execute();
    }


    public void loadRunningApps(Context context, OnTaskLoadRunningAppListener listener) {
        if (loadRunningApps != null) {
            loadRunningApps.cancel(true);
        }
        loadRunningApps = new LoadRunningApps(context, getAppsIconCached(), listener);
        loadRunningApps.execute();
    }


    public void killApps(final Context context, final List<AppInfo> appInfos) {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (AppInfo appInfo : appInfos) {
                        ProcessTaskManager.killTask(context, appInfo.getPackageName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static class LoadRunningApps extends AsyncTask<Void, Void, Void> implements ProcessTaskManager.TaskListEventListener {
        private final WeakReference<Context> contextReference;
        private AppIconCached appIconCached;
        private List<AppInfo> appInfos = new ArrayList<>(0);
        private OnTaskLoadRunningAppListener listener;

        private LoadRunningApps(Context context, AppIconCached appIconCached, OnTaskLoadRunningAppListener listener) {
            this.contextReference = new WeakReference<>(context);
            this.appIconCached = appIconCached;
            this.listener = listener;
        }

        @Override
        public void onTaskListed(AppInfo appInfo, int i) {
            if (listener != null) {
                listener.onTaskLoading(appInfo, i);
            }
            if (appInfos.contains(appInfo)) {
                return;
            }
            appInfos.add(appInfo);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ProcessTaskManager processTaskManager = new ProcessTaskManager(this.contextReference.get());
                processTaskManager.listRunningTasks(this, true, appIconCached);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (listener != null) {
                listener.onTaskLoaded(appInfos);
            }
        }
    }


    static class LoadListInstalledApps extends AsyncTask<Void, Void, List<AppInfo>> {
        private final WeakReference<Context> contextReference;
        private AppIconCached appIconCached;
        private OnTaskLoadRunningAppListener listener;

        private LoadListInstalledApps(Context context, AppIconCached appIconCached, OnTaskLoadRunningAppListener listener) {
            this.contextReference = new WeakReference<>(context);
            this.appIconCached = appIconCached;
            this.listener = listener;
        }

        protected List<AppInfo> doInBackground(Void... params) {
            return AppManager.getAllAppList(this.contextReference.get(), appIconCached, false, true, listener);
        }

        protected void onPostExecute(List<AppInfo> appList) {
            if (listener != null) {
                listener.onTaskLoaded(appList);
            }
        }
    }


}

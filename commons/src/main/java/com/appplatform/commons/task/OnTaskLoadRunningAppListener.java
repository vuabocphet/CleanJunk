package com.appplatform.commons.task;

import java.util.List;

/**
 * Created by HoangSon on 1/11/2018.
 */

public interface OnTaskLoadRunningAppListener {
    void onTaskLoading(AppInfo appInfo, int sizeList);
    void onTaskLoading(AppInfo appInfo);
    void onTaskLoaded(List<AppInfo> list);
}

package com.appplatform.commons.task;

import java.util.List;

/**
 * Created by HoangSon on 1/11/2018.
 */

public abstract class  AbOnTaskLoadRunningAppListener implements OnTaskLoadRunningAppListener{
    @Override
    public void onTaskLoaded(List<AppInfo> list) {

    }

    @Override
    public void onTaskLoading(AppInfo appInfo) {

    }

    @Override
    public void onTaskLoading(AppInfo appInfo, int sizeList) {

    }
}

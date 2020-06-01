package com.segu.cleanjunk.callback;

import com.segu.cleanjunk.model.JunkInfo;

import java.util.ArrayList;

/**
 * Created by mazhuang on 16/1/14.
 */
public interface IScanCallback {
    void onBegin();

    void onProgress(JunkInfo info);

    void onFinish(ArrayList<JunkInfo> children);
}

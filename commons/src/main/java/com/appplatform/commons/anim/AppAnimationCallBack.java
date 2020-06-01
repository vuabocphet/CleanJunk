package com.appplatform.commons.anim;

public interface AppAnimationCallBack {
    void onAnimationCreate();

    void onAnimationFinish();

    interface AnimAdTask {
        void onAdClick();
        void doNextTask();
    }

}

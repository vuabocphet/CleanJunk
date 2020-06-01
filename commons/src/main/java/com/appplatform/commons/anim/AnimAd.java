package com.appplatform.commons.anim;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AnimAd {
    void loadAd(AppAnimationCallBack.AnimAdTask animAdTask);

    void showAd();

    boolean isAdLoaded();

    @IntDef({AdFlow.NONE, AdFlow.AFTER, AdFlow.BEFORE, AdFlow.BOTH})
    @Retention(RetentionPolicy.SOURCE)
    @interface AdFlow {
        int NONE = 0;
        int AFTER = 1;
        int BEFORE = 2;
        int BOTH = 3;
    }
}

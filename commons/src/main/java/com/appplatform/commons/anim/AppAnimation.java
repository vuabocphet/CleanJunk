package com.appplatform.commons.anim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;


public abstract class AppAnimation {
    private Context mContext;
    private View mAnimView;
    private Intent mIntent;
    private AppAnimationCallBack mAnimationCallBack;
    private Handler mHandler;

    public AppAnimation(Context context) {
        this.mContext = context;
    }

    @CallSuper
    public void onCreate(Activity activity) {
        setIntent(activity.getIntent());
        this.mHandler = new Handler();
        if (mAnimationCallBack != null) {
            mAnimationCallBack.onAnimationCreate();
        }
        setContentView();
    }

    public void post(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        }
    }

    public void postDelayed(Runnable r, long delayMillis) {
        if (mHandler != null) {
            mHandler.postDelayed(r, delayMillis);
        }
    }

    protected void removeCallbacksAndMessages() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void finish() {
        if (mAnimationCallBack != null) {
            mAnimationCallBack.onAnimationFinish();
        }
    }

    public void onNewIntent(Intent intent) {

    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onDestroy() {

    }

    public void onBackPressed() {

    }


    public void setAppAnimationCallBack(AppAnimationCallBack animationCallBack) {
        this.mAnimationCallBack = animationCallBack;
    }

    public void setContentView() {
        this.mAnimView = LayoutInflater.from(mContext).inflate(getLayoutId(), null, false);
    }

    @LayoutRes
    public abstract int getLayoutId();

    public <T extends View> T findViewById(@IdRes int id) {
        return getDelegate().findViewById(id);
    }

    public View getDelegate() {
        return mAnimView;
    }


    public Context getContext() {
        return mContext;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent newIntent) {
        mIntent = newIntent;
    }
}

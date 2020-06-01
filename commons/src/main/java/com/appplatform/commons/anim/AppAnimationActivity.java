package com.appplatform.commons.anim;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public abstract class AppAnimationActivity extends AppCompatActivity implements AppAnimationCallBack {
    private AppAnimation appAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (this.appAnimation != null) {
            this.appAnimation.onNewIntent(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.appAnimation != null) {
            this.appAnimation.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.appAnimation != null) {
            this.appAnimation.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.appAnimation != null) {
            this.appAnimation.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.appAnimation != null) {
            this.appAnimation.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.appAnimation != null) {
            this.appAnimation.onDestroy();
        }
    }

    @Override
    public void onAnimationCreate() {

    }

    @Override
    public void onAnimationFinish() {

    }

    @IdRes
    protected abstract int getRootView();

    @LayoutRes
    protected abstract int getLayoutId();

    protected void setAppAnimation(AppAnimation mAppAnimation) {
        this.appAnimation = mAppAnimation;
        if (this.appAnimation == null) return;

        this.appAnimation.setAppAnimationCallBack(this);
        this.appAnimation.onCreate(this);

        ViewGroup rootView = findViewById(getRootView());
        if (rootView != null) {
            rootView.removeAllViews();
            View animView = getAppAnimation().getDelegate();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            animView.setLayoutParams(layoutParams);
            rootView.addView(animView);
        }
    }

    private AppAnimation getAppAnimation() {
        return appAnimation;
    }

}

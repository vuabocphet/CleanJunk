package com.appplatform.commons.anim;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AppAnimationFragment extends Fragment implements AppAnimationCallBack {
    private AppAnimation appAnimation;

    @IdRes
    protected abstract int getRootView();

    @LayoutRes
    protected abstract int getLayoutId();

    protected AppAnimationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = null;
        try {
            layoutView = inflater.inflate(getLayoutId(), container, false);
            this.bindView(layoutView);
            this.initAnimation(layoutView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return layoutView;
    }


    protected void bindView(View layoutView) {
    }

    protected void initAnimation(View layoutView) {
        if (appAnimation == null || !isAdded()) return;

        this.appAnimation.setAppAnimationCallBack(this);
        this.appAnimation.onCreate(getActivity());
        ViewGroup rootView = layoutView.findViewById(getRootView());
        if (rootView != null) {
            rootView.removeAllViews();
            View animView = getAppAnimation().getDelegate();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            animView.setLayoutParams(layoutParams);

            rootView.addView(animView);
        }
    }

    protected void setAppAnimation(AppAnimation mAppAnimation) {
        this.appAnimation = mAppAnimation;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.appAnimation != null) {
            this.appAnimation.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.appAnimation != null) {
            this.appAnimation.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.appAnimation != null) {
            this.appAnimation.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.appAnimation != null) {
            this.appAnimation.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.appAnimation != null) {
            this.appAnimation.onDestroy();
        }
    }

    private AppAnimation getAppAnimation() {
        return appAnimation;
    }

    @Override
    public void onAnimationCreate() {

    }

    @Override
    public void onAnimationFinish() {

    }
}

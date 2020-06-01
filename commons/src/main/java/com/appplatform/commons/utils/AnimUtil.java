package com.appplatform.commons.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.appplatform.commons.R;


/**
 * Created by HoangSon on 1/15/2018.
 */

public class AnimUtil {
    public static void appear(final View view1, final View view2, long delaytime) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(delaytime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = (float) valueAnimator.getAnimatedValue();
                view1.setAlpha(floatValue);
                view2.setAlpha(1.0f - floatValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();
    }
    public static void appear(final View view1 ,long delaytime,final AnimatorListenerAdapter listenerAdapter) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(delaytime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = (float) valueAnimator.getAnimatedValue();
                view1.setAlpha(floatValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(listenerAdapter!= null){
                    listenerAdapter.onAnimationEnd(animation);
                }
            }
        });
        valueAnimator.start();
    }

    public static void appear(final View view1, final View view2, long delaytime, Animator.AnimatorListener listener) {
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(delaytime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = (float) valueAnimator.getAnimatedValue();
                view1.setAlpha(floatValue);
                view2.setAlpha(1.0f - floatValue);
            }
        });
        valueAnimator.addListener(listener);
        valueAnimator.start();
    }


    public static void disappear(final View view1, long delaytime, Animator.AnimatorListener listener) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view1, "scaleY", 1.0F, 0);
        scaleY.setInterpolator(new AccelerateInterpolator());
        scaleY.setDuration(delaytime);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view1, "scaleX", 1.0F, 0);
        scaleX.setInterpolator(new AccelerateInterpolator());
        scaleX.setDuration(delaytime);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view1, "alpha", 1, 1, 0);
        alpha.setDuration(delaytime);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleY, scaleX, alpha);
        animatorSet.addListener(listener);
        animatorSet.start();
    }


    public static void guideRowAnimTop(final Context context, final View view) {
        int count = ValueAnimator.RESTART;
        long duration = 1000;
        Interpolator decelerateInterpolator = new DecelerateInterpolator();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, (float) (-DisplayUtil.getDensity(context, 25.0f)));
        translationY.setDuration(duration);
        translationY.setInterpolator(decelerateInterpolator);
        translationY.setRepeatCount(count);
        translationY.setRepeatMode(count);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.5f);
        alpha.setDuration(duration);
        alpha.setInterpolator(decelerateInterpolator);
        alpha.setRepeatCount(count);
        alpha.setRepeatMode(count);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.commons_access_finish_slide_arrow_fade_in));
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.commons_access_finish_slide_arrow_fade_out);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        });
        animatorSet.start();
    }

    public static void guideRowAnimBottom(final Context context, final View view) {
        Interpolator linearInterpolator = new LinearInterpolator();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", 0.0f, (float) (-DisplayUtil.getDensity(context, 7.0f)));
        objectAnimator.setDuration(1000);
        objectAnimator.setInterpolator(linearInterpolator);
        objectAnimator.setRepeatCount(5);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.commons_access_finish_slide_arrow_fade_out));
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.commons_access_finish_slide_arrow_fade_in));
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    public static void layoutAnimation(LinearLayout linearLayout, Animation.AnimationListener listener) {
        linearLayout.setLayoutAnimation(getLayoutAnimationController(linearLayout.getContext()));
        linearLayout.setLayoutAnimationListener(listener);
    }

    private static LayoutAnimationController getLayoutAnimationController(Context context) {
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.commons_card_item_anim));
        layoutAnimationController.setOrder(0);
        layoutAnimationController.setDelay(0.2f);
        return layoutAnimationController;
    }
}

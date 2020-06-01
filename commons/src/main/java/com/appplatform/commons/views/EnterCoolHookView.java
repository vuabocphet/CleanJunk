package com.appplatform.commons.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.appplatform.commons.R;


/**
 * Created by HoangSon on 3/20/2018.
 */

public class EnterCoolHookView extends View {
    public interface EnterCoolHookViewListener {
        void onAnimEnd();
    }

    private EnterCoolHookViewListener enterCoolHookViewListener;
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int innerColor;
    private int h;
    private int firstOutColor;
    private int j;
    private int secondOutColor;
    private int l;
    private int m;
    private int n;
    private int o;
    private int p;
    private int q;
    private int r;
    private float s;
    private int t;
    private ValueAnimator valueAnimator;
    private Paint coolHookPaint;
    private PathMeasure w;
    private Path x;
    private Path y;
    private float[] z;

    public EnterCoolHookView(Context context) {
        this(context, null);
    }

    public EnterCoolHookView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.coolHookPaint = new Paint();
        this.coolHookPaint.setAntiAlias(true);
        this.coolHookPaint.setStrokeCap(Paint.Cap.ROUND);
        this.coolHookPaint.setPathEffect(new CornerPathEffect(10.0f));
        this.x = new Path();
        this.y = new Path();
        this.w = new PathMeasure(this.x, false);
        this.z = new float[2];
    }


    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.a = i2;
        this.b = i;
        this.c = this.b >> 1;
        this.d = this.a >> 1;
        int max = Math.max(this.b, this.a);
        this.l = max >> 1;
        this.f = (int) (((float) this.l) * 0.5147541f);
        int i5 = (int) ((0.18360655f * ((float) max)) / 2.0f);
        int i6 = (int) ((0.13114753f * ((float) max)) / 2.0f);
        int i7 = (int) (((float) this.c) + (((float) i5) * 0.2f));
        this.m = i7 - i5;
        this.n = this.d;
        this.o = i7 - (i5 - i6);
        this.p = this.d + i6;
        this.q = i5 + i7;
        this.r = this.d - i6;
        this.x.reset();
        this.x.moveTo((float) this.m, (float) this.n);
        this.x.lineTo((float) this.o, (float) this.p);
        this.x.lineTo((float) this.q, (float) this.r);
        this.w.setPath(this.x, false);
        this.s = this.w.getLength();
        this.t = (int) (((float) max) * 0.019672131f);
    }

    @SuppressLint("ResourceType")
    public void setEnterCoolHookViewListener(EnterCoolHookViewListener enterCoolHookViewListenerVar) {
        this.enterCoolHookViewListener = enterCoolHookViewListenerVar;
        if (this.valueAnimator != null && this.valueAnimator.isRunning()) {
            this.valueAnimator.cancel();
        }
        this.valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.valueAnimator.setDuration(1200);
        this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = (Float) valueAnimator.getAnimatedValue();

                if (floatValue < 0.4f) {
                    floatValue /= 0.4f;
                    innerColor = (((int) (255.0f * floatValue)) << 24) | ContextCompat.getColor(getContext(), R.color.commons_cook_hook_view_inner_circle);
                    e = (int) (floatValue * ((float) f));
                } else {
                    floatValue = (floatValue - 0.4f) / 0.6f;
                    float f = floatValue / 0.7f;
                    float f2 = (floatValue - 0.3f) / 0.7f;
                    if (f <= 1.0f) {
                        h = (int) (((l - f) * f) + f);
                        firstOutColor = 865067517;
                        if (f >= 0.5f) {
                            String firstRippleColorString = getResources().getString(R.color.commons_cook_hook_view_first_ripple).substring(3);
                            int firstRippleColorInt = Integer.parseInt(firstRippleColorString.trim(), 16);

                            firstOutColor = (((int) (((1.0f - f) * 51.0f) * 2.0f)) << 24) | firstRippleColorInt;
                        }
                    }
                    if (f2 >= 0.0f) {
                        j = (int) (((l - f) * f2) + f);
                        secondOutColor = 865067517;
                        if (f2 >= 0.5f) {
                            String secondRippleColorString = getResources().getString(R.color.commons_cook_hook_view_second_ripple).substring(3);
                            int secondRippleColorInt = Integer.parseInt(secondRippleColorString.trim(), 16);

                            secondOutColor = (((int) (((1.0f - f2) * 51.0f) * 2.0f)) << 24) | secondRippleColorInt;
                        }
                    }
                    if (floatValue <= 0.25f) {
                        w.getPosTan((floatValue * s) * 4.0f, z, null);
                    }
                }
                invalidate();
            }
        });
        this.valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                initAnim();
            }
        });
        this.valueAnimator.setInterpolator(new LinearInterpolator());
        this.valueAnimator.start();
    }

    @SuppressLint("ResourceType")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.coolHookPaint.setStyle(Paint.Style.FILL);
        if (this.h != 0) {
            this.coolHookPaint.setColor(this.firstOutColor);
            canvas.drawCircle((float) this.c, (float) this.d, (float) this.h, this.coolHookPaint);
        }
        if (this.j != 0) {
            this.coolHookPaint.setColor(this.secondOutColor);
            canvas.drawCircle((float) this.c, (float) this.d, (float) this.j, this.coolHookPaint);
        }
        this.coolHookPaint.setColor(this.innerColor);
        canvas.drawCircle((float) this.c, (float) this.d, (float) this.e, this.coolHookPaint);
        if (this.z[0] != 0.0f && this.z[1] != 0.0f) {
            this.y.reset();
            this.y.moveTo((float) this.m, (float) this.n);
            if (this.z[0] > ((float) this.o)) {
                this.y.lineTo((float) this.o, (float) this.p);
            }
            this.y.lineTo(this.z[0], this.z[1]);
            this.coolHookPaint.setColor(Color.parseColor(getResources().getString(R.color.commons_cook_hook_view_tick)));
            this.coolHookPaint.setStrokeWidth((float) this.t);
            this.coolHookPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(this.y, this.coolHookPaint);
        }
    }

    private void initAnim() {
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.commons_enter_cool_hook_dimiss_anim);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (enterCoolHookViewListener != null) {
                    enterCoolHookViewListener.onAnimEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loadAnimation.setDuration(500);
        loadAnimation.setStartOffset(1000);
        startAnimation(loadAnimation);
    }
}
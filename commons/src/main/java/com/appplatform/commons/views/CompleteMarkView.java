package com.appplatform.commons.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.appplatform.commons.R;
import com.appplatform.commons.utils.BitmapUtil;
import com.appplatform.commons.utils.DisplayUtil;


/**
 * Created by HoangSon on 8/10/2016.
 */
public class CompleteMarkView extends View {
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int stockSize;
    private int alpha;
    private boolean s;
    private boolean isStartAnim;
    private Paint circlePaint;
    private Point point;
    private Rect rect;
    private CompleteMarkListener startListener;
    private CompleteMarkListener animationListener;
    private DrawFilter drawFilter;
    private Bitmap imageMark;
    private ValueAnimator animation1;
    private ValueAnimator animation2;

    public CompleteMarkView(Context context) {
        super(context);
    }

    public CompleteMarkView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public CompleteMarkView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CompleteMarkView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CompleteMarkView);
        Drawable iconMark = typedArray.getDrawable(R.styleable.CompleteMarkView_commons_icon_mark);
        int colorCircle = typedArray.getColor(R.styleable.CompleteMarkView_commons_circle_color, ContextCompat.getColor(context, R.color.commons_mark_view));
        typedArray.recycle();
        this.isStartAnim = false;
        this.f = DisplayUtil.getDensity(context, 100);
        this.stockSize = DisplayUtil.getDensity(context, 0);
        this.h = DisplayUtil.getDensity(context, 83);
        this.drawFilter = new PaintFlagsDrawFilter(0, 1);
        this.circlePaint = new Paint();
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setColor(colorCircle);
        this.circlePaint.setStyle(Paint.Style.STROKE);
        this.circlePaint.setStrokeWidth((float) this.stockSize);
        this.circlePaint.setAlpha(this.alpha);
        this.animation1 = ObjectAnimator.ofFloat(0.0f, 1.0f);
        this.animation2 = ObjectAnimator.ofFloat(0.0f, 1.0f);
        this.animation1.setDuration(700);
        this.animation2.setDuration(1000);
        this.animation1.setInterpolator(new OvershootInterpolator());
        this.animation2.setInterpolator(new DecelerateInterpolator());
        this.animation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = (float) valueAnimator.getAnimatedValue();
                CompleteMarkView.this.e = (int) (floatValue * ((float) CompleteMarkView.this.f));
            }
        });
        this.animation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = (float) valueAnimator.getAnimatedValue();
                CompleteMarkView.this.g = (int) (((float) CompleteMarkView.this.h) + (((float) (CompleteMarkView.this.i - CompleteMarkView.this.h)) * floatValue));
                CompleteMarkView.this.alpha = (int) ((1.0f - floatValue) * 175.0f);
            }
        });
        this.animation1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                CompleteMarkView.this.animation2.start();
            }
        });
        this.animation2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                CompleteMarkView.this.isStartAnim = false;
                if (CompleteMarkView.this.animationListener != null) {
                    CompleteMarkView.this.animationListener.onCompleteMarkListener();
                }
            }
        });
        if (iconMark == null) {
            iconMark = ContextCompat.getDrawable(getContext(), R.drawable.commons_mark_done_icon);
        }
        this.imageMark = BitmapUtil.drawableToBitmap(iconMark);
        this.rect = new Rect();
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        a(i, i2);
    }

    private void a(int i, int i2) {
        if (i != i2) {
            i = i2;
        }
        this.i = i / 2;
        this.point = new Point(i / 2, i2 / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(this.drawFilter);
        super.onDraw(canvas);
        drawMark(canvas);
        drawCircle(canvas);
        update();
    }

    private void update() {
        if (this.isStartAnim) {
            postInvalidate();
        }
    }

    private void drawCircle(Canvas canvas) {
        if (this.g >= this.h && this.point != null) {
            if (!this.s) {
                this.s = true;
                if (this.startListener != null) {
                    this.startListener.onCompleteMarkListener();
                }
            }
            this.circlePaint.setAlpha(this.alpha);
            canvas.drawCircle((float) this.point.x, (float) this.point.y, (float) this.g, this.circlePaint);
        }
    }

    private void drawMark(Canvas canvas) {
        if (this.rect != null && this.imageMark != null && this.point != null) {
            this.rect.set(this.point.x - this.e, this.point.y - this.e, this.point.x + this.e, this.point.y + this.e);
            canvas.drawBitmap(this.imageMark, null, this.rect, null);
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public void setAnimationListener(CompleteMarkListener listener) {
        this.animationListener = listener;
    }


    public void setRingStartListener(CompleteMarkListener listener) {
        this.startListener = listener;
    }

    public void setStartAnim() {
        this.isStartAnim = true;
        this.alpha = 0;
        postInvalidate();
        this.animation1.start();
    }

    public void cancelAnim() {
        try {
            this.animation1.end();
            this.animation2.end();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public interface CompleteMarkListener {
        void onCompleteMarkListener();
    }
}

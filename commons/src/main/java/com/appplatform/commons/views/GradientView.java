package com.appplatform.commons.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Shader;
import android.os.Build;
import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.appplatform.commons.R;
import com.appplatform.commons.utils.ColorSupport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.appplatform.commons.views.GradientView.TypeColor.NORMAL;
import static com.appplatform.commons.views.GradientView.TypeColor.ORANGE;
import static com.appplatform.commons.views.GradientView.TypeColor.RED;


/**
 * Created by HoangSon on 9/15/2016.
 */
public class GradientView extends View {
    //    public static final int[] NORMAL_COLOR = new int[]{Color.parseColor("#1CCCFF"), Color.parseColor("#0E95FF")};
    public static int[] NORMAL_COLOR;
    public static int[] RED_COLOR;
    public static final int[] ORANGE_COLOR = new int[]{Color.parseColor("#FF6B19"), Color.parseColor("#FFAD01")};
    //    public static final int[] RED_COLOR = new int[]{Color.parseColor("#FF8951"), Color.parseColor("#FF2B4E")};
    private static final float[] COLOR_ALPHA = new float[]{0.0F, 1F};
    int defaultColor1;
    int defaultColor2;
    private int[] currentColors1;
    private int[] currentColors2;
    private int[] currentColors3;
    private long delayTime;
    private int widthValue;
    private int heightValue;
    private Paint paint;
    private PaintFlagsDrawFilter paintFlagsDrawFilter;
    private Interpolator interpolator;
    private OnViewChangeBackgroundListener listener;
    private LinearGradient linearGradient;
    private boolean isRunAnim;
    @TypeColor
    private int typeColor;

    public GradientView(Context var1) {
        super(var1);
        this.init();
    }

    public GradientView(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.init();
    }

    public GradientView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        this.init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientView(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.init();

    }

    private void init() {
        Context context = getContext();
        NORMAL_COLOR = new int[]{ContextCompat.getColor(context, R.color.commons_gradient_view_normal_start), ContextCompat.getColor(context, R.color.commons_gradient_view_normal_end)};
        RED_COLOR = new int[]{ContextCompat.getColor(context, R.color.commons_gradient_view_red_start), ContextCompat.getColor(context, R.color.commons_gradient_view_red_end)};

        this.currentColors1 = NORMAL_COLOR;
        this.currentColors2 = NORMAL_COLOR;
        this.currentColors3 = NORMAL_COLOR;
        this.delayTime = 2000L;
        this.defaultColor1 = -16640423;
        this.defaultColor2 = -13279785;
        this.typeColor = NORMAL;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, 5);
        this.interpolator = new LinearInterpolator();
    }

    private int[] changeColor(int[] color1, int[] color2, float alphaColor) {
        int[] colors = new int[NORMAL_COLOR.length];
        for (int i = 0; i < color1.length; ++i) {
            colors[i] = ColorSupport.getColor(color1[i], color2[i], alphaColor);
        }
        return colors;
    }


    private int[] getTypeColor(@TypeColor int typeColor) {
        int[] color = NORMAL_COLOR;
        switch (typeColor) {
            case NORMAL:
                return NORMAL_COLOR;
            case ORANGE:
                return RED_COLOR;
            case RED:
                return RED_COLOR;
            default:
                return color;
        }
    }

    public void changeColor(@TypeColor int typeColor) {
        if (!this.isRunAnim) {
            this.isRunAnim = true;
            this.delayTime = 2000L;
            this.changeColor(typeColor, this.delayTime, null);
        }
    }

    public void changeColor(@TypeColor int typeColor, long delayTime) {
        if (!this.isRunAnim) {
            this.isRunAnim = true;
            this.changeColor(typeColor, delayTime, null);
        }
    }

    public void changeColor(@TypeColor int typeColor, long delayTime, OnViewChangeBackgroundListener listener) {
        this.typeColor = typeColor;
        this.currentColors3 = this.getTypeColor(typeColor);
        this.delayTime = delayTime;
        this.listener = listener;
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0.0F, 1.0F);
        valueAnimator.setDuration(delayTime);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alphaAnim = (float) valueAnimator.getAnimatedValue();
                GradientView.this.currentColors2 = GradientView.this.changeColor(GradientView.this.currentColors1, GradientView.this.currentColors3, alphaAnim);
                GradientView.this.postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (GradientView.this.listener != null) {
                    GradientView.this.listener.onStartChange();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                GradientView.this.isRunAnim = false;
                GradientView.this.currentColors1 = GradientView.this.currentColors3;
                if (GradientView.this.listener != null) {
                    GradientView.this.listener.onChangeCompleted();
                }
            }
        });
        valueAnimator.start();
    }

    @TypeColor
    public int getCurrentType() {
        return this.typeColor;
    }

    @Override
    protected void onDraw(Canvas var1) {
        var1.setDrawFilter(this.paintFlagsDrawFilter);
        if (this.widthValue != 0 && this.heightValue != 0) {
            this.linearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, (float) this.heightValue, this.currentColors2, COLOR_ALPHA, Shader.TileMode.MIRROR);
            this.paint.setShader(this.linearGradient);
            var1.drawRect(0.0F, 0.0F, (float) this.widthValue, (float) this.heightValue, this.paint);
        }
    }

    protected void onMeasure(int var1, int var2) {
        super.onMeasure(var1, var2);
    }

    protected void onSizeChanged(int width, int height, int var3, int var4) {
        super.onSizeChanged(width, height, var3, var4);
        this.widthValue = width;
        this.heightValue = height;
        if (this.widthValue != 0 && this.heightValue != 0) {
            this.linearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, (float) this.heightValue, NORMAL_COLOR, COLOR_ALPHA, Shader.TileMode.MIRROR);
            this.paint.setShader(this.linearGradient);
        }
    }

    public void setColorType(@TypeColor int typeColor) {
        this.currentColors1 = this.getTypeColor(typeColor);
        this.currentColors2 = this.currentColors1;
        this.typeColor = typeColor;
        this.postInvalidate();
    }

    public void setGradientListener(OnViewChangeBackgroundListener listener) {
        this.listener = listener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public interface OnViewChangeBackgroundListener {
        void onStartChange();

        void onChangeCompleted();
    }


    @IntDef({NORMAL, ORANGE, RED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TypeColor {
        int NORMAL = 0;
        int ORANGE = 1;
        int RED = 2;
    }
}

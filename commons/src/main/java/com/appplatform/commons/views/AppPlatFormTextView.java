package com.appplatform.commons.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.appplatform.commons.AppPlatformManager;
import com.appplatform.commons.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by HoangSon on 3/26/2018.
 */

public class AppPlatFormTextView extends AppCompatTextView {

    public AppPlatFormTextView(Context context) {
        super(context);
    }

    public AppPlatFormTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public AppPlatFormTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        try {
            List<Typeface> fonts = Arrays.asList(AppPlatformManager.getInstance().getTypefaceTextView());
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AppPlatFormTextView);
            int typeface_index = typedArray.getInt(R.styleable.AppPlatFormTextView_textStyle, 0);
            boolean isSelected = typedArray.getBoolean(R.styleable.AppPlatFormTextView_selected, false);
            setSelected(isSelected);
            Typeface tf = fonts.get(typeface_index);
            if (tf == null) {
                return;
            }
            super.setTypeface(tf);
            typedArray.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.appplatform.commons.views;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.appplatform.commons.AppPlatformManager;

/**
 * Created by HoangSon on 3/26/2018.
 */

public class AppPlatFormButton extends AppCompatButton {
    public AppPlatFormButton(Context context) {
        super(context);
        this.init();
    }

    public AppPlatFormButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public AppPlatFormButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }


    private void init() {
        try {
            String fontPath = AppPlatformManager.getInstance().getFontButton();
            if (TextUtils.isEmpty(fontPath)) {
                return;
            }
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontPath);
            super.setTypeface(tf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

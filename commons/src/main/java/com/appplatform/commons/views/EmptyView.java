package com.appplatform.commons.views;

import android.annotation.TargetApi;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appplatform.commons.R;

/**
 * Created by HoangSon on 3/1/2018.
 */

public class EmptyView extends LinearLayout {
    public EmptyView(Context context) {
        super(context);
        this.init();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @TargetApi(21)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init();
    }

    private TextView emptyMessage;
    private ImageView emptyIcon;

    private void init() {
        inflate(getContext(), R.layout.commons_empty_layout, this);
        this.emptyMessage = this.findViewById(R.id.commons_empty_text);
        this.emptyIcon = this.findViewById(R.id.commons_empty_icon);
    }

    public void setEmptyMessage(@NonNull String message) {
        emptyMessage.setText(message);
    }

    public void setEmptyIcon(@NonNull int iconRess) {
        emptyIcon.setImageResource(iconRess);
    }


    public void show() {
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(VISIBLE);
            }
        });
    }

    public void hide() {
        post(new Runnable() {
            @Override
            public void run() {
                setVisibility(GONE);
            }
        });
    }

}

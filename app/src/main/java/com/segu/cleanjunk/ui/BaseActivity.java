package com.segu.cleanjunk.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

package com.wangxingxing.mydagger2demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wangxingxing.mydagger2demo.di.component.DaggerOtherComponent;

import javax.inject.Inject;

public class OtherActivity extends AppCompatActivity {

    @Inject
    OtherObject mOtherObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerOtherComponent.create().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }
}
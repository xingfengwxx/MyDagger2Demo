package com.wangxingxing.mydagger2demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wangxingxing.mydagger2demo.di.component.DaggerApplicationComponent;
import com.wangxingxing.mydagger2demo.di.qualifier.TestUserQualifier;
import com.wangxingxing.mydagger2demo.model.UserRepository;
import static com.wangxingxing.mydagger2demo.Constants.*;

import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends AppCompatActivity {

    @Inject
    UserRepository mUserRepository;

    @Inject
    UserRepository mUserRepository2;

    @Inject
    OtherObject mOtherObject;

    @Inject
//    @Named("test1")
    @TestUserQualifier("test1")
    TestUser mTestUser;

    @Inject
//    @Named("test2")
    @TestUserQualifier("test2")
    TestUser mTestUser2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        DaggerApplicationComponent.create().inject(this);
        App.getApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: " + mUserRepository.toString());
        Log.i(TAG, "onCreate: " + mUserRepository2.toString());
        Log.i(TAG, "onCreate: " + mOtherObject.toString());
        Log.i(TAG, "onCreate: " + mTestUser.toString());
        Log.i(TAG, "onCreate: " + mTestUser2.toString());
    }

    public void toLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
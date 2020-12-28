package com.wangxingxing.mydagger2demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wangxingxing.mydagger2demo.di.LoginComponentProvider;
import com.wangxingxing.mydagger2demo.di.component.LoginComponent;
import com.wangxingxing.mydagger2demo.model.UserRepository;
import com.wangxingxing.mydagger2demo.net.StringData;
import com.wangxingxing.mydagger2demo.presenter.LoginPresenter;
import com.wangxingxing.mydagger2demo.viewmodel.LoginViewModel;
import static com.wangxingxing.mydagger2demo.Constants.*;

import javax.inject.Inject;

public class LoginActivity extends AppCompatActivity {

    @Inject
    UserRepository mUserRepository;

    @Inject
    LoginViewModel mLoginViewModel;

    TextView tvContent;

    LoginComponent mLoginComponent;

    @Inject
    LoginPresenter mLoginPresenter;

    @Inject
    TestObject mTestObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLoginComponent = ((LoginComponentProvider) getApplicationContext()).provideLoginComponent();
        mLoginComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i(TAG, "onCreate: " + mUserRepository.toString());
        Log.i(TAG, "onCreate: " + mLoginViewModel.toString());
        Log.i(TAG, "onCreate: " + mTestObject.toString());

        tvContent = findViewById(R.id.tv_content);

        mLoginViewModel.getStringDataMutableLiveData().observe(this, new Observer<StringData>() {
            @Override
            public void onChanged(StringData stringData) {
                tvContent.setText(stringData.getData());
            }
        });

        mLoginViewModel.loadData();

        mLoginPresenter.doLogin();
    }
}
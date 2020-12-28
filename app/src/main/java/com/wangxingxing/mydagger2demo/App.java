package com.wangxingxing.mydagger2demo;

import android.app.Application;

import com.wangxingxing.mydagger2demo.di.LoginComponentProvider;
import com.wangxingxing.mydagger2demo.di.component.ApplicationComponent;
import com.wangxingxing.mydagger2demo.di.component.DaggerApplicationComponent;
import com.wangxingxing.mydagger2demo.di.component.LoginComponent;

/**
 * author : 王星星
 * date : 2020/12/28 10:59
 * email : 1099420259@qq.com
 * description :
 */
public class App extends Application implements LoginComponentProvider {

    private static ApplicationComponent sApplicationComponent = DaggerApplicationComponent.create();

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }

    @Override
    public LoginComponent provideLoginComponent() {
        return sApplicationComponent.loginComponent().crate();
    }
}

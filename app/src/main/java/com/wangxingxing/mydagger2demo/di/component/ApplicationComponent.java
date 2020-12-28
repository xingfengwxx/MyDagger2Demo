package com.wangxingxing.mydagger2demo.di.component;

import com.wangxingxing.mydagger2demo.MainActivity;
import com.wangxingxing.mydagger2demo.di.module.SubcomponentsModule;
import com.wangxingxing.mydagger2demo.di.module.TestObjectModule;
import com.wangxingxing.mydagger2demo.di.module.TestUserModule;
import com.wangxingxing.mydagger2demo.net.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author : 王星星
 * date : 2020/12/28 11:01
 * email : 1099420259@qq.com
 * description :
 */
@Singleton
@Component(modules = {NetworkModule.class, SubcomponentsModule.class, TestObjectModule.class, TestUserModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);

    LoginComponent.Factory loginComponent();
}

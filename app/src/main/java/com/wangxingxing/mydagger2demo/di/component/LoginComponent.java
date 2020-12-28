package com.wangxingxing.mydagger2demo.di.component;

import com.wangxingxing.mydagger2demo.LoginActivity;
import com.wangxingxing.mydagger2demo.di.scope.ActivityScope;
import com.wangxingxing.mydagger2demo.presenter.LoginModule;

import dagger.Subcomponent;

/**
 * author : 王星星
 * date : 2020/12/28 11:48
 * email : 1099420259@qq.com
 * description :
 */
@ActivityScope
@Subcomponent(modules = {LoginModule.class})
public interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        LoginComponent crate();
    }

    void inject(LoginActivity loginActivity);
}

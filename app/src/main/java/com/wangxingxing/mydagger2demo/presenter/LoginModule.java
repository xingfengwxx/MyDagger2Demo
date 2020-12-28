package com.wangxingxing.mydagger2demo.presenter;

import dagger.Binds;
import dagger.Module;

/**
 * author : 王星星
 * date : 2020/12/28 11:49
 * email : 1099420259@qq.com
 * description :
 */
@Module
public abstract class LoginModule {

    /*@Provides
    public LoginPresenter provideLoginPresenter() {
        return new LoginPresenterImpl();
    }*/

    @Binds
    public abstract LoginPresenter provideLoginPresenter(LoginPresenterImpl loginPresenterImpl);
}

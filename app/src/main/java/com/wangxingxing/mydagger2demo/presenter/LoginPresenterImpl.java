package com.wangxingxing.mydagger2demo.presenter;

import android.util.Log;

import static com.wangxingxing.mydagger2demo.Constants.*;

import javax.inject.Inject;

/**
 * author : 王星星
 * date : 2020/12/28 11:51
 * email : 1099420259@qq.com
 * description :
 */
public class LoginPresenterImpl implements LoginPresenter {

    @Inject
    public LoginPresenterImpl() {
    }

    @Override
    public void doLogin() {
        Log.i(TAG, "doLogin: 用户正在登录...");
    }
}

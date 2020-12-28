package com.wangxingxing.mydagger2demo.di.component;

import com.wangxingxing.mydagger2demo.OtherActivity;

import dagger.Component;

/**
 * author : 王星星
 * date : 2020/12/28 15:33
 * email : 1099420259@qq.com
 * description :
 */
@Component
public interface OtherComponent {

    void inject(OtherActivity otherActivity);
}

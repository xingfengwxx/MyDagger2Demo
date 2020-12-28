package com.wangxingxing.mydagger2demo.di.module;


import com.wangxingxing.mydagger2demo.TestUser;
import com.wangxingxing.mydagger2demo.di.qualifier.TestUserQualifier;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * author : 王星星
 * date : 2020/12/28 15:43
 * email : 1099420259@qq.com
 * description :
 */
@Module
public class TestUserModule {


    @Provides
//    @Named("test1")
    @TestUserQualifier("test1")
    public TestUser provideTestUser1() {
        return new TestUser("尼古拉斯赵四");
    }

    @Provides
//    @Named("test2")
    @TestUserQualifier("test2")
    public TestUser provideTestUser2() {
        return new TestUser("旋涡刘能");
    }
}

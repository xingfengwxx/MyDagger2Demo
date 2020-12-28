package com.wangxingxing.mydagger2demo.di.module;

import com.wangxingxing.mydagger2demo.TestObject;

import dagger.Module;
import dagger.Provides;

/**
 * author : 王星星
 * date : 2020/12/28 14:58
 * email : 1099420259@qq.com
 * description :
 */
@Module
public class TestObjectModule {

    @Provides
    public TestObject provideTestObject() {
        return new TestObject();
    }
}

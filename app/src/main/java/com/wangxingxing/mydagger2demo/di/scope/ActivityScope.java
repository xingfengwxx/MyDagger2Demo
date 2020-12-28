package com.wangxingxing.mydagger2demo.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * author : 王星星
 * date : 2020/12/28 11:39
 * email : 1099420259@qq.com
 * description :
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}

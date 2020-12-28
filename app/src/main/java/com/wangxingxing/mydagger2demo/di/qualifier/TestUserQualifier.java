package com.wangxingxing.mydagger2demo.di.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * author : 王星星
 * date : 2020/12/28 15:47
 * email : 1099420259@qq.com
 * description :
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface TestUserQualifier {

    String value() default "";
}

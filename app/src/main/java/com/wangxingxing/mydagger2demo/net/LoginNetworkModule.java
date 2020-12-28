package com.wangxingxing.mydagger2demo.net;

import com.wangxingxing.mydagger2demo.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * author : 王星星
 * date : 2020/12/28 11:22
 * email : 1099420259@qq.com
 * description :
 */
@Module
public class LoginNetworkModule {

    @Singleton
    @Provides
    public ApiService provideApiService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(ApiService.class);
    }
}

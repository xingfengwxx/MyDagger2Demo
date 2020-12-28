package com.wangxingxing.mydagger2demo.net;

import dagger.Module;

/**
 * author : 王星星
 * date : 2020/12/28 11:27
 * email : 1099420259@qq.com
 * description :
 */
@Module(includes = {LoginNetworkModule.class})
public class NetworkModule {

        /*@Singleton
    @Provides
    public ApiService provideApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(ApiService.class);
    }*/
}

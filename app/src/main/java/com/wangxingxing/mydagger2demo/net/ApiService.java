package com.wangxingxing.mydagger2demo.net;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * author : 王星星
 * date : 2020/12/28 11:09
 * email : 1099420259@qq.com
 * description :
 */
public interface ApiService {

    // https://gank.io/api/v2/categories/GanHuo
    @GET("categories/GanHuo")
    Call<String> getGanHuo();

    @GET("/index.html")
    Call<String> indexHtml();
}

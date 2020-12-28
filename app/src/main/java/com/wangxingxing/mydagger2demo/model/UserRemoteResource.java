package com.wangxingxing.mydagger2demo.model;

import com.wangxingxing.mydagger2demo.net.ApiService;

import javax.inject.Inject;

import retrofit2.Callback;

/**
 * author : 王星星
 * date : 2020/12/28 11:08
 * email : 1099420259@qq.com
 * description :
 */
public class UserRemoteResource {

    private final ApiService apiService;

    @Inject
    public UserRemoteResource(ApiService apiService) {
        this.apiService = apiService;
    }

    public void loadData(Callback<String> callback) {
        apiService.getGanHuo().enqueue(callback);
    }
}

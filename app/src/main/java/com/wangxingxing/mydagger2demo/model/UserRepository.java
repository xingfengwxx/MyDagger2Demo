package com.wangxingxing.mydagger2demo.model;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Callback;

/**
 * author : 王星星
 * date : 2020/12/28 11:14
 * email : 1099420259@qq.com
 * description :
 */
@Singleton
public class UserRepository {

    private final UserLocalResource uerLocalResource;
    private final UserRemoteResource userRemoteResource;

    @Inject
    public UserRepository(UserLocalResource uerLocalResource, UserRemoteResource userRemoteResource) {
        this.uerLocalResource = uerLocalResource;
        this.userRemoteResource = userRemoteResource;
    }

    public void loadRemoteData(Callback<String> callback) {
        userRemoteResource.loadData(callback);
    }
}

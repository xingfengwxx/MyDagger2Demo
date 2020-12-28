package com.wangxingxing.mydagger2demo.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wangxingxing.mydagger2demo.Constants;
import com.wangxingxing.mydagger2demo.di.scope.ActivityScope;
import com.wangxingxing.mydagger2demo.model.UserRepository;
import com.wangxingxing.mydagger2demo.net.StringData;
import static com.wangxingxing.mydagger2demo.Constants.*;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author : 王星星
 * date : 2020/12/28 11:38
 * email : 1099420259@qq.com
 * description :
 */
@ActivityScope
public class LoginViewModel {

    private final UserRepository mUserRepository;

    private static MutableLiveData<StringData> sStringDataMutableLiveData;

    public MutableLiveData<StringData> getStringDataMutableLiveData() {
        if (sStringDataMutableLiveData == null) {
            sStringDataMutableLiveData = new MutableLiveData<>();
        }
        return sStringDataMutableLiveData;
    }

    @Inject
    public LoginViewModel(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public void loadData() {
        mUserRepository.loadRemoteData(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                StringData stringData = new StringData(response.body());
                Log.i(TAG, "onResponse: " + stringData.getData());
                if (sStringDataMutableLiveData != null) {
                    sStringDataMutableLiveData.setValue(stringData);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }
}

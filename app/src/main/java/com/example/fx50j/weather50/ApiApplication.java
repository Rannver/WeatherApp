package com.example.fx50j.weather50;


import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

public class ApiApplication extends Application {

    @Override
    public void onCreate() {
        ApiStoreSDK.init(this, "9fc29d5497de5ac40264e96a2096af3e");
        super.onCreate();
    }
}

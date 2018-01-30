package com.yueyue.coolweather.activity;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by yueyue on 2017/5/7.
 */

public class MyApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //初始化LitePal的数据库框架
        LitePal.initialize(this);
    }
}

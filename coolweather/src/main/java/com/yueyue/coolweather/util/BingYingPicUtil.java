package com.yueyue.coolweather.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.yueyue.coolweather.activity.MyApplication;
import com.yueyue.coolweather.config.Constants;
import com.yueyue.coolweather.gson.BingYingPic;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : yueyue on 2018/1/30 23:51
 * desc   :必应每日一图
 */

public class BingYingPicUtil {


    public static void updateBingPic(String bingPicUrl) {
        HttpUtil.sendOkHttpRequest(bingPicUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();

            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void getDailyBingPic(String url) {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String content = response.body().string();
                    BingYingPic bingYingPic = new Gson().fromJson(content, BingYingPic.class);
                    if (bingYingPic != null && bingYingPic.images.size()>0) {
                        String picUrl = Constants.BINGYING_BASE_URL + bingYingPic.images.get(0).bingBasePicUrl;
                        updateBingPic(picUrl);
                    }
                }

            }
        });
    }

}

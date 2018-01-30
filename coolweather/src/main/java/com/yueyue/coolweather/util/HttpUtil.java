package com.yueyue.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 联网从服务端获取数据
 * Created by yueyue on 2017/5/7.
 */

public class HttpUtil {

    /**
     * 发送一个OkHttp请求
     * @param address  请求地址
     * @param callback 请求回调
     */
    public static void sendOkHttpRequest(String address, Callback callback){
        //创建okHttp的实例
        OkHttpClient client = new OkHttpClient();
        //创建联网请求实例
        Request request = new Request.Builder().url(address).build();
        // 开启异步线程访问网络
        client.newCall(request).enqueue(callback);

    }
}

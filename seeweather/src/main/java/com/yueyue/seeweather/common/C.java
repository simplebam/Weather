package com.yueyue.seeweather.common;

import com.yueyue.seeweather.BuildConfig;
import com.yueyue.seeweather.base.BaseApplication;

import java.io.File;

/**
 * Created by HugoXie on 16/5/23.
 * <p>
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info: 常量类
 */
public class C {

    public static final String API_TOKEN = BuildConfig.FirToken;
    public static final String KEY = BuildConfig.WeatherKey;// 和风天气 key

    public static final String ALI_PAY = BuildConfig.ALiPayKey;
    public static final String ALI_PAY1 = "FKX07563WDYUZJTAW5W029";
    public static final String ALI_PAY_HONGBAO = "https://qr.alipay.com/" + ALI_PAY;

    public static final String MULTI_CHECK = "multi_check";

    public static final String ORM_NAME = "cities.db";

    public static final String UNKNOWN_CITY = "unknown city";

    public static final String NET_CACHE = BaseApplication.getAppCacheDir() + File.separator + "NetCache";

    public static final String DANTE = "dante";
}

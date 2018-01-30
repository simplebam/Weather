package com.yueyue.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yueyue.coolweather.R;
import com.yueyue.coolweather.gson.Forecast;
import com.yueyue.coolweather.gson.Weather;
import com.yueyue.coolweather.service.AutoUpdateService;
import com.yueyue.coolweather.util.HttpUtil;
import com.yueyue.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 显示天气信息
 */
public class WeatherActivity extends AppCompatActivity {
    private static String TAG="WeatherActivity";

    public DrawerLayout drawerLayout;//让其具有侧滑栏

    private Button navButton;//title布局中左边那个按钮

    public SwipeRefreshLayout swipeRefresh;//设置下拉刷新

    private ScrollView weatherLayout;//总布局id

    private TextView titleCity;//显示城市的标题

    private TextView titleUpdateTime;//标题中显示更细的时间

    private TextView degreeText;//显示温度

    private TextView weatherInfoText;//显示晴天等字眼的文本

    private LinearLayout forecastLayout;//显示未来三天-五天的天气预报.关键还需要看服务器

    private TextView aqiText;//显示aqi

    private TextView pm25Text;//显示pm2.5

    private TextView comfortText;//显示舒适度

    private TextView carWashText;//显示洗车指数

    private TextView sportText;//显示运动建议

    private ImageView bingPicImg;//显示每日一图的建议

    private String mWeatherId;//需要显示在界面的城市ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //api>=21才可以
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //设置下拉刷新的进度条的颜色
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //每日一图,存储的是每日一图的地址
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }

    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl="https://free-api.heweather.com/v5/weather?city="+weatherId+
                "&key=b9e05332eea2426fb74de09c14c77227";
        //https://free-api.heweather.com/v5/weather?city=广州&key=b9e05332eea2426fb74de09c14c77227
        //http://guolin.tech/api/weather?cityid=CN101281601&key=bc0418b57b2d4918819d3974ac1285d9
        //String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+ "&key=bc0418b57b2d4918819d3974ac1285d9";
//        String encoder="UTF-8";
//        String weatherUrl = null;
//        try {
//            weatherUrl = "https://free-api.heweather.com/v5/weather?city="+
//                    URLEncoder.encode(weatherId,encoder)+"&key=b9e05332eea2426fb74de09c14c77227";
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            Toast.makeText(WeatherActivity.this, "你所用的编码不存在", Toast.LENGTH_SHORT).show();
//        }
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
               // Log.i(TAG,"weather:"+weather+"responseText:"+responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            //需要及时更新一下选中的那个城市ID,防止刷新又是重回以前的数据
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "onResponse获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //隐藏下拉刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "onFailure获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        //隐藏下拉刷新进度条
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        // TODO: 2017/5/9 这里下一个版本存储一个日期如sp,当日子不同的时候才开始联网下载
        //刷新时候需要尝试一下刷新背景图片
        loadBingPic();
    }



    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        //返回的格式为2017-05-18 14:51,而我们需要的是后面的数据
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃"; //使用搜狗输入法可以打出来
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                    forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运行建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        //启动后台更新天气的服务
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }


    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        // TODO: 2017/5/9 搞定好之后更换为第三方数据提供方接口 
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(WeatherActivity.this, "网络加载每日一图失败",
                        Toast.LENGTH_SHORT).show();
                //网络请求失败后加载默认图片
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(R.drawable.bing_default).
                                into(bingPicImg);
                    }
                });

            }
        });
    }
}

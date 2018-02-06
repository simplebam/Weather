package com.yueyue.seeweather.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yueyue.seeweather.R;
import com.yueyue.seeweather.base.ToolbarActivity;
import com.yueyue.seeweather.common.IntentKey;
import com.yueyue.seeweather.modules.main.adapter.WeatherAdapter;
import com.yueyue.seeweather.modules.main.domain.Weather;

import butterknife.BindView;

/**
 * Created by HugoXie on 2017/6/10.
 *
 * Email: Hugo3641@gmail.com
 * GitHub: https://github.com/xcc3641
 * Info: 多城市详细页面
 */

public class DetailCityActivity extends ToolbarActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected int layoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewWithData();
    }

    private void initViewWithData() {
        Intent intent = getIntent();
        Weather weather = (Weather) intent.getSerializableExtra(IntentKey.WEATHER);
        if (weather == null) {
            finish();
        } else {
            safeSetTitle(weather.basic.city);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            WeatherAdapter mAdapter = new WeatherAdapter(weather);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public static void launch(Context context, Weather weather) {
        Intent intent = new Intent(context, DetailCityActivity.class);
        intent.putExtra(IntentKey.WEATHER, weather);
        context.startActivity(intent);
    }
}

package com.yueyue.seeweather.modules.setting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yueyue.seeweather.R;
import com.yueyue.seeweather.base.ToolbarActivity;

public class SettingActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getToolbar().setTitle("设置");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new SettingFragment())
                .commit();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }
}

package com.yueyue.seeweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yueyue.seeweather.BuildConfig;

import butterknife.ButterKnife;

/**
 * author : yueyue on 2018/2/2 23:02
 * desc   :
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
    }

    protected abstract int layoutId();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        if (!BuildConfig.DEBUG) {
            Bugtags.onResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        if (!BuildConfig.DEBUG) {
            Bugtags.onPause(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        if (!BuildConfig.DEBUG) {
            Bugtags.onDispatchTouchEvent(this, event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void setDayTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        activity.getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        // 调用 recreate() 使设置生效
        activity.recreate();
    }

    public static void setNightTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        activity.getDelegate().setLocalNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
        // 调用 recreate() 使设置生效
        activity.recreate();
    }

    public void setTheme(AppCompatActivity activity) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        activity.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        activity.recreate();
    }
}

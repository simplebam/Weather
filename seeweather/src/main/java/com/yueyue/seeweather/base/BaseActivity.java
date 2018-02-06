package com.yueyue.seeweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MotionEvent;

import com.bugtags.library.Bugtags;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends RxAppCompatActivity {
    private static String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
        Log.e("current Activity:"+this.getClass().getSimpleName(),"--onCreate--");
    }

    protected abstract int layoutId();

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("current Activity:"+this.getClass().getSimpleName(),"--onDestroy--");
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

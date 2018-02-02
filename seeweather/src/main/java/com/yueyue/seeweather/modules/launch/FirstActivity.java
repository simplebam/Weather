package com.yueyue.seeweather.modules.launch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yueyue.seeweather.modules.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = FirstActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        //RxJava 创建操作符 timer与interval - CSDN博客
        //       http://blog.csdn.net/axuanqq/article/details/50687490
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        MainActivity.launch(FirstActivity.this);
                        finish();
                    }
                });

    }
}

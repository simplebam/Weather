package com.yueyue.seeweather.base;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.bugtags.library.Bugtags;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.hugo.watcher.Watcher;
import com.squareup.leakcanary.LeakCanary;
import com.yueyue.seeweather.BuildConfig;
import com.yueyue.seeweather.component.CrashHandler;
import com.yueyue.seeweather.component.PLog;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * author : yueyue on 2018/2/2 16:29
 * desc   :
 */

public class BaseApplication extends Application {

    private static String sCacheDir;
    private static Context sAppContext;

    // TODO: 16/8/1 这里的夜间模式 UI 有些没有适配好 暂时放弃夜间模式
    static {
        //静态代码块
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;

        CrashHandler.init(new CrashHandler(this));

        if (!BuildConfig.DEBUG) {
            //FIR.init(this);
            Bugtags.start("b6e803321c11fee9902270e8a3dbbe70",
                    this,
                    Bugtags.BTGInvocationEventBubble);
        } else {
            Watcher.getInstance().start(this);
            Stetho.initializeWithDefaults(this);
        }

        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        LeakCanary.install(this);

        //RxJava2使用过程中遇到的坑 - CSDN博客
        //         http://blog.csdn.net/sr_code_plus/article/details/77189478
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable != null) {
                    PLog.e(throwable.toString());
                } else {
                    PLog.e("call onError but exception is null");
                }
            }
        });

        /*
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && existSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean existSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    public static Context getAppContext() {
        return sAppContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }

}

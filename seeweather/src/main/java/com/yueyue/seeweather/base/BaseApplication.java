package com.yueyue.seeweather.base;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.bugtags.library.Bugtags;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.hugo.watcher.Watcher;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yueyue.seeweather.BuildConfig;
import com.yueyue.seeweather.component.CrashHandler;

import im.fir.sdk.FIR;

public class BaseApplication extends Application {

    private static String sCacheDir;
    private static Context sAppContext;

    //在自己的Application中添加如下代码
    private RefWatcher mRefWatcher;

    // TODO: 16/8/1 这里的夜间模式 UI 有些没有适配好 暂时放弃夜间模式
    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        CrashHandler.init(new CrashHandler(getApplicationContext()));
        if (!BuildConfig.DEBUG) {
            FIR.init(this);
        } else {
            Watcher.getInstance().start(this);
            Stetho.initializeWithDefaults(this);
        }

        Bugtags.start("b6e803321c11fee9902270e8a3dbbe70",
                this,
                Bugtags.BTGInvocationEventBubble);

        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        mRefWatcher = LeakCanary.install(this);


        /**
         * 预先加载三级列表显示省市区的数据
         */
//        CityListLoader.getInstance().loadProData(this);


//         /*
//         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
//         */
//        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
//            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
//        } else {
//            sCacheDir = getApplicationContext().getCacheDir().toString();
//        }

        //考虑到权限问题.这里统一放到内置内存卡去
        sCacheDir = getApplicationContext().getCacheDir().toString();

//这里会引发(BaseApplication.java:54) io.reactivex.exceptions.OnErrorNotImplementedException:
// Attempt to invoke virtual method 'android.database.
// Cursor android.database.sqlite.SQLiteDatabase.query(java.lang.String, java.lang.String[],
// java.lang.String, java.lang.String[],java.lang.String, java.lang.String, java.lang.String)'
// on a null object reference
//        RxJavaPlugins.setErrorHandler(throwable -> {
//            if (throwable != null) {
//                PLog.e(throwable.toString());
//            } else {
//                PLog.e("call onError but exception is null");
//            }
//        });


    }


    //在自己的Application中添加如下代码
    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }


    public static Context getAppContext() {
        return sAppContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}

package com.yueyue.seeweather.base;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.bugtags.library.Bugtags;
import com.yueyue.seeweather.component.CrashHandler;

public class BaseApplication extends Application {

    private static String sCacheDir;
    private static Context sAppContext;


    // TODO: 16/8/1 这里的夜间模式 UI 有些没有适配好 暂时放弃夜间模式
    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
        CrashHandler.init(new CrashHandler(this));

        Bugtags.start("b6e803321c11fee9902270e8a3dbbe70",
                this,
                Bugtags.BTGInvocationEventBubble);



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

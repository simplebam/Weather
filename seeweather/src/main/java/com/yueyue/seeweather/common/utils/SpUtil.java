package com.yueyue.seeweather.common.utils;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;

import com.yueyue.seeweather.base.BaseApplication;

/**
 * Created by hugo on 2016/2/19 0019.
 * <p>
 * 设置相关 包括 sp 的写入
 */
public class SpUtil {

    public static final String CITY_NAME = "city_name"; //选择城市
    public static final String HOUR = "current_hour"; //当前小时

    public static final String CHANGE_ICONS = "change_icons"; //切换图标
    public static final String CLEAR_CACHE = "clear_cache"; //清空缓存
    public static final String AUTO_UPDATE = "change_update_time"; //自动更新时长
    public static final String NOTIFICATION_MODEL = "notification_model";//通知栏常驻
    public static final String ANIM_START = "animation_start";//首页item动画
    public static final String MULTI_CITY_TIPS = "multi_city_tips";//多城市管理提醒设置
    public static final String WATCHER = "watcher";//监控开关


    public static int ONE_HOUR = 1000 * 60 * 60;//60分钟

    private SharedPreferences mPrefs;

    public static SpUtil getInstance() {
        return SPHolder.sInstance;
    }

    private static class SPHolder {
        private static final SpUtil sInstance = new SpUtil();
    }

    private SpUtil() {
        mPrefs = BaseApplication.getAppContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public SpUtil putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public SpUtil putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public SpUtil putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    // 设置当前小时
    public void setCurrentHour(int h) {
        mPrefs.edit().putInt(HOUR, h).apply();
    }

    public int getCurrentHour() {
        return mPrefs.getInt(HOUR, 0);
    }

    // 图标种类相关
    public void setIconType(int type) {
        mPrefs.edit().putInt(CHANGE_ICONS, type).apply();
    }

    public int getIconType() {
        return mPrefs.getInt(CHANGE_ICONS, 0);
    }

    // 自动更新时间 hours
    public void setAutoUpdate(int t) {
        mPrefs.edit().putInt(AUTO_UPDATE, t).apply();
    }

    public int getAutoUpdate() {
        return mPrefs.getInt(AUTO_UPDATE, 3);
    }

    //当前城市
    public void setCityName(String name) {
        mPrefs.edit().putString(CITY_NAME, name).apply();
    }

    public String getCityName() {
        return mPrefs.getString(CITY_NAME, "北京");
    }

    //  通知栏模式 默认为常驻
    public void setNotificationModel(int t) {
        mPrefs.edit().putInt(NOTIFICATION_MODEL, t).apply();
    }

    public int getNotificationModel() {
        return mPrefs.getInt(NOTIFICATION_MODEL, Notification.FLAG_ONGOING_EVENT);
    }

    // 首页 Item 动画效果 默认关闭

    public void setMainAnim(boolean b) {
        mPrefs.edit().putBoolean(ANIM_START, b).apply();
    }

    public boolean getMainAnim() {
        return mPrefs.getBoolean(ANIM_START, false);
    }

    // 多城市管理Tips显示 默认开启

    public void setMultiCityTips(boolean b) {
        mPrefs.edit().putBoolean(MULTI_CITY_TIPS, b).apply();
    }

    public boolean getMultiCityTips() {
        return mPrefs.getBoolean(MULTI_CITY_TIPS, true);
    }


    // 监听开关
    public void setWatcherSwitcher(boolean b) {
        mPrefs.edit().putBoolean(WATCHER, b).apply();
    }

    public boolean getWatcherSwitch() {
        return mPrefs.getBoolean(WATCHER, false);
    }
}

package com.yueyue.seeweather.modules.city.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yueyue.seeweather.common.utils.Util;
import com.yueyue.seeweather.modules.city.domain.City;
import com.yueyue.seeweather.modules.city.domain.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 2015/9/30 0030.
 * 封装数据库操作
 */
@Deprecated
public class WeatherDB {

    public WeatherDB() {

    }

    public static List<Province> loadProvinces(SQLiteDatabase db) {
        List<Province> list = new ArrayList<>();
        if (db == null) return list;
        Cursor cursor = db.query("T_Province", null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Province province = new Province();
                province.mProSort = cursor.getInt(cursor.getColumnIndex("ProSort"));
                province.mProName = cursor.getString(cursor.getColumnIndex("ProName"));
                list.add(province);
            }
        }
        Util.closeQuietly(cursor);
        return list;
    }

    public static List<City> loadCities(SQLiteDatabase db, int ProID) {
        List<City> list = new ArrayList<>();
        if (db == null) return list;
        Cursor cursor = db.query("T_City", null, "ProID = ?", new String[]{String.valueOf(ProID)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                City city = new City();
                city.mCityName = cursor.getString(cursor.getColumnIndex("CityName"));
                city.mProID = ProID;
                city.mCitySort = cursor.getInt(cursor.getColumnIndex("CitySort"));
                list.add(city);
            }
        }

        Util.closeQuietly(cursor);
        return list;
    }
}

package com.yueyue.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * City数据表的字段
 */
public class City extends DataSupport {

    //城市id
    private int id;
    //城市名字
    private String cityName;
    //城市代号
    private int cityCode;
    //记录当前所属省的id值
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

}

package com.yueyue.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省份Province数据表的字段
 * Created by yueyue on 2017/5/7.
 */

public class Province extends DataSupport {
    //每个实例中应有的字段
    private int id;
    //省份代号
    private int provinceCode;
    //省份名字
    private String provinceName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}

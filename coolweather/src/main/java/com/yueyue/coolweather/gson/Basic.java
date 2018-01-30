package com.yueyue.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Basic基本类json
 */
public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

    }

}

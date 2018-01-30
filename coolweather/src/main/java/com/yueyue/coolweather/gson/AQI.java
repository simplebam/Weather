package com.yueyue.coolweather.gson;

/**
 * AQI空气质量json
 */
public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;

        public String pm25;

    }

}

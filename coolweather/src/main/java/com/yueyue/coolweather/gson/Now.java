package com.yueyue.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Now现在json
 */
public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }

}

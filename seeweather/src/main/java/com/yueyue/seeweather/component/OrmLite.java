package com.yueyue.seeweather.component;

import com.litesuits.orm.LiteOrm;
import com.yueyue.seeweather.BuildConfig;
import com.yueyue.seeweather.base.BaseApplication;
import com.yueyue.seeweather.common.C;

/**
 * Created by HugoXie on 16/7/24.
 *
 * Email: Hugo3641@gamil.com
 * GitHub: https://github.com/xcc3641
 * Info:
 */
public class OrmLite {

    static LiteOrm sLiteOrm;

    public static LiteOrm getInstance() {
        getOrmHolder();
        return sLiteOrm;
    }

    private static OrmLite getOrmHolder() {
        return OrmHolder.sInstance;
    }

    private OrmLite() {
        if (sLiteOrm == null) {
            sLiteOrm = LiteOrm.newSingleInstance(BaseApplication.getAppContext(), C.ORM_NAME);
        }
        sLiteOrm.setDebugged(BuildConfig.DEBUG);// open the log
    }

    private static class OrmHolder {
        private static final OrmLite sInstance = new OrmLite();
    }
}

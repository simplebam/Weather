package com.yueyue.seeweather.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * author : yueyue on 2018/2/6 22:01
 * desc   :
 */

public class Sharer {
    public static void shareText(Context context, String title, String text) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(sharingIntent, title));
    }

    public static void shareImage(Context context, Uri uri) {

    }
}

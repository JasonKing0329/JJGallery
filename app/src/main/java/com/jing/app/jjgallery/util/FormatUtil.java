package com.jing.app.jjgallery.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/12/28 0028.
 * kinds of formatter
 */

public class FormatUtil {

    public static String formatDate(long data) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dateFormat.format(new Date(data));
    }
}

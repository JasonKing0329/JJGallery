package com.jing.app.jjgallery.util;

import java.math.BigDecimal;
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

    /**
     * 保留n位小数
     * @param score
     * @param n
     * @return
     */
    public static String formatScore(float score, int n) {
        BigDecimal bd = new BigDecimal(score);
        bd = bd.setScale(n, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    /**
     * 将浮点数转换为小数点后两位，如果末尾0无意义则去掉
     *
     * @param num
     * @return
     */
    public static String formatFloatEnd(float num) {

        String pr = formatScore(num, 2);
        if(pr.indexOf(".") > 0){
            pr = pr.replaceAll("0+?$", "");//去掉多余的0
            pr = pr.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return pr;
    }

}

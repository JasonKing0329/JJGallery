package com.jing.app.jjgallery.gdb.utils;

import android.graphics.Color;

/**
 * Created by Administrator on 2017/1/13 0013.
 * google推荐配色，一些颜色的色阶
 * 每种颜色的前三个值文字用暗色
 * 后面的都可以用白色
 */

public class ColorUtils {

    public static final int DARK_TEXT_FLAG = 3;

    public static int[] redColors = new int[] {
            Color.parseColor("#FFEBEE"),
            Color.parseColor("#FFCDD2"),
            Color.parseColor("#EF9A9A"),
            Color.parseColor("#E57373"),
            Color.parseColor("#EF5350"),
            Color.parseColor("#F44336"),
            Color.parseColor("#E53935"),
            Color.parseColor("#D32F2F"),
            Color.parseColor("#C62828"),
            Color.parseColor("#871C1C")
    };
    public static int[] purpleColors = new int[] {
            Color.parseColor("#F3E5F5"),
            Color.parseColor("#E1BEE7"),
            Color.parseColor("#CE93D8"),
            Color.parseColor("#BA68C8"),
            Color.parseColor("#AB47BC"),
            Color.parseColor("#9C27B0"),
            Color.parseColor("#8E24AA"),
            Color.parseColor("#7B1FA2"),
            Color.parseColor("#6A1B9A"),
            Color.parseColor("#4A148C")
    };

}

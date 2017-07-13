package com.jing.app.jjgallery.util;

import android.graphics.Color;
import android.util.Log;

import java.util.Random;

/**
 * Created by JingYang on 2016/7/13 0013.
 * Description:
 */
public class ColorUtils {

    private static final String TAG = "ColorUtils";
    private static Random random;

    /**
     * 随机亮色
     * 利用hsv模型，v大于0.5, h范围在out of 210-270(滤除青到蓝一半至蓝到红一半之间的颜色)定义为亮色
     * H是色彩,S是深浅,V是明暗
     * 关于HSV模型可以参考http://blog.csdn.net/viewcode/article/details/8203728
     * @return
     */
    public static int randomLightColor() {
        if (random == null) {
            random = new Random();
        }
        float[] hsv = new float[3];
        hsv[0] = random.nextFloat() * 360;
        if (hsv[0] > 210 && hsv[0] < 240) {
            hsv[0] -= 30;
        }
        else if (hsv[0] >= 240 && hsv[0] < 270) {
            hsv[0] += 30;
        }
        hsv[1] = random.nextFloat();
        hsv[2] = random.nextFloat();
        if (hsv[2] < 0.5f) {
            hsv[2] += 0.5f;
        }

//        Log.d(TAG, "hsv[" + hsv[0] + "," + hsv[1] + "," + hsv[2] + "]");
        return Color.HSVToColor(hsv);
    }

    /**
     * 随机暗色，作为背景色，配合白色文字
     * 利用hsv模型，s大于0.7, v大于0.5
     * H是色彩,S是深浅,V是明暗
     * 关于HSV模型可以参考http://blog.csdn.net/viewcode/article/details/8203728
     * @return
     */
    public static int randomWhiteTextBgColor() {
        if (random == null) {
            random = new Random();
        }
        float[] hsv = new float[3];
        hsv[0] = random.nextFloat() * 360;
        hsv[1] = random.nextFloat();
        if (hsv[1] < 0.7f) {
            hsv[1] = 0.7f + hsv[1] / 0.7f * 0.3f;
        }
        hsv[2] = random.nextFloat();
        if (hsv[2] < 0.5f) {
            hsv[2] += 0.5f;
        }

//        Log.d(TAG, "hsv[" + hsv[0] + "," + hsv[1] + "," + hsv[2] + "]");
        return Color.HSVToColor(hsv);
    }

    /**
     *
     * 随机暗色，作为背景色，配合白色文字
     * 利用hsv模型，s大于0.7, v大于0.5
     * hStart>0, hStart+hArg < 360
     * @param hStart hsv模型中h的开始值
     * @param hArg hsv模型中从hStart扫过的角度
     * @return
     */
    public static int randomBgColor(int hStart, int hArg) {
        if (random == null) {
            random = new Random();
        }
        float[] hsv = new float[3];
        hsv[0] = hStart + random.nextFloat() * hArg;
        hsv[1] = random.nextFloat();
        if (hsv[1] < 0.7f) {
            hsv[1] = 0.7f + hsv[1] / 0.7f * 0.3f;
        }
        hsv[2] = random.nextFloat();
        if (hsv[2] < 0.5f) {
            hsv[2] += 0.5f;
        }

//        Log.d(TAG, "hsv[" + hsv[0] + "," + hsv[1] + "," + hsv[2] + "]");
        return Color.HSVToColor(hsv);
    }
}

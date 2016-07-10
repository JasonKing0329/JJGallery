package com.jing.app.jjgallery.model.pub;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by Administrator on 2016/7/10 0010.
 */
public class DrawableUtils {
    public static StateListDrawable getStateDrawable(Context context, int idNormal, int idPressed) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
//        Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
//        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
//        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
    public static StateListDrawable getStateDrawable(Context context, Drawable normalDrawable, Drawable pressDrawable) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressDrawable);
        sd.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        sd.addState(new int[]{}, normalDrawable);
        return sd;
    }
    public static StateListDrawable getColorStateDrawable(Context context, int normalColor, int pressColor) {
        StateListDrawable sd = new StateListDrawable();
        ColorDrawable normal = new ColorDrawable(normalColor);
        ColorDrawable pressed = new ColorDrawable(pressColor);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
}

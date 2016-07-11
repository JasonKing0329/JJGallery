package com.jing.app.jjgallery.viewsystem.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jing.app.jjgallery.R;

/**
 * Created by Administrator on 2016/7/11 0011.
 */
public class SlidingViewManager {

    private View slidingLeftView;
    private View slidingRightView;
    private View.OnClickListener leftListener;
    private View.OnClickListener rightListener;

    public SlidingViewManager(Context context, int leftLayoutRes, int rightLayoutRes){
        slidingLeftView = LayoutInflater.from(context).inflate(leftLayoutRes, null);
        slidingRightView = LayoutInflater.from(context).inflate(rightLayoutRes, null);
        initLeftView();
    }

    public void setLeftOnClickListener(View.OnClickListener listener) {
        leftListener = listener;
    }

    public void setRightOnClickListener(View.OnClickListener listener) {
        rightListener = listener;
    }

    public View getSlidingLeftView() {
        return slidingLeftView;
    }

    public View getSlidingRightView() {
        return slidingRightView;
    }

    private void initLeftView() {
        slidingLeftView.findViewById(R.id.sliding_menu_switch).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_checkall).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_export).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_import).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_theme).setOnClickListener(leftListener);
        slidingLeftView.findViewById(R.id.sliding_menu_exit).setOnClickListener(leftListener);
    }

}

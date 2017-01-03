package com.jing.app.jjgallery.gdb.presenter;

import android.content.Context;
import android.view.MotionEvent;

import com.jing.app.jjgallery.R;

/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class TouchHelper {

    private float touchStartY;
    private float SCROLL_DISTANCE;

    public interface OnTouchActionListener {
        void onSwipeUp();
        void onSwipeBottom();
    }

    private OnTouchActionListener onTouchActionListener;

    public TouchHelper(Context context) {
        SCROLL_DISTANCE = context.getResources().getDimensionPixelSize(R.dimen.timeline_scroll_distance_action);
    }

    public void setOnTouchActionListener(OnTouchActionListener onTouchActionListener) {
        this.onTouchActionListener = onTouchActionListener;
    }

    /**
     * 如果需要使用上拉隐藏actionbar，下拉显示actionbar，需要在Activity的dispatchTouchEvent调用该方法
     * ps.不能在recyclerView上注册onTouchListener，ACTION_DOWN事件会被拦截记录不了startY的值
     * @param event
     */
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float dis = event.getRawY() - touchStartY;
                if (dis < -SCROLL_DISTANCE) {
                    if (onTouchActionListener != null) {
                        onTouchActionListener.onSwipeUp();
                    }
                } else if (dis > SCROLL_DISTANCE) {
                    if (onTouchActionListener != null) {
                        onTouchActionListener.onSwipeBottom();
                    }
                }
                break;
        }
        return true;
    }

}

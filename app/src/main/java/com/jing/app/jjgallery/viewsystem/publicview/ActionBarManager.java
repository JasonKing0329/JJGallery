package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.jing.app.jjgallery.R;

/**
 * Created by JingYang on 2016/8/2 0002.
 * Description:
 */
public class ActionBarManager {

    private float touchStartY;
    private float SCROLL_DISTANCE;
    private ActionBar mActionbar;

    public ActionBarManager(Context context, ActionBar actionBar) {
        mActionbar = actionBar;
        SCROLL_DISTANCE = context.getResources().getDimensionPixelSize(R.dimen.timeline_scroll_distance_action);
    }

    /**
     * 如果需要使用上拉隐藏actionbar，下拉显示actionbar，需要在Activity的dispatchTouchEvent调用该方法
     * ps.不能在recyclerView上注册onTouchListener，ACTION_DOWN事件会被拦截记录不了startY的值
     * @param event
     */
    public void dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dis = event.getRawY() - touchStartY;
                if (dis < -SCROLL_DISTANCE) {
                    if (!mActionbar.isHidden()) {
                        mActionbar.hide();
                    }
                } else if (dis > SCROLL_DISTANCE) {
                    if (!mActionbar.isShowing()) {
                        mActionbar.show();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

}

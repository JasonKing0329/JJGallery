package com.jing.app.jjgallery.viewsystem.publicview;

import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;

/**
 * Created by Administrator on 2016/9/18.
 */
public abstract class CasualBaseAdapter {

    private CasualLayout casualLayout;

    public void setCasualLayout(CasualLayout layout) {
        casualLayout = layout;
    }

    public abstract int getCount();

    public abstract void clear();

    public abstract View getView(int i);

    public abstract Keyword getKeyword(int i);

    public abstract int getItemWidth(int i);

    public abstract int getItemHeight(int i);

    protected abstract void feedKeyword();

    public void notifyDataSetChanged() {
        casualLayout.notifyDataSetChanged();
    }

    public void goToShow(int animMode) {
        casualLayout.go2Show(animMode);
    }

    private float lastX;

    public void onTouchEvent(MotionEvent event) {

        if (casualLayout != null && casualLayout.getVisibility() == View.VISIBLE) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    float x = event.getX();
                    if (x - lastX > 100) {
                        casualLayout.rubKeywords();
                        feedKeyword();
                        goToShow(KeywordsFlow.ANIMATION_OUT);
                    }
                    else if (x - lastX < -100) {
                        casualLayout.rubKeywords();
                        feedKeyword();
                        goToShow(KeywordsFlow.ANIMATION_IN);
                    }
                    break;

                default:
                    break;
            }
        }
    }
}

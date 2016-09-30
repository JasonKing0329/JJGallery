package com.jing.app.jjgallery.viewsystem.publicview.starmap;

import android.view.MotionEvent;
import android.view.View;

import com.jing.app.jjgallery.viewsystem.sub.key.Keyword;
import com.jing.app.jjgallery.viewsystem.sub.key.KeywordsFlow;

/**
 * Created by Administrator on 2016/9/22.
 */
public abstract class StarMapBaseAdapter {

    private StarMapObserver mObserver;
    private StarMapLayout starMapLayout;

    public void setObserver(StarMapObserver observer) {
        mObserver = observer;
        starMapLayout = (StarMapLayout) observer;
    }

    public void notifyDataSetChanged() {
        if (mObserver != null) {
            mObserver.onDataSetChanged();
        }
    }

    public abstract void clear();

    public abstract int getCount();

    protected abstract void feedKeyword();

    public abstract Keyword getKeyword(int position);

    public abstract View createView(int position, View view);

    public abstract void bindViewData(int position, View view, int imageSize);

    public abstract int getTextHeight();

    public void goToShow(int animMode) {
        mObserver.goToShow(animMode);
    }

    private float lastX;

    public void onTouchEvent(MotionEvent event) {

        if (starMapLayout != null && starMapLayout.getVisibility() == View.VISIBLE) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    float x = event.getX();
                    if (x - lastX > 100) {
                        starMapLayout.rubKeywords();
                        feedKeyword();
                        goToShow(KeywordsFlow.ANIMATION_OUT);
                    }
                    else if (x - lastX < -100) {
                        starMapLayout.rubKeywords();
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

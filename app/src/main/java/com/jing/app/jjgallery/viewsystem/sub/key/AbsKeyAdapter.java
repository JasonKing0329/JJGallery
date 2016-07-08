package com.jing.app.jjgallery.viewsystem.sub.key;

import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 */
public abstract class AbsKeyAdapter {

    private List<Keyword> mKeywordList;
    private KeywordsFlow mKeywordsFlow;

    public AbsKeyAdapter(KeywordsFlow keywordsFlow) {
        mKeywordsFlow = keywordsFlow;
        keywordsFlow.setKeywordsNumber(15);
        keywordsFlow.setDuration(800l);
    }

    public void prepareKeyword() {
        mKeywordList = createKeywordList();
    }

    public void feedKeyword() {

        Collections.shuffle(mKeywordList);
        Keyword keyword = null;
        for (int i = 0; i < KeywordsFlow.MAX && i < mKeywordList.size(); i ++) {
            mKeywordsFlow.feedKeyword(mKeywordList.get(i));
        }
    }

    public void goToShow(int animMode) {
        mKeywordsFlow.go2Show(animMode);
    }

    protected abstract List<Keyword> createKeywordList();

    private float lastX;

    public void onTouchEvent(MotionEvent event) {

        if (mKeywordsFlow != null && mKeywordsFlow.getVisibility() == View.VISIBLE) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    float x = event.getX();
                    if (x - lastX > 100) {
                        mKeywordsFlow.rubKeywords();
                        feedKeyword();
                        goToShow(KeywordsFlow.ANIMATION_OUT);
                    }
                    else if (x - lastX < -100) {
                        mKeywordsFlow.rubKeywords();
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

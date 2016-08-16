package com.jing.app.jjgallery.viewsystem.main.order;

import android.content.Context;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.util.DisplayHelper;

/**
 * Created by JingYang on 2016/8/16 0016.
 * Description: 360dp手机，竖屏下item排一列横排
 * 800dp平板，竖屏下item排一列竖排，横屏下固定排两行，每行个数根据item个数决定
 */
public class CardExpandMenu extends LinearLayout implements View.OnClickListener {

    public interface OnExpandMenuClickListener {
        void onExpandMenuItemClick(int position);
    }

    private BaseAdapter mAdapter;
    private OnExpandMenuClickListener onExpandMenuClickListener;

    public CardExpandMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(new CardObserver());
            refreshView();
        }
    }

    public void setOnExpandMenuClickListener(OnExpandMenuClickListener onExpandMenuClickListener) {
        this.onExpandMenuClickListener = onExpandMenuClickListener;
    }

    private class CardObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            refreshView();
        }
    }

    private void refreshView() {
        if (DisplayHelper.isTabModel(getContext())) {// 平板
            setOrientation(VERTICAL);
            // 横屏，共两行
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                int col1 = mAdapter.getCount() / 2;
                int col2 = mAdapter.getCount() - col1;
                if (col2 > col1) {
                    col1 = col2;
                    col2 = mAdapter.getCount() / 2;
                }

                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                params.weight = 1;
                LinearLayout layout = new LinearLayout(getContext());
                addView(layout, params);
                for (int i = 0; i < col1; i ++) {
                    LayoutParams sparams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
                    sparams.weight = 1;
                    refreshViewAt(i, params);
                }

                layout = new LinearLayout(getContext());
                addView(layout, params);
                for (int i = 0; i < col2; i ++) {
                    LayoutParams sparams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
                    sparams.weight = 1;
                    refreshViewAt(col1 + i, params);
                }
            }
            // 竖屏，共1列
            else {
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i ++) {
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                    params.weight = 1;
                    refreshViewAt(i, params);
                }
            }
        }
        // 手机，共1行
        else {
            setOrientation(HORIZONTAL);
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i ++) {
                LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
                params.weight = 1;
                refreshViewAt(i, params);
            }
        }

        startChildAnimation();
    }

    private void refreshViewAt(int index, LayoutParams params) {
        if (index < getChildCount()) {
            mAdapter.getView(index, getChildAt(index), this);
        }
        else {
            addView(mAdapter.getView(index, null, this), params);
        }
        getChildAt(index).setTag(index);
        getChildAt(index).setOnClickListener(this);
    }

    private void startChildAnimation() {
        for (int i = 0; i < getChildCount(); i ++) {
            getChildAt(i).startAnimation(getAppearAnimation(i));
        }
    }

    private Animation getAppearAnimation(int index) {
        Animation anim = new AlphaAnimation(0, 1);
        anim.setStartOffset(index * 200);
        anim.setDuration(300);
        return anim;
    }

    @Override
    public void onClick(View v) {
        if (onExpandMenuClickListener != null) {
            int position = (int) v.getTag();
            onExpandMenuClickListener.onExpandMenuItemClick(position);
        }
    }

}

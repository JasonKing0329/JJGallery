package com.jing.app.jjgallery.gdb.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.king.service.gdb.bean.StarCountBean;
import com.shizhefei.view.indicator.Indicator;

/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class StarIndicatorAdapter extends Indicator.IndicatorAdapter {

    private String[] mTabs = new String[]{
            "All", "1", "0", "0.5"
    };

    @Override
    public int getCount() {
        return mTabs.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_top, parent, false);
        }
        TextView textView = (TextView) convertView;
        //用了固定宽度可以避免TextView文字大小变化，tab宽度变化导致tab抖动现象
//        textView.setWidth(DisplayUtil.dipToPix(getApplicationContext(),50));
        textView.setText(mTabs[position]);
        return convertView;
    }

    public void updateStarCountBean(StarCountBean bean) {
        mTabs[0] = "All (" + bean.getAllNumber() + ")";
        mTabs[1] = "1 (" + bean.getTopNumber() + ")";
        mTabs[2] = "0 (" + bean.getBottomNumber() + ")";
        mTabs[3] = "0.5 (" + bean.getHalfNumber() + ")";
    }
}

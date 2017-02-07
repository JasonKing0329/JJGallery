package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 景阳 on 2016/10/31.
 * 嵌套在ScrollView中，自动扩充wrap全部子项
 */

public class StretchListView extends ListView {
    public StretchListView(Context context) {
        super(context);
    }

    public StretchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

package com.jing.app.jjgallery.viewsystem.publicview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.util.ColorUtils;
import com.king.lib.saveas.ScreenUtils;

import java.util.List;

/**
 * 描述: 充满全屏宽度的横向列表，列表item为纵向布局圆形背景TextView+普通TextView，圆形背景TextView自适应大小
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/10 16:40
 */
public class PointDescLayout extends LinearLayout {

    public PointDescLayout(Context context) {
        super(context);
        init();
    }

    public PointDescLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    public void addPoint(List<String> keys, List<String> contents) {

        if (keys.size() == 0) {
            return;
        }

        int itemMargin = ScreenUtils.dp2px(getContext(), 15);
        int itemWidth = (ScreenUtils.getScreenWidth(getContext()) - itemMargin * (keys.size() + 1)) / keys.size();
        int maxWidth = getContext().getResources().getDimensionPixelSize(R.dimen.gdb_record_fk_size_max);
        if (itemWidth > maxWidth) {
            itemWidth = maxWidth;
        }

        for (int i = 0; i < keys.size(); i ++) {
            LinearLayout group = new LinearLayout(getContext());
            group.setOrientation(VERTICAL);
            group.setGravity(Gravity.CENTER_HORIZONTAL);

            LayoutParams groupParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            groupParams.weight = 1;
            addView(group, groupParams);

            TextView tvKey = new TextView(getContext());
            tvKey.setText(keys.get(i));
            tvKey.setTextColor(Color.WHITE);
            tvKey.setGravity(Gravity.CENTER);
            tvKey.setBackground(getContext().getResources().getDrawable(R.drawable.shape_oval_common));
            updateBackground(tvKey);
            LayoutParams keyParams = new LayoutParams(itemWidth, itemWidth);
            group.addView(tvKey, keyParams);

            TextView tvContent = new TextView(getContext());
            tvContent.setText(contents.get(i));
//            tvContent.setTextColor(getContext().getResources().getColor(R.color.gray));
            LayoutParams contentParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            group.addView(tvContent, contentParams);
        }
    }

    /**
     * 随机生成适用于白色文字的背景
     * @param view
     */
    private void updateBackground(View view) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(ColorUtils.randomWhiteTextBgColor());
    }
}

package com.jing.app.jjgallery.gdb.view.game.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.utils.ColorUtils;
import com.king.lib.tool.ui.RippleFactory;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13 0013.
 * 如果一行，最多只能4个
 * 大于4个，每行最多5个
 */

public class CoachSeasonView extends LinearLayout implements View.OnClickListener {

    private OnCoachSeasonListener onCoachSeasonListener;
    private CoachBean coach;

    public CoachSeasonView(Context context) {
        super(context);
        init();
    }

    public CoachSeasonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    public void setOnCoachSeasonListener(OnCoachSeasonListener onCoachSeasonListener) {
        this.onCoachSeasonListener = onCoachSeasonListener;
    }

    public void setDatas(CoachBean coach, List<SeasonBean> seasonList) {
        this.coach = coach;
        removeAllViews();
        if (seasonList != null) {
            int row = (seasonList.size() - 1) / 5 + 1;
            int column = 5;
            if (seasonList.size() == 5) {
                row = 2;
            }
            if (seasonList.size() < 5) {
                column = seasonList.size();
            }
            else {
                column = (seasonList.size() - 1) / 2 + 1;
            }
            // 超过2行后，每行都按5个算
            if (column > 5) {
                column = 5;
            }

            // 小于5个直接在this里add
            if (seasonList.size() < 5) {
                setOrientation(HORIZONTAL);
                for (int i = 0; i < seasonList.size(); i ++) {
                    addHorItemView(this, i, seasonList.get(i));
                }
            }
            else {
                setOrientation(VERTICAL);
                for (int i = 0; i < row; i ++) {
                    LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(HORIZONTAL);
                    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    params.weight = 1;
                    addView(layout, params);
                    for (int j = 0; j < column; j ++) {
                        if (i * column + j >= seasonList.size()) {
                            break;
                        }
                        addHorItemView(layout, i * column + j, seasonList.get(i * column + j));
                    }
                }
            }
        }
    }

    private void addHorItemView(LinearLayout parent, int index, SeasonBean seasonBean) {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        TextView view = new TextView(getContext());
        view.setBackground(RippleFactory.getRippleBackground(ColorUtils.redColors[index % ColorUtils.redColors.length]
            , getResources().getColor(R.color.darkgray)));
        if (index % ColorUtils.redColors.length < ColorUtils.DARK_TEXT_FLAG) {
            view.setTextColor(getResources().getColor(R.color.grey));
        }
        else {
            view.setTextColor(getResources().getColor(R.color.white));
        }
        view.setText("S" + seasonBean.getSequence());
        view.setTag(seasonBean);
        view.setOnClickListener(this);
        view.setGravity(Gravity.CENTER);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 26);
        parent.addView(view, params);
    }

    @Override
    public void onClick(View v) {
        if (onCoachSeasonListener != null) {
            SeasonBean seasonBean = (SeasonBean) v.getTag();
            onCoachSeasonListener.onClickCoachSeason(coach, seasonBean);
        }
    }

    public interface OnCoachSeasonListener {
        void onClickCoachSeason(CoachBean coach, SeasonBean season);
    }
}

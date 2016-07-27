package com.jing.app.jjgallery.viewsystem.main.timeline;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.ActivityManager;

/**
 * Created by JingYang on 2016/7/25 0025.
 * Description:
 */
public class RightMenuPage implements View.OnClickListener {

    public static final int INDEX_TIMELINE = 0;
    public static final int INDEX_WATERFALL = 1;

    private View currentGroup;
    private View focusBgGroup;

    public RightMenuPage(View content, int focusIndex) {
        content.findViewById(R.id.sliding_right_timeline).setOnClickListener(this);
        content.findViewById(R.id.sliding_right_waterfall).setOnClickListener(this);

        if (focusIndex == INDEX_WATERFALL) {
            currentGroup = content.findViewById(R.id.sliding_right_waterfall);
            focusBgGroup = content.findViewById(R.id.sliding_right_waterfall_bk);
        }
        else if (focusIndex == INDEX_TIMELINE) {
            currentGroup = content.findViewById(R.id.sliding_right_timeline);
            focusBgGroup = content.findViewById(R.id.sliding_right_timeline_bk);
        }
        updateCurrentGroup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sliding_right_timeline:
                if (v != currentGroup) {
                    Activity from = (Activity) currentGroup.getContext();
                    ActivityManager.startTimeLineActivity(from);
                    from.finish();
                }
                break;
            case R.id.sliding_right_waterfall:
                if (v != currentGroup) {
                    Activity from = (Activity) currentGroup.getContext();
                    ActivityManager.startWaterfallActivity((Activity) currentGroup.getContext());
                    from.finish();
                }
                break;
        }
    }

    private void updateCurrentGroup() {
        focusBgGroup.setBackgroundColor(Color.argb(0x99, 0x21, 0x86, 0x76));
    }

}

package com.jing.app.jjgallery.viewsystem.main.order;

import android.graphics.Color;
import android.os.Handler;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.PreferenceValue;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.main.AbsHomeActivity;

/**
 * Created by JingYang on 2016/7/25 0025.
 * Description:
 */
public class RightMenuPage implements View.OnClickListener {
    private ISOrderView sorderView;
    private View currentGroup;
    private View focusBgGroup;

    public RightMenuPage(View content, ISOrderView callback) {
        sorderView = callback;
        content.findViewById(R.id.sliding_right_sorder_grid).setOnClickListener(this);
        content.findViewById(R.id.sliding_right_sorder_thumb).setOnClickListener(this);
        content.findViewById(R.id.sliding_right_sorder_index).setOnClickListener(this);
        content.findViewById(R.id.sliding_right_sorder_access).setOnClickListener(this);

        String mode = SettingProperties.getSOrderDefaultMode(content.getContext());
        if (PreferenceValue.VALUE_SORDER_VIEW_THUMB.equals(mode)) {
            currentGroup = content.findViewById(R.id.sliding_right_sorder_thumb);
            focusBgGroup = content.findViewById(R.id.sliding_right_sorder_thumb_bk);
        }
        else if (PreferenceValue.VALUE_SORDER_VIEW_INDEX.equals(mode)) {
            currentGroup = content.findViewById(R.id.sliding_right_sorder_index);
            focusBgGroup = content.findViewById(R.id.sliding_right_sorder_index_bk);
        }
        else if (PreferenceValue.VALUE_SORDER_VIEW_ACCESS.equals(mode)) {
            currentGroup = content.findViewById(R.id.sliding_right_sorder_access);
            focusBgGroup = content.findViewById(R.id.sliding_right_sorder_access_bk);
        }
        else {
            currentGroup = content.findViewById(R.id.sliding_right_sorder_grid);
            focusBgGroup = content.findViewById(R.id.sliding_right_sorder_grid_bk);
        }
        updateCurrentGroup();
    }

    @Override
    public void onClick(View v) {
        if (v != currentGroup) {
            currentGroup = v;
            focusBgGroup.setBackground(null);
            focusBgGroup = (View) v.getParent();
            updateCurrentGroup();
            if (sorderView != null) {
                switch (v.getId()) {
                    case R.id.sliding_right_sorder_grid:
                        sorderView.onGridPage();
                        break;
                    case R.id.sliding_right_sorder_thumb:
                        sorderView.onThumbPage();
                        break;
                    case R.id.sliding_right_sorder_index:
                        sorderView.onIndexPage();
                        break;
                    case R.id.sliding_right_sorder_access:
                        sorderView.onAccessCountPage();
                        break;
                }
            }
            closeMenu();
        }
    }

    private void closeMenu() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ((AbsHomeActivity) currentGroup.getContext()).toggle();
            }
        }, 500);
    }

    private void updateCurrentGroup() {
        focusBgGroup.setBackgroundColor(Color.argb(0x99, 0x21, 0x86, 0x76));
    }

}

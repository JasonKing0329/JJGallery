package com.jing.app.jjgallery.viewsystem.main.filesystem;

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
    private IFileManagerView filemanagerView;
    private View currentGroup;
    private View focusBgGroup;

    public RightMenuPage(View content, IFileManagerView callback) {
        filemanagerView = callback;
        content.findViewById(R.id.sliding_right_fm_list).setOnClickListener(this);
        content.findViewById(R.id.sliding_right_fm_thumb).setOnClickListener(this);
        content.findViewById(R.id.sliding_right_fm_index).setOnClickListener(this);

        String mode = SettingProperties.getFileManagerDefaultMode(content.getContext());
        if (PreferenceValue.VALUE_FM_VIEW_THUMB.equals(mode)) {
            currentGroup = content.findViewById(R.id.sliding_right_fm_thumb);
            focusBgGroup = content.findViewById(R.id.sliding_right_fm_thumb_bk);
        }
        else if (PreferenceValue.VALUE_FM_VIEW_INDEX.equals(mode)) {
            currentGroup = content.findViewById(R.id.sliding_right_fm_index);
            focusBgGroup = content.findViewById(R.id.sliding_right_fm_index_bk);
        }
        else {
            currentGroup = content.findViewById(R.id.sliding_right_fm_list);
            focusBgGroup = content.findViewById(R.id.sliding_right_fm_index_bk);
        }
        updateCurrentGroup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sliding_right_fm_list:
                if (v != currentGroup) {
                    currentGroup = v;
                    focusBgGroup.setBackground(null);
                    focusBgGroup = (View) v.getParent();
                    updateCurrentGroup();
                    if (filemanagerView != null) {
                        filemanagerView.onListPage();
                    }
                    closeMenu();
                }
                break;
            case R.id.sliding_right_fm_thumb:
                if (v != currentGroup) {
                    currentGroup = v;
                    focusBgGroup.setBackground(null);
                    focusBgGroup = (View) v.getParent();
                    updateCurrentGroup();
                    if (filemanagerView != null) {
                        filemanagerView.onThumbPage();
                    }
                    closeMenu();
                }
                break;
            case R.id.sliding_right_fm_index:
                if (v != currentGroup) {
                    currentGroup = v;
                    focusBgGroup.setBackground(null);
                    focusBgGroup = (View) v.getParent();
                    updateCurrentGroup();
                    if (filemanagerView != null) {
                        filemanagerView.onIndexPage();
                    }
                    closeMenu();
                }
                break;
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

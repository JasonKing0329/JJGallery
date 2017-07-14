package com.jing.app.jjgallery.gdb.view.star;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/14 15:18
 */
public class StarListSettingDialog extends CustomDialog {

    private RadioButton rbRandom;
    private RadioButton rbFix;
    private EditText etTime;

    public static final String[] ANIM_TYPES = new String[]{
            "defaultEffect", "alpha", "rotate", "cube", "flip", "accordion", "zoomFade",
            "fade", "zoomCenter", "zoomStack", "stack", "depth", "zoom", "zoomOut"/*, "parallax"*/
    };

    public StarListSettingDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestCancelAction(true);
        requestSaveAction(true);
    }

    @Override
    protected View getCustomView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dlg_gdb_star_list_nav_setting, null);
        rbRandom = (RadioButton) contentView.findViewById(R.id.rb_random);
        rbFix = (RadioButton) contentView.findViewById(R.id.rb_fixed);
        etTime = (EditText) contentView.findViewById(R.id.et_time);
        rbFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimationSelector();
            }
        });
        rbRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SettingProperties.setGdbStarListNavAnimRandom(getContext(), true);
                }
            }
        });
        rbFix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SettingProperties.setGdbStarListNavAnimRandom(getContext(), false);
                }
            }
        });

        boolean isRandom = SettingProperties.isGdbStarListNavAnimRandom(getContext());
        if (isRandom) {
            rbRandom.setChecked(true);
            rbFix.setText("Fixed");
        }
        else {
            rbFix.setChecked(true);
            try {
                rbFix.setText(formatFixedText(ANIM_TYPES[SettingProperties.getGdbStarListNavAnimType(getContext())]));
            } catch (Exception e) {
                e.printStackTrace();
                rbFix.setText(formatFixedText(ANIM_TYPES[0]));
            }
        }

        etTime.setText(String.valueOf(SettingProperties.getGdbStarListNavAnimTime(getContext())));
        return contentView;
    }

    private String formatFixedText(String type) {
        return "Fixed (" + type + ")";
    }

    private void showAnimationSelector() {
        new AlertDialog.Builder(getContext())
                .setTitle(null)
                .setItems(ANIM_TYPES, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rbFix.setText(formatFixedText(ANIM_TYPES[which]));
                        SettingProperties.setGdbStarListNavAnimType(getContext(), which);
                    }
                }).show();
    }

    @Override
    protected View getCustomToolbar() {
        setTitle(getContext().getString(R.string.gdb_nav_setting));
        return null;
    }

    @Override
    public void onClick(View view) {
        if (view == saveIcon) {
            int time;
            try {
                time = Integer.parseInt(etTime.getText().toString());
            } catch (Exception e) {
                time = 0;
            }
            // 至少2S
            if (time < 2000) {
                time = 2000;
            }
            SettingProperties.setGdbStarListNavAnimTime(getContext(), time);
            actionListener.onSave(null);
        }
        super.onClick(view);
    }
}

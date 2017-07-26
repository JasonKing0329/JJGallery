package com.jing.app.jjgallery.gdb.view.recommend;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.recommend.FilterModel;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class RecordFilterDialog extends CustomDialog {

    private RecyclerView recyclerView;
    private FilterModel mFilterModel;
    private FilterAdapter mAdapter;
    private RadioButton rbRandom;
    private RadioButton rbFix;
    private EditText etTime;
    private CheckBox cbNr;

    public static final String[] ANIM_TYPES = new String[]{
            "defaultEffect", "alpha", "rotate", "cube", "flip", "accordion", "zoomFade",
            "fade", "zoomCenter", "zoomStack", "stack", "depth", "zoom", "zoomOut"/*, "parallax"*/
    };

    public RecordFilterDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
        requestSaveAction(true);
        requestCancelAction(true);
        applyLightThemeStyle();
        setTitle(getContext().getString(R.string.gdb_rec_filter_title));

        HashMap<String, Object> map = new HashMap<>();
        actionListener.onLoadData(map);
        mFilterModel = (FilterModel) map.get("model");
        cbNr.setChecked(mFilterModel.isSupportNR());

        mAdapter = new FilterAdapter(mFilterModel.getList());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected View getCustomView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.gdb_recommend_filter, null);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.recommend_list);
        rbRandom = (RadioButton) contentView.findViewById(R.id.rb_random);
        rbFix = (RadioButton) contentView.findViewById(R.id.rb_fixed);
        etTime = (EditText) contentView.findViewById(R.id.et_time);
        cbNr = (CheckBox) contentView.findViewById(R.id.cb_nr);
        cbNr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFilterModel.setSupportNR(isChecked);
            }
        });
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
                    SettingProperties.setGdbRecmmendAnimRandom(getContext(), true);
                }
            }
        });
        rbFix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SettingProperties.setGdbRecmmendAnimRandom(getContext(), false);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(animator);

        boolean isRandom = SettingProperties.isGdbRecmmendAnimRandom(getContext());
        if (isRandom) {
            rbRandom.setChecked(true);
            rbFix.setText("Fixed");
        }
        else {
            rbFix.setChecked(true);
            try {
                rbFix.setText(formatFixedText(ANIM_TYPES[SettingProperties.getGdbRecommendAnimType(getContext())]));
            } catch (Exception e) {
                e.printStackTrace();
                rbFix.setText(formatFixedText(ANIM_TYPES[0]));
            }
        }

        etTime.setText(String.valueOf(SettingProperties.getGdbRecommendAnimTime(getContext())));
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
                        SettingProperties.setGdbRecommendAnimType(getContext(), which);
                    }
                }).show();
    }

    @Override
    protected View getCustomToolbar() {
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
            SettingProperties.setGdbRecommendAnimTime(getContext(), time);
            actionListener.onSave(mFilterModel);
        }
        super.onClick(view);
    }
}

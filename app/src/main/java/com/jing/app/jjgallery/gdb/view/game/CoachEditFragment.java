package com.jing.app.jjgallery.gdb.view.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.gdb.view.game.custom.CoachSeasonView;
import com.jing.app.jjgallery.gdb.view.game.custom.TextListSelector;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbActivity;
import com.king.lib.tool.ui.RippleFactory;
import com.king.service.gdb.game.bean.CoachBean;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class CoachEditFragment extends GameEditFragment {

    private final int REQUEST_IMAGE = 211;
    private ImageView ivCoach;
    private EditText etName;
    private TextView tvType;
    private TextView tvCount;
    private CoachSeasonView coachSeasonView;
    private ICoachManager coachManager;

    private CoachBean coachBean;

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_coach_edit;
    }

    @Override
    protected View getActionSaveView(View contentView) {
        return contentView.findViewById(R.id.coach_edit_save);
    }

    @Override
    protected void initSubView(View contentView, Bundle bundle) {

        coachManager = (ICoachManager) gameManager;

        tvType = (TextView) contentView.findViewById(R.id.coach_edit_type);
        tvCount = (TextView) contentView.findViewById(R.id.coach_edit_count);
        etName = (EditText) contentView.findViewById(R.id.coach_edit_name);
        ivCoach = (ImageView) contentView.findViewById(R.id.coach_edit_image);
        coachSeasonView = (CoachSeasonView) contentView.findViewById(R.id.coach_seasonview);
        ivCoach.setOnClickListener(this);
        contentView.findViewById(R.id.coach_edit_type_group).setOnClickListener(this);

        actionSave.setBackground(RippleFactory.getRippleBackground(
                getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.darkgray)
        ));

        initData(bundle);
    }

    private void initData(Bundle bundle) {
        if (bundle != null && bundle.getBoolean(KEY_INIT_WITH_DATA)) {
            int id = bundle.getInt(KEY_ID);
            coachBean = coachManager.getPresenter().getCoachById(id);
            tvCount.setText(String.valueOf(coachBean.getSeasonCount()));
            tvType.setText(GameValueMap.fromCoachType(coachBean.getType()));
            etName.setText(coachBean.getName());
            SImageLoader.getInstance().displayImage(coachBean.getImagePath(), ivCoach);

            coachSeasonView.setDatas(coachBean, coachManager.getCoachSeasonManager().getCoachSeasonList(coachBean));
        }
        else {
            coachBean = new CoachBean();
            coachBean.setSeasonCount(0);
            coachBean.setType("Half");
        }
    }

    @Override
    protected void onActionSave() {
        String name = etName.getText().toString();
        coachBean.setName(name);
        coachBean.setType(GameValueMap.toCoachType(tvType.getText().toString()));
        coachManager.onSaveData(coachBean);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.coach_edit_image:
                selectImage(REQUEST_IMAGE);
                break;
            case R.id.coach_edit_type_group:
                new TextListSelector().show(getActivity(), new String[]{"Top", "Bottom", "Half"}
                        , new TextListSelector.OnListItemSelectListener() {
                            @Override
                            public void onSelect(String text) {
                                tvType.setText(text);
                            }
                        });
                break;
        }
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent().setClass(getActivity(), ThumbActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    String imagePath = data.getStringExtra(Constants.KEY_THUMBFOLDER_CHOOSE_CONTENT);
                    coachBean.setImagePath(imagePath);
                    SImageLoader.getInstance().displayImage(imagePath, ivCoach);
                }
                break;
        }
    }
}

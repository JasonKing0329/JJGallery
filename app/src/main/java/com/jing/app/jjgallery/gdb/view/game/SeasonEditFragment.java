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
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbActivity;
import com.king.lib.tool.ui.RippleFactory;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class SeasonEditFragment extends GameEditFragment {

    private final int REQUEST_COACH1 = 201;
    private final int REQUEST_COACH2 = 202;
    private final int REQUEST_COACH3 = 203;
    private final int REQUEST_COACH4 = 204;
    private final int REQUEST_COVER = 205;

    private TextView tvSequence;
    private TextView tvRule;
    private EditText etName;
    private ImageView ivCoach1;
    private ImageView ivCoach2;
    private ImageView ivCoach3;
    private ImageView ivCoach4;
    private ImageView ivCover;

    private SeasonBean seasonBean;

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_season_edit;
    }

    @Override
    protected View getActionSaveView(View contentView) {
        return contentView.findViewById(R.id.season_edit_save);
    }

    @Override
    protected void initSubView(View contentView, Bundle bundle) {

        tvSequence = (TextView) contentView.findViewById(R.id.season_edit_sequence_tv);
        tvRule = (TextView) contentView.findViewById(R.id.season_edit_rule_tv);
        etName = (EditText) contentView.findViewById(R.id.season_edit_name);
        ivCoach1 = (ImageView) contentView.findViewById(R.id.season_edit_coach1);
        ivCoach2 = (ImageView) contentView.findViewById(R.id.season_edit_coach2);
        ivCoach3 = (ImageView) contentView.findViewById(R.id.season_edit_coach3);
        ivCoach4 = (ImageView) contentView.findViewById(R.id.season_edit_coach4);
        ivCover = (ImageView) contentView.findViewById(R.id.season_edit_cover);
        ivCoach1.setOnClickListener(this);
        ivCoach2.setOnClickListener(this);
        ivCoach3.setOnClickListener(this);
        ivCoach4.setOnClickListener(this);
        ivCover.setOnClickListener(this);
        contentView.findViewById(R.id.season_edit_sequence_add).setOnClickListener(this);
        contentView.findViewById(R.id.season_edit_sequence_minus).setOnClickListener(this);
        contentView.findViewById(R.id.season_edit_rule).setOnClickListener(this);

        actionSave.setBackground(RippleFactory.getRippleBackground(
                getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.darkgray)
        ));

        initData(bundle);
    }

    private void initData(Bundle bundle) {
        if (bundle != null && bundle.getBoolean(KEY_INIT_WITH_DATA)) {
            int id = bundle.getInt(KEY_ID);
            seasonBean = gameManager.getPresenter().getSeasonById(id);
            tvRule.setText(seasonBean.getMatchRule() == 0 ? "default":String.valueOf(seasonBean.getMatchRule()));
            tvSequence.setText(String.valueOf(seasonBean.getSequence()));
            etName.setText(seasonBean.getName());
            SImageLoader.getInstance().displayImage(seasonBean.getCoverPath(), ivCover);

            CoachBean coach1 = gameManager.getPresenter().getCoachById(seasonBean.getCoachId1());
            CoachBean coach2 = gameManager.getPresenter().getCoachById(seasonBean.getCoachId2());
            CoachBean coach3 = gameManager.getPresenter().getCoachById(seasonBean.getCoachId3());
            CoachBean coach4 = gameManager.getPresenter().getCoachById(seasonBean.getCoachId4());
            SImageLoader.getInstance().displayImage(coach1.getImagePath(), ivCoach1);
            SImageLoader.getInstance().displayImage(coach2.getImagePath(), ivCoach2);
            SImageLoader.getInstance().displayImage(coach3.getImagePath(), ivCoach3);
            SImageLoader.getInstance().displayImage(coach4.getImagePath(), ivCoach4);
        }
        else {
            seasonBean = new SeasonBean();
            seasonBean.setSequence(1);
            seasonBean.setMatchRule(0);
            seasonBean.setCoachId1(-1);
            seasonBean.setCoachId1(-1);
            seasonBean.setCoachId1(-1);
            seasonBean.setCoachId1(-1);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.season_edit_sequence_add:
                int sequence = Integer.parseInt(tvSequence.getText().toString());
                sequence ++;
                tvSequence.setText(String.valueOf(sequence));
                break;
            case R.id.season_edit_sequence_minus:
                sequence = Integer.parseInt(tvSequence.getText().toString());
                if (sequence > 1) {
                    sequence --;
                    tvSequence.setText(String.valueOf(sequence));
                }
                break;
            case R.id.season_edit_rule:
                break;
            case R.id.season_edit_coach1:
                selectCoach(REQUEST_COACH1);
                break;
            case R.id.season_edit_coach2:
                selectCoach(REQUEST_COACH2);
                break;
            case R.id.season_edit_coach3:
                selectCoach(REQUEST_COACH3);
                break;
            case R.id.season_edit_coach4:
                selectCoach(REQUEST_COACH4);
                break;
            case R.id.season_edit_cover:
                selectImage(REQUEST_COVER);
                break;
        }
    }

    @Override
    protected void onActionSave() {
        String name = etName.getText().toString();
        seasonBean.setName(name);
        seasonBean.setSequence(Integer.parseInt(tvSequence.getText().toString()));
        gameManager.onSaveData(seasonBean);
    }

    private void selectCoach(int requestCode) {
        Intent intent = new Intent().setClass(getActivity(), CoachActivity.class);
        startActivityForResult(intent, requestCode);
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent().setClass(getActivity(), ThumbActivity.class);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_COACH1:
                if (resultCode == Activity.RESULT_OK) {
                    setCoach(ivCoach1, data.getIntExtra(CoachActivity.RESP_COACH_ID, -1));
                }
                break;
            case REQUEST_COACH2:
                if (resultCode == Activity.RESULT_OK) {
                    setCoach(ivCoach2, data.getIntExtra(CoachActivity.RESP_COACH_ID, -1));
                }
                break;
            case REQUEST_COACH3:
                if (resultCode == Activity.RESULT_OK) {
                    setCoach(ivCoach3, data.getIntExtra(CoachActivity.RESP_COACH_ID, -1));
                }
                break;
            case REQUEST_COACH4:
                if (resultCode == Activity.RESULT_OK) {
                    setCoach(ivCoach4, data.getIntExtra(CoachActivity.RESP_COACH_ID, -1));
                }
                break;
            case REQUEST_COVER:
                if (resultCode == Activity.RESULT_OK) {
                    setCover(data.getStringExtra(Constants.KEY_THUMBFOLDER_CHOOSE_CONTENT));
                }
                break;
            default:
                break;
        }
    }

    private void setCoach(ImageView imageView, int coachId) {
        // 已选coach不能重复选择
        if (coachIsSelected(coachId)) {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_game_repeat_coach), ProgressProvider.TOAST_WARNING);
            return;
        }
        CoachBean coach = gameManager.getPresenter().getCoachById(coachId);
        if (coach != null) {
            if (imageView == ivCoach1) {
                seasonBean.setCoachId1(coachId);
            }
            else if (imageView == ivCoach2) {
                seasonBean.setCoachId2(coachId);
            }
            else if (imageView == ivCoach3) {
                seasonBean.setCoachId3(coachId);
            }
            else if (imageView == ivCoach4) {
                seasonBean.setCoachId4(coachId);
            }
            SImageLoader.getInstance().displayImage(coach.getImagePath(), imageView);
        }
    }

    private boolean coachIsSelected(int coachId) {
        return seasonBean.getCoachId1() == coachId || seasonBean.getCoachId2() == coachId
                || seasonBean.getCoachId3() == coachId || seasonBean.getCoachId4() == coachId;
    }

    public void setCover(String coverPath) {
        DebugLog.e(coverPath);
        seasonBean.setCoverPath(coverPath);
        SImageLoader.getInstance().displayImage(coverPath, ivCover);
    }
}

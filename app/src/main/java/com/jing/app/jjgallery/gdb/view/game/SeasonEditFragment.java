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
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbActivity;
import com.king.lib.tool.ui.RippleFactory;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class SeasonEditFragment extends GameEditFragment implements View.OnClickListener {

    public static final String KEY_INIT_WITH_DATA = "init_with_data";
    public static final String KEY_SEASON_ID = "season_id";

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
    private TextView tvSave;

    private SeasonBean seasonBean;

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_season_edit;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {

        tvSave = (TextView) contentView.findViewById(R.id.season_edit_save);
        tvSequence = (TextView) contentView.findViewById(R.id.season_edit_sequence_tv);
        tvRule = (TextView) contentView.findViewById(R.id.season_edit_rule_tv);
        etName = (EditText) contentView.findViewById(R.id.season_edit_name);
        ivCoach1 = (ImageView) contentView.findViewById(R.id.season_edit_coach1);
        ivCoach2 = (ImageView) contentView.findViewById(R.id.season_edit_coach2);
        ivCoach3 = (ImageView) contentView.findViewById(R.id.season_edit_coach3);
        ivCoach4 = (ImageView) contentView.findViewById(R.id.season_edit_coach4);
        ivCover = (ImageView) contentView.findViewById(R.id.season_edit_cover);
        tvSave.setOnClickListener(this);
        ivCoach1.setOnClickListener(this);
        ivCoach2.setOnClickListener(this);
        ivCoach3.setOnClickListener(this);
        ivCoach4.setOnClickListener(this);
        ivCover.setOnClickListener(this);
        contentView.findViewById(R.id.season_edit_sequence).setOnClickListener(this);
        contentView.findViewById(R.id.season_edit_rule).setOnClickListener(this);

        tvSave.setBackground(RippleFactory.getRippleBackground(
                getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.darkgray)
        ));

        initData(bundle);
    }

    private void initData(Bundle bundle) {
        if (bundle.getBoolean(KEY_INIT_WITH_DATA)) {
            int id = bundle.getInt(KEY_SEASON_ID);
            seasonBean = gameManager.getPresenter().getSeasonById(id);
            tvRule.setText(seasonBean.getMatchRule() == 0 ? "default":String.valueOf(seasonBean.getMatchRule()));
            tvSequence.setText(String.valueOf(seasonBean.getSequence()));
            etName.setText(seasonBean.getName());
            SImageLoader.getInstance().displayImage(seasonBean.getCoverPath(), ivCover);
            //FIXME init coach
        }
        else {
            seasonBean = new SeasonBean();
            seasonBean.setSequence(1);
            seasonBean.setMatchRule(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.season_edit_save:
                String name = etName.getText().toString();
                seasonBean.setName(name);
                gameManager.onSaveData(seasonBean);
                break;
            case R.id.season_edit_sequence:
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

    private void selectCoach(int requestCode) {
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
                break;
            case REQUEST_COACH2:
                break;
            case REQUEST_COACH3:
                break;
            case REQUEST_COACH4:
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

    public void setCover(String coverPath) {
        DebugLog.e(coverPath);
        seasonBean.setCoverPath(coverPath);
        SImageLoader.getInstance().displayImage(coverPath, ivCover);
    }
}

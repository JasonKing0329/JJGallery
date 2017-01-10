package com.jing.app.jjgallery.gdb.view.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.viewsystem.sub.thumb.ThumbActivity;
import com.king.lib.tool.ui.RippleFactory;
import com.king.service.gdb.game.bean.SeasonBean;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class SeasonEditFragment extends GameFragment implements View.OnClickListener {

    public static final String KEY_INIT_WITH_DATA = "init_with_data";
    public static final String KEY_SEASON_ID = "season_id";

    private final int REQUEST_COACH1 = 201;
    private final int REQUEST_COACH2 = 202;
    private final int REQUEST_COACH3 = 203;
    private final int REQUEST_COACH4 = 204;
    private final int REQUEST_COVER = 205;

    private ISeasonManager seasonManager;

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
            seasonBean = seasonManager.getPresenter().getSeasonById(id);
            tvRule.setText(seasonBean.getMatchRule() == 0 ? "default":String.valueOf(seasonBean.getMatchRule()));
            tvSequence.setText(String.valueOf(seasonBean.getSequence()));
            etName.setText(seasonBean.getName());
            //FIXME init coach
        }
        else {
            seasonBean = new SeasonBean();
            seasonBean.setSequence(1);
            seasonBean.setMatchRule(0);
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        seasonManager = (ISeasonManager) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.season_edit_save:
                String name = etName.getText().toString();
                seasonBean.setName(name);
                seasonManager.onSaveSeasonBean(seasonBean);
                break;
            case R.id.season_edit_sequence:
                break;
            case R.id.season_edit_rule:
                break;
            case R.id.season_edit_coach1:
                selectImage(REQUEST_COACH1);
                break;
            case R.id.season_edit_coach2:
                selectImage(REQUEST_COACH2);
                break;
            case R.id.season_edit_coach3:
                selectImage(REQUEST_COACH3);
                break;
            case R.id.season_edit_coach4:
                selectImage(REQUEST_COACH4);
                break;
            case R.id.season_edit_cover:
                selectImage(REQUEST_COVER);
                break;
        }
    }

    private void selectImage(int requestCode) {
        Intent intent = new Intent().setClass(getActivity(), ThumbActivity.class);
        getActivity().startActivityForResult(intent, requestCode);
    }
}

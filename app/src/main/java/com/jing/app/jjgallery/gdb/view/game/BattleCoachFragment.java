package com.jing.app.jjgallery.gdb.view.game;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.IBattleView;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:38
 */
public class BattleCoachFragment extends GameFragment implements View.OnClickListener {

    private IBattleView battleView;

    private ImageView ivCoach1;
    private ImageView ivCoach2;
    private ImageView ivCoach3;
    private ImageView ivCoach4;
    private TextView tvCoach1;
    private TextView tvCoach2;
    private TextView tvCoach3;
    private TextView tvCoach4;

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_battle_coach;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {
        battleView.getActionbar().clearActionIcon();
        battleView.getActionbar().addBackIcon();
        ivCoach1 = (ImageView) contentView.findViewById(R.id.battle_coach1_image);
        tvCoach1 = (TextView) contentView.findViewById(R.id.battle_coach1_text);
        contentView.findViewById(R.id.battle_coach1).setOnClickListener(this);
        ivCoach2 = (ImageView) contentView.findViewById(R.id.battle_coach2_image);
        tvCoach2 = (TextView) contentView.findViewById(R.id.battle_coach2_text);
        contentView.findViewById(R.id.battle_coach2).setOnClickListener(this);
        ivCoach3 = (ImageView) contentView.findViewById(R.id.battle_coach3_image);
        tvCoach3 = (TextView) contentView.findViewById(R.id.battle_coach3_text);
        contentView.findViewById(R.id.battle_coach3).setOnClickListener(this);
        ivCoach4 = (ImageView) contentView.findViewById(R.id.battle_coach4_image);
        tvCoach4 = (TextView) contentView.findViewById(R.id.battle_coach4_text);
        contentView.findViewById(R.id.battle_coach4).setOnClickListener(this);

        showCoaches();
    }

    private void showCoaches() {
        SImageLoader.getInstance().displayImage(battleView.getBattleData().getCoach1().getImagePath(), ivCoach1);
        SImageLoader.getInstance().displayImage(battleView.getBattleData().getCoach2().getImagePath(), ivCoach2);
        SImageLoader.getInstance().displayImage(battleView.getBattleData().getCoach3().getImagePath(), ivCoach3);
        SImageLoader.getInstance().displayImage(battleView.getBattleData().getCoach4().getImagePath(), ivCoach4);
        tvCoach1.setText("Team " + battleView.getBattleData().getCoach1().getName());
        tvCoach2.setText("Team " + battleView.getBattleData().getCoach2().getName());
        tvCoach3.setText("Team " + battleView.getBattleData().getCoach3().getName());
        tvCoach4.setText("Team " + battleView.getBattleData().getCoach4().getName());
        ((ProgressProvider) getActivity()).dismissProgressCycler();
    }

    @Override
    protected void onAttachActivity(Context context) {
        battleView = (IBattleView) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.battle_coach1:
                battleView.showCoachBattle(0);
                break;
            case R.id.battle_coach2:
                battleView.showCoachBattle(1);
                break;
            case R.id.battle_coach3:
                battleView.showCoachBattle(2);
                break;
            case R.id.battle_coach4:
                battleView.showCoachBattle(3);
                break;
        }
    }

}

package com.jing.app.jjgallery.gdb.view.game.battlecross;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 11:07
 */
public class CrossCoachFragment extends BaseCoachFragment implements View.OnClickListener {
    
    private ICrossView crossView;

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
        return R.layout.gdb_game_cross_coach;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {
        showActionbar();
        ivCoach1 = (ImageView) contentView.findViewById(R.id.cross_coach1_image);
        tvCoach1 = (TextView) contentView.findViewById(R.id.cross_coach1_text);
        ivCoach2 = (ImageView) contentView.findViewById(R.id.cross_coach2_image);
        tvCoach2 = (TextView) contentView.findViewById(R.id.cross_coach2_text);
        ivCoach3 = (ImageView) contentView.findViewById(R.id.cross_coach3_image);
        tvCoach3 = (TextView) contentView.findViewById(R.id.cross_coach3_text);
        ivCoach4 = (ImageView) contentView.findViewById(R.id.cross_coach4_image);
        tvCoach4 = (TextView) contentView.findViewById(R.id.cross_coach4_text);
        contentView.findViewById(R.id.cross_coach12).setOnClickListener(this);
        contentView.findViewById(R.id.cross_coach34).setOnClickListener(this);

        showCoaches();
    }

    private void showActionbar() {
        crossView.getActionbar().clearActionIcon();
        crossView.getActionbar().addBackIcon();
    }

    private void showCoaches() {
        SImageLoader.getInstance().displayImage(crossView.getBattleData().getCoach1().getImagePath(), ivCoach1);
        SImageLoader.getInstance().displayImage(crossView.getBattleData().getCoach2().getImagePath(), ivCoach2);
        SImageLoader.getInstance().displayImage(crossView.getBattleData().getCoach3().getImagePath(), ivCoach3);
        SImageLoader.getInstance().displayImage(crossView.getBattleData().getCoach4().getImagePath(), ivCoach4);
        tvCoach1.setText(crossView.getBattleData().getCoach1().getName());
        tvCoach2.setText(crossView.getBattleData().getCoach2().getName());
        tvCoach3.setText(crossView.getBattleData().getCoach3().getName());
        tvCoach4.setText(crossView.getBattleData().getCoach4().getName());
        ((ProgressProvider) getActivity()).dismissProgressCycler();
    }

    @Override
    protected void onAttachActivity(Context context) {
        crossView = (ICrossView) context;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showActionbar();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cross_coach12:
                crossView.showCoachCross(0);
                break;
            case R.id.cross_coach34:
                crossView.showCoachCross(1);
                break;
        }
    }
}

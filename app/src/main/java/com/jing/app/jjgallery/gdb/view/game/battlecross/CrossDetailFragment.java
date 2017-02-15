package com.jing.app.jjgallery.gdb.view.game.battlecross;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BaseBattleDetailData;
import com.jing.app.jjgallery.gdb.model.game.CrossDetailData;
import com.jing.app.jjgallery.gdb.model.game.GameConstants;
import com.jing.app.jjgallery.gdb.view.game.view.BaseResultDialog;
import com.jing.app.jjgallery.gdb.view.game.view.BaseRoundManager;
import com.jing.app.jjgallery.gdb.view.game.view.CrossResultDialog;
import com.jing.app.jjgallery.gdb.view.game.view.CrossRoundManager;
import com.jing.app.jjgallery.gdb.view.game.view.OnBattleItemListener;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.king.service.gdb.game.bean.CrossBean;
import com.king.service.gdb.game.bean.CrossResultBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/15 11:08
 */
public class CrossDetailFragment extends BaseDetailFragment implements OnBattleItemListener<CrossBean> {

    private ICrossView crossView;
    private CrossDetailData detailData;

    @Override
    public IBattleCross getIBattleCross(Context context) {
        crossView = (ICrossView) context;
        return crossView;
    }

    @Override
    protected void initCoachData(int index) {
        // TODO 根据现阶段的match rule，固定coach1&coach2, coach3&coach4
        switch (index) {
            case 0:
                detailData.setCoach(crossView.getBattleData().getCoach1());
                detailData.setCoach2(crossView.getBattleData().getCoach2());
                break;
            case 1:
                detailData.setCoach(crossView.getBattleData().getCoach3());
                detailData.setCoach2(crossView.getBattleData().getCoach4());
                break;
        }
    }

    @Override
    protected BaseBattleDetailData createDetailData() {
        detailData = new CrossDetailData();
        return detailData;
    }

    @Override
    protected void initSubView(View contentView) {

        contentView.findViewById(R.id.battle_candidate_separate).setVisibility(View.VISIBLE);
        contentView.findViewById(R.id.battle_candidate_title_group).setVisibility(View.VISIBLE);
        ((TextView) contentView.findViewById(R.id.battle_candidate_coach1)).setText(detailData.getCoach().getName());
        ((TextView) contentView.findViewById(R.id.battle_candidate_coach2)).setText(detailData.getCoach2().getName());

        crossView.getPresenter().setDetailView(this);
        crossView.getPresenter().loadDetails(detailData);
    }

    @Override
    protected BaseRoundManager createRoundManager(LinearLayout llCardsContainer) {
        return new CrossRoundManager(detailData, llCardsContainer, detailData.getCrossList(), this);
    }

    @Override
    protected BaseResultDialog createResultDialog(Context context
            , CustomDialog.OnCustomDialogActionListener listener) {
        CrossResultDialog dialog = new CrossResultDialog(context, listener);
        dialog.setTitle("Cross result");
        dialog.setOnBattleResultListener(new BaseResultDialog.OnBattleResultListener<CrossResultBean>() {

            @Override
            public void onCreateBattleResultDatas(List<CrossResultBean> datas) {
                createResultBeanDatas(datas);
            }
        });
        return dialog;
    }

    @Override
    protected void onPrepareResultData(HashMap<String, Object> data) {
        data.put("battles", detailData.getBattleList());
        data.put("battleDetailBean", detailData);
    }

    protected void createResultBeanDatas(final List<CrossResultBean> datas) {
        // 数据库中已有记录提醒是否覆盖
        if (crossView.getPresenter().isCrossResultExist(detailData.getSeason().getId(), detailData.getCoach().getId(), detailData.getCoach2().getId())) {
            String msg = getResources().getString(R.string.gdb_game_battle_result_exist);
            msg = msg.replace("%s", detailData.getCoach().getName() + " & " + detailData.getCoach2().getName());
            new DefaultDialogManager().showWarningActionDialog(getActivity()
                    , msg
                    , getResources().getString(R.string.yes)
                    , null
                    , getResources().getString(R.string.no)
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                ((ProgressProvider) getActivity()).showProgressCycler();
                                // 先删除再添加
                                crossView.getPresenter().deleteCrossResults(detailData.getSeason().getId(), detailData.getCoach().getId(), detailData.getCoach2().getId());
                                saveCrossResult(datas);
                                ((ProgressProvider) getActivity()).dismissProgressCycler();
                            }
                            else {

                            }
                        }
                    });
        }
        else {
            ((ProgressProvider) getActivity()).showProgressCycler();
            saveCrossResult(datas);
            ((ProgressProvider) getActivity()).dismissProgressCycler();
        }
    }

    private void saveCrossResult(List<CrossResultBean> datas) {
        if (crossView.getPresenter().saveCrossResultBeans(datas, GameConstants.BATTLE_PROMOTE_ITEM)) {
            ((ProgressProvider) getActivity()).showToastLong("Successful", ProgressProvider.TOAST_SUCCESS);
        }
        else {
            ((ProgressProvider) getActivity()).showToastLong("Failed", ProgressProvider.TOAST_ERROR);
        }
    }

    @Override
    public PlayerBean getPlayerBean(int playerId) {
        return crossView.getPresenter().getPlayer(detailData, playerId);
    }

    @Override
    public void onAddBattleBean(CrossBean bean) {
        crossView.getPresenter().saveCrossBean(bean);
    }

    @Override
    public void onRemoveBattleBean(CrossBean bean) {
        crossView.getPresenter().deleteCrossBean(bean);
    }
}

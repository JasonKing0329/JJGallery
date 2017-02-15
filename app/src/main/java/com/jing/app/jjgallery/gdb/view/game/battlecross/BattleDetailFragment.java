package com.jing.app.jjgallery.gdb.view.game.battlecross;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BaseBattleDetailData;
import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.jing.app.jjgallery.gdb.model.game.GameConstants;
import com.jing.app.jjgallery.gdb.view.game.view.BaseResultDialog;
import com.jing.app.jjgallery.gdb.view.game.view.BaseRoundManager;
import com.jing.app.jjgallery.gdb.view.game.view.BattleResultDialog;
import com.jing.app.jjgallery.gdb.view.game.view.BattleRoundManager;
import com.jing.app.jjgallery.gdb.view.game.view.OnBattleItemListener;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.DefaultDialogManager;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.BattleResultBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:39
 */
public class BattleDetailFragment extends BaseDetailFragment implements OnBattleItemListener<BattleBean> {

    private IBattleView battleView;
    private BattleDetailData detailData;

    @Override
    public IBattleCross getIBattleCross(Context context) {
        battleView = (IBattleView) context;
        return battleView;
    }

    @Override
    protected void initCoachData(int index) {
        switch (index) {
            case 0:
                detailData.setCoach(battleView.getBattleData().getCoach1());
                break;
            case 1:
                detailData.setCoach(battleView.getBattleData().getCoach2());
                break;
            case 2:
                detailData.setCoach(battleView.getBattleData().getCoach3());
                break;
            case 3:
                detailData.setCoach(battleView.getBattleData().getCoach4());
                break;
        }
    }

    @Override
    protected BaseBattleDetailData createDetailData() {
        detailData = new BattleDetailData();
        return detailData;
    }

    @Override
    protected void initSubView(View contentView) {
        battleView.getPresenter().setDetailView(this);
        battleView.getPresenter().loadDeatails(detailData);
    }

    @Override
    protected BaseRoundManager createRoundManager(LinearLayout llCardsContainer) {
        return new BattleRoundManager(detailData, llCardsContainer, detailData.getBattleList(), this);
    }

    @Override
    protected BaseResultDialog createResultDialog(Context context
            , CustomDialog.OnCustomDialogActionListener listener) {
        BattleResultDialog dialog = new BattleResultDialog(context, listener);
        dialog.setTitle("Battle result");
        dialog.setOnBattleResultListener(new BaseResultDialog.OnBattleResultListener<BattleResultBean>() {
            @Override
            public void onCreateBattleResultDatas(final List<BattleResultBean> datas) {
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

    protected void createResultBeanDatas(final List<BattleResultBean> datas) {
        // 数据库中已有记录提醒是否覆盖
        if (battleView.getPresenter().isBattleResultExist(detailData.getSeason().getId(), detailData.getCoach().getId())) {
            String msg = getResources().getString(R.string.gdb_game_battle_result_exist);
            msg = msg.replace("%s", detailData.getCoach().getName());
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
                                battleView.getPresenter().deleteBattleResults(detailData.getSeason().getId(), detailData.getCoach().getId());
                                saveBattleResult(datas);
                                ((ProgressProvider) getActivity()).dismissProgressCycler();
                            }
                            else {

                            }
                        }
                    });
        }
        else {
            ((ProgressProvider) getActivity()).showProgressCycler();
            saveBattleResult(datas);
            ((ProgressProvider) getActivity()).dismissProgressCycler();
        }
    }

    private void saveBattleResult(List<BattleResultBean> datas) {
        if (battleView.getPresenter().saveBattleResultBeans(datas, GameConstants.BATTLE_PROMOTE_ITEM)) {
            ((ProgressProvider) getActivity()).showToastLong("Successful", ProgressProvider.TOAST_SUCCESS);
        }
        else {
            ((ProgressProvider) getActivity()).showToastLong("Failed", ProgressProvider.TOAST_ERROR);
        }
    }

    @Override
    public PlayerBean getPlayerBean(int playerId) {
        return battleView.getPresenter().getPlayer(detailData, playerId);
    }

    @Override
    public void onAddBattleBean(BattleBean bean) {
        battleView.getPresenter().saveBattleBean(bean);
    }

    @Override
    public void onRemoveBattleBean(BattleBean bean) {
        battleView.getPresenter().deleteBattleBean(bean);
    }

}

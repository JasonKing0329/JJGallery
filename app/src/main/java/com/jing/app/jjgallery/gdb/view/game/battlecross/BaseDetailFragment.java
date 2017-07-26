package com.jing.app.jjgallery.gdb.view.game.battlecross;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BaseBattleDetailData;
import com.jing.app.jjgallery.gdb.view.game.GameFragment;
import com.jing.app.jjgallery.gdb.view.game.IBattleDetailView;
import com.jing.app.jjgallery.gdb.view.game.adapter.BattlePlayerAdapter;
import com.jing.app.jjgallery.gdb.view.game.adapter.IPlayerImageProvider;
import com.jing.app.jjgallery.gdb.view.game.view.BaseResultDialog;
import com.jing.app.jjgallery.gdb.view.game.view.BaseRoundManager;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/14 17:32
 */
public abstract class BaseDetailFragment extends GameFragment implements IBattleDetailView, IPlayerImageProvider
        , BattlePlayerAdapter.OnPlayerItemListener {

    private IBattleCross battleCross;
    private BaseRoundManager battleRoundManager;
    private BaseBattleDetailData detailData;

    private RecyclerView rvTops;
    private RecyclerView rvBottoms;
    private LinearLayout llCardsContainer;
    private BattlePlayerAdapter adapterTop;
    private BattlePlayerAdapter adapterBottom;

    @Override
    protected void onAttachActivity(Context context) {
        battleCross = getIBattleCross(context);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            showActionbar();
        }
    }

    public abstract IBattleCross getIBattleCross(Context context);

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_battle_detail;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {
        showActionbar();
        rvTops = (RecyclerView) contentView.findViewById(R.id.battle_rv_top);
        rvBottoms = (RecyclerView) contentView.findViewById(R.id.battle_rv_bottom);
        llCardsContainer = (LinearLayout) contentView.findViewById(R.id.battle_round_container);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTops.setLayoutManager(manager);
        rvTops.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBottoms.setLayoutManager(manager);
        rvBottoms.setItemAnimator(new DefaultItemAnimator());

        detailData = createDetailData();
        detailData.setSeason(battleCross.getBattleData().getSeason());
        int index = bundle.getInt("index");
        initCoachData(index);
        battleCross.getActionbar().setTitle("Team " + detailData.getCoach().getName());

        ((ProgressProvider) getActivity()).showProgressCycler();
        initSubView(contentView);
    }

    private void showActionbar() {
        battleCross.getActionbar().clearActionIcon();
        battleCross.getActionbar().addGroupAddIcon(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRound();
            }
        });
        battleCross.getActionbar().addMenuIcon();
        battleCross.getActionbar().addCoverIcon(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResultDialog();
            }
        });
        battleCross.getActionbar().addBackIcon();
    }

    protected abstract void initCoachData(int index);

    protected abstract BaseBattleDetailData createDetailData();

    protected abstract void initSubView(View contentView);

    private void addNewRound() {
        battleRoundManager.addNewRound();
    }

    @Override
    public void onDetailLoaded() {
        // show group

        adapterTop = new BattlePlayerAdapter(detailData.getPlayerListTop(), this);
        adapterTop.setOnPlayerItemListener(this);
        rvTops.setAdapter(adapterTop);
        adapterBottom = new BattlePlayerAdapter(detailData.getPlayerListBottom(), this);
        adapterBottom.setOnPlayerItemListener(this);
        rvBottoms.setAdapter(adapterBottom);

        // show battles
        battleRoundManager = createRoundManager(llCardsContainer);

        ((ProgressProvider) getActivity()).dismissProgressCycler();
    }

    protected abstract BaseRoundManager createRoundManager(LinearLayout llCardsContainer);

    @Override
    public String getPlayerImage(String name) {
        return battleCross.getGamePresenter().getStarImage(name);
    }

    @Override
    public void onPlayerItemClick(PlayerBean bean) {
        battleRoundManager.addPlayerToFocusItem(bean);
    }

    @Override
    public void onPlayerItemLongClick(BattlePlayerAdapter adapter, List<PlayerBean> list, int position) {
        Star star = battleCross.getGamePresenter().queryStarByName(list.get(position).getName());
        ActivityManager.startStarActivity(getActivity(), star);
    }

    private void showResultDialog() {
        BaseResultDialog dialog = createResultDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                onPrepareResultData(data);
            }
        });
        dialog.show();
    }

    protected abstract BaseResultDialog createResultDialog(Context context
            , CustomDialog.OnCustomDialogActionListener onCustomDialogActionListener);

    protected abstract void onPrepareResultData(HashMap<String, Object> data);

}

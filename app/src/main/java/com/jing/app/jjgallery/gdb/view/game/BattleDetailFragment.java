package com.jing.app.jjgallery.gdb.view.game;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.model.game.BattleDetailData;
import com.jing.app.jjgallery.gdb.view.IBattleView;
import com.jing.app.jjgallery.gdb.view.game.adapter.BattlePlayerAdapter;
import com.jing.app.jjgallery.gdb.view.game.adapter.IPlayerImageProvider;
import com.jing.app.jjgallery.gdb.view.game.view.BattleResultDialog;
import com.jing.app.jjgallery.gdb.view.game.view.BattleRoundManager;
import com.jing.app.jjgallery.gdb.view.game.view.OnBattleItemListener;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.HashMap;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:39
 */
public class BattleDetailFragment extends GameFragment implements IBattleDetailView, IPlayerImageProvider
    , BattlePlayerAdapter.OnPlayerItemListener, OnBattleItemListener {

    private IBattleView battleView;
    private RecyclerView rvTops;
    private RecyclerView rvBottoms;
    private LinearLayout llCardsContainer;
    private BattlePlayerAdapter adapterTop;
    private BattlePlayerAdapter adapterBottom;
    private BattleRoundManager battleRoundManager;

    private BattleDetailData detailData;

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_battle_detail;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {
        battleView.getActionbar().clearActionIcon();
        battleView.getActionbar().addGroupAddIcon(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRound();
            }
        });
        battleView.getActionbar().addMenuIcon();
        battleView.getActionbar().addCoverIcon(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResultDialog();
            }
        });
        battleView.getActionbar().addBackIcon();
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

        detailData = new BattleDetailData();
        detailData.setSeason(battleView.getBattleData().getSeason());
        int index = bundle.getInt("index");
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
        battleView.getActionbar().setTitle("Team " + detailData.getCoach().getName());
        battleView.getPresenter().setDetailView(this);

        ((ProgressProvider) getActivity()).showProgressCycler();
        battleView.getPresenter().loadDeatails(detailData);
    }

    private void showResultDialog() {
        BattleResultDialog dialog = new BattleResultDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
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
                data.put("battles", detailData.getBattleList());
                data.put("battleDetailBean", detailData);
            }
        });
        dialog.setTitle("Battle result");
        dialog.show();
    }

    private void addNewRound() {
        battleRoundManager.addNewRound();
    }

    @Override
    protected void onAttachActivity(Context context) {
        battleView = (IBattleView) context;
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
        battleRoundManager = new BattleRoundManager(detailData, llCardsContainer, detailData.getBattleList(), this);

        ((ProgressProvider) getActivity()).dismissProgressCycler();
    }

    @Override
    public String getPlayerImage(String name) {
        return battleView.getPresenter().getStarImage(name);
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

    @Override
    public void onPlayerItemClick(PlayerBean bean) {
        battleRoundManager.addPlayerToFocusItem(bean);
    }

    @Override
    public void onPlayerItemLongClick(BattlePlayerAdapter adapter, List<PlayerBean> list, int position) {

    }
}

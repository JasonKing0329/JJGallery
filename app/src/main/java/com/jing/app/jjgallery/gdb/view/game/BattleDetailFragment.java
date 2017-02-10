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
import com.jing.app.jjgallery.gdb.view.game.adapter.BattleItemAdapter;
import com.jing.app.jjgallery.gdb.view.game.adapter.BattlePlayerAdapter;
import com.jing.app.jjgallery.gdb.view.game.adapter.IPlayerImageProvider;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.king.service.gdb.game.bean.BattleBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/8 11:39
 */
public class BattleDetailFragment extends GameFragment implements IBattleDetailView, IPlayerImageProvider
    , BattlePlayerAdapter.OnPlayerItemListener, BattleItemAdapter.OnBattleItemListener{

    private IBattleView battleView;
    private RecyclerView rvTops;
    private RecyclerView rvBottoms;
    private LinearLayout llCardsContainer;
    private BattlePlayerAdapter adapterTop;
    private BattlePlayerAdapter adapterBottom;
    private BattleItemAdapter battleItemAdapter;

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
            case 3:
                detailData.setCoach(battleView.getBattleData().getCoach3());
                break;
            case 4:
                detailData.setCoach(battleView.getBattleData().getCoach4());
                break;
        }
        battleView.getPresenter().setDetailView(this);

        ((ProgressProvider) getActivity()).showProgressCycler();
        battleView.getPresenter().loadDeatails(detailData);
    }

    private void addNewRound() {
        battleItemAdapter.addNewRound();
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
        battleItemAdapter = new BattleItemAdapter(detailData, llCardsContainer, detailData.getBattleList());
        battleItemAdapter.setOnBattleItemListener(this);
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

    }

    @Override
    public void onRemoveBattleBean(BattleBean bean) {

    }

    @Override
    public void onPlayerItemClick(PlayerBean bean) {
        battleItemAdapter.addPlayerToFocusItem(bean);
    }

    @Override
    public void onPlayerItemLongClick(BattlePlayerAdapter adapter, List<PlayerBean> list, int position) {

    }
}

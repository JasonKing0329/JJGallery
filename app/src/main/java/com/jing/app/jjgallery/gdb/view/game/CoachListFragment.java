package com.jing.app.jjgallery.gdb.view.game;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.game.adapter.CoachListAdapter;
import com.jing.app.jjgallery.gdb.view.game.custom.CoachSeasonView;
import com.king.service.gdb.game.bean.CoachBean;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class CoachListFragment extends GameListFragment implements IGameList<CoachBean>, CoachSeasonView.OnCoachSeasonListener
    , CoachListAdapter.OnCoachSelectListener{

    private List<CoachBean> coachList;
    private RecyclerView recyclerView;
    private CoachListAdapter coachListAdapter;

    private ICoachManager coachManager;
    @Override
    public IGameList getIGameList() {
        return this;
    }

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_coach_list;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {

        coachManager = (ICoachManager) gameManager;

        recyclerView = (RecyclerView) contentView.findViewById(R.id.coach_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) > 0) {
                    outRect.top = getResources().getDimensionPixelSize(R.dimen.gdb_season_item_top);
                }
            }
        });

        initValue();
    }

    private void initValue() {
        coachList = coachManager.getPresenter().getCoachList();
        coachListAdapter = new CoachListAdapter(coachList, coachManager.getCoachSeasonManager());
        coachListAdapter.setOnCoachSeasonListener(this);
        coachListAdapter.setOnCoachSelectListener(this);
        recyclerView.setAdapter(coachListAdapter);
    }

    @Override
    public void onDataUpdated(CoachBean bean) {
        boolean isNewCoach = true;
        if (coachList != null) {
            for (int i = 0; i < coachList.size(); i ++) {
                if (coachList.get(i).getId() == bean.getId()) {
                    isNewCoach = false;
                    coachList.set(i, bean);
                    break;
                }
            }
        }

        if (isNewCoach) {
            if (coachList == null) {
                coachList = new ArrayList<>();
                coachListAdapter.setCoachList(coachList);
            }
            coachList.add(bean);
            coachListAdapter.notifyDataSetChanged();
        }
        else {
            coachListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickCoachSeason(CoachBean coach, SeasonBean season) {

    }

    @Override
    public void onCoachSelect(CoachBean coachBean) {
        coachManager.onCoachSelect(coachBean);
    }

    @Override
    public void onCoachLongSelect(CoachBean coachBean) {
        coachManager.updateData(coachBean);
    }
}

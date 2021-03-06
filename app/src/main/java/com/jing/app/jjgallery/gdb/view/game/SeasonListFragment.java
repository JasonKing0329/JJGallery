package com.jing.app.jjgallery.gdb.view.game;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.view.game.adapter.FolderItemManager;
import com.jing.app.jjgallery.gdb.view.game.adapter.SeasonListAdapter;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DefaultDialogManager;
import com.king.service.gdb.game.bean.SeasonBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 景阳 on 2017/1/10.
 */

public class SeasonListFragment extends GameListFragment implements FolderItemManager.FolderItemListener, IGameList<SeasonBean> {

    private List<SeasonBean> seasonList;
    private RecyclerView recyclerView;
    private SeasonListAdapter seasonListAdapter;

    @Override
    protected int getContentView() {
        return R.layout.gdb_game_season_list;
    }

    @Override
    protected void initView(View contentView, Bundle bundle) {

        recyclerView = (RecyclerView) contentView.findViewById(R.id.season_list);
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
        seasonList = gameManager.getPresenter().getSeasonList();
        seasonListAdapter = new SeasonListAdapter(seasonList);
        seasonListAdapter.setFolderItemListener(this);
        recyclerView.setAdapter(seasonListAdapter);
    }

    @Override
    public void onBattle(SeasonBean bean) {
        ActivityManager.startGdbGameBattleActivity(getActivity(), bean.getId());
    }

    @Override
    public void onCross(SeasonBean bean) {
        ActivityManager.startGdbGameCrossActivity(getActivity(), bean.getId());
    }

    @Override
    public void onFinal(SeasonBean bean) {

    }

    @Override
    public void onGroup(SeasonBean bean) {
        ActivityManager.startGdbGameGroupActivity(getActivity(), bean.getId());
    }

    @Override
    public void onFolderSetting(SeasonBean seasonBean) {
        gameManager.updateData(seasonBean);
    }

    @Override
    public void onFolderDelete(final SeasonBean seasonBean) {
        new DefaultDialogManager().showOptionDialog(getActivity(), null, getString(R.string.gdb_game_delete_season)
                , getResources().getString(R.string.yes)
                , null
                , getResources().getString(R.string.no)
                , new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            ((ProgressProvider) getActivity()).showProgressCycler();
                            gameManager.deleteData(seasonBean);
                            ((ProgressProvider) getActivity()).dismissProgressCycler();
                            seasonList.remove(seasonBean);
                            seasonListAdapter.notifyDataSetChanged();
                        }
                    }
                }
                , new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                }
        );
    }

    /**
     * new season is added or one season is updated
     * directly update bean instead of re-query from database
     * @param bean
     */
    @Override
    public void onDataUpdated(SeasonBean bean) {
        boolean isNewSeason = true;
        if (seasonList != null) {
            for (int i = 0; i < seasonList.size(); i ++) {
                if (seasonList.get(i).getId() == bean.getId()) {
                    isNewSeason = false;
                    seasonList.set(i, bean);
                    break;
                }
            }
        }

        if (isNewSeason) {
            if (seasonList == null) {
                seasonList = new ArrayList<>();
                seasonListAdapter.setSeasonList(seasonList);
            }
            seasonList.add(bean);
            seasonListAdapter.notifyDataSetChanged();
        }
        else {
            seasonListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public IGameList getIGameList() {
        return this;
    }
}

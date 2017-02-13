package com.jing.app.jjgallery.gdb.view.game;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.GamePlayerBean;
import com.jing.app.jjgallery.gdb.model.game.GroupData;
import com.jing.app.jjgallery.gdb.presenter.game.GroupPresenter;
import com.jing.app.jjgallery.gdb.view.game.adapter.GroupPlayerAdapter;
import com.jing.app.jjgallery.gdb.view.game.adapter.IPlayerImageProvider;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.sub.dialog.ShowImageDialog;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.game.bean.GroupBean;
import com.king.service.gdb.game.bean.PlayerBean;

import java.util.List;

public class GroupActivity extends BaseActivity implements View.OnClickListener
    , IGroupView, IPlayerImageProvider, GroupPlayerAdapter.OnPlayerItemListener{

    public static final String KEY_SEASON_ID = "key_season_id";

    private ImageView ivCandidate;
    private ImageView ivCoach1;
    private ImageView ivCoach2;
    private ImageView ivCoach3;
    private ImageView ivCoach4;
    
    private RecyclerView rvCoach1Top;
    private RecyclerView rvCoach1Bottom;
    private RecyclerView rvCoach2Top;
    private RecyclerView rvCoach2Bottom;
    private RecyclerView rvCoach3Top;
    private RecyclerView rvCoach3Bottom;
    private RecyclerView rvCoach4Top;
    private RecyclerView rvCoach4Bottom;
    
    private TextView tvCandidateName;
    private TextView tvCandidateType;

    private ImageView ivCoach1Down;
    private ImageView ivCoach2Down;
    private ImageView ivCoach3Down;
    private ImageView ivCoach4Down;
    
    private GroupPlayerAdapter top1Adapter;
    private GroupPlayerAdapter top2Adapter;
    private GroupPlayerAdapter top3Adapter;
    private GroupPlayerAdapter top4Adapter;
    private GroupPlayerAdapter bottom1Adapter;
    private GroupPlayerAdapter bottom2Adapter;
    private GroupPlayerAdapter bottom3Adapter;
    private GroupPlayerAdapter bottom4Adapter;

    private TextView tvType1;
    private TextView tvType0;

    private ImageView ivNext;
    private ImageView ivNextStop;

    private GroupPresenter gamePresenter;
    private GroupData groupData;
    private GamePlayerBean curPlayerBean;

    private String playerFromMode;

    private ShowImageDialog showImageDialog;

    @Override
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.gdb_game_group_list;
    }

    @Override
    public void initController() {
        gamePresenter = new GroupPresenter(this);
        playerFromMode = GDBProperites.STAR_MODE_ALL;
    }

    @Override
    public void initView() {
        mActionBar.addBackIcon();
        mActionBar.addCoverIcon();
        ivCandidate = (ImageView) findViewById(R.id.group_candidate_image);
        ivCoach1 = (ImageView) findViewById(R.id.group_coach1);
        ivCoach1Down = (ImageView) findViewById(R.id.group_coach1_down);
        rvCoach1Top = (RecyclerView) findViewById(R.id.group_coach1_list_top);
        rvCoach1Bottom = (RecyclerView) findViewById(R.id.group_coach1_list_bottom);
        ivCoach2 = (ImageView) findViewById(R.id.group_coach2);
        ivCoach2Down = (ImageView) findViewById(R.id.group_coach2_down);
        rvCoach2Top = (RecyclerView) findViewById(R.id.group_coach2_list_top);
        rvCoach2Bottom = (RecyclerView) findViewById(R.id.group_coach2_list_bottom);
        ivCoach3 = (ImageView) findViewById(R.id.group_coach3);
        ivCoach3Down = (ImageView) findViewById(R.id.group_coach3_down);
        rvCoach3Top = (RecyclerView) findViewById(R.id.group_coach3_list_top);
        rvCoach3Bottom = (RecyclerView) findViewById(R.id.group_coach3_list_bottom);
        ivCoach4 = (ImageView) findViewById(R.id.group_coach4);
        ivCoach4Down = (ImageView) findViewById(R.id.group_coach4_down);
        rvCoach4Top = (RecyclerView) findViewById(R.id.group_coach4_list_top);
        rvCoach4Bottom = (RecyclerView) findViewById(R.id.group_coach4_list_bottom);
        tvCandidateName = (TextView) findViewById(R.id.group_candidate_name);
        tvCandidateType = (TextView) findViewById(R.id.group_candidate_type);
        tvType0 = (TextView) findViewById(R.id.group_candidate_type0);
        tvType1 = (TextView) findViewById(R.id.group_candidate_type1);
        ivCoach1Down.setOnClickListener(this);
        ivCoach2Down.setOnClickListener(this);
        ivCoach3Down.setOnClickListener(this);
        ivCoach4Down.setOnClickListener(this);
        tvType0.setOnClickListener(this);
        tvType1.setOnClickListener(this);
        ivNext = (ImageView) findViewById(R.id.group_candidate_next);
        ivNext.setOnClickListener(this);
        ivNextStop = (ImageView) findViewById(R.id.group_candidate_next_stop);
        ivNextStop.setOnClickListener(this);
        initRecyclerViews();
    }

    private void initRecyclerViews() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach1Top.setLayoutManager(manager);
        rvCoach1Top.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach1Bottom.setLayoutManager(manager);
        rvCoach1Bottom.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach2Top.setLayoutManager(manager);
        rvCoach2Top.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach2Bottom.setLayoutManager(manager);
        rvCoach2Bottom.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach3Top.setLayoutManager(manager);
        rvCoach3Top.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach3Bottom.setLayoutManager(manager);
        rvCoach3Bottom.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach4Top.setLayoutManager(manager);
        rvCoach4Top.setItemAnimator(new DefaultItemAnimator());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCoach4Bottom.setLayoutManager(manager);
        rvCoach4Bottom.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initBackgroundWork() {
        int seasonId = getIntent().getIntExtra(KEY_SEASON_ID, -1);
        showProgressCycler();
        gamePresenter.loadGroupDatas(seasonId);
    }

    @Override
    public void onGroupDataLoaded(GroupData groupData) {
        this.groupData = groupData;
        SImageLoader.getInstance().displayImage(groupData.getCoach1().getImagePath(), ivCoach1);
        SImageLoader.getInstance().displayImage(groupData.getCoach2().getImagePath(), ivCoach2);
        SImageLoader.getInstance().displayImage(groupData.getCoach3().getImagePath(), ivCoach3);
        SImageLoader.getInstance().displayImage(groupData.getCoach4().getImagePath(), ivCoach4);
        top1Adapter = new GroupPlayerAdapter(groupData.getPlayerListTop1(), this);
        top1Adapter.setOnPlayerItemListener(this);
        rvCoach1Top.setAdapter(top1Adapter);
        bottom1Adapter = new GroupPlayerAdapter(groupData.getPlayerListBottom1(), this);
        bottom1Adapter.setOnPlayerItemListener(this);
        rvCoach1Bottom.setAdapter(bottom1Adapter);
        top2Adapter = new GroupPlayerAdapter(groupData.getPlayerListTop2(), this);
        top2Adapter.setOnPlayerItemListener(this);
        rvCoach2Top.setAdapter(top2Adapter);
        bottom2Adapter = new GroupPlayerAdapter(groupData.getPlayerListBottom2(), this);
        bottom2Adapter.setOnPlayerItemListener(this);
        rvCoach2Bottom.setAdapter(bottom2Adapter);
        top3Adapter = new GroupPlayerAdapter(groupData.getPlayerListTop3(), this);
        top3Adapter.setOnPlayerItemListener(this);
        rvCoach3Top.setAdapter(top3Adapter);
        bottom3Adapter = new GroupPlayerAdapter(groupData.getPlayerListBottom3(), this);
        bottom3Adapter.setOnPlayerItemListener(this);
        rvCoach3Bottom.setAdapter(bottom3Adapter);
        top4Adapter = new GroupPlayerAdapter(groupData.getPlayerListTop4(), this);
        top4Adapter.setOnPlayerItemListener(this);
        rvCoach4Top.setAdapter(top4Adapter);
        bottom4Adapter = new GroupPlayerAdapter(groupData.getPlayerListBottom4(), this);
        bottom4Adapter.setOnPlayerItemListener(this);
        rvCoach4Bottom.setAdapter(bottom4Adapter);
        dismissProgressCycler();
    }

    @Override
    public void onPlayerRecommended(GamePlayerBean player) {
        curPlayerBean = player;
        tvCandidateName.setText(player.getStar().getName());
        SImageLoader.getInstance().displayImage(gamePresenter.getStarImage(player.getStar().getName()), ivCandidate);
        if (player.getStar().getBeBottom() > 0) {
            if (player.getStar().getBeTop() > 0) {
                tvCandidateType.setText("0.5");
                tvType0.setVisibility(View.VISIBLE);
                tvType1.setVisibility(View.VISIBLE);
                tvType0.setSelected(false);
                tvType1.setSelected(false);
                tvType0.setEnabled(!player.isHasBeenBottom());
                tvType1.setEnabled(!player.isHasBeenTop());
            }
            else {
                tvCandidateType.setText("0");
                tvType0.setVisibility(View.VISIBLE);
                tvType1.setVisibility(View.GONE);
                tvType0.setSelected(true);
                tvType0.setEnabled(true);
                tvType1.setSelected(false);
            }
        }
        else {
            tvCandidateType.setText("1");
            tvType0.setVisibility(View.GONE);
            tvType1.setVisibility(View.VISIBLE);
            tvType0.setSelected(false);
            tvType1.setSelected(true);
            tvType1.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_coach1_down:
                if (checkType()) {
                    choseCoach1();
                }
                break;
            case R.id.group_coach2_down:
                if (checkType()) {
                    choseCoach2();
                }
                break;
            case R.id.group_coach3_down:
                if (checkType()) {
                    choseCoach3();
                }
                break;
            case R.id.group_coach4_down:
                if (checkType()) {
                    choseCoach4();
                }
                break;
            case R.id.group_candidate_next:
                ivNextStop.setVisibility(View.VISIBLE);
                ivNext.setVisibility(View.GONE);
                gamePresenter.nextPlayer(playerFromMode);
                break;
            case R.id.group_candidate_next_stop:
                ivNextStop.setVisibility(View.GONE);
                ivNext.setVisibility(View.VISIBLE);
                gamePresenter.stopRandom();
                break;
            case R.id.group_candidate_type0:
                setFocusType(tvType0);
                break;
            case R.id.group_candidate_type1:
                setFocusType(tvType1);
                break;
        }
    }

    private boolean checkType() {
        if (!tvType0.isSelected() && !tvType1.isSelected()) {
            showToastLong("Please chose player's type", ProgressProvider.TOAST_WARNING);
            return false;
        }
        return true;
    }

    private void choseCoach1() {
        if (curPlayerBean != null) {
            if (tvType0.isSelected()) {
                curPlayerBean.setBottomCoachId(groupData.getCoach1().getId());
                curPlayerBean.setBottomSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 0);
                GroupBean bean = new GroupBean();
                bean.setType(0);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach1().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);
                
                groupData.getPlayerListBottom1().add(curPlayerBean);
                bottom1Adapter.notifyDataSetChanged();
            }
            else {
                curPlayerBean.setTopCoachId(groupData.getCoach1().getId());
                curPlayerBean.setTopSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 1);
                GroupBean bean = new GroupBean();
                bean.setType(1);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach1().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);
                
                groupData.getPlayerListTop1().add(curPlayerBean);
                top1Adapter.notifyDataSetChanged();
            }
        }
    }

    private void choseCoach2() {
        if (curPlayerBean != null) {
            if (tvType0.isSelected()) {
                curPlayerBean.setBottomCoachId(groupData.getCoach2().getId());
                curPlayerBean.setBottomSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 0);
                GroupBean bean = new GroupBean();
                bean.setType(0);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach2().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);

                groupData.getPlayerListBottom2().add(curPlayerBean);
                bottom2Adapter.notifyDataSetChanged();
            }
            else {
                curPlayerBean.setTopCoachId(groupData.getCoach2().getId());
                curPlayerBean.setTopSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 1);
                GroupBean bean = new GroupBean();
                bean.setType(1);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach2().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);

                groupData.getPlayerListTop2().add(curPlayerBean);
                top2Adapter.notifyDataSetChanged();
            }
        }
    }

    private void choseCoach3() {
        if (curPlayerBean != null) {
            if (tvType0.isSelected()) {
                curPlayerBean.setBottomCoachId(groupData.getCoach3().getId());
                curPlayerBean.setBottomSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 0);
                GroupBean bean = new GroupBean();
                bean.setType(0);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach3().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);

                groupData.getPlayerListBottom3().add(curPlayerBean);
                bottom3Adapter.notifyDataSetChanged();
            }
            else {
                curPlayerBean.setTopCoachId(groupData.getCoach3().getId());
                curPlayerBean.setTopSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 1);
                GroupBean bean = new GroupBean();
                bean.setType(1);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach3().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);

                groupData.getPlayerListTop3().add(curPlayerBean);
                top3Adapter.notifyDataSetChanged();
            }
        }
    }

    private void choseCoach4() {
        if (curPlayerBean != null) {
            if (tvType0.isSelected()) {
                curPlayerBean.setBottomCoachId(groupData.getCoach4().getId());
                curPlayerBean.setBottomSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 0);
                GroupBean bean = new GroupBean();
                bean.setType(0);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach4().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);

                groupData.getPlayerListBottom4().add(curPlayerBean);
                bottom4Adapter.notifyDataSetChanged();
            }
            else {
                curPlayerBean.setTopCoachId(groupData.getCoach4().getId());
                curPlayerBean.setTopSeasonId(groupData.getSeason().getId());
                curPlayerBean.setName(curPlayerBean.getStar().getName());
                int playerId = gamePresenter.savePlayer(curPlayerBean, 1);
                GroupBean bean = new GroupBean();
                bean.setType(1);
                bean.setPlayerId(playerId);
                bean.setCoachId(groupData.getCoach4().getId());
                bean.setSeasonId(groupData.getSeason().getId());
                bean.setPlayerName(curPlayerBean.getName());
                gamePresenter.saveGroupBean(bean);

                groupData.getPlayerListTop4().add(curPlayerBean);
                top4Adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public String getPlayerImage(String name) {
        return gamePresenter.getStarImage(name);
    }

    public void setFocusType(TextView view) {
        TextView unFocusView = view == tvType0 ? tvType1:tvType0;
        view.setSelected(true);
        unFocusView.setSelected(false);
    }

    @Override
    public void onIconClick(View view) {
        super.onIconClick(view);
        switch (view.getId()) {
            case R.id.actionbar_cover:
                showListDialog();
                break;
        }
    }

    private void showListDialog() {
        String[] items = new String[] {
          "All", "1", "0", "0.5"
        };
        new AlertDialog.Builder(this)
            .setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            playerFromMode = GDBProperites.STAR_MODE_ALL;
                            break;
                        case 1:
                            playerFromMode = GDBProperites.STAR_MODE_TOP;
                            break;
                        case 2:
                            playerFromMode = GDBProperites.STAR_MODE_BOTTOM;
                            break;
                        case 3:
                            playerFromMode = GDBProperites.STAR_MODE_HALF;
                            break;
                    }
                }
            })
            .setTitle(null)
            .show();
    }

    @Override
    public void onPlayerItemClick(PlayerBean bean) {
        if (showImageDialog == null) {
            showImageDialog = new ShowImageDialog(this, null, 0);
        }
        showImageDialog.setImagePath(gamePresenter.getStarImage(bean.getName()));
        showImageDialog.show();
    }

    @Override
    public void onPlayerItemLongClick(final GroupPlayerAdapter adapter, final List<PlayerBean> list, final int position) {
        String[] items = new String[] {
                "StarActivity", "Delete"
        };
        new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Star star = gamePresenter.queryStarByName(list.get(position).getName());
                                ActivityManager.startStarActivity(GroupActivity.this, star);
                                break;
                            case 1:
                                showProgressCycler();
                                gamePresenter.deletePlayer(list.get(position).getId(), groupData.getSeason().getId());
                                dismissProgressCycler();
                                list.remove(position);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                })
                .setTitle(null)
                .show();
    }
}

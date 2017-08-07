package com.jing.app.jjgallery.gdb.view.star;

import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.star.StarSwipePresenter;
import com.jing.app.jjgallery.gdb.presenter.star.SwipeAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.RecordCardAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.RecordsListAdapter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.swipeview.SwipeFlingAdapterView;
import com.jing.app.jjgallery.viewsystem.sub.dialog.DefaultDialogManager;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/4 11:18
 */
public class StarSwipeActivity extends GBaseActivity implements IStarSwipeView {

    @BindView(R.id.rv_records)
    RecyclerView rvRecords;
    @BindView(R.id.rv_records_hor)
    RecyclerView rvRecordsHor;
    @BindView(R.id.sfv_stars)
    SwipeFlingAdapterView sfvStars;
    @BindView(R.id.iv_orientation)
    ImageView ivOrientation;

    private StarSwipePresenter presenter;
    private SwipeAdapter adapter;
    private List<StarProxy> starList;

    private RecordsListAdapter recordsListAdapter;
    private RecordCardAdapter recordCardAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_gdb_rec_star;
    }

    @Override
    protected void initController() {
        presenter = new StarSwipePresenter(this);
        starList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecords.setLayoutManager(manager);

        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecordsHor.setLayoutManager(manager);

        boolean isHorizontal = SettingProperties.isGdbSwipeListHorizontal();
        rvRecordsHor.setVisibility(isHorizontal ? View.VISIBLE:View.GONE);
        rvRecords.setVisibility(isHorizontal ? View.GONE:View.VISIBLE);
        
        sfvStars.setMinStackInAdapter(3);
        sfvStars.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                starList.remove(0);
                adapter.setList(starList);
                adapter.notifyDataSetChanged();
            }

            /**
             * not favor or cancel favor
             * @param dataObject
             */
            @Override
            public void onLeftCardExit(Object dataObject) {
                final StarProxy star = (StarProxy) dataObject;
                if (star.getFavorBean() != null) {
                    new DefaultDialogManager().showOptionDialog(StarSwipeActivity.this, null, "Are you sure to make " + star.getStar().getName() + " unfavor?"
                            , getString(R.string.ok), null, getString(R.string.cancel)
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i == DialogInterface.BUTTON_POSITIVE) {
                                        FavorBean bean = new FavorBean();
                                        bean.setStarId(star.getStar().getId());
                                        bean.setFavor(0);
                                        bean.setStarName(star.getStar().getName());
                                        presenter.saveFavor(bean);
                                    }
                                }
                            }, null);
                }
                updateRecords();
            }

            /**
             * mark favor if not already be favored
             * @param dataObject
             */
            @Override
            public void onRightCardExit(Object dataObject) {
                final StarProxy star = (StarProxy) dataObject;
                if (star.getFavorBean() == null) {
                    new DefaultDialogManager().openInputDialog(StarSwipeActivity.this, "Mark favor to " + star.getStar().getName()
                            , new DefaultDialogManager.OnDialogActionListener() {
                                @Override
                                public void onOk(String name) {
                                    try {
                                        int favor = Integer.parseInt(name);
                                        FavorBean bean = new FavorBean();
                                        bean.setStarId(star.getStar().getId());
                                        bean.setFavor(favor);
                                        bean.setStarName(star.getStar().getName());
                                        presenter.saveFavor(bean);
                                    } catch (Exception e) {

                                    }
                                }
                    });
                }
                updateRecords();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                presenter.loadNewStars();
            }

            @Override
            public void onScroll(float progress, float scrollXProgress) {

            }
        });
    }

    @Override
    protected void initBackgroundWork() {
        presenter.loadNewStars();
    }

    @Override
    public void onStarLoaded(List<StarProxy> list) {
        starList.addAll(list);
        if (adapter == null) {
            adapter = new SwipeAdapter();
            adapter.setList(list);
            adapter.setOnSwipeItemListener(new SwipeAdapter.OnSwipeItemListener() {
                @Override
                public void onClickStar(StarProxy star) {
                    ActivityManager.startStarActivity(StarSwipeActivity.this, star.getStar());
                }
            });
            sfvStars.setAdapter(adapter);

            updateRecords();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateRecords() {
        Star star = starList.get(0).getStar();
        if (SettingProperties.isGdbSwipeListHorizontal()) {
            rvRecords.setVisibility(View.GONE);
            updateHorizontalList(star);
        }
        else {
            rvRecordsHor.setVisibility(View.GONE);
            updateVerticalList(star);
        }
        sfvStars.invalidate();
        sfvStars.requestLayout();
    }

    private void updateHorizontalList(Star star) {
        if (recordCardAdapter == null) {
            recordCardAdapter = new RecordCardAdapter();
            recordCardAdapter.setOnCardActionListener(new RecordCardAdapter.OnCardActionListener() {
                @Override
                public void onClickCardItem(Record record) {
                    ActivityManager.startGdbRecordActivity(StarSwipeActivity.this, record);
                }
            });
            recordCardAdapter.setRecordList(star.getRecordList());
            recordCardAdapter.setCurrentStar(star);
            rvRecordsHor.setAdapter(recordCardAdapter);
        } else {
            recordCardAdapter.setRecordList(star.getRecordList());
            recordCardAdapter.notifyDataSetChanged();
        }

        if (star.getRecordList() == null || star.getRecordList().size() == 0) {
            rvRecordsHor.setVisibility(View.GONE);
        } else {
            rvRecordsHor.setVisibility(View.VISIBLE);
        }
    }

    private void updateVerticalList(Star star) {
        if (recordsListAdapter == null) {
            recordsListAdapter = new RecordsListAdapter(this, star.getRecordList());
            recordsListAdapter.setItemClickListener(new RecordsListAdapter.OnRecordItemClickListener() {
                @Override
                public void onClickRecordItem(Record record) {
                    ActivityManager.startGdbRecordActivity(StarSwipeActivity.this, record);
                }
            });
            rvRecords.setAdapter(recordsListAdapter);
        } else {
            recordsListAdapter.setRecordList(star.getRecordList());
            recordsListAdapter.notifyDataSetChanged();
        }

        if (star.getRecordList() == null || star.getRecordList().size() == 0) {
            rvRecords.setVisibility(View.GONE);
        } else {
            rvRecords.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_list, R.id.iv_back, R.id.iv_orientation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_list:
                ActivityManager.startGDBStarListActivity(this, null);
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_orientation:
                boolean isHorizontal = SettingProperties.isGdbSwipeListHorizontal();
                if (isHorizontal) {
                    SettingProperties.setGdbSwipeListOrientation(false);
                    ivOrientation.setImageResource(R.drawable.ic_panorama_horizontal_3f51b5_36dp);
                }
                else {
                    SettingProperties.setGdbSwipeListOrientation(true);
                    ivOrientation.setImageResource(R.drawable.ic_panorama_vertical_3f51b5_36dp);
                }
                updateRecords();
                break;
        }
    }
}

package com.jing.app.jjgallery.gdb.view.star;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.star.StarSwipePresenter;
import com.jing.app.jjgallery.gdb.presenter.star.SwipeAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.RecordsListAdapter;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.swipeview.SwipeFlingAdapterView;
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
    @BindView(R.id.sfv_stars)
    SwipeFlingAdapterView sfvStars;

    private StarSwipePresenter presenter;
    private SwipeAdapter adapter;
    private List<StarProxy> starList;

    private RecordsListAdapter recordsListAdapter;

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

        sfvStars.setMinStackInAdapter(3);
        sfvStars.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                starList.remove(0);
                adapter.setList(starList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                updateRecords();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
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
            sfvStars.setAdapter(adapter);

            updateRecords();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateRecords() {
        Star star = starList.get(0).getStar();
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

        if (star.getRecordList().size() == 0) {
            rvRecords.setVisibility(View.GONE);
        } else {
            rvRecords.setVisibility(View.VISIBLE);
        }
        sfvStars.invalidate();
        sfvStars.requestLayout();
    }

    @OnClick({R.id.iv_list, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_list:
                ActivityManager.startGDBStarListActivity(this, null);
                finish();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

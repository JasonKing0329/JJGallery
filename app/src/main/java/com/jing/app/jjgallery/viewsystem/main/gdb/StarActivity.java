package com.jing.app.jjgallery.viewsystem.main.gdb;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.RecordOneVOne;

import java.util.List;

public class StarActivity extends BaseActivity implements IStarView {

    private StarRecordsAdapter recordsAdapter;
    private PullZoomRecyclerView recyclerView;
    private GdbPresenter mPresenter;

    @Override
    protected boolean isActionBarNeed() {
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_star;
    }

    @Override
    protected void initController() {
        mPresenter = new GdbPresenter(this);
    }

    @Override
    protected void initView() {
        recyclerView = (PullZoomRecyclerView) findViewById(R.id.gdb_star_recycler_view);
    }

    @Override
    protected void initBackgroundWork() {

    }

    @Override
    public void onRecordsLoaded(List<RecordOneVOne> list) {

    }
}

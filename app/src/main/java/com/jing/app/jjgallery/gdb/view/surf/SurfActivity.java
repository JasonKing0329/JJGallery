package com.jing.app.jjgallery.gdb.view.surf;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.presenter.surf.SurfPresenter;
import com.jing.app.jjgallery.gdb.view.pub.AutoLoadMoreRecyclerView;
import com.jing.app.jjgallery.http.HttpConstants;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.http.bean.response.FolderResponse;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.king.service.gdb.bean.Record;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/27 10:57
 */
public class SurfActivity extends GBaseActivity implements ISurfView, SurfAdapter.OnSurfItemActionListener {

    @BindView(R.id.rv_files)
    AutoLoadMoreRecyclerView rvFiles;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout srRefresh;
    @BindView(R.id.fab_top)
    FloatingActionButton fabTop;
    @BindView(R.id.tv_parent)
    TextView tvParent;
    @BindView(R.id.group_folder)
    ViewGroup groupFolder;

    private SurfPresenter presenter;

    private FileBean currentFileBean;
    private SurfAdapter surfAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_gdb_surf;
    }

    @Override
    protected void initController() {
        presenter = new SurfPresenter(this);
        SImageLoader.getInstance().setDefaultImgRes(R.drawable.default_cover);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        groupFolder.setVisibility(View.GONE);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Surf");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFiles.setLayoutManager(manager);

        rvFiles.setEnableLoadMore(true);
        rvFiles.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        srRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCurrentFolder();
            }
        });

        fabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvFiles.scrollToPosition(0);
            }
        });
    }

    @Override
    protected void initBackgroundWork() {

        currentFileBean = new FileBean();
        currentFileBean.setPath(HttpConstants.FOLDER_TYPE_CONTENT);
        currentFileBean.setName(HttpConstants.FOLDER_TYPE_CONTENT);

        loadCurrentFolder();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestFail() {
        if (srRefresh.isRefreshing()) {
            srRefresh.setRefreshing(false);
        }
        dismissProgress();
        showToastLong(getString(R.string.gdb_request_fail));
    }

    @Override
    public void onFolderReceived(FolderResponse bean) {
        updateSurfList(bean.getFileList());
        if (srRefresh.isRefreshing()) {
            srRefresh.setRefreshing(false);
        }
        dismissProgress();
    }

    @OnClick(R.id.group_folder)
    public void onViewClicked() {
        currentFileBean = currentFileBean.getParentBean();
        loadCurrentFolder();
    }

    private void loadCurrentFolder() {
        showProgress(getString(R.string.loading));
        DebugLog.e(currentFileBean.getPath());
        if (HttpConstants.FOLDER_TYPE_CONTENT.equals(currentFileBean.getPath())) {
            presenter.surf(HttpConstants.FOLDER_TYPE_CONTENT, null);
        }
        else {
            presenter.surf(HttpConstants.FOLDER_TYPE_FOLDER, currentFileBean.getPath());
        }

        if (currentFileBean.getParentBean() == null) {
            if (groupFolder.getVisibility() == View.VISIBLE) {
                groupFolder.startAnimation(getFollowDisappearAnim());
            }
        }
        else {
            tvParent.setText(currentFileBean.getParentBean().getName());
            if (groupFolder.getVisibility() == View.GONE) {
                groupFolder.startAnimation(getFollowAppearAnim());
            }
        }

    }

    /**
     * AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
     AnticipateInterpolator 开始的时候向后然后向前甩
     AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
     BounceInterpolator   动画结束的时候弹起
     CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
     DecelerateInterpolator 在动画开始的地方快然后慢
     LinearInterpolator   以常量速率改变
     OvershootInterpolator    向前甩一定值后再回到原来位置
     * @return
     */
    private Animation getFollowAppearAnim() {
        groupFolder.setVisibility(View.VISIBLE);
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(1000);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    private Animation getFollowDisappearAnim() {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f
                , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(1000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                groupFolder.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return anim;
    }

    public void updateSurfList(List<FileBean> fileList) {
        if (surfAdapter == null) {
            surfAdapter = new SurfAdapter(fileList);
            surfAdapter.setOnSurfItemActionListener(this);
            rvFiles.setAdapter(surfAdapter);
        } else {
            surfAdapter.setList(fileList);
            surfAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickSurfFolder(FileBean fileBean) {
        fileBean.setParentBean(currentFileBean);
        currentFileBean = fileBean;

        loadCurrentFolder();
    }

    @Override
    public void onClickSurfRecord(Record record) {
        if (record.getId() != 0) {
            ActivityManager.startGdbRecordActivity(this, record);
        }
    }

}

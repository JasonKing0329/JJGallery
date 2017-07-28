package com.jing.app.jjgallery.gdb.view.surf;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseActivity;
import com.jing.app.jjgallery.gdb.presenter.surf.SurfPresenter;
import com.jing.app.jjgallery.gdb.view.pub.AutoLoadMoreRecyclerView;
import com.jing.app.jjgallery.gdb.view.record.SortDialog;
import com.jing.app.jjgallery.http.HttpConstants;
import com.jing.app.jjgallery.http.bean.data.FileBean;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;
import com.king.service.gdb.bean.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.fab_top)
    FloatingActionButton fabTop;
    @BindView(R.id.tv_parent)
    TextView tvParent;
    @BindView(R.id.group_folder)
    ViewGroup groupFolder;

    private SurfPresenter presenter;

    private FileBean currentFileBean;
    private SurfAdapter surfAdapter;
    private List<SurfFileBean> surfFileList;
    private int currentSortMode;
    private boolean currentSortDesc;

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
    public void onBackPressed() {
        if (currentFileBean != null && currentFileBean.getParentBean() != null) {
            onBackClicked();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gdb_surf, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gdb_surf_relate:
                presenter.relateToDatabase(surfFileList);
                break;
            case R.id.menu_gdb_surf_sort:
                showSortDialog();
                break;
            case R.id.menu_gdb_surf_refresh:
                loadCurrentFolder();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        SortDialog sortDialog = new SortDialog(this, new CustomDialog.OnCustomDialogActionListener() {

            @Override
            public boolean onSave(Object object) {
                Map<String, Object> map = (Map<String, Object>) object;
                currentSortMode = (int) map.get("sortMode");
                currentSortDesc = (Boolean) map.get("desc");
                presenter.sortFileList(surfFileList, currentSortMode, currentSortDesc);
                return true;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put("sortMode", currentSortMode);
                data.put("desc", currentSortDesc);
            }
        });
        sortDialog.show();
    }

    @Override
    public void onRequestFail() {
        dismissProgress();
        showToastLong(getString(R.string.gdb_request_fail));
    }

    @Override
    public void onFolderReceived(List<SurfFileBean> list) {
        surfFileList = list;
        updateSurfList(list);
        dismissProgress();
    }

    @Override
    public void onRecordRelated(int index) {
        if (!isDestroyed()) {
            surfAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void onSortFinished() {
        surfAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.group_folder)
    public void onBackClicked() {
        currentFileBean = currentFileBean.getParentBean();
        loadCurrentFolder();
    }

    private void loadCurrentFolder() {
        showProgress(getString(R.string.loading));
        DebugLog.e(currentFileBean.getPath());
        if (HttpConstants.FOLDER_TYPE_CONTENT.equals(currentFileBean.getPath())) {
            presenter.surf(HttpConstants.FOLDER_TYPE_CONTENT, null, SettingProperties.isGdbSurfRelated());
        }
        else {
            presenter.surf(HttpConstants.FOLDER_TYPE_FOLDER, currentFileBean.getPath(), SettingProperties.isGdbSurfRelated());
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

    public void updateSurfList(List<SurfFileBean> list) {
        if (surfAdapter == null) {
            surfAdapter = new SurfAdapter(list);
            surfAdapter.setOnSurfItemActionListener(this);
            rvFiles.setAdapter(surfAdapter);
        } else {
            surfAdapter.setList(list);
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
        if (record != null) {
            ActivityManager.startGdbRecordActivity(this, record);
        }
    }

}

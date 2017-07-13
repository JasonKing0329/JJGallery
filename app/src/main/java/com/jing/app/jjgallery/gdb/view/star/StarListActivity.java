package com.jing.app.jjgallery.gdb.view.star;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.presenter.star.StarListPresenter;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.StarCountBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/7/12 9:33
 */
public class StarListActivity extends GDBListActivity implements IStarListHolder, IStarListHeaderView {

    private final String[] titles = new String[]{
            "All", "1", "0", "0.5"
    };

    @BindView(R.id.side_bar)
    WaveSideBarView sideBar;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private StarListPresenter starPresenter;
    private StarListPagerAdapter pagerAdapter;

    private ActionBar actionBar;

    @Override
    public int getContentView() {
        return R.layout.activity_gdb_star_list;
    }

    @Override
    protected void initController() {
        starPresenter = new StarListPresenter(this);
        presenter = starPresenter;
        starPresenter.setStarListHeaderView(this);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        initActionbar();

        sideBar.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                pagerAdapter.getItem(viewpager.getCurrentItem()).onLetterChange(letter);
            }
        });

    }

    @Override
    protected void initBackgroundWork() {
        // 查询tabLayout的数据，回调在onStarCountLoaded
        starPresenter.queryIndicatorData();

        // 检查服务端新文件，回调在父类
        starPresenter.checkNewStarFile();
    }

    private void initActionbar() {
        actionBar = new ActionBar(this, findViewById(R.id.group_actionbar));
        actionBar.setActionIconListener(iconListener);
        actionBar.setActionMenuListener(menuListener);
        actionBar.setActionSearchListener(searchListener);
        actionBar.clearActionIcon();
        actionBar.addMenuIcon();
        actionBar.addSearchIcon();
        actionBar.addBackIcon();
        actionBar.addSortIcon();
        actionBar.addFavorIcon();
        actionBar.addIndexIcon();
        actionBar.setTitle(getString(R.string.gdb_title_star));
    }

    /**
     * tabLayout 标题对应数量
     * @param bean
     */
    @Override
    public void onStarCountLoaded(StarCountBean bean) {
        initFragments(bean);
    }

    private void initFragments(StarCountBean bean) {
        pagerAdapter = new StarListPagerAdapter(getSupportFragmentManager());
        StarListFragment fragmentAll = new StarListFragment();
        fragmentAll.setStarMode(GDBProperites.STAR_MODE_ALL);
        pagerAdapter.addFragment(fragmentAll, titles[0] + "(" + bean.getAllNumber() + ")");
        StarListFragment fragment1 = new StarListFragment();
        fragment1.setStarMode(GDBProperites.STAR_MODE_TOP);
        pagerAdapter.addFragment(fragment1, titles[1] + "(" + bean.getTopNumber() + ")");
        StarListFragment fragment0 = new StarListFragment();
        fragment0.setStarMode(GDBProperites.STAR_MODE_BOTTOM);
        pagerAdapter.addFragment(fragment0, titles[2] + "(" + bean.getBottomNumber() + ")");
        StarListFragment fragment05 = new StarListFragment();
        fragment05.setStarMode(GDBProperites.STAR_MODE_HALF);
        pagerAdapter.addFragment(fragment05, titles[3] + "(" + bean.getHalfNumber() + ")");
        viewpager.setAdapter(pagerAdapter);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[2]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[3]));
        tabLayout.setupWithViewPager(viewpager);
    }

    private ActionBar.ActionIconListener iconListener = new ActionBar.ActionIconListener() {
        @Override
        public void onBack() {
            finish();
        }

        @Override
        public void onIconClick(View view) {
            switch (view.getId()) {
                case R.id.actionbar_sort:
//                    starFragment.changeSortType();
                    break;
                case R.id.actionbar_index:
                    changeSideBarVisible();
                    break;
                case R.id.actionbar_favor:
//                    starFragment.changeFavorList();
                    break;
            }
        }
    };

    private ActionBar.ActionMenuListener menuListener = new ActionBar.ActionMenuListener() {
        @Override
        public void createMenu(MenuInflater menuInflater, Menu menu) {
            loadMenu(menuInflater, menu);
        }

        @Override
        public void onPrepareMenu(MenuInflater menuInflater, Menu menu) {
            loadMenu(menuInflater, menu);
        }

        private void loadMenu(MenuInflater menuInflater, Menu menu) {
            menu.clear();
            menuInflater.inflate(R.menu.gdb_star_list, menu);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_gdb_check_server:
                    starPresenter.checkNewStarFile();
                    break;
                case R.id.menu_gdb_recommend:
                    showRecommendDialog();
                    break;
                case R.id.menu_gdb_download:
                    showDownloadDialog();
                    break;
            }
            return false;
        }
    };

    private ActionBar.ActionSearchListener searchListener = new ActionBar.ActionSearchListener() {
        @Override
        public void onTextChanged(String text, int start, int before, int count) {
            pagerAdapter.getItem(viewpager.getCurrentItem()).filterStar(text);
        }
    };

    @Override
    public void onServerConnectSuccess() {
        starPresenter.checkNewStarFile();
    }

    @Override
    public void onDownloadFinished() {
        pagerAdapter.getItem(viewpager.getCurrentItem()).refreshList();
    }

    @Override
    protected String getListType() {
        return Command.TYPE_STAR;
    }

    @Override
    protected String getSavePath() {
        return Configuration.GDB_IMG_STAR;
    }

    @Override
    protected List<DownloadItem> getListToDownload(List<DownloadItem> downloadList, List<DownloadItem> repeatList) {
        List<DownloadItem> newList = starPresenter.pickStarToDownload(downloadList, repeatList);
        return newList;
    }

    @Override
    public StarListPresenter getPresenter() {
        return starPresenter;
    }

    public void changeSideBarVisible() {
        sideBar.setVisibility(sideBar.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        invalidateSideBar();
    }

    private void invalidateSideBar() {
        if (sideBar.getVisibility() == View.VISIBLE) {
            // post刷新mSideBarView，根据调试发现重写初始化后WaveSideBarView会重新执行onMeasure(width,height=0)->onDraw->onMeasure(width,height=正确值)
            // 缺少重新onDraw的过程，因此通过delay执行mSideBarView.invalidate()可以激活onDraw事件，根据正确的值重新绘制
            // 用mSideBarView.post/postDelayed总是不准确
            sideBar.post(new Runnable() {
                @Override
                public void run() {
                    sideBar.requestLayout();
                    sideBar.invalidate();
                }
            });
        }
    }

}

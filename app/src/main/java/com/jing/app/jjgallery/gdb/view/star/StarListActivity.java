package com.jing.app.jjgallery.gdb.view.star;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.allure.lbanners.LMBanners;
import com.allure.lbanners.adapter.LBaseAdapter;
import com.allure.lbanners.transformer.TransitionEffect;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.presenter.star.StarListPresenter;
import com.jing.app.jjgallery.gdb.view.list.GDBListActivity;
import com.jing.app.jjgallery.gdb.view.recommend.RecordFilterDialog;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.service.encrypt.EncryptUtil;
import com.jing.app.jjgallery.service.http.Command;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.StarCountBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.lmbanner)
    LMBanners lmBanners;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private StarListPresenter starPresenter;
    private StarListPagerAdapter pagerAdapter;

    private ActionBar actionBar;
    private StarListSettingDialog settingDialog;

    private int curSortMode;

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
        initBanner();

        sideBar.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                pagerAdapter.getItem(viewpager.getCurrentItem()).onLetterChange(letter);
            }
        });
        curSortMode = GdbConstants.STAR_SORT_NAME;

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerAdapter.getItem(position).reloadStarList(curSortMode);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initBackgroundWork() {
        // load favor list for banner, 回调在onFavorListLoaded
        starPresenter.loadFavorList();

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
        actionBar.addSortByNumIcon();
        actionBar.addFavorIcon();
        actionBar.addIndexIcon();
        actionBar.setTitle(getString(R.string.gdb_title_star));
    }

    private void initBanner() {
        // 禁用btnStart(只在onPageScroll触发后有效)
        lmBanners.isGuide(false);
        // 不显示引导圆点
        lmBanners.hideIndicatorLayout();
        // 轮播切换时间
        lmBanners.setDurtion(SettingProperties.getGdbStarListNavAnimTime(this));
        if (SettingProperties.isGdbStarListNavAnimRandom(this)) {
            Random random = new Random();
            int type = Math.abs(random.nextInt()) % RecordFilterDialog.ANIM_TYPES.length;
            setScrollAnim(type);
        }
        else {
            setScrollAnim(SettingProperties.getGdbStarListNavAnimType(this));
        }
    }

    @Override
    public void onFavorListLoaded() {

        progress.setVisibility(View.GONE);

        // show banner

        // 采用getView时生成随机推荐，这里初始化3个item就够了（LMBanner内部也是根据view pager设置下标
        // 来循环的）
        List<FavorBean> list = new ArrayList<>();
        list.add(new FavorBean());
        list.add(new FavorBean());
        list.add(new FavorBean());
        HeadBannerAdapter adapter = new HeadBannerAdapter();
        lmBanners.setAdapter(adapter, list);

        // 这里一定要加载完后再设置可见，因为LMBanners的内部代码里有一个btnStart，本来是受isGuide的控制
        // 但是1.0.8版本里只在onPageScroll里面判断了这个属性。导致如果一开始LMBanners处于可见状态，
        // adapter里还没有数据，btnStart就会一直显示在那里，知道开始触发onPageScroll才会隐藏
        // 本来引入library，在setGuide把btnStart的visibility置为gone就可以了，但是这个项目已经引入了很多module了，就不再引入了
        lmBanners.setVisibility(View.VISIBLE);
    }

    /**
     * tabLayout 标题对应数量
     *
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
        fragmentAll.setSortMode(curSortMode);
        pagerAdapter.addFragment(fragmentAll, titles[0] + "(" + bean.getAllNumber() + ")");
        StarListFragment fragment1 = new StarListFragment();
        fragment1.setStarMode(GDBProperites.STAR_MODE_TOP);
        fragment1.setSortMode(curSortMode);
        pagerAdapter.addFragment(fragment1, titles[1] + "(" + bean.getTopNumber() + ")");
        StarListFragment fragment0 = new StarListFragment();
        fragment0.setStarMode(GDBProperites.STAR_MODE_BOTTOM);
        fragment0.setSortMode(curSortMode);
        pagerAdapter.addFragment(fragment0, titles[2] + "(" + bean.getBottomNumber() + ")");
        StarListFragment fragment05 = new StarListFragment();
        fragment05.setStarMode(GDBProperites.STAR_MODE_HALF);
        fragment05.setSortMode(curSortMode);
        pagerAdapter.addFragment(fragment05, titles[3] + "(" + bean.getHalfNumber() + ")");
        viewpager.setAdapter(pagerAdapter);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[2]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[3]));
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lmBanners != null) {
            lmBanners.stopImageTimerTask();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lmBanners != null) {
            lmBanners.startImageTimerTask();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lmBanners != null) {
            lmBanners.clearImageTimerTask();
        }
    }

    private ActionBar.ActionIconListener iconListener = new ActionBar.ActionIconListener() {
        @Override
        public void onBack() {
            finish();
        }

        @Override
        public void onIconClick(View view) {
            switch (view.getId()) {
                case R.id.actionbar_sort_by_num:
                    if (view.isSelected()) {
                        view.setSelected(false);
                        actionBar.addIndexIcon();
                        curSortMode = GdbConstants.STAR_SORT_NAME;
                    } else {
                        view.setSelected(true);
                        actionBar.hideIndexIcon();
                        findViewById(R.id.actionbar_favor).setSelected(false);
                        curSortMode = GdbConstants.STAR_SORT_RECORDS;
                        if (sideBar.getVisibility() == View.VISIBLE) {
                            sideBar.setVisibility(View.GONE);
                        }
                    }
                    pagerAdapter.getItem(viewpager.getCurrentItem()).reloadStarList(curSortMode);
                    break;
                case R.id.actionbar_index:
                    changeSideBarVisible();
                    break;
                case R.id.actionbar_favor:
                    if (view.isSelected()) {
                        view.setSelected(false);
                        actionBar.addIndexIcon();
                        curSortMode = GdbConstants.STAR_SORT_NAME;
                    } else {
                        view.setSelected(true);
                        actionBar.hideIndexIcon();
                        findViewById(R.id.actionbar_sort_by_num).setSelected(false);
                        curSortMode = GdbConstants.STAR_SORT_FAVOR;
                        if (sideBar.getVisibility() == View.VISIBLE) {
                            sideBar.setVisibility(View.GONE);
                        }
                    }
                    pagerAdapter.getItem(viewpager.getCurrentItem()).reloadStarList(curSortMode);
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

    @Override
    public void onBackPressed() {
        if (actionBar != null && actionBar.isSearchVisible()) {
            actionBar.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.group_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_setting:
                showSettingDialog();
                break;
        }
    }

    private void showSettingDialog() {
        if (settingDialog == null) {
            settingDialog = new StarListSettingDialog(this, new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    initBanner();
                    return false;
                }

                @Override
                public boolean onCancel() {
                    return false;
                }

                @Override
                public void onLoadData(HashMap<String, Object> data) {

                }
            });
        }
        settingDialog.show();
    }

    private void onClickBannerItem(FavorBean bean) {
        ActivityManager.startStarActivity(this, bean.getStarId());
    }

    /**
     * 切换时的动画模式
     * @param position
     */
    private void setScrollAnim(int position){
        switch (position) {
            case 0:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Default);//Default
                break;
            case 1:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Alpha);//Alpha
                break;
            case 2:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Rotate);//Rotate
                break;
            case 3:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Cube);//Cube
                break;
            case 4:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Flip);//Flip
                break;
            case 5:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Accordion);//Accordion
                break;
            case 6:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.ZoomFade);//ZoomFade
                break;
            case 7:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Fade);//Fade
                break;
            case 8:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.ZoomCenter);//ZoomCenter
                break;
            case 9:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.ZoomStack);//ZoomStack
                break;
            case 10:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Stack);//Stack
                break;
            case 11:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Depth);//Depth
                break;
            case 12:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.Zoom);//Zoom
                break;
            case 13:
                lmBanners.setHoriZontalTransitionEffect(TransitionEffect.ZoomOut);//ZoomOut
                break;
//            case 14:
//                lmBanners.setHoriZontalCustomTransformer(new ParallaxTransformer(R.id.id_image));//Parallax
//                break;

        }
    }

    private class HeadBannerAdapter implements LBaseAdapter<FavorBean>, View.OnClickListener {

        @Override
        public View getView(LMBanners lBanners, Context context, int position, FavorBean bean) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_gdb_star_list_banner, null);

            bean = starPresenter.nextFavorStar();
            if (bean != null) {
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_star);
                String path = Configuration.GDB_IMG_STAR + "/" + bean.getStarName() + EncryptUtil.getFileExtra();
                // list 列表缓存了小图，这里需要先清除小图
                SImageLoader.getInstance().removeCache(path);
                SImageLoader.getInstance().displayImage(path, imageView);

                RelativeLayout groupContainer = (RelativeLayout) view.findViewById(R.id.group_container);
                groupContainer.setTag(bean);
                groupContainer.setOnClickListener(this);
            }
            return view;
        }

        @Override
        public void onClick(View v) {
            FavorBean bean = (FavorBean) v.getTag();
            onClickBannerItem(bean);
        }

    }
}

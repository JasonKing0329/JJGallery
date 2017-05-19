package com.jing.app.jjgallery.gdb.view.star;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.bean.http.DownloadItem;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.presenter.TouchHelper;
import com.jing.app.jjgallery.gdb.view.pub.DownloadDialog;
import com.jing.app.jjgallery.gdb.view.list.IGdbFragment;
import com.jing.app.jjgallery.gdb.view.list.IListPageParent;
import com.jing.app.jjgallery.gdb.view.pub.PinnedHeaderDecoration;
import com.jing.app.jjgallery.gdb.view.adapter.StarIndicatorAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListNumAdapter;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.gdb.view.recommend.RecommendDialog;
import com.jing.app.jjgallery.viewsystem.publicview.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.Star;
import com.king.service.gdb.bean.StarCountBean;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends Fragment implements IGdbStarListView, OnStarClickListener
    , IGdbFragment, TouchHelper.OnTouchActionListener {

    private int mSortMode;
    private RecyclerView mRecyclerView;
    private WaveSideBarView mSideBarView;

    private StarListAdapter mNameAdapter;

    private StarListNumAdapter mNumberAdapter;
    private IListPageParent iListPageParent;

    private DownloadDialog downloadDialog;
    private RecommendDialog recommendDialog;

    private FixedIndicatorView indicatorView;
    private StarIndicatorAdapter indicatorAdapter;

    private TouchHelper touchHelper;
    private boolean isSwiping;

    // see GDBProperties.STAR_MODE_XXX
    private String curStarMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        iListPageParent = (IListPageParent) getActivity();
        touchHelper = new TouchHelper(getActivity());
        touchHelper.setOnTouchActionListener(this);

        initActionbar();

        View view = inflater.inflate(R.layout.page_gdb_starlist, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gdb_star_recycler_view);
        mSideBarView = (WaveSideBarView) view.findViewById(R.id.gdb_star_side_view);
        indicatorView = (FixedIndicatorView) view.findViewById(R.id.gdb_star_indicator_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mNameAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                }
            }
        });

        ((ProgressProvider) getActivity()).showProgressCycler();

        initIndicators();
        iListPageParent.getPresenter().loadStarList(curStarMode);
        return view;
    }

    private void initIndicators() {
        indicatorAdapter = new StarIndicatorAdapter();
        indicatorView.setAdapter(indicatorAdapter);

        float unSelectSize = 16;
        float selectSize = unSelectSize * 1.2f;

        int selectColor = getResources().getColor(R.color.tab_top_text_2);
        int unSelectColor = getResources().getColor(R.color.tab_top_text_1);
        int underlineColor = getResources().getColor(R.color.actionbar_bk_blue);
        if (ThemeManager.getInstance().isDarkTheme(getActivity())) {
            selectColor = getResources().getColor(R.color.tab_top_text_2_dark);
            unSelectColor = getResources().getColor(R.color.tab_top_text_1_dark);
            underlineColor = getResources().getColor(R.color.tab_top_text_2_dark);
        }
        int height = getResources().getDimensionPixelSize(R.dimen.gdb_indicator_height);
        indicatorView.setScrollBar(new ColorBar(getActivity(), underlineColor, height));
        indicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        indicatorView.setOnItemSelectListener(new Indicator.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View selectItemView, int select, int preSelect) {
                switch (select) {
                    case 1:
                        curStarMode = GDBProperites.STAR_MODE_TOP;
                        break;
                    case 2:
                        curStarMode = GDBProperites.STAR_MODE_BOTTOM;
                        break;
                    case 3:
                        curStarMode = GDBProperites.STAR_MODE_HALF;
                        break;
                    default:
                        curStarMode = GDBProperites.STAR_MODE_ALL;
                        break;
                }
                loadStar();
            }
        });
        indicatorView.setCurrentItem(0,true);
        curStarMode = GDBProperites.STAR_MODE_ALL;

        iListPageParent.getPresenter().queryIndicatorData();
    }

    private void initActionbar() {
        iListPageParent.getActionbar().clearActionIcon();
        iListPageParent.getActionbar().addMenuIcon();
        iListPageParent.getActionbar().addSearchIcon();
        iListPageParent.getActionbar().addHomeIcon();
        iListPageParent.getActionbar().addSortIcon();
        iListPageParent.getActionbar().addFavorIcon();
        iListPageParent.getActionbar().addIndexIcon();
    }

    public void onIconClick(View view) {
        switch (view.getId()) {
            case R.id.actionbar_sort:
                if (mSortMode == GdbConstants.STAR_SORT_NAME) {
                    mSortMode = GdbConstants.STAR_SORT_RECORDS;
                }
                else {
                    mSortMode = GdbConstants.STAR_SORT_NAME;
                }
                loadStar();
                break;
            case R.id.actionbar_index:
                mSideBarView.setVisibility(mSideBarView.getVisibility() == View.GONE ? View.VISIBLE:View.GONE);
                break;
            case R.id.actionbar_favor:
                if (mSortMode == GdbConstants.STAR_SORT_NAME) {
                    mSortMode = GdbConstants.STAR_SORT_FAVOR;
                }
                else {
                    mSortMode = GdbConstants.STAR_SORT_NAME;
                }
                loadStar();
                break;
        }
    }

    private void loadStar() {
        if (mSortMode == GdbConstants.STAR_SORT_NAME) {
            sortByName();
        }
        else if (mSortMode == GdbConstants.STAR_SORT_FAVOR) {
            sortByFavor();
        }
        else {
            sortByRecordNumbers();
        }
    }

    private void sortByName() {
        iListPageParent.getPresenter().loadStarList(curStarMode);
    }

    private void sortByRecordNumbers() {
        iListPageParent.getPresenter().loadStarListOrderByNumber(curStarMode);
    }

    private void sortByFavor() {
        iListPageParent.getPresenter().loadStarListOrderByFavor(curStarMode);
    }

    @Override
    public void onLoadStarList(List<StarProxy> list) {
        if (mSortMode == GdbConstants.STAR_SORT_RECORDS) {
            mNumberAdapter = new StarListNumAdapter(list);
            mNumberAdapter.setPresenter(iListPageParent.getPresenter());
            mNumberAdapter.setOnStarClickListener(this);
            mRecyclerView.setAdapter(mNumberAdapter);
        }
        else {
            mNameAdapter = new StarListAdapter(getActivity(), list);
            mNameAdapter.setPresenter(iListPageParent.getPresenter());
            mNameAdapter.setOnStarClickListener(this);
            mRecyclerView.setAdapter(mNameAdapter);
        }
        ((ProgressProvider) getActivity()).dismissProgressCycler();

        iListPageParent.getPresenter().checkNewStarFile();
    }

    @Override
    public void onStarCountLoaded(StarCountBean bean) {
        indicatorAdapter.updateStarCountBean(bean);
        indicatorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStarClick(StarProxy star) {
        // 由于当前界面加载的star图片都是50*50的小图，但是lru包里的ImageLoader会在缓存中保存图片实例
        // 进入Star page后，加载的图片key没有变，就会从缓存读取，最后就只能显示很模糊的小图
        // 因此，在这里要删除掉该图的缓存，迫使其重新加载
        SImageLoader.getInstance().removeCache(star.getImagePath());
        ActivityManager.startStarActivity(getActivity(), star.getStar());
    }

    public void onTextChanged(String text, int start, int before, int count) {
        if (mNameAdapter != null) {
            mNameAdapter.onStarFilter(text);
        }
    }

    public void reInit() {
        // post刷新mSideBarView，根据调试发现重写初始化后WaveSideBarView会重新执行onMeasure(width,height=0)->onDraw->onMeasure(width,height=正确值)
        // 缺少重新onDraw的过程，因此通过delay执行mSideBarView.invalidate()可以激活onDraw事件，根据正确的值重新绘制
        // 用mSideBarView.post/postDelayed总是不准确
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSideBarView.invalidate();
            }
        }, 200);
    }

    @Override
    public void onServerConnected() {
//        ((ProgressProvider) getActivity()).showToastShort(getString(R.string.gdb_server_online), ProgressProvider.TOAST_SUCCESS);
        if (isVisible()) {
            iListPageParent.getPresenter().checkNewStarFile();
        }
    }

    @Override
    public void onServerUnavailable() {
        if (isVisible()) {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_server_offline), ProgressProvider.TOAST_ERROR);
        }
    }

    @Override
    public void onRequestFail() {
        if (isVisible()) {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_request_fail), ProgressProvider.TOAST_ERROR);
        }
    }

    @Override
    public void onCheckPass(boolean hasNew, final List<DownloadItem> downloadList) {
        if (hasNew) {
            if (downloadDialog == null) {
                downloadDialog = new DownloadDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        return false;
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        List<DownloadItem> repeatList = new ArrayList<>();
                        data.put("items", iListPageParent.getPresenter().pickStarToDownload(downloadList, repeatList));
                        data.put("existedItems", repeatList);
                        data.put("savePath", Configuration.GDB_IMG_STAR);
                        data.put("optionMsg", String.format(getContext().getString(R.string.gdb_option_download), downloadList.size()));
                    }
                });
                downloadDialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
                    @Override
                    public void onDownloadFinish(DownloadItem item) {

                    }

                    @Override
                    public void onDownloadFinish(List<DownloadItem> downloadList) {
                        // 所有内容下载完成后，统一进行异步encypt，然后更新starImageMap和recordImageMap，完成后通知adapter更新
                        iListPageParent.getPresenter().finishDownload(downloadList);
                    }
                });
            }
            else {
                List<DownloadItem> repeatList = new ArrayList<>();
                List<DownloadItem> newList = iListPageParent.getPresenter().pickStarToDownload(downloadList, repeatList);
                downloadDialog.newUpdate(newList, repeatList);
            }
            downloadDialog.show();
        }
        else {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_no_new_images), ProgressProvider.TOAST_INFOR);
        }
    }

    @Override
    public void onDownloadItemEncrypted() {
        mNameAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_gdb_check_server:
                iListPageParent.getPresenter().checkNewStarFile();
                break;
            case R.id.menu_gdb_recommend:
                if (recommendDialog == null) {
                    recommendDialog = new RecommendDialog(getActivity());
                }
                recommendDialog.show();
                break;
            case R.id.menu_gdb_download:
                if (downloadDialog != null) {
                    downloadDialog.show();
                }
                break;
        }
        return false;
    }

    public void reloadStarList() {
        iListPageParent.getPresenter().loadStarList(curStarMode);
    }

    public void checkServerStatus() {
        // 只要检测完更新，无论成功或失败都要接着开始检测图片更新
        iListPageParent.getPresenter().checkServerStatus();
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (touchHelper != null) {
            return touchHelper.dispatchTouchEvent(event);
        }
        return false;
    }

    @Override
    public void onSwipeUp() {
        // 防止多次重复执行动画，动画执行结束再置为false
        if (!isSwiping && indicatorView.getVisibility() != View.GONE) {
            isSwiping = true;
            hideIndicator();
        }
    }

    @Override
    public void onSwipeBottom() {
        // 防止多次重复执行动画，动画执行结束再置为false
        if (!isSwiping && indicatorView.getVisibility() != View.VISIBLE) {
            isSwiping = true;
            showIndicator();
        }
    }

    /**
     * hide actionbar
     */
    public void hideIndicator() {
        Animation anim = getDisapplearAnim();
        indicatorView.startAnimation(anim);
        anim.setAnimationListener(disappearListener);
    }

    /**
     * show action bar, default status is show
     */
    public void showIndicator() {
        Animation anim = getAppearAnim();
        indicatorView.startAnimation(anim);
        anim.setAnimationListener(appearListener);
        indicatorView.setVisibility(View.VISIBLE);
    }

    private Animation.AnimationListener appearListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isSwiping = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener disappearListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            indicatorView.setVisibility(View.GONE);
            isSwiping = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * actionbar show animation
     * @return
     */
    public Animation getAppearAnim() {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
            , Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0);
        anim.setDuration(500);
        return anim;
    }

    /**
     * actionbar hide animation
     * @return
     */
    public Animation getDisapplearAnim() {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        anim.setDuration(500);
//        Animation disappearAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.disappear);
        return anim;
    }
}

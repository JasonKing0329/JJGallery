package com.jing.app.jjgallery.gdb.view.star;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.TouchHelper;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.adapter.StarIndicatorAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListNumAdapter;
import com.jing.app.jjgallery.gdb.view.pub.PinnedHeaderDecoration;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.GDBProperites;
import com.king.service.gdb.bean.StarCountBean;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends GBaseFragment implements OnStarClickListener, IStarListView
        , TouchHelper.OnTouchActionListener {

    @BindView(R.id.indicator_view)
    FixedIndicatorView indicatorView;
    @BindView(R.id.rv_star)
    RecyclerView rvStar;
    @BindView(R.id.side_bar)
    WaveSideBarView sideBar;

    private int mSortMode;

    private StarListAdapter mNameAdapter;

    private StarListNumAdapter mNumberAdapter;
    private IStarListHolder holder;

    private StarIndicatorAdapter indicatorAdapter;

    private TouchHelper touchHelper;
    private boolean isSwiping;

    // see GDBProperties.STAR_MODE_XXX
    private String curStarMode;

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        this.holder = (IStarListHolder) holder;
        this.holder.getPresenter().setStarListView(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.page_gdb_starlist;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        touchHelper = new TouchHelper(getActivity());
        touchHelper.setOnTouchActionListener(this);

        rvStar.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        rvStar.addItemDecoration(decoration);

        sideBar.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mNameAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    rvStar.scrollToPosition(pos);
                }
            }
        });

        showProgressCycler();
        initIndicators();
        holder.getPresenter().loadStarList(curStarMode);
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
        indicatorView.setCurrentItem(0, true);
        curStarMode = GDBProperites.STAR_MODE_ALL;

        holder.getPresenter().queryIndicatorData();
    }

    private void loadStar() {
        if (mSortMode == GdbConstants.STAR_SORT_NAME) {
            sortByName();
        } else if (mSortMode == GdbConstants.STAR_SORT_FAVOR) {
            sortByFavor();
        } else {
            sortByRecordNumbers();
        }
    }

    private void sortByName() {
        holder.getPresenter().loadStarList(curStarMode);
    }

    private void sortByRecordNumbers() {
        holder.getPresenter().loadStarListOrderByNumber(curStarMode);
    }

    private void sortByFavor() {
        holder.getPresenter().loadStarListOrderByFavor(curStarMode);
    }

    @Override
    public void onLoadStarList(List<StarProxy> list) {
        if (mSortMode == GdbConstants.STAR_SORT_RECORDS) {
            mNumberAdapter = new StarListNumAdapter(list);
            mNumberAdapter.setPresenter(holder.getPresenter());
            mNumberAdapter.setOnStarClickListener(this);
            rvStar.setAdapter(mNumberAdapter);
        } else {
            mNameAdapter = new StarListAdapter(getActivity(), list);
            mNameAdapter.setPresenter(holder.getPresenter());
            mNameAdapter.setOnStarClickListener(this);
            rvStar.setAdapter(mNameAdapter);
        }
        dismissProgressCycler();
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

    public void filterStar(String text) {
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
                sideBar.invalidate();
            }
        }, 200);
    }

    public void reloadStarList() {
        holder.getPresenter().loadStarList(curStarMode);
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
     *
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
     *
     * @return
     */
    public Animation getDisapplearAnim() {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0
                , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f);
        anim.setDuration(500);
//        Animation disappearAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.disappear);
        return anim;
    }

    /**
     * change sort style
     * switch between STAR_SORT_NAME and STAR_SORT_RECORDS
     */
    public void changeSortType() {
        if (mSortMode == GdbConstants.STAR_SORT_NAME) {
            mSortMode = GdbConstants.STAR_SORT_RECORDS;
        } else {
            mSortMode = GdbConstants.STAR_SORT_NAME;
        }
        loadStar();
    }

    public void changeSideBarVisible() {
        sideBar.setVisibility(sideBar.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    public void changeFavorList() {
        if (mSortMode == GdbConstants.STAR_SORT_NAME) {
            mSortMode = GdbConstants.STAR_SORT_FAVOR;
        } else {
            mSortMode = GdbConstants.STAR_SORT_NAME;
        }
        loadStar();
    }

    public void refreshList() {
        mNameAdapter.notifyDataSetChanged();
    }
}

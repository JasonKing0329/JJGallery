package com.jing.app.jjgallery.gdb.view.star;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.adapter.StarListAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListNumAdapter;
import com.jing.app.jjgallery.gdb.view.pub.PinnedHeaderDecoration;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.viewsystem.ActivityManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends GBaseFragment implements OnStarClickListener, IStarListView {

    @BindView(R.id.rv_star)
    RecyclerView rvStar;

    private int mSortMode;

    private StarListAdapter mNameAdapter;

    private StarListNumAdapter mNumberAdapter;
    private IStarListHolder holder;

    // see GDBProperties.STAR_MODE_XXX
    private String curStarMode;

    public void setStarMode(String curStarMode) {
        this.curStarMode = curStarMode;
    }

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        this.holder = (IStarListHolder) holder;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.page_gdb_starlist;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        rvStar.setLayoutManager(new LinearLayoutManager(getActivity()));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        rvStar.addItemDecoration(decoration);

        showProgressCycler();
        holder.getPresenter().loadStarList(curStarMode, this);
    }

    public void onLetterChange(String letter) {
        int pos = mNameAdapter.getLetterPosition(letter);

        if (pos != -1) {
            rvStar.scrollToPosition(pos);
        }
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
        holder.getPresenter().loadStarList(curStarMode, this);
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

    public void reloadStarList() {
        holder.getPresenter().loadStarList(curStarMode, this);
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

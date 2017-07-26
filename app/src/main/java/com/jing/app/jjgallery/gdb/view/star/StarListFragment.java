package com.jing.app.jjgallery.gdb.view.star;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jing.app.jjgallery.BaseFragmentV4;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GdbConstants;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.adapter.StarListAdapter;
import com.jing.app.jjgallery.gdb.view.adapter.StarListNumAdapter;
import com.jing.app.jjgallery.gdb.view.pub.PinnedHeaderDecoration;
import com.jing.app.jjgallery.viewsystem.ActivityManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends BaseFragmentV4 implements OnStarClickListener, IStarListView {

    @BindView(R.id.rv_star)
    RecyclerView rvStar;

    private int mSortMode;

    private StarListAdapter mNameAdapter;

    private StarListNumAdapter mNumberAdapter;
    private IStarListHolder holder;

    // see GDBProperties.STAR_MODE_XXX
    private String curStarMode;
    // see GDBProperties.STAR_SORT_XXX
    private int curSortMode;

    public void setStarMode(String curStarMode) {
        this.curStarMode = curStarMode;
    }

    public void setSortMode(int curSortMode) {
        this.curSortMode = curSortMode;
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

        holder.getPresenter().loadStarList(curStarMode, curSortMode, this);
    }

    public void onLetterChange(String letter) {
        int pos = mNameAdapter.getLetterPosition(letter);

        if (pos != -1) {
            rvStar.scrollToPosition(pos);
        }
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
    }

    @Override
    public void onStarClick(StarProxy star) {
        ActivityManager.startStarActivity(getActivity(), star.getStar());
    }

    public void filterStar(String text) {
        if (mNameAdapter != null) {
            mNameAdapter.onStarFilter(text);
        }
    }

    public void reloadStarList(int sortMode) {
        if (curSortMode != sortMode) {
            curSortMode = sortMode;
            holder.getPresenter().loadStarList(curStarMode, curSortMode, this);
        }
    }

    public void refreshList() {
        mNameAdapter.notifyDataSetChanged();
    }

}

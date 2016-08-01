package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.Star;

import java.util.List;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends Fragment implements IGdbView, StarListAdapter.OnStarClickListener {

    private RecyclerView mRecyclerView;
    private WaveSideBarView mSideBarView;

    private GdbPresenter gdbPresenter;
    private StarListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gdbPresenter = new GdbPresenter(this);

        View view = inflater.inflate(R.layout.page_gdb_starlist, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.gdb_star_recycler_view);
        mSideBarView = (WaveSideBarView) view.findViewById(R.id.gdb_star_side_view);
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
                int pos = mAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                }
            }
        });

        gdbPresenter.loadStarList();
        return view;
    }

    @Override
    public void onLoadStarList(List<Star> list) {
        mAdapter = new StarListAdapter(getActivity(), list);
        mAdapter.setOnStarClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStarClick(Star star) {
        ActivityManager.startStarActivity(getActivity(), star);
    }
}

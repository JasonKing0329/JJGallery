package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;

/**
 * Created by JingYang on 2016/8/1 0001.
 * Description:
 */
public class StarFragment extends Fragment implements IStarView {

    private int starId;
    private GdbPresenter mPresenter;
    protected PullZoomRecyclerView mRecyclerView;

    public StarFragment(int starId) {
        this.starId = starId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new GdbPresenter(this);
        return inflater.inflate(R.layout.fragment_pull_zoom_header, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (PullZoomRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).showProgressCycler();
        }
        mPresenter.loadStar(starId);
    }

    protected RecyclerListAdapter createPullZoomAdapter(StarProxy star) {
        return new StarRecordsAdapter(star, mRecyclerView);
    }

    @Override
    public void onStarLoaded(StarProxy star) {
        mRecyclerView.setAdapter(createPullZoomAdapter(star));
        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).dismissProgressCycler();
        }
    }
}

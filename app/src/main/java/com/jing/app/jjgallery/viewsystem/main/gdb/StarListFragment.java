package com.jing.app.jjgallery.viewsystem.main.gdb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.bean.StarProxy;
import com.jing.app.jjgallery.bean.http.GdbRespBean;
import com.jing.app.jjgallery.presenter.main.GdbPresenter;
import com.jing.app.jjgallery.service.http.HttpMethods;
import com.jing.app.jjgallery.service.http.progress.ProgressListener;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.publicview.WaveSideBarView;
import com.king.service.gdb.bean.Star;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/30 0030.
 */
public class StarListFragment extends Fragment implements IGdbStarListView, StarListAdapter.OnStarClickListener {

    private RecyclerView mRecyclerView;
    private WaveSideBarView mSideBarView;

    private GdbPresenter gdbPresenter;
    private StarListAdapter mAdapter;
    private ActionBar mActionbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gdbPresenter = new GdbPresenter(this);
        initActionbar();

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

        ((ProgressProvider) getActivity()).showProgressCycler();
        gdbPresenter.loadStarList();
        return view;
    }

    public void setActionbar(ActionBar actionbar) {
        this.mActionbar = actionbar;
    }

    private void initActionbar() {
        mActionbar.clearActionIcon();
        mActionbar.addMenuIcon();
        mActionbar.addSearchIcon();
        mActionbar.addHomeIcon();
    }

    @Override
    public void onLoadStarList(List<Star> list) {
        mAdapter = new StarListAdapter(getActivity(), list);
        mAdapter.setPresenter(gdbPresenter);
        mAdapter.setOnStarClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        ((ProgressProvider) getActivity()).dismissProgressCycler();

        gdbPresenter.checkServerStatus();
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
        if (mAdapter != null) {
            mAdapter.onStarFilter(text);
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
        }, 100);
    }

    @Override
    public void onServerConnected() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_server_online), ProgressProvider.TOAST_SUCCESS);
        gdbPresenter.checkNewStarFile();
    }

    @Override
    public void onServerUnavailable() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_server_offline), ProgressProvider.TOAST_ERROR);
    }

    @Override
    public void onRequestFail() {
        ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_request_fail), ProgressProvider.TOAST_ERROR);
    }

    @Override
    public void onCheckPass(boolean hasNew, final List<String> fileNames) {
        if (hasNew) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.gdb_option_download_star)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String fileName = fileNames.get(0);
                            gdbPresenter.downloadFile(fileName, "star", new ProgressListener() {
                                @Override
                                public void update(long bytesRead, long contentLength, boolean done) {
                                    DebugLog.e("progress:" + (100 * 1f * bytesRead / contentLength));
                                }
                            });
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
        else {
            ((ProgressProvider) getActivity()).showToastLong(getString(R.string.gdb_no_new_images), ProgressProvider.TOAST_INFOR);
        }
    }

}

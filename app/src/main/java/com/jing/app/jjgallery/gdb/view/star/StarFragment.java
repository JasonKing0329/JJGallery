package com.jing.app.jjgallery.gdb.view.star;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.BaseSlidingActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.presenter.star.StarPresenter;
import com.jing.app.jjgallery.gdb.view.pub.BannerAnimDialogFragment;
import com.jing.app.jjgallery.gdb.view.record.SortDialog;
import com.jing.app.jjgallery.gdb.view.adapter.StarRecordsAdapter;
import com.jing.app.jjgallery.presenter.main.SettingProperties;
import com.jing.app.jjgallery.viewsystem.ActivityManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;
import com.jing.app.jjgallery.viewsystem.sub.dialog.CustomDialog;
import com.jing.app.jjgallery.viewsystem.publicview.PullZoomRecyclerView;
import com.king.service.gdb.bean.FavorBean;
import com.king.service.gdb.bean.Record;
import com.king.service.gdb.bean.Star;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JingYang on 2016/8/1 0001.
 * Description:
 */
public class StarFragment extends Fragment implements IStarView, StarRecordsAdapter.OnRecordItemClickListener {

    private int starId;
    private StarPresenter mPresenter;
    protected PullZoomRecyclerView mRecyclerView;
    private StarRecordsAdapter mAdapter;

    public ActionBar mActionbar;
    private int currentSortMode = -1;
    private boolean currentSortDesc = true;
    private StarProxy starProxy;

    private BannerAnimDialogFragment bannerSettingDialog;
    
    public void setStarId(int starId) {
        this.starId = starId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new StarPresenter(this);
        currentSortMode = SettingProperties.getGdbStarRecordOrderMode(getActivity());
        return inflater.inflate(R.layout.fragment_pull_zoom_header, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initActionbar();

        mRecyclerView = (PullZoomRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initValue();
    }

    public void initValue() {
        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).showProgressCycler();
        }
        mPresenter.loadStar(starId);
        onResume();
    }

    /**
     * 由于StarActivity被设置为了singleTask，重新启动一个StarActivity的时候不会执行onCreate，这时候就需要在
     * StarActivity的onNewIntent设置完成后重新获取starId进行重新加载了
     */
    public void onNewIntent() {
        initValue();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setActionbar(ActionBar actionbar) {
        this.mActionbar = actionbar;
    }

    private void initActionbar() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).requestActionbarFloating(false);
        }
        else if (getActivity() instanceof BaseSlidingActivity) {
            ((BaseSlidingActivity) getActivity()).requestActionbarFloating(false);
        }
        mActionbar.addSortIcon();
        mActionbar.addBackIcon();
        mActionbar.hide();
    }

    @Override
    public void onStarLoaded(StarProxy star) {
        starProxy = star;
        mPresenter.sortRecords(star.getStar().getRecordList(), currentSortMode, currentSortDesc);

        boolean isStarFavor = mPresenter.isStarFavor(star.getStar().getId());
        mAdapter = new StarRecordsAdapter(star, mRecyclerView);
        mAdapter.setStarFavor(isStarFavor);
        mAdapter.setItemClickListener(this);
        mAdapter.setSortMode(currentSortMode);
        mRecyclerView.setAdapter(mAdapter);
        if (getActivity() instanceof ProgressProvider) {
            ((ProgressProvider) getActivity()).dismissProgressCycler();
        }
    }

    public void onIconClick(View view) {
        if (view.getId() == R.id.actionbar_sort) {
            new SortDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    Map<String, Object> map = (Map<String, Object>) object;
                    int sortMode = (int) map.get("sortMode");
                    boolean desc = (Boolean) map.get("desc");
                    if (currentSortMode != sortMode || currentSortDesc != desc) {
                        currentSortMode = sortMode;
                        currentSortDesc = desc;
                        SettingProperties.setGdbStarRecordOrderMode(getActivity(), currentSortMode);
                        refresh();
                    }
                    return false;
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
            }).show();
        }
    }

    private void refresh() {
        mPresenter.sortRecords(starProxy.getStar().getRecordList(), currentSortMode, currentSortDesc);
        mAdapter.setSortMode(currentSortMode);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecordItem(Pair<View, String>[] pairs, Record record) {
        // set anchor views of transition animation
        ActivityManager.startGdbRecordActivity(getActivity(), record, pairs);
    }

    @Override
    public void onFavorStar(Star star, int score) {
        FavorBean bean = new FavorBean();
        bean.setStarId(star.getId());
        bean.setFavor(score);
        bean.setStarName(star.getName());
        mPresenter.saveFavor(bean);
    }

    @Override
    public void showAnimSetting() {
        if (bannerSettingDialog == null) {
            bannerSettingDialog = new BannerAnimDialogFragment();
            bannerSettingDialog.setOnAnimSettingListener(new BannerAnimDialogFragment.OnAnimSettingListener() {
                @Override
                public void onRandomAnim(boolean random) {
                    SettingProperties.setGdbStarNavAnimRandom(getActivity(), random);
                }

                @Override
                public boolean isRandomAnim() {
                    return SettingProperties.isGdbStarNavAnimRandom(getActivity());
                }

                @Override
                public int getAnimType() {
                    return SettingProperties.getGdbStarNavAnimType(getActivity());
                }

                @Override
                public void onSaveAnimType(int type) {
                    SettingProperties.setGdbStarNavAnimType(getActivity(), type);
                }

                @Override
                public int getAnimTime() {
                    return SettingProperties.getGdbStarNavAnimTime(getActivity());
                }

                @Override
                public void onSaveAnimTime(int time) {
                    SettingProperties.setGdbStarNavAnimTime(getActivity(), time);
                }

                @Override
                public void onParamsSaved() {
                    mAdapter.refreshBanner();
                }
            });
        }
        bannerSettingDialog.show(getChildFragmentManager(), "BannerAnimDialogFragment");
    }

}

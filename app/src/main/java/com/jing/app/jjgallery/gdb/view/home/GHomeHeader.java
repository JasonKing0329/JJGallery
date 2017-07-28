package com.jing.app.jjgallery.gdb.view.home;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.bean.StarProxy;
import com.jing.app.jjgallery.gdb.view.recommend.RecommendFragment;
import com.jing.app.jjgallery.service.image.SImageLoader;
import com.jing.app.jjgallery.util.DisplayHelper;
import com.jing.app.jjgallery.viewsystem.ActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public class GHomeHeader extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_star1)
    RoundedImageView ivStar1;
    @BindView(R.id.iv_star2)
    RoundedImageView ivStar2;
    @BindView(R.id.iv_star3)
    RoundedImageView ivStar3;
    @BindView(R.id.iv_star4)
    RoundedImageView ivStar4;
    @BindView(R.id.tv_star_name1)
    TextView tvStarName1;
    @BindView(R.id.tv_star_name2)
    TextView tvStarName2;
    @BindView(R.id.tv_star_name3)
    TextView tvStarName3;
    @BindView(R.id.tv_star_name4)
    TextView tvStarName4;

    private OnStarListener onStarListener;
    private GHomeBean homeBean;

    public GHomeHeader(View itemView, FragmentManager fragmentManager, OnStarListener onStarListener) {
        super(itemView);
        this.onStarListener = onStarListener;
        ButterKnife.bind(this, itemView);
        initRecommentd(fragmentManager);
    }

    private void initRecommentd(FragmentManager fragmentManager) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        RecommendFragment fragment = new RecommendFragment();
        ft.add(R.id.group_recommend, fragment, "RecommendFragment");
        ft.commit();
    }

    public void bindView(GHomeBean bean) {

        homeBean = bean;

        if (bean.getStarList().size() > 0) {
            tvStarName1.setText(bean.getStarList().get(0).getStar().getName());
            SImageLoader.getInstance().displayImage(
                    bean.getStarList().get(0).getImagePath(), ivStar1);
        }
        if (bean.getStarList().size() > 1) {
            tvStarName2.setText(bean.getStarList().get(1).getStar().getName());
            SImageLoader.getInstance().displayImage(
                    bean.getStarList().get(1).getImagePath(), ivStar2);
        }
        if (bean.getStarList().size() > 2) {
            tvStarName3.setText(bean.getStarList().get(2).getStar().getName());
            SImageLoader.getInstance().displayImage(
                    bean.getStarList().get(2).getImagePath(), ivStar3);
        }
        // 平板显示4个
        if (DisplayHelper.isTabModel(ivStar4.getContext())) {
            if (bean.getStarList().size() > 3) {
                tvStarName4.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                tvStarName4.setText(bean.getStarList().get(3).getStar().getName());
                SImageLoader.getInstance().displayImage(
                        bean.getStarList().get(3).getImagePath(), ivStar4);
            }
        }
    }

    @OnClick({R.id.group_recommend, R.id.group_starlist, R.id.group_game, R.id.group_surf})
    public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.group_recommend:
                    ActivityManager.startGDBRecordListActivity((Activity) view.getContext(), null);
                    break;
                case R.id.group_starlist:
                    onStarListener.onStarGroupClicked();
                    break;
                case R.id.group_game:
                    ActivityManager.startGDBGameActivity((Activity) view.getContext(), null);
                    break;
                case R.id.group_surf:
                    ActivityManager.startGdbSurfActivity((Activity) view.getContext(), null);
                    break;
            }
    }

    @OnClick({R.id.iv_star1, R.id.iv_star2, R.id.iv_star3, R.id.iv_star4})
    public void onStarClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_star1:
                onStarListener.onStarClicked(homeBean.getStarList().get(0));
                break;
            case R.id.iv_star2:
                onStarListener.onStarClicked(homeBean.getStarList().get(1));
                break;
            case R.id.iv_star3:
                onStarListener.onStarClicked(homeBean.getStarList().get(2));
                break;
            case R.id.iv_star4:
                onStarListener.onStarClicked(homeBean.getStarList().get(3));
                break;
        }
    }

    public interface OnStarListener {
        void onStarGroupClicked();
        void onStarClicked(StarProxy starProxy);
    }
}

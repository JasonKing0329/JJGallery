package com.jing.app.jjgallery.gdb.view.home;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.gdb.GBaseFragment;
import com.jing.app.jjgallery.gdb.view.IFragmentHolder;
import com.jing.app.jjgallery.gdb.view.recommend.RecommendFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/19 13:48
 */
public class GHomeFragment extends GBaseFragment {

    @BindView(R.id.iv_record_image)
    RoundedImageView ivRecordImage;
    @BindView(R.id.iv_star1)
    RoundedImageView ivStar1;
    @BindView(R.id.iv_star2)
    RoundedImageView ivStar2;
    @BindView(R.id.iv_star3)
    RoundedImageView ivStar3;
    @BindView(R.id.tv_star_name1)
    TextView tvStarName1;
    @BindView(R.id.tv_star_name2)
    TextView tvStarName2;
    @BindView(R.id.tv_star_name3)
    TextView tvStarName3;
    @BindView(R.id.group_recommend)
    RelativeLayout groupRecommend;
    @BindView(R.id.rv_records)
    RecyclerView rvRecords;
//    @BindView(R.id.bmb_menu)
//    BoomMenuButton bmbMenu;

    private IHomeHolder homeHolder;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home_v4;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);

        initRecommentd();

        loadHomeData();
    }

    private void initRecommentd() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        RecommendFragment fragment = new RecommendFragment();
        ft.add(R.id.group_recommend, fragment, "RecommendFragment");
        ft.commit();
    }

    @Override
    protected void bindFragmentHolder(IFragmentHolder holder) {
        homeHolder = (IHomeHolder) holder;
    }

    private void loadHomeData() {
        homeHolder.getPresenter().loadHomeData();
    }

    @OnClick({R.id.group_record, R.id.group_star, R.id.group_game})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_record:
                break;
            case R.id.group_star:
                break;
            case R.id.group_game:
                break;
        }
    }
}

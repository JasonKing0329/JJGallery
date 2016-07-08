package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.jing.app.jjgallery.BaseActivity;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.sub.ThumbPresenter;
import com.jing.app.jjgallery.viewsystem.IFragment;

public class ThumbActivity extends BaseActivity {

    private IFragment mFragment;
    private ThumbPresenter mPresenter;

    @Override
    protected boolean isActionBarNeed() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_thumb;
    }

    @Override
    protected void initController() {
        mPresenter = new ThumbPresenter();
    }

    @Override
    protected void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        mFragment = new ThumbFragment();
        mFragment.setActionbar(mActionBar);
        mFragment.setPresenter(mPresenter);
        ft.replace(R.id.thumb_fragment_container, (Fragment) mFragment, "ThumbFragment");
        ft.commit();
    }

    @Override
    protected void initBackgroundWork() {

    }
}

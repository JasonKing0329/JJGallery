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
    public boolean isActionBarNeed() {
        return true;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_thumb;
    }

    @Override
    public void initController() {
        mPresenter = new ThumbPresenter();
    }

    @Override
    public void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        mFragment = new ThumbFragment();
        mFragment.setActionbar(mActionBar);
        mFragment.setPresenter(mPresenter);
        ft.replace(R.id.thumb_fragment_container, (Fragment) mFragment, "ThumbFragment");
        ft.commit();
    }

    @Override
    public void initBackgroundWork() {

    }
}

package com.jing.app.jjgallery.viewsystem.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.util.DebugLog;
import com.jing.app.jjgallery.viewsystem.IColorPage;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by JingYang on 2016/8/4 0004.
 * Description:
 */
public abstract class BaseFragment extends Fragment implements IFragment {

    protected ActionBar mActionbar;
    protected IPage page;
    protected View contentView;
    protected BasePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(getLayoutResId(), null);
            page = createPage(contentView);
            page.initActionbar(mActionbar);
            page.setPresenter(mPresenter);
            page.initData();
        }
        return contentView;
    }

    protected abstract int getLayoutResId();

    protected abstract IPage createPage(View contentView);

    @Override
    public void setActionbar(ActionBar actionbar) {
        mActionbar = actionbar;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public IPage getPage() {
        return page;
    }

    @Override
    public IColorPage getColorPage() {
        if (page instanceof IColorPage) {
            return (IColorPage) page;
        }
        return null;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }
}

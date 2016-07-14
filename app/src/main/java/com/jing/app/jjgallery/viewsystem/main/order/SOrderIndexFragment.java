package com.jing.app.jjgallery.viewsystem.main.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.order.SOrderPresenter;
import com.jing.app.jjgallery.viewsystem.IColorPage;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by JingYang on 2016/7/14 0014.
 * Description:
 */
public class SOrderIndexFragment extends Fragment implements IFragment {
    private ActionBar mActionbar;
    private IPage sorderIndexPage;
    private View contentView;

    private SOrderPresenter mPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.page_file_manager_index, null);
            sorderIndexPage = new SOrderIndexPage(getActivity(), contentView);
            sorderIndexPage.initActionbar(mActionbar);
            sorderIndexPage.setPresenter(mPresenter);
            sorderIndexPage.initData();
        }
        return contentView;
    }

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
        return sorderIndexPage;
    }

    @Override
    public IColorPage getColorPage() {
        if (sorderIndexPage instanceof IColorPage) {
            return (IColorPage) sorderIndexPage;
        }
        return null;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (SOrderPresenter) presenter;
    }
}

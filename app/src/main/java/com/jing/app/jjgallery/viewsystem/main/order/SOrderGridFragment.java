package com.jing.app.jjgallery.viewsystem.main.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
 * Created by JingYang on 2016/7/20 0020.
 * Description:
 */
public class SOrderGridFragment extends Fragment implements IFragment {

    private final String TAG = "SOrderGridFragment";
    private IPage sorderGridPage;
    private View contentView;

    private ActionBar mActionbar;
    private SOrderPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        if (contentView == null) {
            Log.d(TAG, "reload view & page");
            contentView = inflater.inflate(R.layout.page_order, null);
            sorderGridPage = new SOrderGridPage(getActivity(), contentView);
            sorderGridPage.initActionbar(mActionbar);
            sorderGridPage.setPresenter(mPresenter);
            sorderGridPage.initData();
        }
        return contentView;
    }

    @Override
    public void setActionbar(ActionBar mActionbar) {
        this.mActionbar = mActionbar;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public IPage getPage() {
        return sorderGridPage;
    }

    @Override
    public IColorPage getColorPage() {
        if (sorderGridPage instanceof IColorPage) {
            return (IColorPage) sorderGridPage;
        }
        return null;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (SOrderPresenter) presenter;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (sorderGridPage != null) {
            ((SOrderGridPage) sorderGridPage).onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

}

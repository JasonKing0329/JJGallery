package com.jing.app.jjgallery.viewsystem.sub.thumb;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Constants;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by JingYang on 2016/7/8 0008.
 * Description:
 */
public abstract class ThumbFragment extends Fragment implements IFragment {

    private final String TAG = "ThumbFragment";
    private View contentView;

    private IPage mPage;
    private BasePresenter mPresenter;
    private ActionBar mActionbar;

    /**
     * thumb view仅作为选择器时的回调接口
     */
    protected IThumbSelector thumbSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        if (contentView == null) {
            Log.d(TAG, "reload view & page");
            contentView = inflater.inflate(R.layout.thumbfolder_main_l, null);
            mPage = createThumbPage(contentView);
            mPage.initActionbar(mActionbar);
            mPage.setPresenter(mPresenter);
            mPage.initData();
        }
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IThumbSelector) {
            thumbSelector = (IThumbSelector) context;
        }
    }

    protected abstract IPage createThumbPage(View contentView);

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
        return mPage;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }
}

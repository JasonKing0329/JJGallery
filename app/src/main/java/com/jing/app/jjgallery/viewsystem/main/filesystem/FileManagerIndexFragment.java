package com.jing.app.jjgallery.viewsystem.main.filesystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jing.app.jjgallery.BasePresenter;
import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.presenter.main.filesystem.FileManagerPresenter;
import com.jing.app.jjgallery.viewsystem.IFragment;
import com.jing.app.jjgallery.viewsystem.IPage;
import com.jing.app.jjgallery.viewsystem.publicview.ActionBar;

/**
 * Created by JingYang on 2016/7/7 0007.
 * Description:
 */
public class FileManagerIndexFragment extends Fragment implements IFragment {

    private final String TAG = "IndexFragment";
    private IPage fileIndexPage;
    private View contentView;

    private ActionBar mActionbar;
    private FileManagerPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (contentView == null) {
            Log.d(TAG, "reload view & page");
            contentView = inflater.inflate(R.layout.page_file_manager_index, null);
            fileIndexPage = new FileManagerIndexPage(getActivity(), contentView);
            fileIndexPage.initActionbar(mActionbar);
            fileIndexPage.setPresenter(mPresenter);
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
        return fileIndexPage;
    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = (FileManagerPresenter) presenter;
    }
}
